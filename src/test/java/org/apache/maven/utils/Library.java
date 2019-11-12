package org.apache.maven.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

public class Library {

	public static String resourceLocation = System.getProperty("user.dir").toString().replace("\\", "\\\\") + "\\src\\test\\resources";

	/** 
	 * This is a method used to get data from excel sheet and store in HashMap
	 * @param query
	 * @param fileNameAndLoc
	 * @return HashMap
	 * @throws Exception
	 */
	public static HashMap<String, String> getDataFromExcel(String query, String fileNameAndLoc) throws Exception {

		try {
		HashMap<String, String> hm = new HashMap<String, String>();
		Recordset rs;
		ArrayList<String> fieldNamesList;
		StringBuilder fieldNameString = new StringBuilder();
		String fieldName, fieldValue;

		fileNameAndLoc = resourceLocation + "\\" + fileNameAndLoc;

		if ((query == null) || (fileNameAndLoc == null)) {
			throw new Exception("query and/or fileNameAndLoc arguements to the getDataFromExcel method cannot be null or blank.");
		}
		if (new File(fileNameAndLoc).exists() == false) {
			throw new Exception("The File "
					+ fileNameAndLoc 
					+ " passed as an argument to the getDataFromExcel method was not found");				
		}

		Fillo file =  new Fillo();
		com.codoid.products.fillo.Connection conn = file.getConnection(fileNameAndLoc);
		rs = conn.executeQuery(query);
		
		fieldNamesList = rs.getFieldNames();
		int fieldCount = 0;
		for (String currentFieldName : fieldNamesList) {
			fieldCount = fieldCount +1;
			if (fieldCount == 1) {
				fieldNameString.append(currentFieldName);
			} else {
				fieldNameString.append(";" + currentFieldName);	
			}	
		}
		long resultsCount = 0;
		while(rs.next()) {
			resultsCount = resultsCount + 1 ;
			for (int i = 0; i < fieldCount; i++) {
				fieldName = fieldNamesList.get(i);
				fieldValue = (String) rs.getField(fieldName);
				if (fieldValue == null) {
					fieldValue = "";
				}
				hm.put(fieldName, fieldValue);
			}
		}
		if (conn != null) {
			conn.close();		
		}
		if (rs != null) {
			rs.close();
		}
		return hm;
	}
		catch (FilloException fe) {
			return null;
		}
	}
}
