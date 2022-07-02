package ru.liga.repository;

import ru.liga.model.Currency;
import ru.liga.utils.FileUtils;
import ru.liga.utils.ReadCSV;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CurrencyRepository {

    private static CurrencyRepository instance;
    private static final List<Currency> historyData = new ArrayList<>();

    static {
        FileUtils fileUtils = FileUtils.getInstance();
        ReadCSV readCSV = new ReadCSV();
        historyData.addAll(readCSV.setCSVParser(fileUtils.findFilePath("EUR")).getCurrencyFromCSV());
        historyData.addAll(readCSV.setCSVParser(fileUtils.findFilePath("USD")).getCurrencyFromCSV());
        historyData.addAll(readCSV.setCSVParser(fileUtils.findFilePath("TRY")).getCurrencyFromCSV());
        historyData.addAll(readCSV.setCSVParser(fileUtils.findFilePath("BGN")).getCurrencyFromCSV());
        historyData.addAll(readCSV.setCSVParser(fileUtils.findFilePath("AMD")).getCurrencyFromCSV());
    }

    private CurrencyRepository() {
    }

    public static synchronized CurrencyRepository getInstance() {
        if (instance == null) {
            instance = new CurrencyRepository();
        }
        return instance;
    }

    /**
     * Поиск валюты по названию за указанную дату
     *
     * @param name Название валюты
     * @param date Дата за которую требуется найти валюту
     * @return Валюта за указанную дату
     */
    public Currency findOneByNameAndDate(String name, LocalDate date) {
        return historyData.stream()
                .filter((cur) -> name.equals(cur.getName()) && date.isEqual(cur.getDate()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Поиск нескольких валют по названию с указанной даты
     *
     * @param name            Название валюты
     * @param date            Дата с которой требуется искать валюты
     * @param currencyNumbers Количество записей которые нужно достать
     * @return Список из нескольких валют с указанной даты
     */
    public List<Currency> findLastSeveralFromDateByName(String name, LocalDate date, int currencyNumbers) {
        List<Currency> lastSeven = new ArrayList<>();
        LocalDate startDate = date;
        for (int i = 0; i < currencyNumbers; ) {
            Currency cur = this.findOneByNameAndDate(name, startDate);
            if (cur != null) {
                lastSeven.add(cur);
                i++;
            }
            startDate = startDate.minusDays(1);
        }

        return lastSeven;
    }

    /**
     * Добавление новой валюты
     *
     * @param currency Новая валюта
     */
    public void addNewCurrency(Currency currency) {
        historyData.add(currency);
    }
}
