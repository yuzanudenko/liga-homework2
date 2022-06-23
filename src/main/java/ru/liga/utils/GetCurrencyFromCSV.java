package ru.liga.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import ru.liga.model.Currency;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class GetCurrencyFromCSV {

    /**
     * Читаем csv файл и формируем коллекцию исторических данных по курсу валюты
     *
     * @param path Путь к файлу
     * @return Коллекция исторических данных по курсу валюты
     */
    public static Map<String, Currency> readCSV(Path path) {
        Map<String, Currency> records = new HashMap<>();
        try (
                Reader reader = Files.newBufferedReader(path);
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                        .builder()
                        .setDelimiter(';')
                        .setIgnoreEmptyLines(true)
                        .setIgnoreSurroundingSpaces(true)
                        .setTrim(true)
                        .build())
        ) {
            for (CSVRecord csvRecord : csvParser) {
                if (csvRecord.getRecordNumber() != 1L) {
                    Currency cur = new Currency();
                    cur.setNominal(Integer.parseInt(csvRecord.get(0).replace(" ", "")));
                    cur.setRate(Double.parseDouble(csvRecord.get(2).replace(",", ".")));

                    records.put(csvRecord.get(1), cur);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return records;
    }
}
