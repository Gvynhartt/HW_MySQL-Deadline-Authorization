package ru.netology.pages;

import com.codeborne.selenide.Condition;
import ru.netology.data.MsDataHelper;

import static com.codeborne.selenide.Selenide.$x;

public class LoginPage {

    public void enterLogin(MsDataHelper.UserEntry someUser) { // параметр нужен, т.к. мы хотим вставлять разных пользователей #svobodavybora
        $x("//span[@data-test-id='login']/descendant::input[@name='login']").
                setValue(someUser.getDefUserLogin());
    }

    public void enterPassword(MsDataHelper.UserEntry someUser) { // параметр нужен, т.к. мы хотим вставлять разных пользователей #svobodavybora
        $x("//span[@data-test-id='password']/descendant::input[@name='password']").
                setValue(someUser.getDefUserPassword());
    }

    public AuthCodePage pressContinue() {
        $x("//button[@data-test-id='action-login']/span[@class='button__content']").click();
        return new AuthCodePage();
    }

    public void notifyAboutInvalidCredentials() {
        $x("//div[@data-test-id='error-notification']").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }
}
