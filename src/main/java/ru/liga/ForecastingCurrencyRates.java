package ru.liga;

import ru.liga.model.Currency;
import ru.liga.utils.FileUtils;
import ru.liga.utils.Forecasting;
import ru.liga.utils.GetCurrencyFromCSV;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Scanner;

public class ForecastingCurrencyRates {
    static Map<String, Currency> eurHistory;
    static Map<String, Currency> usdHistory;
    static Map<String, Currency> tryHistory;

    static {
        eurHistory = GetCurrencyFromCSV.readCSV(FileUtils.findFilePath("EUR"));
        usdHistory = GetCurrencyFromCSV.readCSV(FileUtils.findFilePath("USD"));
        tryHistory = GetCurrencyFromCSV.readCSV(FileUtils.findFilePath("TRY"));
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        while (true) {
            String commandLine = in.nextLine();
            String[] command = commandLine.toUpperCase().trim().split("\\s+");

            if ("EXIT".equals(command[0])) {
                break;
            } else if (!"RATE".equals(command[0]) || command.length != 3) {
                System.out.println("Wrong command!");
                continue;
            }

            Map<String, Currency> rateHistory;
            switch (command[1]) {
                case "EUR": rateHistory = eurHistory; break;
                case "TRY": rateHistory = tryHistory; break;
                case "USD": rateHistory = usdHistory; break;
                default: System.out.println("Wrong currency!"); continue;
            }

            Calendar currentDate = new GregorianCalendar();
            switch (command[2]) {
                case "TOMORROW": Forecasting.forecastingTomorrowRate(rateHistory, currentDate); break;
                case "WEEK": Forecasting.forecastingWeekRate(rateHistory, currentDate); break;
                default: System.out.println("Wrong period!");
            }
        }

        in.close();
    }
}