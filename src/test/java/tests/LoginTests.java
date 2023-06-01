package tests;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import ru.netology.data.MsDataHelper;
import ru.netology.data.UserEntry;
import ru.netology.pages.AuthCodePage;
import ru.netology.pages.CardsNotFoundPage;
import ru.netology.pages.LoginPage;

import static com.codeborne.selenide.Selenide.open;

public class LoginTests {

    @AfterAll
    public static void shdClearData() {
        MsDataHelper.cleanDataInDBafterLogin();
    }

    @Test
    @DisplayName("Login with data for Vasya (valid)")
    public void shdLoginAsVasyaNormal() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");

        UserEntry dzaVasya = MsDataHelper.generateDefaultVasya();

        LoginPage loginPage = new LoginPage();
        loginPage.enterLogin(dzaVasya);
        loginPage.enterPassword(dzaVasya);
        loginPage.pressContinue();
        AuthCodePage authCodePage = new AuthCodePage();
        authCodePage.enterAuthCode(MsDataHelper.getLastAuthCodeFromDB());
        authCodePage.pressSubmit();
        CardsNotFoundPage emptySpaces = new CardsNotFoundPage();
        emptySpaces.keepYourHandsOffMyStash();
    }

    @Test
    @DisplayName("Login with data for Petya (valid)")
    public void shdLoginAsPetyaNormal() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");

        UserEntry dzaPetya = MsDataHelper.generateDefaultPetya();

        LoginPage loginPage = new LoginPage();
        loginPage.enterLogin(dzaPetya);
        loginPage.enterPassword(dzaPetya);
        loginPage.pressContinue();
        AuthCodePage authCodePage = new AuthCodePage();
        authCodePage.enterAuthCode(MsDataHelper.getLastAuthCodeFromDB());
        authCodePage.pressSubmit();
        CardsNotFoundPage emptySpaces = new CardsNotFoundPage();
        emptySpaces.keepYourHandsOffMyStash();
    }

    @Test
    @DisplayName("Login with randomly generated user data (invalid)")
    public void shdLoginAsRandomUser() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");

        UserEntry averageJoe = MsDataHelper.generateUserWithRandomCredentials();

        LoginPage loginPage = new LoginPage();
        loginPage.enterLogin(averageJoe);
        loginPage.enterPassword(averageJoe);
        loginPage.pressContinue();
        loginPage.notifyAboutInvalidCredentials();
    }

    @Test
    @DisplayName("Login as Petya with random code (invalid)")
    public void shdLoginAsPetyaRandomCode() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");

        UserEntry dzaPetya = MsDataHelper.generateDefaultPetya();

        LoginPage loginPage = new LoginPage();
        loginPage.enterLogin(dzaPetya);
        loginPage.enterPassword(dzaPetya);
        loginPage.pressContinue();
        AuthCodePage authCodePage = new AuthCodePage();
        authCodePage.enterAuthCode(MsDataHelper.generateRandomAuthCode());
        authCodePage.pressSubmit();
        authCodePage.notifyAboutInvalidCode();
    }

    @Test
    @DisplayName("Login as Vasya with wrong password thrice and poluchit' BAN (инвалид)")
    public void shdGetBlockedAfterTripleWrongPass() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");

        UserEntry dzaVasya = MsDataHelper.generateDefaultVasya();

        LoginPage loginPage = new LoginPage();
        loginPage.enterLogin(dzaVasya);
        loginPage.enterPassword(dzaVasya);
        loginPage.pressContinue();
        AuthCodePage authCodePage = new AuthCodePage();
        authCodePage.enterRandomAuthCode();
        authCodePage.pressSubmit();
        authCodePage.notifyAboutInvalidCode();
        authCodePage.backToLoginPage(); // цикл раз

        loginPage.enterLogin(dzaVasya);
        loginPage.enterPassword(dzaVasya);
        loginPage.pressContinue();
        authCodePage.enterRandomAuthCode();
        authCodePage.pressSubmit();
        authCodePage.notifyAboutInvalidCode();
        authCodePage.backToLoginPage(); // цикл двас

        loginPage.enterLogin(dzaVasya);
        loginPage.enterPassword(dzaVasya);
        loginPage.pressContinue();
        authCodePage.enterRandomAuthCode();
        authCodePage.pressSubmit();
        authCodePage.notifyAboutInvalidCode();
        authCodePage.backToLoginPage(); // цикл Трисс

        loginPage.enterLogin(dzaVasya);
        loginPage.enterPassword(dzaVasya);
        loginPage.pressContinue();
        authCodePage.enterRandomAuthCode();
        authCodePage.pressSubmit();
        authCodePage.notifyAboutCodeEntryLimit(); // кiнец

        /** Как выяснилось, блокировка в SUT действительно есть, правда, почему-то при вводе кода, а не пароля,
         * причём выяснил я это лишь когда стал запускать все тесты одной пачкой и случайно заметил, что на
         * цатую итерацию уведомление поменялось.*/
    }

}
