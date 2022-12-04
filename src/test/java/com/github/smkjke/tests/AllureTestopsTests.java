package com.github.smkjke.tests;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.github.smkjke.api.AuthorizationApi;
import com.github.smkjke.api.TestCaseApi;
import com.github.smkjke.models.Step;
import com.github.smkjke.models.StepsCaseBody;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.github.smkjke.api.AuthorizationApi.ALLURE_TESTOPS_SESSION;
import static com.github.smkjke.api.TestCaseApi.createTestStep;
import static com.github.smkjke.specs.LoginSpecs.*;
import static io.qameta.allure.Allure.step;

import org.openqa.selenium.Cookie;

import java.util.List;

public class AllureTestopsTests extends BaseTest {

    private void setCookies() {
        String authorizationCookie = new AuthorizationApi()
                .getAuthorizationCookie(TOKEN, USERNAME, PASSWORD);

        open("/favicon.ico");
        getWebDriver().manage().addCookie(new Cookie(ALLURE_TESTOPS_SESSION, authorizationCookie));
    }


    @Test
    @DisplayName("Create test case and test steps")
    void createTestCaseAndAddSteps() {
        StepsCaseBody stepsCaseBody = new StepsCaseBody();
        Step stepOne = Step.generateRandomStep();
        Step stepTwo = Step.generateRandomStep();
        stepsCaseBody.setSteps(List.of(stepOne, stepTwo));


        int id = step("Create Test case",
                TestCaseApi::createTestCase);

        step("Create Test steps",
                () -> createTestStep(id, stepsCaseBody));

        step("Check all steps have a correct keyword and name", () -> {
                    setCookies();
                    List<Step> list = stepsCaseBody.getSteps();
                    open("/project/1722/test-cases/" + id);
                    ElementsCollection collection = $$x(".//ul[@class='TreeElement']/li")
                            .shouldHave(CollectionCondition.size(list.size()));

                    for (int i = 0; i < collection.size(); i++) {
                        collection.get(i).$x(".//div[@class='StepKeyword']").shouldHave(text(list.get(i).getKeyword()));
                        collection.get(i).$x(".//pre[@class='Multiline']").shouldHave(text(list.get(i).getName()));
                    }
                }
        );
    }

}
