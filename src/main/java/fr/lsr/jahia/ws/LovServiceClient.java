package fr.lsr.jahia.ws;

import fr.lsr.jahia.ws.wsdl.lov.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBElement;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class LovServiceClient extends AbstractWebServiceClient<ObjectFactory> {
    private static final Logger logger = LoggerFactory.getLogger(LovServiceClient.class);

    private static final String DEFAULT_URI = "https://api3.lumesse-talenthub.com/HRIS/SOAP/LOV?api_key=uj8p6vaqdzcequvqvcfqjkzf";

    public LovServiceClient(SecurityInterceptor securityInterceptor) {
        super(new ObjectFactory(), securityInterceptor, DEFAULT_URI);
    }

    private Set<LovDescendantDto> getLov(String lov) {
        GetLovWithValuesByName request = new GetLovWithValuesByName();
        request.setLangCode(LangCode.FR);
        request.setLovName(lov);

        JAXBElement<GetLovWithValuesByNameResponse> responseWrapper = (JAXBElement<GetLovWithValuesByNameResponse>) getWebServiceTemplate()
                .marshalSendAndReceive(getObjectFactory().createGetLovWithValuesByName(request));
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
