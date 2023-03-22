package org.example.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.PersonalInfoFormTO;
import org.example.model.VisaFormTO;

import java.io.IOException;
import java.io.InputStream;

public class IoUtils {

    private final static Logger logger = LogManager.getLogger(IoUtils.class);

    public static PersonalInfoFormTO readPersonalInfoFromFile() {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = PersonalInfoFormTO.class.getResourceAsStream("/DEFAULT_PERSONAL_INFO_FORM.json");
        try {
            return mapper.readValue(is, PersonalInfoFormTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static VisaFormTO readVisaInfoFromFile() {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = VisaFormTO.class.getResourceAsStream("/DEFAULT_VISA_APPLICATION_FORM.json");
        try {
            return mapper.readValue(is, VisaFormTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
