package org.example.auslanderbehorde.formfiller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.auslanderbehorde.formfiller.enums.ServiceType;

public class ResidenceTitleInfoDTO {

    private final Boolean isResidencePermitPresent;
    private final String residencePermitId;
    private final ServiceType serviceType;

    public ResidenceTitleInfoDTO(@JsonProperty("isResidencePermitPresent") Boolean isResidencePermitPresent,
                                 @JsonProperty("residencePermitId") String residencePermitId,
                                 @JsonProperty("serviceType") ServiceType serviceType) {
        this.isResidencePermitPresent = isResidencePermitPresent;
        this.residencePermitId = residencePermitId;
        this.serviceType = serviceType;
    }

    public Boolean getResidencePermitPresent() {
        return isResidencePermitPresent;
    }

    public String getResidencePermitId() {
        return residencePermitId;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    @Override
    public String toString() {
        return "ResidenceTitleInfoDTO{" + "isResidencePermitPresent=" + isResidencePermitPresent + ", residencePermitId='" + residencePermitId + '\'' + ", serviceTyp=" + serviceType + '}';
    }
}

