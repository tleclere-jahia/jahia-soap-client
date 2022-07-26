package org.foo.modules.jahia.lumesse;

public enum Lov {
    JOB_FAMILY("JOB_FAMILY"), JOB_FAMILY_EXTERNE("JOB_FAMILY_EXTERNE"),
    JOBS("JOBS"), REGION("REGION"), REGIME("REGIME"),
    TYPE_ORGANISME("TYPE_ORGANISME"), ORGANISME("ORGANISME"), CLASSIFICATION("CLASSIFICATION"),
    CONTRACT_TYPE("CONTRACT_TYPE"), COEFFICIENT_RG("COEFFICIENT_RG"), NIVEAU_RG("NIVEAU_RG"),
    CURRENCY("CURRENCY"), COMPENSATION_PERIOD("COMPENSATION_PERIOD"), JOBS2("JOBS2");

    private final String name;

    Lov(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
