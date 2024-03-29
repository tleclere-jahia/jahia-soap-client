package org.foo.modules.jahia.lumesse;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

public abstract class AbstractSecureWebServiceClient<T> extends WebServiceGatewaySupport {
    private final T objectFactory;

    public AbstractSecureWebServiceClient(T objectFactory, SecurityInterceptor securityInterceptor, String defaultUri) {
        super();
        this.objectFactory = objectFactory;
        setDefaultUri(defaultUri);
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setBeanClassLoader(LumesseService.class.getClassLoader());
        marshaller.setContextPath(objectFactory.getClass().getPackage().getName());
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);
        setInterceptors(new ClientInterceptor[]{securityInterceptor});
    }

    protected T getObjectFactory() {
        return objectFactory;
    }
}
