package ru.yandex.practicum.sleeptracker;

import java.time.LocalDateTime;
import java.util.Objects;

//класс в котором хранятся данные по одной сессии сна
public class SleepingSession {
    private final LocalDateTime startSleepSessionTime;
    private final LocalDateTime endSleepSessionTime;
    private final SleepQuality sleepQuality;

    public SleepingSession(LocalDateTime startSleepSessionTime, LocalDateTime endSleepSessionTime, SleepQuality sleepQuality) {
        this.startSleepSessionTime = startSleepSessionTime;
        this.endSleepSessionTime = endSleepSessionTime;
        this.sleepQuality = sleepQuality;
    }

    public LocalDateTime getStartSleepSessionTime() {
        return startSleepSessionTime;
    }

    public LocalDateTime getEndSleepSessionTime() {
        return endSleepSessionTime;
    }

    public SleepQuality getSleepQuality() {
        return sleepQuality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SleepingSession that = (SleepingSession) o;
        return Objects.equals(startSleepSessionTime, that.startSleepSessionTime) &&
                Objects.equals(endSleepSessionTime, that.endSleepSessionTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startSleepSessionTime, endSleepSessionTime);
    }
}
