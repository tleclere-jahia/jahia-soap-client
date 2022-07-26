package org.foo.modules.jahia.dolist;

import org.foo.modules.jahia.dolist.ws.authentication.AuthenticationRequest;
import org.foo.modules.jahia.dolist.ws.authentication.GetAuthenticationToken;
import org.foo.modules.jahia.dolist.ws.authentication.GetAuthenticationTokenResponse;
import org.foo.modules.jahia.dolist.ws.authentication.ObjectFactory;
import org.foo.modules.jahia.dolist.ws.contact.AuthenticationTokenContext;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

public class DoListAuthenticationClient extends WebServiceGatewaySupport {
    private static final String DEFAULT_URI = "http://api.dolist.net/v2/AuthenticationService.svc/soap1.1";

    private final ObjectFactory objectFactory;
    private final String authenticationKey;
    private final String accountId;

    public DoListAuthenticationClient(String accountId, String authenticationKey) {
        this.accountId = accountId;
        this.authenticationKey = authenticationKey;
        objectFactory = new ObjectFactory();
        setDefaultUri(DEFAULT_URI);
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setBeanClassLoader(DoListService.class.getClassLoader());
        marshaller.setContextPath(objectFactory.getClass().getPackage().getName());
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);
    }

    public AuthenticationTokenContext getAuthenticationTokenContext() {
        AuthenticationTokenContext authToken = new AuthenticationTokenContext();
        authToken.setAccountID(Integer.parseInt(accountId));

        GetAuthenticationToken request = objectFactory.createGetAuthenticationToken();
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setAccountID(Integer.parseInt(accountId));
        authenticationRequest.setAuthenticationKey(authenticationKey);
        request.setAuthenticationRequest(authenticationRequest);
        GetAuthenticationTokenResponse response = (GetAuthenticationTokenResponse) getWebServiceTemplate().marshalSendAndReceive(request,
                new SoapActionCallback("http://api.dolist.net/v2/AuthenticationService/GetAuthenticationToken"));
        authToken.setKey(response.getGetAuthenticationTokenResult().getKey());

        return authToken;
    }
}
