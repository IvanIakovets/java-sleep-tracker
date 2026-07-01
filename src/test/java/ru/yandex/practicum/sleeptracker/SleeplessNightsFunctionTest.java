package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.sleepfunctions.SleeplessNightsFunction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SleeplessNightsFunctionTest {
    private SleeplessNightsFunction function;
    private DateTimeFormatter formatter;

    @BeforeEach
    void setUp() {
        function = new SleeplessNightsFunction();
        formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
    }

    @Test
    void standardSleeplessNightsFunction() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(createSession("01.10.25 23:15", "02.10.25 07:30", SleepQuality.GOOD));
        sessions.add(createSession("02.10.25 23:50", "03.10.25 06:40", SleepQuality.NORMAL));
        sessions.add(createSession("03.10.25 14:10", "03.10.25 15:00", SleepQuality.NORMAL));
        sessions.add(createSession("03.10.25 23:40", "04.10.25 08:00", SleepQuality.BAD));
        sessions.add(createSession("05.10.25 00:10", "05.10.25 06:20", SleepQuality.GOOD));
        sessions.add(createSession("05.10.25 13:30", "05.10.25 14:15", SleepQuality.NORMAL));
        sessions.add(createSession("06.10.25 22:30", "07.10.25 05:50", SleepQuality.GOOD));
        sessions.add(createSession("07.10.25 23:45", "08.10.25 06:30", SleepQuality.GOOD));
        sessions.add(createSession("08.10.25 23:50", "09.10.25 07:10", SleepQuality.GOOD));
        sessions.add(createSession("10.10.25 13:00", "10.10.25 14:30", SleepQuality.NORMAL));
        sessions.add(createSession("10.10.25 23:55", "11.10.25 06:10", SleepQuality.GOOD));
        sessions.add(createSession("11.10.25 23:10", "12.10.25 07:00", SleepQuality.BAD));
        sessions.add(createSession("30.10.25 23:50", "31.10.25 06:30", SleepQuality.GOOD));

        SleepAnalysisResult result = function.analyzeSleepingSession(sessions);

        String expected = "1 из 11 ночей";
        assertEquals(expected, result.getResultValue());
        assertEquals("Бессонных ночей найдено: ", result.getFunctionMassage());
    }

    @Test
    void noSleeplessNightsFunction() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(createSession("01.10.25 22:00", "02.10.25 06:00", SleepQuality.GOOD));
        sessions.add(createSession("02.10.25 23:00", "03.10.25 05:00", SleepQuality.GOOD));
        sessions.add(createSession("03.10.25 22:30", "04.10.25 06:30", SleepQuality.GOOD));

        SleepAnalysisResult result = function.analyzeSleepingSession(sessions);

        assertEquals("0 из 3 ночей", result.getResultValue());
    }

    @Test
    void allSleeplessNightsFunction() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(createSession("01.10.25 07:00", "01.10.25 11:00", SleepQuality.GOOD));
        sessions.add(createSession("02.10.25 14:00", "02.10.25 15:00", SleepQuality.GOOD));
        sessions.add(createSession("04.10.25 09:00", "04.10.25 13:00", SleepQuality.GOOD));

        SleepAnalysisResult result = function.analyzeSleepingSession(sessions);

        assertEquals("3 из 3 ночей", result.getResultValue());
    }

    @Test
    void acrossTheNoonSleeplessNightsFunction() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(createSession("01.10.25 23:00", "02.10.25 02:00", SleepQuality.GOOD));

        SleepAnalysisResult result = function.analyzeSleepingSession(sessions);

        assertEquals("0 из 1 ночей", result.getResultValue());
    }

    @Test
    void inNightSleeplessNightsFunction() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(createSession("01.10.25 01:00", "01.10.25 05:00", SleepQuality.GOOD));

        SleepAnalysisResult result = function.analyzeSleepingSession(sessions);

        assertEquals("0 из 1 ночей", result.getResultValue());
    }

    @Test
    void onlyOneNightInSleeplessNightsFunction() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(createSession("01.10.25 14:00", "01.10.25 15:00", SleepQuality.GOOD));
        sessions.add(createSession("01.10.25 23:00", "02.10.25 07:00", SleepQuality.GOOD));

        SleepAnalysisResult result = function.analyzeSleepingSession(sessions);

        assertEquals("0 из 1 ночей", result.getResultValue());
    }

    @Test
    void allDurationInNightSleeplessNightsFunction() {
        // Подготовка: сессия ровно с 00:00 до 06:00
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(createSession("01.10.25 00:00", "01.10.25 06:00", SleepQuality.GOOD));

        SleepAnalysisResult result = function.analyzeSleepingSession(sessions);

        assertEquals("0 из 1 ночей", result.getResultValue());
    }

    @Test
    void twoSleepInNightSleeplessNightsFunction() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(createSession("01.10.25 23:00", "02.10.25 03:00", SleepQuality.GOOD));
        sessions.add(createSession("02.10.25 04:00", "02.10.25 08:00", SleepQuality.GOOD));

        SleepAnalysisResult result = function.analyzeSleepingSession(sessions);

        assertEquals("0 из 1 ночей", result.getResultValue());
    }

    private SleepingSession createSession(String startStr, String endStr, SleepQuality quality) {
        LocalDateTime start = LocalDateTime.parse(startStr, formatter);
        LocalDateTime end = LocalDateTime.parse(endStr, formatter);
        return new SleepingSession(start, end, quality);
    }
}
