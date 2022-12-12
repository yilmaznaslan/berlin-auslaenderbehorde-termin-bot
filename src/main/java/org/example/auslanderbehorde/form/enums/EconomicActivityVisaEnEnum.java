package org.example.auslanderbehorde.form.enums;

public enum EconomicActivityVisaEnEnum implements VisaEnum {

    BLUECARD("SERVICEWAHL_EN123-0-1-1-324659", "EU Blue Card / Blaue Karte EU (sect. 18b para. 2)"),
    FREELANCE("SERVICEWAHL_EN123-0-1-1-328332", "Residence permit for a freelance employment - Issuance (sect. 21 para. 5)"),
    LONG_TERM_EU_MEMBER("SERVICEWAHL_EN123-0-1-1-325475", "Residence permit for foreigners with a long-term residence in an EU member state (sect. 38a)"),
    JOB_SEEKING_QUALIFIED("SERVICEWAHL_EN123-0-1-1-324661", "Residence permit for job-seeking qualified skilled workers – Issuance (sect. 20)"),
    JOB_SEEKING_ACADEMIC("SERVICEWAHL_EN123-0-1-1-329328", "Residence permit for qualified skilled workers with an academic education (sect. 18b para. 1)"),
    VOCATIONAL_TRAINING("SERVICEWAHL_EN123-0-1-1-305304", "Residence permit for qualified skilled workers with vocational training (sect. 18a)");

    private final String id;
    private final String dataTag0;

    EconomicActivityVisaEnEnum(String id, String dataTag0) {
        this.id = id;
        this.dataTag0 = dataTag0;
    }

    public String getId() {
        return id;
    }

    public String getDataTag0() {
        return dataTag0;
    }
}
