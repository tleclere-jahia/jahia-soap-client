package fr.lsr.jahia.ws;

import fr.lsr.jahia.ws.wsdl.foadvert.*;
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
import java.util.List;
import java.util.Set;

@Component(service = FoAdvertServiceClient.class, immediate = true)
public class FoAdvertServiceClient extends WebServiceGatewaySupport {
    private static final Logger logger = LoggerFactory.getLogger(FoAdvertServiceClient.class);

    private static final String DEFAULT_URI = "https://api3.lumesse-talenthub.com/CareerPortal/SOAP/FoAdvert?api_key=yauwsvaqkvwdmxfat8488fyx";
    private static final String CONTEXT_PATH = "fr.lsr.jahia.ws.wsdl.foadvert";
    private static final int PAGE_SIZE = 100;

    private final ObjectFactory objectFactory;
    private ClientInterceptor securityIntercepor;

    @Reference
    private void setSecurityIntercepor(ClientInterceptor securityIntercepor) {
        this.securityIntercepor = securityIntercepor;
    }

    public FoAdvertServiceClient() {
        super();
        setDefaultUri(DEFAULT_URI);
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath(CONTEXT_PATH);
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);
        setInterceptors(new ClientInterceptor[]{securityIntercepor});
        this.objectFactory = new ObjectFactory();
    }

    public int countAdvertisements() {
        GetAdvertisements request = new GetAdvertisements();
        request.setFirstResult(0);
        request.setMaxResults(1);
        request.setSearchCriteriaDto(new SearchCriteriaDto());
        request.setSortingDetailsDto(new SortingDetailsDto());
        request.setLangCode(LangCode.FR);

        JAXBElement<GetAdvertisementsResponse> responseWrapper = (JAXBElement<GetAdvertisementsResponse>) getWebServiceTemplate()
                .marshalSendAndReceive(objectFactory.createGetAdvertisements(request));
        GetAdvertisementsResponse response = responseWrapper.getValue();
        return response.getAdvertisementResult().getTotalResults();
    }

    public Set<AdvertisementDto> getAdvertisements(int pageIndex) {
        GetAdvertisements request = new GetAdvertisements();
        request.setFirstResult(pageIndex);
        request.setMaxResults(PAGE_SIZE);
        request.setSearchCriteriaDto(new SearchCriteriaDto());
        SortingDetailsDto sortingDetailsDto = new SortingDetailsDto();
        sortingDetailsDto.setColumnName(Constants.SORTING_COLUMN);
        sortingDetailsDto.setSortType(SortType.DESCENDING);
        request.setSortingDetailsDto(sortingDetailsDto);
        request.setLangCode(LangCode.FR);

        JAXBElement<GetAdvertisementsResponse> responseWrapper = (JAXBElement<GetAdvertisementsResponse>) getWebServiceTemplate()
                .marshalSendAndReceive(objectFactory.createGetAdvertisements(request));
        GetAdvertisementsResponse response = responseWrapper.getValue();
        return Collections.unmodifiableSet(new HashSet<>(response.getAdvertisementResult().getAdvertisements().getAdvertisement()));
    }

    public AdvertisementDto getAdvertisement(Long offerId) {
        GetAdvertisementById request = new GetAdvertisementById();
        request.setPostingTargetId(offerId);
        request.setLangCode(null);
        request.setShowImages(null);

        JAXBElement<GetAdvertisementByIdResponse> responseWrapper = (JAXBElement<GetAdvertisementByIdResponse>) getWebServiceTemplate()
                .marshalSendAndReceive(objectFactory.createGetAdvertisementById(request));
        GetAdvertisementByIdResponse response = responseWrapper.getValue();
        return response.getAdvertisement();
    }

    public AvailableSearchCriteriaDto search(String lovOrders) {
        GetCriteria request = new GetCriteria();
        request.setLangCode(LangCode.FR);
        request.setLovOrders(lovOrders);
        request.setSearchCriteriaSorting(null);

        JAXBElement<GetCriteriaResponse> responseWrapper = (JAXBElement<GetCriteriaResponse>) getWebServiceTemplate()
                .marshalSendAndReceive(objectFactory.createGetCriteria(request));
        GetCriteriaResponse response = responseWrapper.getValue();
        return response.getStandardCriteriaWithLovs();
    }

    public Set<CountryCriterion> getRegions() {
        AvailableSearchCriteriaDto result = search("1,2,3,4,5,6,7,8,9,10,11,12");
        return Collections.unmodifiableSet(new HashSet<>(result.getCountries().getCountry()));
    }

    public Set<LovWithActivatorsCriterion> getJobFamilies() {
        AvailableSearchCriteriaDto result = search("1,2,3,4,5,6,7,8,9,10,11,12");
        List<LovCriterion> jobFamilies = result.getCustomlovs().getCustomLov();
        for (LovCriterion lc : jobFamilies) {
            if (lc.getLabel().equals(Lov.JOB_FAMILY.getName())) {
                return Collections.unmodifiableSet(new HashSet<>(lc.getCriteria().getCriterion()));
            }
        }
        return Collections.emptySet();
    }

    public Set<LovWithActivatorsCriterion> getJobMetiers() {
        AvailableSearchCriteriaDto result = search("1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100");
        List<LovCriterion> jobFamilies = result.getCustomlovs().getCustomLov();
        for (LovCriterion lc : jobFamilies) {
            if (lc.getLabel().equals(Lov.JOBS2.getName())) {
                return Collections.unmodifiableSet(new HashSet<>(lc.getCriteria().getCriterion()));
            }
        }
        return Collections.emptySet();
    }
}
