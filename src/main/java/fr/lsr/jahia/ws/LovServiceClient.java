package fr.lsr.jahia.ws;

import fr.lsr.jahia.ws.wsdl.lov.*;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

import javax.xml.bind.JAXBElement;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component(service = LovServiceClient.class, immediate = true)
public class LovServiceClient extends WebServiceGatewaySupport {
    private static final Logger logger = LoggerFactory.getLogger(LovServiceClient.class);

    private static final String DEFAULT_URI = "https://api3.lumesse-talenthub.com/HRIS/SOAP/LOV?api_key=uj8p6vaqdzcequvqvcfqjkzf";
    private static final String CONTEXT_PATH = "fr.lsr.jahia.ws.wsdl.lov";
    private final ObjectFactory objectFactory;
    private ClientInterceptor securityIntercepor;

    @Reference
    private void setSecurityIntercepor(ClientInterceptor securityIntercepor) {
        this.securityIntercepor = securityIntercepor;
    }

    public LovServiceClient() {
        super();
        setDefaultUri(DEFAULT_URI);
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath(CONTEXT_PATH);
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);
        setInterceptors(new ClientInterceptor[]{securityIntercepor});
        objectFactory = new ObjectFactory();
    }

    private Set<LovDescendantDto> getLov(String lov) {
        GetLovWithValuesByName request = new GetLovWithValuesByName();
        request.setLangCode(LangCode.FR);
        request.setLovName(lov);

        JAXBElement<GetLovWithValuesByNameResponse> responseWrapper = (JAXBElement<GetLovWithValuesByNameResponse>) getWebServiceTemplate()
                .marshalSendAndReceive(objectFactory.createGetLovWithValuesByName(request));
        GetLovWithValuesByNameResponse response = responseWrapper.getValue();
        return Collections.unmodifiableSet(new HashSet<>(response.getLovWithValues()));
    }

    public Set<LovDescendantDto> getContractTypes() {
        return getLov(Lov.CONTRACT_TYPE.getName());
    }

    public Set<LovDescendantDto> getTypesOrganisme() {
        return getLov(Lov.TYPE_ORGANISME.getName());
    }

    public Set<LovDescendantDto> getClassifications() {
        return getLov(Lov.CLASSIFICATION.getName());
    }

    public Set<LovDescendantDto> getRegimes() {
        return getLov(Lov.REGIME.getName());
    }
}
