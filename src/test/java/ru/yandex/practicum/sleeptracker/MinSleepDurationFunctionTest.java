package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.sleepfunctions.MinSleepDurationFunction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MinSleepDurationFunctionTest {
    private MinSleepDurationFunction function;

    @BeforeEach
    void setUp() {
        function = new MinSleepDurationFunction();
    }

    @Test
    void minDuration_whenSessionsExist() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(createSession("01.10.25 22:00", "02.10.25 06:00", SleepQuality.GOOD)); // 480 минут
        sessions.add(createSession("02.10.25 23:00", "03.10.25 01:00", SleepQuality.GOOD)); // 120 минут
        sessions.add(createSession("03.10.25 22:30", "04.10.25 07:00", SleepQuality.GOOD)); // 510 минут

        SleepAnalysisResult result = function.analyzeSleepingSession(sessions);

        assertEquals("Минимальная продолжительность сессии: ", result.getFunctionMassage());
        assertEquals("120 минут", result.getResultValue());
    }

    @Test
    void oneMinDuration_whenSessionsExist() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(createSession("01.10.25 22:00", "01.10.25 22:01", SleepQuality.GOOD)); // 1 минута
        sessions.add(createSession("02.10.25 23:00", "03.10.25 01:00", SleepQuality.GOOD)); // 120 минут
        sessions.add(createSession("03.10.25 22:30", "04.10.25 07:00", SleepQuality.GOOD)); // 510 минут

        SleepAnalysisResult result = function.analyzeSleepingSession(sessions);

        assertEquals("Минимальная продолжительность сессии: ", result.getFunctionMassage());
        assertEquals("1 минут", result.getResultValue());
    }

    @Test
    void twoDaysDuration_whenSessionsExist() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(createSession("01.10.25 22:00", "10.10.25 04:00", SleepQuality.GOOD));
        sessions.add(createSession("02.10.25 23:00", "08.10.25 01:00", SleepQuality.GOOD));
        sessions.add(createSession("03.10.25 22:30", "05.10.25 07:00", SleepQuality.GOOD));

        SleepAnalysisResult result = function.analyzeSleepingSession(sessions);

        assertEquals("Минимальная продолжительность сессии: ", result.getFunctionMassage());
        assertEquals("1950 минут", result.getResultValue());
    }



    private SleepingSession createSession(String startStr, String endStr, SleepQuality quality) {
        java.time.format.DateTimeFormatter formatter =
                java.time.format.DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        LocalDateTime start = LocalDateTime.parse(startStr, formatter);
        LocalDateTime end = LocalDateTime.parse(endStr, formatter);
        return new SleepingSession(start, end, quality);
    }

}
