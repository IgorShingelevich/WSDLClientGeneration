
import java.math.BigInteger;

public class WsdlClientExample0 {

    public static void main(String[] args) {
        // Create a service instance
        com.dataaccess.webservicesserver.NumberConversion service = new com.dataaccess.webservicesserver.NumberConversion();

        // Get the SOAP port
        com.dataaccess.webservicesserver.NumberConversionSoapType soap = service.getNumberConversionSoap();

        // Call the service method and print the result
        String result = soap.numberToWords(BigInteger.valueOf(12345));
        System.out.println("Number to words result: " + result); //twelve thousand three hundred and forty five
    }
}