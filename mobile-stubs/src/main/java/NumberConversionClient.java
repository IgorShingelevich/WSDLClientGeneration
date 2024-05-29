package com.example; // Replace with your package name

import com.dataaccess.www.webservicesserver.NumberConversion;
import com.dataaccess.www.webservicesserver.NumberConversionLocator;
import com.dataaccess.www.webservicesserver.NumberConversionSoapType;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

public class NumberConversionClient {
    public static void main(String[] args) {
        try {
            // Create the service locator
            NumberConversionLocator locator = new NumberConversionLocator();

            // Get the Soap12 port
            NumberConversionSoapType soap12Port = locator.getNumberConversionSoap12(new URL("https://www.dataaccess.com/webservicesserver/numberconversion.wso"));

            // Create the request object
            org.apache.axis.types.UnsignedLong number = new org.apache.axis.types.UnsignedLong(123);

            // Invoke the numberToWords method
            String wordRepresentation = soap12Port.numberToWords(number);

            System.out.println("Word representation: " + wordRepresentation);
        } catch (ServiceException | MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}