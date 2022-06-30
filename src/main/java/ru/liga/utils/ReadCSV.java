package ru.liga.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import ru.liga.config.Constants;
import ru.liga.model.Currency;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReadCSV {

    private CSVParser csvParser;

    /**
     * Читаем csv файл
     *
     * @param path Путь к файлу
     * @return Экземпляр текущего класса
     */
    public ReadCSV setCSVParser(Path path) {
        try {
            Reader reader = Files.newBufferedReader(path);
            this.csvParser = new CSVParser(reader, CSVFormat.DEFAULT.builder()
                    .setDelimiter(';')
                    .setIgnoreEmptyLines(true)
                    .setIgnoreSurroundingSpaces(true)
                    .setTrim(true)
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .build());
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Формируем список исторических данных по курсу валюты
     *
     * @return Список исторических данных по курсу валюты
     */
    public List<Currency> getCurrencyFromCSV() {
        List<Currency> records = new ArrayList<>();
        for (CSVRecord csvRecord : this.csvParser) {
            Currency cur = new Currency();
            cur.setNominal(Integer.parseInt(csvRecord.get("nominal")
                    .replace(" ", "")
                    .replace(".", "")));
            cur.setRate(new BigDecimal(csvRecord.get("curs")
                    .replace(",", ".")));
            cur.setDate(LocalDate.parse(csvRecord.get("data"), Constants.FORMATTER));
            cur.setName(csvRecord.get("cdx"));

            records.add(cur);
        }
        return records;
    }
}
