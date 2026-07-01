package ru.yandex.practicum.sleeptracker.sleepfunctions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SleeplessNightsFunction implements SleepingAnalysisFunction {
    private final String functionMassage = "Бессонных ночей найдено: ";
    private final LocalTime nightPeriodStart = LocalTime.of(0,0);
    private final LocalTime nightPeriodFinish = LocalTime.of(6,0);

    @Override
    public SleepAnalysisResult analyzeSleepingSession(List<SleepingSession> sleepingSession) {


        Map<LocalDate, Boolean> nightWithSleep = sleepingSession.stream()
                .collect(Collectors.toMap(
                        session -> getNightDate(session),
                        session -> isNightSession(session),
                        (existing, replacment) -> existing || replacment,
                        LinkedHashMap::new
                ));

        List<LocalDate> allNightsInLogs = sleepingSession.stream()
                .map(session -> getNightDate(session))
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        long sleeplessNights = allNightsInLogs.stream()
                .filter(date ->!nightWithSleep.getOrDefault(date,false))
                .count();

        return new SleepAnalysisResult(functionMassage, sleeplessNights + " из " + allNightsInLogs.size() + " ночей");

    }

    private LocalDate getNightDate(SleepingSession session) {
        LocalDateTime sessionStart = session.getStartSleepSessionTime();
        LocalTime sessionStartTime = sessionStart.toLocalTime();
        LocalDate sessionStartDay = sessionStart.toLocalDate();

        if (sessionStartTime.isAfter(LocalTime.NOON)) {
            return sessionStartDay.plusDays(1);
        } else {
            return sessionStartDay;
        }
    }

    private boolean isNightSession(SleepingSession session) {
        LocalDateTime sessionStart = session.getStartSleepSessionTime();
        LocalDateTime sessionEnd = session.getEndSleepSessionTime();

        LocalDate nightDate = getNightDate(session);
        LocalDateTime nightStart = nightDate.atTime(nightPeriodStart);
        LocalDateTime nightEnd = nightDate.atTime(nightPeriodFinish);

        if (sessionStart.isBefore(nightEnd) && sessionEnd.isAfter(nightStart)){
            return true;
        } else {
            return false;
        }
    }
}
