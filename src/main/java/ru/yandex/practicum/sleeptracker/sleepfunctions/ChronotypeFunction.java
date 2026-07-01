package ru.yandex.practicum.sleeptracker.sleepfunctions;

import ru.yandex.practicum.sleeptracker.Chronotype;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class ChronotypeFunction implements SleepingAnalysisFunction {
    private final String functionMassage = "Ваш хронотип: ";
    private static final LocalTime owlSleepStart = LocalTime.of(23, 0);  // 23:00
    private static final LocalTime owlSleepEnd = LocalTime.of(9, 0);      // 09:00
    private static final LocalTime larkSleepStart = LocalTime.of(22, 0);   // 22:00
    private static final LocalTime larkSleepEnd = LocalTime.of(7, 0);
    private final LocalTime nightPeriodStart = LocalTime.of(0,0);
    private final LocalTime nightPeriodFinish = LocalTime.of(6,0);

    @Override
    public SleepAnalysisResult analyzeSleepingSession(List<SleepingSession> sleepingSession) {
        Map<LocalDate, List<SleepingSession>> sleepingSessionsByNight = sleepingSession.stream()
                .collect(Collectors.groupingBy(
                        session -> getNightDate(session),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        Map<Chronotype, Long> chronotypeCount = sleepingSessionsByNight.entrySet().stream()
                .filter(entry ->hasNightSleep(entry.getValue()))
                .map(entry -> determineChronotype(entry.getValue()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.groupingBy(
                        chronotype -> chronotype,
                        Collectors.counting()
                ));
        if (chronotypeCount.isEmpty()) {
            return new SleepAnalysisResult(functionMassage, "недостаточно данных для определения");
        }

        Chronotype result = chronotypeCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(Chronotype.PIGEON);

        if (chronotypeCount.get(Chronotype.OWL) == chronotypeCount.get(Chronotype.LARK)) {
            result = Chronotype.PIGEON;
        }

        return new SleepAnalysisResult(functionMassage, result);

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

    private boolean hasNightSleep(List<SleepingSession> nightSessions) {
        return nightSessions.stream()
                .anyMatch(this::isNightSession);
    }

    private boolean isNightSession(SleepingSession session) {
        LocalDateTime sessionStart = session.getStartSleepSessionTime();
        LocalDateTime sessionEnd = session.getEndSleepSessionTime();

        LocalDate nightDate = getNightDate(session);
        LocalDateTime nightStart = nightDate.atTime(nightPeriodStart);
        LocalDateTime nightEnd = nightDate.atTime(nightPeriodFinish);

        if (sessionStart.isBefore(nightEnd) && sessionEnd.isAfter(nightStart)) {
            return true;
        } else {
            return false;
        }
    }
    private Optional<Chronotype> determineChronotype(List<SleepingSession> nightSessions) {
        SleepingSession mainSession = nightSessions.stream()
                .filter(this::isNightSession)
                .max(Comparator.comparingLong(session ->
                        java.time.Duration.between(
                                session.getStartSleepSessionTime(),
                                session.getEndSleepSessionTime()
                        ).toMinutes()
                ))
                .orElse(null);

        if (mainSession == null) {
            return Optional.empty();
        }

        LocalTime sleepStart = mainSession.getStartSleepSessionTime().toLocalTime();
        LocalTime wakeEnd = mainSession.getEndSleepSessionTime().toLocalTime();

        if (sleepStart.isAfter(owlSleepStart) && wakeEnd.isAfter(owlSleepEnd)) {
            return Optional.of(Chronotype.OWL);
        } else if (sleepStart.isBefore(larkSleepStart) && wakeEnd.isBefore(larkSleepEnd)) {
            return Optional.of(Chronotype.LARK);
        } else {
            return Optional.of(Chronotype.PIGEON);
        }
    }
}
