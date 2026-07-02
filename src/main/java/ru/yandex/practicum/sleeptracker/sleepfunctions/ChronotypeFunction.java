package ru.yandex.practicum.sleeptracker.sleepfunctions;

import ru.yandex.practicum.sleeptracker.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class ChronotypeFunction implements SleepingAnalysisFunction {
    private static final String FUNCTION_MESSAGE = "Ваш хронотип: ";
    private static final LocalTime owlSleepStart = LocalTime.of(23, 0);  // 23:00
    private static final LocalTime owlSleepEnd = LocalTime.of(9, 0);      // 09:00
    private static final LocalTime larkSleepStart = LocalTime.of(22, 0);   // 22:00
    private static final LocalTime larkSleepEnd = LocalTime.of(7, 0);
    NightSessionPredictor nightSessionPredictor = new NightSessionPredictor();

    @Override
    public SleepAnalysisResult analyzeSleepingSession(List<SleepingSession> sleepingSession) {
        Map<LocalDate, List<SleepingSession>> sleepingSessionsByNight = sleepingSession.stream()
                .collect(Collectors.groupingBy(
                        session -> nightSessionPredictor.getNightDate(session),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        Map<Chronotype, Long> chronotypeCount = sleepingSessionsByNight.entrySet().stream()
                .filter(entry -> hasNightSleep(entry.getValue()))
                .map(entry -> determineChronotype(entry.getValue()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.groupingBy(
                        chronotype -> chronotype,
                        Collectors.counting()
                ));
        if (chronotypeCount.isEmpty()) {
            return new SleepAnalysisResult(FUNCTION_MESSAGE, "недостаточно данных для определения");
        }

        long maxCountChronotype = chronotypeCount.values().stream()
                .max(Long::compareTo)
                .orElse(0L);

        List<Chronotype> maxType = chronotypeCount.entrySet().stream()
                .filter(entry -> entry.getValue() == maxCountChronotype)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        Chronotype result;
        if (maxType.size() > 1) {
            result = Chronotype.PIGEON;
        } else {
            result = maxType.get(0);
        }

        return new SleepAnalysisResult(FUNCTION_MESSAGE, result);

    }

    private boolean hasNightSleep(List<SleepingSession> nightSessions) {
        return nightSessions.stream()
                .anyMatch(session -> nightSessionPredictor.isNightSession(session));
    }

    private Optional<Chronotype> determineChronotype(List<SleepingSession> nightSessions) {
        SleepingSession mainSession = nightSessions.stream()
                .filter(nightSessionPredictor::isNightSession)
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

        if (sleepStart.isAfter(owlSleepStart) || wakeEnd.isAfter(owlSleepEnd)) {
            return Optional.of(Chronotype.OWL);
        } else if (sleepStart.isBefore(larkSleepStart) && wakeEnd.isBefore(larkSleepEnd)) {
            return Optional.of(Chronotype.LARK);
        } else {
            return Optional.of(Chronotype.PIGEON);
        }
    }
}
