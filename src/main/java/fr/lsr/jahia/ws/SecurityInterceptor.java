package fr.lsr.jahia.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

import javax.xml.soap.*;

public class SecurityInterceptor implements ClientInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);

    @Override
    public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
        SOAPMessage soapMessage = ((SaajSoapMessage) messageContext.getRequest()).getSaajMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        try {
            SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
            SOAPHeader soapHeader = soapEnvelope.getHeader();
            Name headerElementName = soapEnvelope.createName("Security", "wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
            SOAPHeaderElement soapHeaderElement = soapHeader.addHeaderElement(headerElementName);
            SOAPElement usernameTokenSOAPElement = soapHeaderElement.addChildElement("UsernameToken", "wsse");
            SOAPElement userNameSOAPElement = usernameTokenSOAPElement.addChildElement("Username", "wsse");
            userNameSOAPElement.addTextNode("QMOFK026203F3VBQB8MLO8MJ3:guest:FO");
            SOAPElement passwordSOAPElement = usernameTokenSOAPElement.addChildElement("Password", "wsse");
            passwordSOAPElement.addTextNode("guest");
            soapMessage.saveChanges();
            return true;
        } catch (SOAPException e) {
            logger.error("", e);
            return false;
        }
    }

    @Override
    public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
        return false;
    }

    @Override
    public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
        return false;
    }
}
