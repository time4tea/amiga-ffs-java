package net.time4tea.adf.blocks;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class DateCalculatorTest {

    @Test
    void startOfEpoch() {
        assertThat(DateCalculator.calculate(0, 0, 0), equalTo(LocalDateTime.of(1978, 1, 1, 0, 0, 0, 0)));
    }

    @Test
    void someDate() {
        assertThat(DateCalculator.calculate(6105, 1013, 135), equalTo(LocalDateTime.parse("1994-09-19T16:53:02.700")));
    }
}