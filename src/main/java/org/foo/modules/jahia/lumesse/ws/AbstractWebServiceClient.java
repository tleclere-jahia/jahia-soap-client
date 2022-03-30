package org.foo.modules.jahia.lumesse.ws;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

import java.util.Collections;

public abstract class AbstractWebServiceClient<T> extends WebServiceGatewaySupport {
    private final T objectFactory;

    public AbstractWebServiceClient(T objectFactory, SecurityInterceptor securityInterceptor, String defaultUri) {
        super();
        this.objectFactory = objectFactory;
        setDefaultUri(defaultUri);
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setBeanClassLoader(LumesseService.class.getClassLoader());
        marshaller.setContextPath(objectFactory.getClass().getPackage().getName());
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);
        setInterceptors(Collections.singleton(securityInterceptor).toArray(new ClientInterceptor[0]));
    }

    protected T getObjectFactory() {
        return objectFactory;
    }
}
