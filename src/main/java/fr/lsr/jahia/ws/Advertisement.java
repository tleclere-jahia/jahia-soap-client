package fr.lsr.jahia.ws;

import fr.lsr.jahia.ws.wsdl.foadvert.AdvertisementDto;
import fr.lsr.jahia.ws.wsdl.foadvert.CustomField;
import fr.lsr.jahia.ws.wsdl.foadvert.LovCriterion;
import fr.lsr.jahia.ws.wsdl.foadvert.LovWithActivatorsCriterion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Advertisement {
    private String typeOrganisme;
    private String contractType;
    private String city;
    private String organisme;
    private String niveau;
    private Date postingEndDate;
    private Date postingStartDate;
    private String jobNumber;
    private String currency;
    private String compensationPeriod;
    private String compensation;
    private String title;
    private Long id;
    private String metier;
    private List<CustomField> customFields;
    private AdvertisementDto advertisementDto;

    public Advertisement(AdvertisementDto advertisementDto) {
        this.advertisementDto = advertisementDto;
        this.title = advertisementDto.getJobTitle();
        this.id = advertisementDto.getId();
        this.city = advertisementDto.getLocation();
        if (advertisementDto.getPostingEndDate() != null) {
            this.postingEndDate = advertisementDto.getPostingEndDate().toGregorianCalendar().getTime();
        }
        if (advertisementDto.getPostingStartDate() != null) {
            this.postingStartDate = advertisementDto.getPostingStartDate().toGregorianCalendar().getTime();
        }
        jobNumber = advertisementDto.getJobNumber();
        List<LovCriterion> list = new ArrayList<>();
        if (advertisementDto.getCustomLovs() != null) {
            list.addAll(advertisementDto.getCustomLovs().getCustomLov());
        }
        if (advertisementDto.getStandardLovs() != null) {
            list.addAll(advertisementDto.getStandardLovs().getStandardLov());
        }

        String lovValue;
        LovWithActivatorsCriterion criterion = null;
        for (LovCriterion lovCriterion : list) {
            String label = null;
            String value = null;
            if (lovCriterion.getCriteria() != null && !lovCriterion.getCriteria().getCriterion().isEmpty()) {
                criterion = lovCriterion.getCriteria().getCriterion().get(0);
                if (criterion != null) {
                    label = criterion.getLabel();
                    value = criterion.getValue();
                }
            }

            if (label != null) {
                lovValue = lovCriterion.getValue();
                if (Lov.ORGANISME.getName().equals(lovValue)) {
                    this.organisme = label;
                } else if (Lov.CONTRACT_TYPE.getName().equals(lovValue)) {
                    this.contractType = label;
                } else if (Lov.NIVEAU_RG.getName().equals(lovValue)) {
                    this.niveau = label;
                } else if (Lov.CURRENCY.getName().equals(lovValue)) {
                    this.currency = label;
                } else if (Lov.COMPENSATION_PERIOD.getName().equals(lovValue)) {
                    this.compensationPeriod = label;
                } else if (Lov.JOBS.getName().equals(lovValue)) {
                    this.metier = value;
                }
            }

            if (advertisementDto.isShowCompensation() && (advertisementDto.getCompensationMinValue() != null
                    || advertisementDto.getCompensationMaxValue() != null)) {
                StringBuilder s = new StringBuilder();
                if (advertisementDto.getCompensationMinValue() != null) {
                    s.append(advertisementDto.getCompensationMinValue().intValue());
                }
                if (advertisementDto.getCompensationMaxValue() != null) {
                    s.append("-");
                    s.append(advertisementDto.getCompensationMaxValue().intValue());
                }
                if (this.currency != null) {
                    s.append(" ");
                    s.append(this.currency);
                }
                s.append(" par ");
                if (this.compensationPeriod != null) {
                    s.append(this.compensationPeriod);
                }
                this.compensation = s.toString().toLowerCase();
            }

            if (advertisementDto.getCustomFields() != null) {
                customFields = advertisementDto.getCustomFields().getCustomField();
            }
        }
    }

    public AdvertisementDto getAdvertisementDto() {
        return advertisementDto;
    }

    public Long getId() {
        return id;
    }

    public String getOrganisme() {
        return organisme;
    }

    public String getContractType() {
        return contractType;
    }

    public String getTitle() {
        return title;
    }

    public String getCity() {
        return city;
    }

    public String getNiveau() {
        return niveau;
    }

    public String getCompensation() {
        return compensation;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public List<CustomField> getCustomFields() {
        return customFields;
    }
}