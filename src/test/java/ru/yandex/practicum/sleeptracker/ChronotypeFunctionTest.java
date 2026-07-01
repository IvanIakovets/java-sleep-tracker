package ru.yandex.practicum.sleeptracker;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.sleepfunctions.ChronotypeFunction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ChronotypeFunctionTest {
    private ChronotypeFunction function;
    private DateTimeFormatter formatter;

    @BeforeEach
    void setUp() {
        function = new ChronotypeFunction();
        formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
    }

    @Test
    void chronotypeIsOwl() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(createSession("01.10.25 23:30", "02.10.25 09:30", SleepQuality.GOOD));
        sessions.add(createSession("02.10.25 23:45", "03.10.25 10:00", SleepQuality.GOOD));
        sessions.add(createSession("03.10.25 23:15", "04.10.25 09:15", SleepQuality.GOOD));
        sessions.add(createSession("04.10.25 21:45", "05.10.25 06:00", SleepQuality.GOOD));
        sessions.add(createSession("05.10.25 21:15", "06.10.25 06:15", SleepQuality.GOOD));

        SleepAnalysisResult result = function.analyzeSleepingSession(sessions);

        assertEquals(Chronotype.OWL, result.getResultValue());
        assertEquals("Ваш хронотип: ", result.getFunctionMassage());
    }

    @Test
    void chronotypeIsLark() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(createSession("01.10.25 23:30", "02.10.25 09:30", SleepQuality.GOOD));
        sessions.add(createSession("02.10.25 23:45", "03.10.25 10:00", SleepQuality.GOOD));
        sessions.add(createSession("03.10.25 21:15", "04.10.25 06:15", SleepQuality.GOOD));
        sessions.add(createSession("04.10.25 21:45", "05.10.25 06:00", SleepQuality.GOOD));
        sessions.add(createSession("05.10.25 21:15", "06.10.25 06:15", SleepQuality.GOOD));

        SleepAnalysisResult result = function.analyzeSleepingSession(sessions);

        assertEquals(Chronotype.LARK, result.getResultValue());
        assertEquals("Ваш хронотип: ", result.getFunctionMassage());
    }

    @Test
    void chronotypeIsPigeon() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(createSession("01.10.25 23:30", "02.10.25 09:30", SleepQuality.GOOD));
        sessions.add(createSession("02.10.25 23:45", "03.10.25 10:00", SleepQuality.GOOD));
        sessions.add(createSession("04.10.25 21:45", "05.10.25 06:00", SleepQuality.GOOD));
        sessions.add(createSession("05.10.25 21:15", "06.10.25 06:15", SleepQuality.GOOD));

        SleepAnalysisResult result = function.analyzeSleepingSession(sessions);

        assertEquals(Chronotype.PIGEON, result.getResultValue());
        assertEquals("Ваш хронотип: ", result.getFunctionMassage());
    }

    @Test
    void chronotypeIsPigeonTwo() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(createSession("01.10.25 23:30", "02.10.25 06:30", SleepQuality.GOOD));
        sessions.add(createSession("02.10.25 23:45", "03.10.25 06:00", SleepQuality.GOOD));
        sessions.add(createSession("04.10.25 23:45", "05.10.25 06:00", SleepQuality.GOOD));
        sessions.add(createSession("05.10.25 23:15", "06.10.25 06:15", SleepQuality.GOOD));

        SleepAnalysisResult result = function.analyzeSleepingSession(sessions);

        assertEquals(Chronotype.PIGEON, result.getResultValue());
        assertEquals("Ваш хронотип: ", result.getFunctionMassage());
    }

    @Test
    void chronotypeIgnoreDailySessions() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(createSession("01.10.25 23:30", "02.10.25 09:30", SleepQuality.GOOD)); // Сова
        sessions.add(createSession("02.10.25 14:00", "02.10.25 15:00", SleepQuality.GOOD)); // Дневная
        sessions.add(createSession("03.10.25 14:00", "03.10.25 15:00", SleepQuality.GOOD)); // Дневная


        SleepAnalysisResult result = function.analyzeSleepingSession(sessions);

        assertEquals(Chronotype.OWL, result.getResultValue());
    }

    @Test
    void chronotypeOwlInTime() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(createSession("01.10.25 23:01", "02.10.25 09:01", SleepQuality.GOOD));
        sessions.add(createSession("02.10.25 23:01", "03.10.25 09:01", SleepQuality.GOOD));
        sessions.add(createSession("03.10.25 21:00", "04.10.25 06:00", SleepQuality.GOOD));


        SleepAnalysisResult result = function.analyzeSleepingSession(sessions);

        assertEquals(Chronotype.OWL, result.getResultValue());
    }

    @Test
    void chronotypeLarkInTime() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(createSession("01.10.25 21:59", "02.10.25 06:59", SleepQuality.GOOD));
        sessions.add(createSession("02.10.25 21:59", "03.10.25 06:59", SleepQuality.GOOD));
        sessions.add(createSession("03.10.25 23:30", "04.10.25 15:00", SleepQuality.GOOD));


        SleepAnalysisResult result = function.analyzeSleepingSession(sessions);

        assertEquals(Chronotype.LARK, result.getResultValue());
    }

    private SleepingSession createSession(String startStr, String endStr, SleepQuality quality) {
        java.time.format.DateTimeFormatter formatter =
                java.time.format.DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        LocalDateTime start = LocalDateTime.parse(startStr, formatter);
        LocalDateTime end = LocalDateTime.parse(endStr, formatter);
        return new SleepingSession(start, end, quality);
    }
}
