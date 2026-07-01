package ru.yandex.practicum.sleeptracker;

import java.util.List;

//интерфейс для функций приложения
@FunctionalInterface
public interface SleepingAnalysisFunction {
    SleepAnalysisResult analyzeSleepingSession (List<SleepingSession> sleepingSession);

}

