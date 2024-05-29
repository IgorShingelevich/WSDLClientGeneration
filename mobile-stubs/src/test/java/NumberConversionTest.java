import com.dataaccess.www.webservicesserver.NumberConversionLocator;
import com.dataaccess.www.webservicesserver.NumberConversionSoapType;
import org.apache.axis.types.UnsignedLong;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

class NumberConversionTest extends WebServiceGatewaySupport {

    private NumberConversionSoapType soap12Port;

    @BeforeEach
    void setUp() throws ServiceException, MalformedURLException {
        // Create the service locator
        NumberConversionLocator locator = new NumberConversionLocator();

        // Get the Soap12 port
        soap12Port = locator.getNumberConversionSoap12(new URL("https://www.dataaccess.com/webservicesserver/numberconversion.wso"));

        // Configure the WebServiceTemplate
        getWebServiceTemplate().setMarshaller(new org.springframework.oxm.jaxb.Jaxb2Marshaller());
        getWebServiceTemplate().setUnmarshaller(new org.springframework.oxm.jaxb.Jaxb2Marshaller());
    }

    @Test
    void testNumberToWords() throws RemoteException {
        // Create the request object
        UnsignedLong number = new UnsignedLong(123);

        // Invoke the numberToWords method
        String wordRepresentation = soap12Port.numberToWords(number);

        // Assert the result
        Assertions.assertThat(wordRepresentation).isEqualTo("one hundred and twenty three ");
    }
}