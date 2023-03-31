package org.example.formhandlers;

import org.example.BaseTestSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Section5ReservationHandlerTest extends BaseTestSetup {

    String path_DE = Section5ReservationHandlerTest.class.getClassLoader().getResource("page_after_step4_sendform_21_2023-01-16_12:45:02.html").getPath();
    String url_DE = "file:".concat(path_DE);

    @Test
    void ASSERT_THAT_form_is_sent_WHEN_sendForm_is_called() {
        // GIVEN
        driver.get(url_DE);

        Section5ReservationHandler formFiller = new Section5ReservationHandler(driver);

        // WHEN && THEN
        Assertions.assertDoesNotThrow(() -> formFiller.fillAndSendForm());

    }

}