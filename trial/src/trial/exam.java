package trial;

import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.AfterClass;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class exam {
public static WebDriver driver;
Object data[][];
String result[];
int k  = 0;
@BeforeClass
public  void setUp ()
{
	XSSFWorkbook workbook;
	try {
		String path = System.getProperty("user.dir");
		workbook = new XSSFWorkbook(path+"/excel/RegistrationInput.xlsx");
		XSSFSheet sheet = workbook.getSheetAt(0);
		int RowCount = sheet.getLastRowNum();
		int ColumnCount = sheet.getRow(0).getPhysicalNumberOfCells();
		data = new Object [RowCount][ColumnCount-1];
		result = new String [RowCount];
		DataFormatter formatter = new DataFormatter();
		for(int i = 1; i < RowCount; i++)
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
		//driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get("https://www.facebook.com/");
	
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
@DataProvider(name="RegData")
public Object[][] getData ()
{
	return data;
}

@Test(dataProvider="RegData")
public void Registration(String Email, String ReEmail, String firstName, String LastName, String Password, String Days, String Month, String Year ) throws InterruptedException
{
	driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
	WebElement email = driver.findElement(By.xpath("//*[@id='u_0_r']"));
	email.sendKeys(Email);
	WebDriverWait wait = new WebDriverWait(driver, 50);
	WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("reg_email_confirmation__")));
	WebElement remail = driver.findElement(By.name("reg_email_confirmation__"));
	remail.sendKeys(ReEmail);
	WebElement fName = driver.findElement(By.xpath("//*[@id='u_0_m']"));
	fName.sendKeys(firstName);
	WebElement LName = driver.findElement(By.xpath("//*[@id='u_0_o']"));
	LName.sendKeys(LastName);
	WebElement password = driver.findElement(By.xpath("//*[@id='u_0_y']"));
	password.sendKeys(Password);
	Select days =new Select(driver.findElement(By.id("day")));
	days.selectByValue(Days);
	Select month =new Select(driver.findElement(By.id("month")));
	month.selectByValue(Month);
	Select year =new Select(driver.findElement(By.id("year")));
	year.selectByValue(Year);
	driver.findElement(By.name("sex")).click();
	Thread.sleep(3000);
	driver.findElement(By.name("websubmit")).sendKeys(Keys.ENTER);
	Thread.sleep(3000);
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
