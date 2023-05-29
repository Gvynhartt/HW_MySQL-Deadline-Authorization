package ru.netology.pages;

import com.codeborne.selenide.Condition;

import static com.codeborne.selenide.Selenide.$x;

public class CardsNotFoundPage {
    String dashboardTitle = "//h2[@data-test-id='dashboard']";
    // здесь могло быть что-то полезное для Вас, но, к сожалению, имеются только издержки безмерно здорового и крайне неконфликтного производственного процесса в IT-конторах. R.I.P.

    public CardsNotFoundPage keepYourHandsOffMyStash() { // проверяем видимость страницы через один из (целого одного из) элементов
        $x("//h2[@data-test-id='dashboard']").shouldBe(Condition.visible);
        return new CardsNotFoundPage();
    }
}
