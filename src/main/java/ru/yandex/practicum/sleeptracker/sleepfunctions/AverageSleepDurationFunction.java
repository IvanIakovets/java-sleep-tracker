package ru.yandex.practicum.sleeptracker.sleepfunctions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.Duration;
import java.util.List;

public class AverageSleepDurationFunction implements SleepingAnalysisFunction {
    private final String functionMassage = "Средняя продолжительность сессии: ";

    @Override
    public SleepAnalysisResult analyzeSleepingSession(List<SleepingSession> sleepingSession) {
        double averageSessionMinutes = sleepingSession.stream()
                .mapToLong(session -> Duration.between(session.getStartSleepSessionTime(),
                        session.getEndSleepSessionTime()).toMinutes())
                .average()
                .orElse(0.0);

        String result = String.format("%.1f минут", averageSessionMinutes);
        return new SleepAnalysisResult(functionMassage, result);
    }

    public String getFunctionMassage() {
        return functionMassage;
    }
}
