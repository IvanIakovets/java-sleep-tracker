package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.sleepfunctions.TotalSessionsFunction;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TotalSessionsFunctionTest {
    private TotalSessionsFunction function;

    @BeforeEach
    void setUp() {
        function = new TotalSessionsFunction();
    }

    // Проверка на обработку сессий, проверка на пустоту и null заведена в классе SleepTrackerAppTest
    @Test
    void analyzeSleepingSession_shouldReturnCorrectCount_whenSessionsExist() {
        // Подготовка
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(createSession("01.10.25 22:00", "02.10.25 06:00", SleepQuality.GOOD));
        sessions.add(createSession("02.10.25 23:00", "03.10.25 07:00", SleepQuality.NORMAL));
        sessions.add(createSession("03.10.25 22:30", "04.10.25 06:30", SleepQuality.BAD));

        // Выполнение
        SleepAnalysisResult result = function.analyzeSleepingSession(sessions);

        // Проверка
        assertEquals(3, result.getResultValue(), "Должно быть 3 сессии");
        assertEquals("Общее количество сессий сна: ", result.getFunctionMassage());
    }

    @Test
    void analyzeSleepingSession_shouldReturnOne_whenSingleSession() {
        // Подготовка (given)
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(createSession("01.10.25 22:00", "02.10.25 06:00", SleepQuality.GOOD));

        // Выполнение (when)
        SleepAnalysisResult result = function.analyzeSleepingSession(sessions);

        // Проверка (then)
        assertEquals(1, result.getResultValue(), "Одна сессия должна дать 1");
    }

    // Проверка на обработку большого количества сессий, проверка на дубли заведена в классе SleepTrackerAppTest
    @Test
    void analyzeSleepingSession_shouldReturnLargeCount_whenManySessions() {
        // Подготовка (given)
        List<SleepingSession> sessions = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            sessions.add(createSession("01.10.25 22:00", "02.10.25 06:00", SleepQuality.GOOD));
        }

        SleepAnalysisResult result = function.analyzeSleepingSession(sessions);

        assertEquals(100, result.getResultValue(), "Должно быть 100 сессий");
    }

    private SleepingSession createSession(String startStr, String endStr, SleepQuality quality) {
        java.time.format.DateTimeFormatter formatter =
                java.time.format.DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        LocalDateTime start = LocalDateTime.parse(startStr, formatter);
        LocalDateTime end = LocalDateTime.parse(endStr, formatter);
        return new SleepingSession(start, end, quality);
    }
}
