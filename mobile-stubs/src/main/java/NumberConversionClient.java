
import com.dataaccess.www.webservicesserver.NumberConversion;
import com.dataaccess.www.webservicesserver.NumberConversionLocator;
import com.dataaccess.www.webservicesserver.NumberConversionSoapType;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

public class NumberConversionClient extends WebServiceGatewaySupport {
    public static void main(String[] args) {
        NumberConversionClient client = new NumberConversionClient();
        client.invokeWebService();
    }

    private void invokeWebService() {
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

    @Override
    protected void initGateway() {
        // Configure the WebServiceTemplate
        getWebServiceTemplate().setMessageSender(new HttpComponentsMessageSender());
        getWebServiceTemplate().setMarshaller(new org.springframework.oxm.jaxb.Jaxb2Marshaller());
        getWebServiceTemplate().setUnmarshaller(new org.springframework.oxm.jaxb.Jaxb2Marshaller());
    }
}