package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.*;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;

public class MsDataHelper {

    @Value
    public static class UserEntry {
        private String defUserLogin;
        private String defUserPassword;
    }

    public static UserEntry generateDefaultVasya() {
        UserEntry defaultVasya = new UserEntry("vasya", "qwerty123");
        return defaultVasya;
    }

    public static UserEntry generateDefaultPetya() {
        UserEntry defaultPetya = new UserEntry("petya", "123qwerty");
        return defaultPetya;
    }

    public static UserEntry generateUserWithRandomCredentials() {
        Faker randomUser = new Faker(new Locale("en"));
        String randomLogin = randomUser.food().vegetable().replaceAll("[ ]", "") + randomUser.animal().name() +
                randomUser.number().numberBetween(1984, 2007);
        String randomPassword = randomUser.phoneNumber().cellPhone().replaceAll("[ +()-]", "");
        UserEntry randomEntry = new UserEntry(randomLogin, randomPassword);
        return randomEntry;
    }

    @SneakyThrows
    public static VerificationCode getLastAuthCodeFromDB() {
        QueryRunner roadRunner = new QueryRunner();
        var codeQuery = "SELECT code FROM auth_codes ORDER BY created DESC limit 1;";

        try (
                var whosYourConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/app-db", "abobus", "3450d");
        ) {
            var charCodeFromDB = roadRunner.query(whosYourConnection, codeQuery, new ScalarHandler<String>());
            return new VerificationCode(charCodeFromDB);
        } catch (SQLException codeQueryException) {
            codeQueryException.printStackTrace();
        }
        return null;
    }

    public static VerificationCode generateRandomAuthCode() {
        Faker randomCode = new Faker();
        String charCode = Integer.toString(randomCode.number().numberBetween(9999, 999999));
        return new VerificationCode(charCode);
    }

    @SneakyThrows
    public static void cleanDataInDBafterLogin() { // та самая "настройка вычистки данных за SUT"
        QueryRunner roadRunner = new QueryRunner();
        // Итак, требуется удалять целую пачку данных:
        // 1) все сгенерированые коды авторизации,
        // 2) карты пользователей,
        // 3) самих пользователей,
        // причём именно в таком порядке.
        String clearAuthCodes = "DELETE FROM auth_codes";
        String clearCards = "DELETE FROM cards";
        String clearUsers = "DELETE FROM users";

        try (
                Connection whosYourConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/app-db", "abobus", "3450d");
        ) {
            int codesCleared = roadRunner.update(whosYourConnection, clearAuthCodes);
            int cardsCleared = roadRunner.update(whosYourConnection, clearCards);
            int usersCleared = roadRunner.update(whosYourConnection, clearUsers);
        }
    }
}
