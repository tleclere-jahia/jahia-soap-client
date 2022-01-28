package fr.lsr.jahia.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.lsr.jahia.ws.wsdl.foadvert.AdvertisementDto;
import fr.lsr.jahia.ws.wsdl.foadvert.CountryCriterion;
import fr.lsr.jahia.ws.wsdl.foadvert.LovWithActivatorsCriterion;
import fr.lsr.jahia.ws.wsdl.lov.LovDescendantDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class LumesseService {
    private static final Logger logger = LoggerFactory.getLogger(LumesseService.class);

    private FoAdvertServiceClient foAdvertServiceClient;
    private LovServiceClient lovServiceClient;
    private final ObjectMapper objectMapper;

    public LumesseService() {
        objectMapper = new ObjectMapper();
    }

    public void setFoAdvertServiceClient(FoAdvertServiceClient foAdvertServiceClient) {
        this.foAdvertServiceClient = foAdvertServiceClient;
    }

    public void setLovServiceClient(LovServiceClient lovServiceClient) {
        this.lovServiceClient = lovServiceClient;
    }

    public int countAdvertisements() {
        try {
            return foAdvertServiceClient.countAdvertisements();
        } catch (Exception e) {
            logger.error("", e);
        }
        return 0;
    }

    public Set<Advertisement> getAllAdvertisements() {
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info("Cache Lumesse Job ran at " + dateFormat.format(new Date()));

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

        logger.info("Cache Lumesse Job ended at " + dateFormat.format(new Date()));
    }
}
