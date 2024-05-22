package org.example;

import com.dataaccess.webservicesserver.NumberConversion;
import com.dataaccess.webservicesserver.NumberConversionSoapType;

import java.math.BigInteger;

public class WsdlClientExample {
    public static void main(String[] args) {
        // Create a service instance
        NumberConversion service = new NumberConversion();

        // Get the SOAP port
        NumberConversionSoapType soap = service.getNumberConversionSoap();

        // Call the service method and print the result
        String result = soap.numberToWords(BigInteger.valueOf(12345));
        System.out.println("Number to words result: " + result);
    }
}
