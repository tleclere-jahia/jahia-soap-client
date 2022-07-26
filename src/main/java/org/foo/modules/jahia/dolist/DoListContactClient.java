package org.foo.modules.jahia.dolist;

import org.foo.modules.jahia.dolist.ws.contact.*;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import java.util.List;
import java.util.stream.Collectors;

public class DoListContactClient extends WebServiceGatewaySupport {
    private static final String DEFAULT_URI = "http://api.dolist.net/v2/ContactManagementService.svc/soap1.1";
    private final ObjectFactory objectFactory;
    private final AuthenticationTokenContext authenticationTokenContext;

    public DoListContactClient(AuthenticationTokenContext authenticationTokenContext) {
        this.authenticationTokenContext = authenticationTokenContext;
        objectFactory = new ObjectFactory();
        setDefaultUri(DEFAULT_URI);
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setBeanClassLoader(DoListService.class.getClassLoader());
        marshaller.setContextPath(objectFactory.getClass().getPackage().getName());
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);
    }

    public List<String> getContacts() {
        GetContact request = objectFactory.createGetContact();
        request.setRequest(new GetContactRequest());
        request.setToken(authenticationTokenContext);
        GetContactResponse response = (GetContactResponse) getWebServiceTemplate().marshalSendAndReceive(request,
                new SoapActionCallback("http://api.dolist.net/v2/ContactManagementService/GetContact"));
        return response.getGetContactResult().getContactList().getContactDatas().stream().map(ContactData::getEmail).collect(Collectors.toList());
    }
}
