package TestCases;

import java.io.File;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import PageMethods.AdactinApplication;
import PageMethods.AdactinHomePage;
import PageMethods.ConfirmationPage;
import PageMethods.ForgetPassword;
import PageMethods.NewUserRegistration;
import PageMethods.SearchHotel;
import PageMethods.SelectHotel;
import Utilities.Common;
import Utilities.GenericKeywords;

import com.relevantcodes.extentreports.ExtentReports;

@Listeners({ Utilities.TestListener.class })
public class TC_004 extends Common {

	public static int count = 1;	
	private AdactinHomePage adactinHomePage;		
	
	
	@BeforeClass
	public void start(){
		try{
			if(GenericKeywords.outputDirectory == null){
				startup();									
			}
		}catch(Exception ex){
			Common.writeToLogFile("ERROR","Exception Caught in startup activities, Message is ->"+ex.getMessage());
		}
	}

	
	@Test
	public void TC_004() {
		String strName = new Exception().getStackTrace()[0].getMethodName();		
		adactinHomePage = testStart(strName,"New User Register functionality");
				
		
		for (String testDataSet : GenericKeywords.testCaseDataSets) {
			GenericKeywords.testCaseDataRow = returnRowNumber(testDataSet);
			testStepInfoStart(testDataSet);
			
			adactinHomePage.UserRegistration();
			
			testStepInfoEnd(testDataSet);
			}
		testEnd();

	}
	
}
