package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.sleepfunctions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SleepTrackerApp {
    static Scanner scanner = new Scanner(System.in);
    private static String userLogPath = null;
    private static Optional<ArrayList<SleepingSession>> sessions;

    //сюда добавляем все существующие функции
    private static final Map<String, SleepingAnalysisFunction> FUNCTIONS = new LinkedHashMap<>();
    static {
        // блок добавления функций
        FUNCTIONS.put("Подсчет количества сессий сна", new TotalSessionsFunction());
        FUNCTIONS.put("Подсчет минимальной продолжительности сессии", new MinSleepDurationFunction());
        FUNCTIONS.put("Подсчет максимальной продолжительности сессии", new MaxSleepDurationFunction());
        FUNCTIONS.put("Подсчет средней продолжительности сессии", new AverageSleepDurationFunction());
        FUNCTIONS.put("Подсчет количества плохих сессий сна", new BadSleepSessionsFunction());
        FUNCTIONS.put("Подсчет бессонных ночей", new SleeplessNightsFunction());
        FUNCTIONS.put("Определение хронотипа",new ChronotypeFunction());
    }

    // основа работы приложения
    public static void main(String[] args) {
        System.out.println("Добро пожаловать в Sleep Tracker Analyzer");
        while (true) {
            printMenu();
            try {
                int userMenuChoice = scanner.nextInt();
                scanner.nextLine();
                switch (userMenuChoice) {
                    case 1:
                        readUserConsole().ifPresentOrElse(
                                path -> userLogPath = path,
                                () -> System.out.println("Попробуйте ввести путь повторно.")
                        );
                        break;
                    case 2:
                        readAppFunctional();
                        break;
                    case 3:
                        if (userLogPath == null) {
                            System.out.println("Сначала укажите путь к логу сна(пункт 1).");
                            break;
                        }

                        sessions = sleepLogHandler(Path.of(userLogPath));
                        if (sessions.isPresent()) {
                            analyseSleepTrack(sessions.get());
                        } else {
                            break;
                        }
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("\nВыбрана неверная команда!\n");
                }
            } catch (Exception e) {
                System.out.println("Произошла ошибка: " + e.getMessage());
            }
        }
    }

    //печать меню приложения
    private static void printMenu() {
        System.out.println("\nВыберите пункт меню:");
        System.out.println("1. - Указать путь к логу сна");
        System.out.println("2. - Отобразить список доступных функций");
        System.out.println("3. - Провести анализ сна");
        System.out.println("4. - Выход");
    }

    //метод для чтения файла лога сна и его обработки
    // /Users/ivanya/Documents/study/java-sleep-tracker./src/main/resources/sleep_log.txt
    private static Optional<ArrayList<SleepingSession>> sleepLogHandler(Path sleepLogPath) {
        try (Stream<String> lines = Files.lines(sleepLogPath, StandardCharsets.UTF_8)){
            ArrayList<SleepingSession> sleepSessions = lines
                    .filter(line -> !line.isBlank())
                    .map(SleepTrackerApp::parseLineToSleepSession)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toCollection(ArrayList::new));

            if (sleepSessions.isEmpty()){
                System.out.println("Файл с логами сна пуст");
                return Optional.empty();
            }
            Set<SleepingSession> uniqueSessionsSet = sleepSessions.stream()
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            return Optional.of(new ArrayList<>(uniqueSessionsSet));

        } catch (IOException e) {
            System.out.println("Не указан путь к логу сна.");
            return Optional.empty();
        }
    }

    //метод для получения строки от
    //пользователя и ее проверки на пустоту и наличие файла по пути
    private static Optional<String> readUserConsole() {
        System.out.println("Введите ПУТЬ к файлу с логом сна:");
        String userPath = scanner.nextLine().trim();
        scanner.nextLine();
        if (userPath.isEmpty()) {
            System.out.println("Ошибка: Введена пустая строка.");
            return Optional.empty();
        }
        if (!Files.exists(Path.of(userPath))) {
            System.out.println("Ошибка: По заданному пути, файла не существует.");
            return Optional.empty();
        }
        return Optional.of(userPath);
    }

    //метод выводит список доступных функций
    private static void readAppFunctional() {
        if (FUNCTIONS.isEmpty()) {
            System.out.println("Ошибка: функции отсутствуют.");
        } else {
            System.out.println("\n___Доступные функции для анализа___\n");
            FUNCTIONS.keySet()
                    .forEach(System.out::println);
        }
    }


    static void analyseSleepTrack(ArrayList<SleepingSession> sleepingSessions) {
        if (sleepingSessions == null || sleepingSessions.isEmpty()) {
            return;
        }
        if (FUNCTIONS.isEmpty()) {
            System.out.println("Отсутствую функции для анализа сна");
            return;
        }
        System.out.println("\n___Результаты анализа сна___");
        FUNCTIONS.values().stream()
                .map(func -> func.analyzeSleepingSession(sleepingSessions))
                .forEach(result -> System.out.println("- " + result));
    }

    //проводим парсинг строчек и преобразуем их в переменные в классе SleepSession
    static Optional<SleepingSession> parseLineToSleepSession(String line) {
        try {
            String[] lineParts = line.split(";");
            if (lineParts.length != 3) {
                System.out.println("Некорректный формат строки: " + line);
                System.out.println("Правильный формат: 01.10.25 23:15;02.10.25 07:30;GOOD");
                return Optional.empty();
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
            LocalDateTime startSleepSessionTime = LocalDateTime.parse(lineParts[0].trim(), formatter);
            LocalDateTime endSleepSessionTime = LocalDateTime.parse(lineParts[1].trim(), formatter);
            SleepQuality quality = SleepQuality.valueOf(lineParts[2].trim());

            return Optional.of(new SleepingSession(startSleepSessionTime, endSleepSessionTime,quality));

        } catch (DateTimeException e){
            System.out.println("Ошибка парсинга даты в строке: " + line);
            return Optional.empty();
        } catch (IllegalArgumentException e) {
            System.out.println("Некорректное качество сна в строке: " + line);
            System.out.println("Допустимые значения: GOOD, NORMAL, BAD");
            return Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}