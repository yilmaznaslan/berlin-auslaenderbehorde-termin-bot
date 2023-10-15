package com.yilmaznaslan.forms;

public class FormTO {

    private final PersonalInfoFormTO personalInfoFormTO;
    private final VisaFormTO visaFormTO;

    public FormTO(PersonalInfoFormTO personalInfoFormTO, VisaFormTO visaFormTO) {
        this.personalInfoFormTO = personalInfoFormTO;
        this.visaFormTO = visaFormTO;
    }


    public PersonalInfoFormTO getPersonalInfoFormTO() {
        return personalInfoFormTO;
    }

    public VisaFormTO getVisaFormTO() {
        return visaFormTO;
    }
}
