package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.sleepfunctions.BadSleepSessionsFunction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BadSleepSessionsFunctionTest {
    private BadSleepSessionsFunction function;

    @BeforeEach
    void setUp() {
        function = new BadSleepSessionsFunction();
    }

    @Test
    void BadQualityInSessions() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(createSession("01.10.25 22:00", "02.10.25 06:00", SleepQuality.BAD)); // 480 минут
        sessions.add(createSession("02.10.25 23:00", "03.10.25 01:00", SleepQuality.GOOD)); // 120 минут
        sessions.add(createSession("03.10.25 22:30", "04.10.25 07:00", SleepQuality.BAD)); // 510 минут

        SleepAnalysisResult result = function.analyzeSleepingSession(sessions);

        assertEquals("Количество сессий с плохим качеством сна: ", result.getFunctionMassage());
        assertEquals((long)2, result.getResultValue());
    }

    @Test
    void AllSessionsBadQuality() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(createSession("01.10.25 22:00", "01.10.25 22:02", SleepQuality.BAD));
        sessions.add(createSession("02.10.25 22:00", "02.10.25 22:02", SleepQuality.BAD));
        sessions.add(createSession("03.10.25 22:00", "03.10.25 22:02", SleepQuality.BAD));

        SleepAnalysisResult result = function.analyzeSleepingSession(sessions);

        assertEquals("Количество сессий с плохим качеством сна: ", result.getFunctionMassage());
        assertEquals("все сессии", result.getResultValue());
    }

    @Test
    void NoSessionsBadQuality() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(createSession("01.10.25 22:00", "01.10.25 22:02", SleepQuality.GOOD));
        sessions.add(createSession("02.10.25 22:00", "02.10.25 22:02", SleepQuality.GOOD));
        sessions.add(createSession("03.10.25 22:00", "03.10.25 22:02", SleepQuality.GOOD));

        SleepAnalysisResult result = function.analyzeSleepingSession(sessions);

        assertEquals("Количество сессий с плохим качеством сна: ", result.getFunctionMassage());
        assertEquals("отсутствуют", result.getResultValue());
    }

    private SleepingSession createSession(String startStr, String endStr, SleepQuality quality) {
        java.time.format.DateTimeFormatter formatter =
                java.time.format.DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        LocalDateTime start = LocalDateTime.parse(startStr, formatter);
        LocalDateTime end = LocalDateTime.parse(endStr, formatter);
        return new SleepingSession(start, end, quality);
    }
}
