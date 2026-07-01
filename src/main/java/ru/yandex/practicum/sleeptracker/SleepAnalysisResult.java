package ru.yandex.practicum.sleeptracker;

//класс обертка для анализа результатов функции
public class SleepAnalysisResult {
    private final String functionMassage;
    private final Object resultValue;

    public SleepAnalysisResult(String functionalMassage, Object resultValue) {
        this.functionMassage = functionalMassage;
        this.resultValue = resultValue;
    }

    public Object getResultValue() {
        return resultValue;
    }

    public String getFunctionMassage() {
        return functionMassage;
    }

    @Override
    public String toString() {
        return functionMassage + resultValue;
    }
}
