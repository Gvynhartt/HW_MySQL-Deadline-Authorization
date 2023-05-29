package tests;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.data.MsDataHelper;
import ru.netology.pages.AuthCodePage;
import ru.netology.pages.CardsNotFoundPage;
import ru.netology.pages.LoginPage;

import static com.codeborne.selenide.Selenide.open;

public class LoginTests {

    @Test
    @DisplayName("Login with data for Vasya (valid)")
    public void shdLoginAsVasyaNormal() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");

        MsDataHelper.UserEntry dzaVasya = MsDataHelper.generateDefaultVasya();

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

        MsDataHelper.UserEntry dzaPetya = MsDataHelper.generateDefaultPetya();

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

        MsDataHelper.UserEntry averageJoe = MsDataHelper.generateUserWithRandomCredentials();

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

        MsDataHelper.UserEntry dzaPetya = MsDataHelper.generateDefaultPetya();

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

        MsDataHelper.UserEntry dzaVasya = MsDataHelper.generateDefaultVasya();
        MsDataHelper.UserEntry someRando = MsDataHelper.generateUserWithRandomCredentials();

        LoginPage loginPage = new LoginPage();
        loginPage.enterLogin(dzaVasya);
        loginPage.enterPassword(someRando);
        loginPage.enterPassword(someRando);
        loginPage.enterPassword(someRando);
        loginPage.pressContinue();
        loginPage.notifyAboutInvalidCredentials();

        /** "P.S. Неплохо бы ещё проверить, что при трёхкратном неверном вводе пароля система блокируется" -
         * оно, может, и неплохо, однако неясно, что именно я должен тестировать, т. к. ноль пояснений на предмет
         * что это за блокировка, в чём она выражается и как можно установить факт оной. Лично моя система блокируется
         * от подобных формулировок задач. Это к вопросу о тестировании требований. */
    }

    @Test
    public void shdClearData() {
        MsDataHelper.cleanDataInDBafterLogin();
    } // строго говоря, тестировать удаление нет нужды, однако очистку данных откуда-то запускать надо
}
