package fr.lsr.jahia.ws;

public enum Lov {
    JOB_FAMILY("Domaine de métiers externe"), JOB_FAMILY_EXTERNE("Domaine de mtiers externe"), JOBS(
            "Metiers la Secu recrute"), REGION("Region@France"), REGIME("Regime"), TYPE_ORGANISME(
            "TypeOrganisme"), ORGANISME("Organisme"), CLASSIFICATION("GrilleClassification"), CONTRACT_TYPE(
            "ContractType"), COEFFICIENT_RG("Coefficient RG"), NIVEAU_RG("Niveau RG"), CURRENCY(
            "Currency"), COMPENSATION_PERIOD("CompensationPeriod"), JOBS2("Métiers (la Sécu recrute)");
    private String name;

    Lov(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}