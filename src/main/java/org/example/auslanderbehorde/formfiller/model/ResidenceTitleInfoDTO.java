package org.example.auslanderbehorde.formfiller.model;

import org.example.auslanderbehorde.formfiller.enums.ServiceType;

import java.util.Optional;

public record ResidenceTitleInfoDTO(Optional<Boolean> isResidencePermitPresent,
                                    Optional<String> residencePermitId,
                                    ServiceType serviceType) {

}

