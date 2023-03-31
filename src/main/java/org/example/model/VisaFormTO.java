package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VisaFormTO {

    private final Boolean isResidencePermitPresent;
    private final String residencePermitId;
    private final String serviceType;
    private final String visaLabelValue;
    private final String visaPurposeValue;

    public VisaFormTO(@JsonProperty("isResidencePermitPresent") Boolean isResidencePermitPresent,
                      @JsonProperty("residencePermitId") String residencePermitId,
                      @JsonProperty("serviceType") String serviceType,
                      @JsonProperty("visaLabelValue") String visaLabelValue,
                      @JsonProperty("visaPurposeValue") String visaPurposeValue) {
        this.isResidencePermitPresent = isResidencePermitPresent;
        this.residencePermitId = residencePermitId;
        this.serviceType = serviceType;
        this.visaLabelValue = visaLabelValue;
        this.visaPurposeValue = visaPurposeValue;
    }

    public Boolean getResidencePermitPresent() {
        return isResidencePermitPresent;
    }

    public String getResidencePermitId() {
        return residencePermitId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getVisaLabelValue() {
        return visaLabelValue;
    }

    public String getVisaPurposeValue() {
        return visaPurposeValue;
    }

    @Override
    public String toString() {
        return "VisaFormTO{" +
                "isResidencePermitPresent=" + isResidencePermitPresent +
                ", residencePermitId='" + residencePermitId + '\'' +
                ", serviceType='" + serviceType + '\'' +
                ", visaLabelValue='" + visaLabelValue + '\'' +
                ", visaPurposeValue='" + visaPurposeValue + '\'' +
                '}';
    }
}

