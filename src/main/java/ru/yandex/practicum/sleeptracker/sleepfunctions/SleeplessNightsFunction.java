package ru.yandex.practicum.sleeptracker.sleepfunctions;

import ru.yandex.practicum.sleeptracker.NightSessionPredictor;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SleeplessNightsFunction implements SleepingAnalysisFunction {
    private static final String FUNCTION_MESSAGE = "Бессонных ночей найдено: ";
    private static final NightSessionPredictor nightSessionPredictor = new NightSessionPredictor();

    @Override
    public SleepAnalysisResult analyzeSleepingSession(List<SleepingSession> sleepingSession) {


        Map<LocalDate, Boolean> nightWithSleep = sleepingSession.stream()
                .collect(Collectors.toMap(
                        session -> nightSessionPredictor.getNightDate(session),
                        session -> nightSessionPredictor.isNightSession(session),
                        (existing, replacment) -> existing || replacment,
                        LinkedHashMap::new
                ));

        LocalDate logDaysStart = sleepingSession.stream()
                .map(session -> nightSessionPredictor.getNightDate(session))
                .min(LocalDate::compareTo)
                .orElse(null);

        LocalDate logDaysEnd = sleepingSession.stream()
                .map(session -> nightSessionPredictor.getNightDate(session))
                .max(LocalDate::compareTo)
                .orElse(null);

        if (logDaysStart == null || logDaysEnd == null) {
            return new SleepAnalysisResult(FUNCTION_MESSAGE, "нет данных");
        }

        List<LocalDate> allNights = logDaysStart.datesUntil(logDaysEnd.plusDays(1))
                .collect(Collectors.toList());


        long sleeplessNights = allNights.stream()
                .filter(date -> !nightWithSleep.getOrDefault(date,false))
                .count();

        return new SleepAnalysisResult(FUNCTION_MESSAGE, sleeplessNights + " из " + allNights.size() + " ночей");

    }
}
