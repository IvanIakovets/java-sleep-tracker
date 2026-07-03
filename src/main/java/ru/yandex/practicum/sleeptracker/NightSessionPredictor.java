package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class NightSessionPredictor {
    private final LocalTime nightPeriodStart = LocalTime.of(0,0);
    private final LocalTime nightPeriodFinish = LocalTime.of(6,0);

    public boolean isNightSession(SleepingSession session) {
        LocalDateTime sessionStart = session.getStartSleepSessionTime();
        LocalDateTime sessionEnd = session.getEndSleepSessionTime();

        LocalDate nightDate = getNightDate(session);
        LocalDateTime nightStart = nightDate.atTime(nightPeriodStart);
        LocalDateTime nightEnd = nightDate.atTime(nightPeriodFinish);

        return sessionStart.isBefore(nightEnd) && sessionEnd.isAfter(nightStart);
    }

    public LocalDate getNightDate(SleepingSession session) {
        LocalDateTime sessionStart = session.getStartSleepSessionTime();
        LocalTime sessionStartTime = sessionStart.toLocalTime();
        LocalDate sessionStartDay = sessionStart.toLocalDate();

        if (sessionStartTime.isAfter(LocalTime.NOON)) {
            return sessionStartDay.plusDays(1);
        } else {
            return sessionStartDay;
        }
    }
}
