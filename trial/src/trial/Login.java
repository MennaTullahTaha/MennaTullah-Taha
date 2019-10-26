package trial;

import static org.testng.Assert.assertTrue;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class Login {

	WebDriver driver = exam.driver;
	Object data[][];
	String result[];
	int k  = 0;
	@BeforeClass
	public  void setUp ()
	{
		XSSFWorkbook workbook;
		try 
		{
			String path = System.getProperty("user.dir");
			workbook = new XSSFWorkbook(path+"/excel/LoginInput.xlsx");
			XSSFSheet sheet = workbook.getSheetAt(0);
			int RowCount = sheet.getLastRowNum();
			int ColumnCount = sheet.getRow(0).getPhysicalNumberOfCells();
			data = new Object [RowCount][ColumnCount-1];
			result = new String [RowCount];
			DataFormatter formatter = new DataFormatter();
			for(int i = 1; i < RowCount-2; i++)
			{
				for(int j = 0; j < ColumnCount-1; j++)
				{
					String val = formatter.formatCellValue(sheet.getRow(i).getCell(j));
					if(val!=null)
					data[i-1][j] = val;
					
				}
			}
			for(int i = 1; i < RowCount-1; i++)
			{
					String val = formatter.formatCellValue(sheet.getRow(i).getCell(ColumnCount-1));
					if(val!=null)
					result[i-1] = val;
			}
			driver = new ChromeDriver();
			driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.get("https://www.facebook.com/");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@DataProvider(name="LoginData")
	public Object[][] getData ()
	{
		return data;
	}
	
	@Test(dataProvider="LoginData")
	public void LoginPage (String Email, String Password) throws InterruptedException
	{

		driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
		WebElement email = driver.findElement(By.xpath("//*[@id='email']"));
		email.clear();
		email.sendKeys(Email);
		WebElement password = driver.findElement(By.xpath("//*[@id='pass']"));
		password.clear();
		password.sendKeys(Password);
		driver.findElement(By.id("loginbutton")).sendKeys(Keys.ENTER);
		Boolean isPresent = driver.findElements(By.xpath("//*[@id='navItem_4748854339']/a/div")).size() > 0;
		String validate;
		if(isPresent)
			validate = "pass";
		else
			validate = "fail";
		assertTrue(validate.contains(result[k]));
		if(isPresent)
		{
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[contains(@id,'userNavigationLabel')]")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//span[@class='_54nh'][contains(.,'Log Out')]")).click();
		}
		else
		{
			driver.get("https://www.facebook.com/");
		}
		k++;
	}

}
