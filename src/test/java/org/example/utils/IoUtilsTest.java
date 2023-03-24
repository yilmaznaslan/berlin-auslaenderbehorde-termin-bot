package org.example.utils;

import org.example.BaseTestSetup;
import org.example.business.formhandlers.Section4VisaFormHandler;
import org.junit.jupiter.api.Test;

import static org.example.utils.IoUtils.savePage;

class IoUtilsTest extends BaseTestSetup {

    static String path = Section4VisaFormHandler.class.getClassLoader().getResource("page_section3_dateSelection_2023-02-03_10:23:27.html").getPath();
    static String url = "file:".concat(path);

    @Test
    void ASSERT_THAT_isCalenderFound_returns_true() {
        // GIVEN
        driver.get(url);

        // WHEN
        savePage(driver, "testPage", "demo");

        // THEN

    }

}