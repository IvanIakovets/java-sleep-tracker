package ru.yandex.practicum.sleeptracker.sleepfunctions;

import ru.yandex.practicum.sleeptracker.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class ChronotypeFunction implements SleepingAnalysisFunction {
    private static final String FUNCTION_MESSAGE = "Ваш хронотип: ";
    private static final NightSessionPredictor nightSessionPredictor = new NightSessionPredictor();

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

        Chronotype result = maxType.size() > 1 ? Chronotype.PIGEON : maxType.get(0);

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
                        Duration.between(
                                session.getStartSleepSessionTime(),
                                session.getEndSleepSessionTime()
                        ).toMinutes()
                ))
                .orElse(null);

        if (mainSession == null) {
            return Optional.empty();
        }

        LocalTime startTime = mainSession.getStartSleepSessionTime().toLocalTime();
        LocalTime endTime = mainSession.getEndSleepSessionTime().toLocalTime();

        boolean isOwlStart = startTime.isAfter(LocalTime.of(23, 0)) ||
                (startTime.isBefore(LocalTime.of(6, 0)) && startTime.isAfter(LocalTime.of(0, 0)));

        boolean isOwlEnd = endTime.isAfter(LocalTime.of(9, 0)) ||
                (endTime.isBefore(LocalTime.of(6, 0)) && endTime.isAfter(LocalTime.of(0, 0)));

        boolean isLarkStart = startTime.isAfter(LocalTime.of(18, 0)) &&
                startTime.isBefore(LocalTime.of(22, 0));

        boolean isLarkEnd = endTime.isAfter(LocalTime.of(4, 0)) &&
                endTime.isBefore(LocalTime.of(7, 0));

        if (isOwlStart && isOwlEnd) {
            return Optional.of(Chronotype.OWL);
        } else if (isLarkStart && isLarkEnd) {
            return Optional.of(Chronotype.LARK);
        } else {
            return Optional.of(Chronotype.PIGEON);
        }
    }
}