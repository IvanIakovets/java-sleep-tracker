package ru.yandex.practicum.sleeptracker.sleepfunctions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepQuality;
import ru.yandex.practicum.sleeptracker.SleepingAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;

public class BadSleepSessionsFunction implements SleepingAnalysisFunction {
    private static final String FUNCTION_MESSAGE = "Количество сессий с плохим качеством сна: ";

    @Override
    public SleepAnalysisResult analyzeSleepingSession(List<SleepingSession> sleepingSession) {
        long bedSession = sleepingSession.stream()
                .filter(session -> session.getSleepQuality() == (SleepQuality.BAD))
                .count();

        if (bedSession == 0) {
            return new SleepAnalysisResult(FUNCTION_MESSAGE, "отсутствуют");
        }
        if (bedSession == sleepingSession.size()) {
            return new SleepAnalysisResult(FUNCTION_MESSAGE, "все сессии");
        }

        return new SleepAnalysisResult(FUNCTION_MESSAGE, bedSession);
    }
}
