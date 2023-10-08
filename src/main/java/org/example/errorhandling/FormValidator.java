package org.example.errorhandling;

import org.example.forms.VisaFormTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FormValidator {

    private static final Logger logger = LoggerFactory.getLogger(FormValidator.class);

    public boolean isResidenceTitleInfoVerified(VisaFormTO visaFormTO) {
        logger.info("Verifying form: {}", visaFormTO);
        String serviceType = visaFormTO.getServiceType();
        Boolean isResidencePermitPresent = visaFormTO.getResidencePermitPresent();
        String residencePermitId = visaFormTO.getResidencePermitId();

        if (serviceType.equals("Apply for a residence title")) {
            if (isResidencePermitPresent == null) {
                return false;
            }

            if (isResidencePermitPresent && residencePermitId == null) {
                return false;
            }

            if (!isResidencePermitPresent.booleanValue() && residencePermitId != null) {
                return false;
            }
        }

        if (serviceType.equals("Extend a residence title")) {

            return residencePermitId != null;
        }

        return true;
    }

}
