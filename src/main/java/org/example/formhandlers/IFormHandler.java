package org.example.formhandlers;

import org.example.exceptions.FormValidationFailed;
import org.openqa.selenium.remote.RemoteWebDriver;

public interface IFormHandler {

    boolean fillAndSendForm() throws InterruptedException, FormValidationFailed;

    RemoteWebDriver getDriver();
}
