package org.example.auslanderbehorde.formfiller;

import org.example.auslanderbehorde.formfiller.model.Section2FormInputs;
import org.example.auslanderbehorde.formfiller.model.Section4FormInputs;

public record FormInputTO(Section2FormInputs section2FormInputs,
                          Section4FormInputs section4FormInputs) {
    public Section2FormInputs section2FormInputs() {
        return section2FormInputs;
    }

    @Override
    public Section4FormInputs section4FormInputs() {
        return section4FormInputs;
    }

    public FormInputTO {
    }
}
