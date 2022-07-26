package org.foo.modules.jahia.lumesse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.foo.modules.jahia.lumesse.ws.wsdl.foadvert.AdvertisementDto;
import org.foo.modules.jahia.lumesse.ws.wsdl.foadvert.CountryCriterion;
import org.foo.modules.jahia.lumesse.ws.wsdl.foadvert.LovWithActivatorsCriterion;
import org.foo.modules.jahia.lumesse.ws.wsdl.lov.LovDescendantDto;
import org.jahia.utils.ClassLoaderUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component(service = LumesseService.class)
public class LumesseService {
    private static final Logger logger = LoggerFactory.getLogger(LumesseService.class);

    private final ObjectMapper objectMapper;
    private SecurityInterceptor securityInterceptor;
    private FoAdvertServiceClient foAdvertServiceClient;
    private LovServiceClient lovServiceClient;

    public LumesseService() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Activate
    private void onActivate(Map<String, ?> configuration) {
        securityInterceptor = null;
        foAdvertServiceClient = null;
        lovServiceClient = null;
        if (configuration.containsKey("username") && configuration.containsKey("password")) {
            securityInterceptor = new SecurityInterceptor((String) configuration.get("username"),
                    (String) configuration.get("password"));
            if (configuration.containsKey("foAdvertService.apiKey") && configuration.containsKey("foAdvertService.pageSize")) {
                foAdvertServiceClient = ClassLoaderUtils.executeWith(LumesseService.class.getClassLoader(),
                        () -> new FoAdvertServiceClient(securityInterceptor, (String) configuration.get("foAdvertService.apiKey"),
                                Integer.parseInt((String) configuration.get("foAdvertService.pageSize"))));
            }
            if (configuration.containsKey("lovService.apiKey")) {
                lovServiceClient = ClassLoaderUtils.executeWith(LumesseService.class.getClassLoader(),
                        () -> new LovServiceClient(securityInterceptor, (String) configuration.get("lovService.apiKey")));
            }
        }
    }

    public int countAdvertisements() {
        if (foAdvertServiceClient == null) {
            logger.warn("FoAdvertServiceClient is null");
            return 0;
        }

        try {
            return foAdvertServiceClient.countAdvertisements();
        } catch (Exception e) {
            logger.error("", e);
        }
        return 0;
    }

    public Set<Advertisement> getAllAdvertisements() {
        if (foAdvertServiceClient == null) {
            logger.warn("FoAdvertServiceClient is null");
            return Collections.emptySet();
        }

        Set<AdvertisementDto> advertisements = new HashSet<>();
        try {
            int maxResults = countAdvertisements();
            int firstResult = 0;

            Set<AdvertisementDto> results;
            while (firstResult < maxResults) {
                results = foAdvertServiceClient.getAdvertisements(firstResult);
                advertisements.addAll(results);
                firstResult += results.size();
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return Collections.unmodifiableSet(advertisements.stream().map(Advertisement::new).collect(Collectors.toSet()));
    }

    public void updateFromLumesse() {
        if (foAdvertServiceClient == null) {
            logger.warn("FoAdvertServiceClient is null");
            return;
        }
        if (lovServiceClient == null) {
            logger.warn("LovServiceClient is null");
            return;
        }

        logger.info("Cache Lumesse Job ran at {}", Instant.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

        Set<Advertisement> advertisements = getAllAdvertisements();
        Set<LovDescendantDto> contractTypes = lovServiceClient.getContractTypes();
        Set<CountryCriterion> regions = foAdvertServiceClient.getRegions();
        Set<LovDescendantDto> regimes = lovServiceClient.getRegimes();
        Set<LovDescendantDto> typesOrganisme = lovServiceClient.getTypesOrganisme();
        Set<LovWithActivatorsCriterion> jobFamilies = foAdvertServiceClient.getJobFamilies();
        Set<LovDescendantDto> classifications = lovServiceClient.getClassifications();
        Set<LovWithActivatorsCriterion> jobMetiers = foAdvertServiceClient.getJobMetiers();

        for (Advertisement ad : advertisements) {
            try {
                logger.info("{}", objectMapper.writeValueAsString(ad));
            } catch (JsonProcessingException e) {
                logger.error("", e);
            }
        }

        logger.info("Cache Lumesse Job ended at {}", Instant.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    }

    public List<String> getLovs() {
        return lovServiceClient.getContractTypes().stream().map(LovDescendantDto::getLovName).collect(Collectors.toList());
    }
}
