package com.github.smkjke.tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.smkjke.config.CredentialsConfig;
import com.github.smkjke.config.WebDriverProvider;
import com.github.smkjke.helpers.Attachments;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import io.qameta.allure.selenide.AllureSelenide;

public class BaseTest {

    public static CredentialsConfig credentialsConfig = ConfigFactory.create(CredentialsConfig.class);

    @BeforeAll
    static void setUp() {
        WebDriverProvider.configure();
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterEach
    void addAttachments() {
        Attachments.screenshotAs("Last screenshot");
        Attachments.pageSource();
        Attachments.browserConsoleLogs();
        Attachments.addVideo();
    }

}
