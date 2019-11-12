# RestSample
To test rest service for capital city based on country code.
The test script is present in /src/test/java/org/apache/maven/rest/RestTest.java
The script will perform dynamic validation of rest services based on the user input - Code (Numeric) or Country Name (String)
If the User inputs code, then https://restcountries.eu/rest/v2/callingcode/{callingcode} will be invoked
If the User inputs Country Name, then https://restcountries.eu/rest/v2/name/{name} will be invoked
The test data used for comparison with the rest service output is present in /src/test/rescources/Data.xlsx
The execution of RestTest.java is through Run as "TestNG Test" through Eclipse IDE
