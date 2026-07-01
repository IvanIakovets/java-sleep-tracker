package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
public class SleepTrackerAppTest {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    @Test
    void parseLineToSleepSession() {
        // Подготовка
        String validLine = "01.10.25 23:15;02.10.25 07:30;GOOD";

        // Выполнение
        Optional<SleepingSession> result = SleepTrackerApp.parseLineToSleepSession(validLine);

        // Проверка
        assertTrue(result.isPresent());
        SleepingSession session = result.get();
        assertNotNull(session);
        assertEquals(SleepQuality.GOOD, session.getSleepQuality());
    }

    @Test
    void parseLineWithSpaceToSleepSession() {
        String lineWithSpaces = "01.10.25 23:15; 02.10.25 07:30; GOOD";

        Optional<SleepingSession> result = SleepTrackerApp.parseLineToSleepSession(lineWithSpaces);

        assertTrue(result.isPresent());
        assertEquals(SleepQuality.GOOD, result.get().getSleepQuality());
    }

    @Test
    void parseBadLineToSleepSession() {
        String invalidLine = "01.10.25 23:15;02.10.25 07:30";

        Optional<SleepingSession> result = SleepTrackerApp.parseLineToSleepSession(invalidLine);

        assertTrue(result.isEmpty());
    }

    @Test
    void parseLineBadDateToSleepSession() {
        String invalidDate = "99.99.99 99:99;02.10.25 07:30;GOOD";

        Optional<SleepingSession> result = SleepTrackerApp.parseLineToSleepSession(invalidDate);

        assertTrue(result.isEmpty());
    }

    @Test
    void parseLineBadQualityToSleepSession() {
        String invalidQuality = "01.10.25 23:15;02.10.25 07:30;EXCELLENT";

        Optional<SleepingSession> result = SleepTrackerApp.parseLineToSleepSession(invalidQuality);

        assertTrue(result.isEmpty());
    }

    @Test
    void parseLineToSleepSession_shouldParseDateCorrectly() {
        String line = "01.10.25 23:15;02.10.25 07:30;GOOD";

        Optional<SleepingSession> result = SleepTrackerApp.parseLineToSleepSession(line);

        assertTrue(result.isPresent());
        SleepingSession session = result.get();

        LocalDateTime expectedStart = LocalDateTime.parse("01.10.25 23:15", formatter);
        LocalDateTime expectedEnd = LocalDateTime.parse("02.10.25 07:30", formatter);

        assertEquals(expectedStart, session.getStartSleepSessionTime());
        assertEquals(expectedEnd, session.getEndSleepSessionTime());
    }

}