package org.example.auslanderbehorde.formfiller.business;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.auslanderbehorde.formfiller.enums.ServiceType;
import org.example.auslanderbehorde.formfiller.enums.applyforavisa.EconomicActivityVisaDe;
import org.example.auslanderbehorde.formfiller.enums.visaextension.VisaExtensionForEducationalPurposeVisaEnum_DE;
import org.example.auslanderbehorde.formfiller.model.PersonalInfoDTO;
import org.example.auslanderbehorde.formfiller.model.ResidenceTitleInfoDTO;
import org.example.auslanderbehorde.formfiller.model.Section2FormInputs;
import org.example.auslanderbehorde.formfiller.model.Section4FormInputs;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.io.InputStream;

import static org.example.auslanderbehorde.formfiller.business.DriverManager.initDriverHeadless;

public class FormManager {

    public final static Logger logger = LogManager.getLogger(FormManager.class);

    public static PersonalInfoDTO readPersonalInfoFromFile() {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = PersonalInfoDTO.class.getResourceAsStream("/personalInfoDTO.json");
        try {
            return mapper.readValue(is, PersonalInfoDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ResidenceTitleInfoDTO readVisaInfoFromFile() {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = ResidenceTitleInfoDTO.class.getResourceAsStream("/residentTitleInfoDTO.json");
        try {
            return mapper.readValue(is, ResidenceTitleInfoDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isResidenceTitleInfoVerified(ResidenceTitleInfoDTO residenceTitleInfoDTO) {
        logger.info("Verifying form: {}", residenceTitleInfoDTO);
        ServiceType serviceType = residenceTitleInfoDTO.getServiceType();
        Boolean isResidencePermitPresent = residenceTitleInfoDTO.getResidencePermitPresent();
        String residencePermitId = residenceTitleInfoDTO.getResidencePermitId();

        if (serviceType.equals(ServiceType.APPLY_FOR_A_RESIDENCE_TITLE)) {
            if (isResidencePermitPresent == null) {
                return false;
            }

            if (isResidencePermitPresent && residencePermitId == null) {
                return false;
            }

            if (!isResidencePermitPresent && residencePermitId != null) {
                return false;
            }
        }

        if (serviceType.equals(ServiceType.EXTEND_A_RESIDENCE_TITLE)) {
            if (isResidencePermitPresent != null) {
                return false;
            }

            if (residencePermitId == null) {
                return false;
            }
        }

        return true;
    }

    public static void startForm(PersonalInfoDTO personalInfoDTO, ResidenceTitleInfoDTO residenceTitleInfoDTO) {


        Section2FormInputs section2FormInputs = new Section2FormInputs(
                personalInfoDTO.getCitizenshipValue(),
                personalInfoDTO.getApplicationsNumber(),
                personalInfoDTO.getFamilyStatus(),
                residenceTitleInfoDTO.getServiceType(),
                VisaExtensionForEducationalPurposeVisaEnum_DE.PURPOSE_OF_STUDYING);

        Section2FormInputs section2FormInputs_2 = new Section2FormInputs(
                personalInfoDTO.getCitizenshipValue(),
                personalInfoDTO.getApplicationsNumber(),
                personalInfoDTO.getFamilyStatus(),
                residenceTitleInfoDTO.getServiceType(),
                EconomicActivityVisaDe.BLUECARD);

        Section4FormInputs section4FormInputs = new Section4FormInputs(
                personalInfoDTO.getFirstName(),
                personalInfoDTO.getLastName(),
                personalInfoDTO.getEmailAddress(),
                personalInfoDTO.getBirthdate(),
                residenceTitleInfoDTO.getResidencePermitPresent(),
                residenceTitleInfoDTO.getResidencePermitId(),
                ServiceType.EXTEND_A_RESIDENCE_TITLE);
        Section4FormInputs section4FormInputs_2 = new Section4FormInputs(
                personalInfoDTO.getFirstName(),
                personalInfoDTO.getLastName(),
                personalInfoDTO.getEmailAddress(),
                personalInfoDTO.getBirthdate(),
                true,
                residenceTitleInfoDTO.getResidencePermitId(),
                ServiceType.APPLY_FOR_A_RESIDENCE_TITLE);


        RemoteWebDriver remoteWebDriver = initDriverHeadless();
        TerminFinder terminFinder = new TerminFinder(section4FormInputs, section2FormInputs, remoteWebDriver);
        terminFinder.startScanning();


        //RemoteWebDriver remoteWebDriver = initDriverHeadless();
        //TerminFinder terminFinder_2 = new TerminFinder(section4FormInputs_2, section2FormInputs_2, remoteWebDriver);
        //terminFinder_2.startScanning();

    }


}