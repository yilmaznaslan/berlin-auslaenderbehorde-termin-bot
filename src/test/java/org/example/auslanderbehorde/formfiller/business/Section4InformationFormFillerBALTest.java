package org.example.auslanderbehorde.formfiller.business;

import org.example.auslanderbehorde.formfiller.exceptions.ElementNotFoundTimeoutException;
import org.example.auslanderbehorde.formfiller.exceptions.InteractionFailedException;
import org.example.auslanderbehorde.formfiller.model.Section4FormInputs;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import static org.example.auslanderbehorde.formfiller.enums.Section4FormParameterEnum.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class Section4InformationFormFillerBALTest {

    String path_DE = Section2ServiceSelection.class.getClassLoader().getResource("page_timeslot_0_2023-01-05_06:10:23.html").getPath();
    String url_DE = "file:".concat(path_DE);
    String firstName = "firstName";
    String lastName = "lastname";
    String email = "yilmazn.aslan@gmail.com";
    String birthdate = "12.03.1993";
    static ChromeDriver driver;

    Section4FormInputs form = new Section4FormInputs(firstName, lastName, email, birthdate, true);

    @BeforeAll
    static void initDriver() {
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
    }

    @AfterAll
    static void quitDriver() {
        driver.quit();
    }

    @Test
    void ASSERT_THAT_firstName_is_entered_WHEN_enterFirstName_is_called() throws ElementNotFoundTimeoutException, InterruptedException {
        // GIVEN
        driver.get(url_DE);
        Section4InformationFormFillerBAL formFiller = new Section4InformationFormFillerBAL(form, driver);

        // WHEN
        formFiller.enterFirstName();

        // THEN
        String elementId = FIRSTNAME.getId();
        String elementDescription = FIRSTNAME.name();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        Assertions.assertEquals(firstName, element.getAttribute("value"));
    }

    @Test
    void ASSERT_THAT_lastName_is_entered_WHEN_enterLastName_is_called() throws ElementNotFoundTimeoutException, InterruptedException {
        // GIVEN
        driver.get(url_DE);

        Section4InformationFormFillerBAL formFiller = new Section4InformationFormFillerBAL(form, driver);

        // WHEN
        formFiller.enterLastName();

        // THEN
        String elementId = LASTNAME.getId();
        String elementDescription = LASTNAME.name();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        Assertions.assertEquals(lastName, element.getAttribute("value"));
        ;
    }

    @Test
    void ASSERT_THAT_email_is_entered_WHEN_enterEmail_is_called() throws ElementNotFoundTimeoutException, InterruptedException {
        // GIVEN
        driver.get(url_DE);

        Section4InformationFormFillerBAL formFiller = new Section4InformationFormFillerBAL(form, driver);

        // WHEN
        formFiller.enterEmail();

        // THEN
        String elementId = EMAIL.getId();
        String elementDescription = EMAIL.name();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        Assertions.assertEquals(email, element.getAttribute("value"));
        ;
    }

    @Test
    void ASSERT_THAT_birthdate_is_entered_WHEN_enterBirthdate_is_called() throws ElementNotFoundTimeoutException, InterruptedException {
        // GIVEN
        driver.get(url_DE);
        Section4InformationFormFillerBAL formFiller = new Section4InformationFormFillerBAL(form, driver);

        // WHEN
        formFiller.enterBirthdate();

        // THEN
        String elementId = BIRTHDATE.getId();
        String elementDescription = BIRTHDATE.name();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        Assertions.assertEquals(birthdate, element.getAttribute("value"));
        ;
    }

    @Test
    void ASSERT_THAT_residencePermit_is_selected_true_WHEN_selectResidencePermit_is_called_GIVEN_residencePermit_exist() throws ElementNotFoundTimeoutException, InterruptedException, InteractionFailedException {
        // GIVEN
        driver.get(url_DE);
        Section4InformationFormFillerBAL formFiller = new Section4InformationFormFillerBAL(form, driver);

        // WHEN
        formFiller.selectResidencePermit();

        // THEN
        String elementId = RESIDENCE_PERMIT.getId();
        String elementDescription = RESIDENCE_PERMIT.name();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        Select select = new Select(element);
        WebElement option = select.getFirstSelectedOption();
        String actualValue = option.getText();
        Assertions.assertEquals("Ja", actualValue);
        ;
    }

    @Test
    void ASSERT_THAT_residencePermit_is_selected_true_WHEN_selectResidencePermit_is_called_GIVEN_residencepermit_not_exist() throws ElementNotFoundTimeoutException, InterruptedException, InteractionFailedException {
        // GIVEN
        driver.get(url_DE);
        Section4FormInputs form = new Section4FormInputs(firstName, lastName, email, birthdate, false);
        Section4InformationFormFillerBAL formFiller = new Section4InformationFormFillerBAL(form, driver);

        // WHEN
        formFiller.selectResidencePermit();

        // THEN
        String elementId = RESIDENCE_PERMIT.getId();
        String elementDescription = RESIDENCE_PERMIT.name();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        Select select = new Select(element);
        WebElement option = select.getFirstSelectedOption();
        String actualValue = option.getText();
        Assertions.assertEquals("Nein", actualValue);
        ;
    }

    @Test
    void ASSERT_THAT_form_is_sent_WHEN_sendForm_is_called_GIVEN_form_is_filled() throws ElementNotFoundTimeoutException, InterruptedException, InteractionFailedException {
        // GIVEN
        driver.get(url_DE);
        Section4InformationFormFillerBAL formFiller = new Section4InformationFormFillerBAL(form, driver);
        formFiller.fillForm();

        // WHEN
        formFiller.sendForm();

        // THEN
        ;
    }

    @Test
    void ASSERT_THAT_form_is_filled_and_sent_WHEN_sendForm_is_called_GIVEN_form_is_filled() throws ElementNotFoundTimeoutException, InterruptedException, InteractionFailedException {
        // GIVEN
        driver.get(url_DE);

        // WHEN
        Section4InformationFormFillerBAL spiedFormFiller = spy(new Section4InformationFormFillerBAL(form, driver));
        spiedFormFiller.fillAndSendForm();

        // THEN
        verify(spiedFormFiller).fillForm();
        verify(spiedFormFiller).sendForm();
    }
}