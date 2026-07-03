package ru.yandex.practicum.sleeptracker.sleepfunctions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.Duration;
import java.util.List;

public class MaxSleepDurationFunction implements SleepingAnalysisFunction {
    private static final String FUNCTION_MESSAGE = "Максимальная продолжительность сессии: ";

    @Override
    public SleepAnalysisResult analyzeSleepingSession(List<SleepingSession> sleepingSession) {
        long maxSessionMinutes = sleepingSession.stream()
                .mapToLong(session -> Duration.between(session.getStartSleepSessionTime(),
                        session.getEndSleepSessionTime()).toMinutes())
                .max()
                .orElse(0);

        String result = String.format("%s минут", maxSessionMinutes);
        return new SleepAnalysisResult(FUNCTION_MESSAGE, result);
    }
}
