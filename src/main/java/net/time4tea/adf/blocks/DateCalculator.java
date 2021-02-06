package net.time4tea.adf.blocks;

import java.time.LocalDateTime;
import java.time.Month;

public class DateCalculator {
    public static LocalDateTime calculate(int days, int mins, int ticks) {
        return LocalDateTime.of(1978, Month.JANUARY, 1, 0, 0, 0)
                .plusDays(days)
                .plusMinutes(mins)
                .plusNanos((1_000_000_000 / 50L) * ticks);
    }
}
