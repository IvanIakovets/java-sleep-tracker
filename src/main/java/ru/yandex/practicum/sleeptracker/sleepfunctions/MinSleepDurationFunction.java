package ru.yandex.practicum.sleeptracker.sleepfunctions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.Duration;
import java.util.List;

public class MinSleepDurationFunction implements SleepingAnalysisFunction {
    private final String functionMassage = "Минимальная продолжительность сессии: ";

    @Override
    public SleepAnalysisResult analyzeSleepingSession(List<SleepingSession> sleepingSession) {
        long minSessionMinutes = sleepingSession.stream()
                .mapToLong(session -> Duration.between(session.getStartSleepSessionTime(),
                        session.getEndSleepSessionTime()).toMinutes())
                .min()
                .orElse(0);

        String result = String.format("%s минут", minSessionMinutes);
        return new SleepAnalysisResult(functionMassage, result);
    }

    public String getFunctionMassage() {
        return functionMassage;
    }
}
