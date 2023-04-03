package org.example;

import org.example.formhandlers.Section2ServiceSelectionHandler;
import org.example.model.PersonalInfoFormTO;
import org.example.model.VisaFormTO;
import org.junit.jupiter.api.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

class TerminFinderTest extends BaseTestSetup{

    static String pathToScreenShot = TerminFinder.class.getClassLoader().getResource("screenshot.png").getPath();

    private PersonalInfoFormTO personalInfoFormTO;
    private VisaFormTO visaFormTO;
    private TerminFinder terminFinder;

    RemoteWebDriver remoteWebDriver = mock(RemoteWebDriver.class);

    @BeforeEach
    void setUp() {
        personalInfoFormTO = mock(PersonalInfoFormTO.class);
        visaFormTO = mock(VisaFormTO.class);
        terminFinder = new TerminFinder(visaFormTO, personalInfoFormTO, remoteWebDriver);
    }

    @AfterEach
    void tearDown() {
        terminFinder = null;
    }

    @Test
    @DisplayName("Should handle exception when IFormHandler.fillAndSendForm() throws exception")
    void testFillAndSendFormWithExceptionHandlingWhenIFormHandlerThrowsException() throws InterruptedException {
        // Given
        Section2ServiceSelectionHandler formHandler = mock(Section2ServiceSelectionHandler.class);
        when(formHandler.fillAndSendForm()).thenThrow(new TimeoutException());
        when(remoteWebDriver.getPageSource()).thenReturn("");
        when(remoteWebDriver.getScreenshotAs(OutputType.FILE)).thenReturn(new File(pathToScreenShot));

        // When
        boolean actual = terminFinder.fillAndSendFormWithExceptionHandling(formHandler);

        // Then
        assertFalse(actual);
    }


    @Test
    void testFillAndSendFormWithExceptionHandlingWhenIFormHandlerThrowsExceptionl() throws InterruptedException {
        // Given
        TerminFinder terminFinderSpy = spy(new TerminFinder(personalInfoFormTO, visaFormTO));
        doThrow(new WebDriverException()).when(terminFinderSpy).getFormPage();

        // When && Then
        Assertions.assertDoesNotThrow(() -> terminFinderSpy.run());

        // Then
        verify(terminFinderSpy, never()).fillAndSendFormWithExceptionHandling(any());

    }
}
