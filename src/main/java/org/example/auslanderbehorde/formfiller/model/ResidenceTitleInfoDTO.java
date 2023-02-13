package org.example.auslanderbehorde.formfiller.model;

import org.example.auslanderbehorde.formfiller.enums.ServiceType;

import java.util.Optional;

public record ResidenceTitleInfoDTO(Boolean isResidencePermitPresent,
                                    String residencePermitId,
                                    ServiceType serviceType) {

}

