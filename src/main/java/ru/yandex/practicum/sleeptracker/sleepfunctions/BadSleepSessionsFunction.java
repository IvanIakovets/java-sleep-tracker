package ru.yandex.practicum.sleeptracker.sleepfunctions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepQuality;
import ru.yandex.practicum.sleeptracker.SleepingAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;

public class BadSleepSessionsFunction implements SleepingAnalysisFunction {
    private final String functionMassage = "Количество сессий с плохим качеством сна: ";

    @Override
    public SleepAnalysisResult analyzeSleepingSession(List<SleepingSession> sleepingSession) {
        long bedSession = sleepingSession.stream()
                .filter(session -> session.getSleepQuality() == (SleepQuality.BAD))
                .count();

        if (bedSession == 0) {
            return new SleepAnalysisResult(functionMassage, "отсутствуют");
        }
        if (bedSession == sleepingSession.size()) {
            return new SleepAnalysisResult(functionMassage, "все сессии");
        }

        return new SleepAnalysisResult(functionMassage, bedSession);
    }

    public String getFunctionMassage() {
        return functionMassage;
    }
}
