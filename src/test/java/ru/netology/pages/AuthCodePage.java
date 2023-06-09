package ru.netology.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import ru.netology.data.MsDataHelper;
import ru.netology.data.VerificationCode;

import static com.codeborne.selenide.Selenide.$x;

public class AuthCodePage {

    public void enterAuthCode(VerificationCode someCode) {
        $x("//span[@data-test-id='code']/descendant::input[@name='code']").
                setValue(someCode.getUserCode());
    }

    public void enterRandomAuthCode() {
        $x("//span[@data-test-id='code']/descendant::input[@name='code']").
                setValue(MsDataHelper.generateRandomAuthCode().getUserCode());
    }

    public CardsNotFoundPage pressSubmit() {
        $x("//button[@data-test-id='action-verify']/span[@class='button__content']").click();
        return new CardsNotFoundPage();
    }

    public void notifyAboutInvalidCode() {
        $x("//div[@data-test-id='error-notification']").shouldHave(Condition.text("Ошибка! Неверно указан код! Попробуйте ещё раз."));
    }

    public LoginPage backToLoginPage() {
        Selenide.back();
        return new LoginPage();
    }

    public LoginPage notifyAboutCodeEntryLimit() {
        $x("//div[@data-test-id='error-notification']/descendant::div[@class='notification__content']")
                .shouldHave(Condition.text("Ошибка! Превышено количество попыток ввода кода!"));
        return new LoginPage();
    }
}
