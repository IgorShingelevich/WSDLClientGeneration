import com.dataaccess.www.webservicesserver.NumberConversionLocator;
import com.dataaccess.www.webservicesserver.NumberConversionSoapType;
import org.apache.axis.types.UnsignedLong;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.HttpUrlConnectionMessageSender;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;

import javax.xml.rpc.ServiceException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

class NumberConversionOkTest extends WebServiceGatewaySupport {

    private NumberConversionSoapType soap12Port;

    @BeforeEach
    void setUp() throws ServiceException, MalformedURLException {
        // Create the service locator
        NumberConversionLocator locator = new NumberConversionLocator();

        // Get the Soap12 port
        soap12Port = locator.getNumberConversionSoap12(new URL("https://www.dataaccess.com/webservicesserver/numberconversion.wso"));
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

    @Test
    void testNumberToWordsUsingMessageFactory() throws Exception {
        // Create a new instance of SaajSoapMessageFactory
        SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
        messageFactory.setMessageFactory(MessageFactory.newInstance());

        // Create a new instance of WebServiceTemplate
        WebServiceTemplate webServiceTemplate = new WebServiceTemplate(messageFactory);

        // Set message sender to handle the connection
        webServiceTemplate.setMessageSender(new HttpUrlConnectionMessageSender());

        // Create a SOAP request message as a string
        String requestPayload = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://www.dataaccess.com/webservicesserver/\">" +
                "<soapenv:Header/>" +
                "<soapenv:Body>" +
                "<web:NumberToWords>" +
                "<web:ubiNum>123</web:ubiNum>" +
                "</web:NumberToWords>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        // Send the request and receive the response
        String response = (String) webServiceTemplate.sendAndReceive(
                "https://www.dataaccess.com/webservicesserver/numberconversion.wso",
                new WebServiceMessageCallback() {
                    @Override
                    public void doWithMessage(WebServiceMessage message) throws IOException {
                        SaajSoapMessage saajMessage = (SaajSoapMessage) message;
                        try {
                            saajMessage.getSaajMessage().getSOAPPart().setContent(new StreamSource(new java.io.StringReader(requestPayload)));
                        } catch (SOAPException e) {
                            throw new RuntimeException(e);
                        }
                        saajMessage.setSoapAction("http://www.dataaccess.com/webservicesserver/NumberToWords");

                        //set JWT token
                        String jwtToken = "tokenHere";
                        saajMessage.getSaajMessage().getMimeHeaders().addHeader("Authorization", "Bearer "+jwtToken);
                    }
                },
                message -> {
                    SaajSoapMessage saajResponseMessage = (SaajSoapMessage) message;
                    return transformSourceToString(saajResponseMessage.getPayloadSource());
                }
        );

        // Assert the response
        Assertions.assertThat(response).contains("one hundred and twenty three");
    }

    private String transformSourceToString(Source source) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error transforming source to string", e);
        }
    }
}
