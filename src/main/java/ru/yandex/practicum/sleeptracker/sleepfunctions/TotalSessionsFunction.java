package ru.yandex.practicum.sleeptracker.sleepfunctions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;

public class TotalSessionsFunction implements SleepingAnalysisFunction {
    private static final String FUNCTION_MESSAGE = "Общее количество сессий сна: ";

    @Override
    public SleepAnalysisResult analyzeSleepingSession(List<SleepingSession> sleepingSession) {
        return new SleepAnalysisResult(FUNCTION_MESSAGE, sleepingSession.size());
    }
}
