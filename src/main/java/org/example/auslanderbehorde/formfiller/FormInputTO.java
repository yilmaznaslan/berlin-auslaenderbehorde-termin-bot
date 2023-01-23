package org.example.auslanderbehorde.formfiller;

import org.example.auslanderbehorde.formfiller.model.FormInputs;
import org.example.auslanderbehorde.formfiller.model.Section4FormInputs;

public record FormInputTO(FormInputs formInputs,
                          Section4FormInputs section4FormInputs) {
    @Override
    public FormInputs formInputs() {
        return formInputs;
    }

    @Override
    public Section4FormInputs section4FormInputs() {
        return section4FormInputs;
    }

    public FormInputTO {
    }
}
