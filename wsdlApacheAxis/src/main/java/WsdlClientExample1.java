import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.message.SOAPBodyElement;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.math.BigInteger;
import java.net.URL;

public class WsdlClientExample1 {
    public static void main(String[] args) {
        try {
            String endpoint = "https://www.dataaccess.com/webservicesserver/NumberConversion.wso";
            String namespace = "http://www.dataaccess.com/webservicesserver/";

            // Create a service instance
            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(new URL(endpoint));
            call.setOperationName(new QName(namespace, "NumberToWords"));
            call.setSOAPActionURI(namespace + "NumberToWords");

            // Manually construct the SOAP request
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element numberToWords = doc.createElementNS(namespace, "NumberToWords");
            Element ubiNum = doc.createElement("ubiNum");
            ubiNum.appendChild(doc.createTextNode(new BigInteger("12345").toString()));
            numberToWords.appendChild(ubiNum);
            SOAPBodyElement bodyElement = new SOAPBodyElement(numberToWords);

            SOAPEnvelope envelope = new SOAPEnvelope();
            envelope.addBodyElement(bodyElement);

            // Invoke the service
            SOAPEnvelope responseEnvelope = call.invoke(envelope);

            // Extract the response correctly using DOM methods
            NodeList nodeList = responseEnvelope.getBody().getElementsByTagName("NumberToWordsResult");
            String result = null;
            if (nodeList != null && nodeList.getLength() > 0) {
                Node node = nodeList.item(0);
                if (node != null) {
                    result = node.getFirstChild().getNodeValue();
                }
            }

            // Print the result
            System.out.println("Number to words result: " + result);//twelve thousand three hundred and forty five

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
