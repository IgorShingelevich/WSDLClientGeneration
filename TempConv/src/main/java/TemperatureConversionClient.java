package org.example;

import https.www_w3schools_com.xml.TempConvert;
import https.www_w3schools_com.xml.TempConvertSoap;

public class TemperatureConversionClient {

    public static void main(String[] args) {
        try {
            // Create a service instance
            TempConvert service = new TempConvert();

            // Get the SOAP port
            TempConvertSoap soap = service.getTempConvertSoap();

            // Manually set the endpoint address if needed (e.g., if the WSDL URL has changed)
            ((javax.xml.ws.BindingProvider) soap).getRequestContext()
                    .put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "https://www.w3schools.com/xml/tempconvert.asmx");

            // Convert Fahrenheit to Celsius and print the result
            String fahrenheit = "100";
            String celsiusResult = soap.fahrenheitToCelsius(fahrenheit);
            System.out.println(fahrenheit + " Fahrenheit is " + celsiusResult + " Celsius");

            // Convert Celsius to Fahrenheit and print the result
            String celsius = "37";
            String fahrenheitResult = soap.celsiusToFahrenheit(celsius);
            System.out.println(celsius + " Celsius is " + fahrenheitResult + " Fahrenheit");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
