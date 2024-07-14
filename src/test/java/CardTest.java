
import data.DataGeneration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;


import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CardTest {
    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }


    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGeneration.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGeneration.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGeneration.generateDate(daysToAddForSecondMeeting);
        $("[data-test-id='city'] input").setValue(validUser.getCity());

        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);

        $("[data-test-id=date] input").setValue(firstMeetingDate);

        $("[data-test-id='name'] input").setValue(validUser.getName());

        $("[data-test-id='phone'] input").setValue(validUser.getPhone());

        $("[data-test-id='agreement']").click();
        $(byText("Запланировать")).click();
        $(byText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content").
                shouldHave(exactText("Встреча успешно запланирована на " + firstMeetingDate)).

                shouldBe(visible);
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);

        $("[data-test-id=date] input").setValue(secondMeetingDate);
        $(byText("Запланировать")).click();

        $("[data-test-id='replan-notification'] .notification__content").
                shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?")).
                shouldBe(visible);

        $("[data-test-id='replan-notification'] button").click();
        $("[data-test-id='success-notification'] .notification__content").
                shouldHave(exactText("Встреча успешно запланирована на " + secondMeetingDate)).
                shouldBe(visible);
    }


}
