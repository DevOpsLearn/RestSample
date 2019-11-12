package org.apache.maven.rest;
import static com.jayway.restassured.RestAssured.*;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.maven.utils.Library;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner; 

public class RestTest {
	String val = "";
	String svc1 = "";
	String svc2 = "";
	
	@Test
	public void restGenSrvTest() throws Exception {

		HashMap<String, String> oExcelData = new LinkedHashMap <String, String>();
		String capital, region, language, name, currencies, fullQuery;
		String query = "Select * from MasterData where Country = ";
		SoftAssert sa= new SoftAssert();

		try {
			
			// Search by phone intl code (callingcode)
			val = "";
			svc1 = "callingcode";
			svc2 = "name";
			Response res;

			// Using Scanner for Getting Input from User 
			Scanner in = new Scanner(System.in); 

			System.out.println("Please enter the country code or country name; if you wish to close. Press O");
			String val1 = in.nextLine(); 
			System.out.println("You entered country code "+ val1); 

			while (!(val1.contentEquals("000")))
			{
				if (NumberUtils.isNumber(val1)) {
					System.out.println("You entered : " + val1);
					res = given(). 
							when().then().statusCode(200).
							get("http://restcountries.eu/rest/v2/{service}/{value}", svc1, val1);
				} else {
			        System.out.println("The input is not an integer");
			        res = given(). 
							when().then().statusCode(200).
							get("http://restcountries.eu/rest/v2/{service}/{value}", svc2, val1);
			    }
				/*
				Response res = given(). 
						when().then().statusCode(200).
						get("http://restcountries.eu/rest/v2/{service}/{value}", svc, val1);
				*/
				JsonPath jp = new JsonPath(res.asString());

				capital = jp.get("capital").toString();
				region = jp.get("region").toString();
				language = jp.get("languages.name").toString();
				name = jp.get("name").toString();
				currencies = jp.get("currencies.code").toString();

				System.out.println("Capital: "+ capital);
				System.out.println("Region: "+ region);
				System.out.println("Language: "+ language);
				System.out.println("name: "+ name);
				System.out.println("currency: "+ currencies);

				fullQuery = query + "'" + name.substring(1,name.length()-1) + "'";
				oExcelData = Library.getDataFromExcel(fullQuery, "Data.xlsx");
				if (oExcelData == null) {
					System.out.println("Entered code is not present in Master data");
					System.out.println("Please enter another country code; if you wish to close. Press 000");
					val1 = in.nextLine(); 
					continue;
				}
				
				sa.assertEquals(capital.replaceAll("\\[","").replaceAll("\\]", "") ,oExcelData.get("Capital"));
				sa.assertEquals(region.replaceAll("\\[","").replaceAll("\\]", "") ,oExcelData.get("Region"));
				sa.assertEquals(language.replaceAll("\\[","").replaceAll("\\]", "") ,oExcelData.get("Language"));
				sa.assertEquals(name.replaceAll("\\[","").replaceAll("\\]", "") ,oExcelData.get("Country"));
				sa.assertEquals(currencies.replaceAll("\\[","").replaceAll("\\]", "") ,oExcelData.get("Currency"));
				
				
				System.out.println("Please enter another country code; if you wish to close. Press 000");
				val1 = in.nextLine(); 
			}
		}
		catch(AssertionError ae){
			ae.printStackTrace();
		}
		finally {
			System.out.println("End of the test execution");
			sa.assertAll();
		}
	}
}