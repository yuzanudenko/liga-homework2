package ru.liga.controller;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.liga.config.Constants;
import ru.liga.exception.CurrencyNotFoundException;
import ru.liga.exception.WrongCommandException;
import ru.liga.model.Currency;
import ru.liga.repository.CurrencyRepository;
import ru.liga.service.Forecasting;
import ru.liga.service.ForecastingFactory;
import ru.liga.utils.CommandUtils;
import ru.liga.utils.GraphUtils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.liga.config.Constants.PRINT_FORMATTER;

@Slf4j
public class TelegramController extends TelegramLongPollingBot implements Controller {

    Forecasting forecasting;
    CommandUtils commandUtils;
    CurrencyRepository currencyRepository;
    GraphUtils graph;

    public TelegramController(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
        this.commandUtils = new CommandUtils();
    }

    @Override
    public String getBotUsername() {
        return "@YUZ_ForecastingRate_bot";
    }

    @Override
    public String getBotToken() {
        return "5516119660:AAHNtbzZExfhr_gNLU7N0X8zKd0sC7BMuXg";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                String commandLine = message.getText();
                log.debug("Start processing command: " + commandLine);
                try {
                    Map<String, String> commandMap = commandUtils.processInputCommand(commandLine);

                    forecasting = ForecastingFactory.createForecastingAlgorithm(commandMap.get("ALG"), currencyRepository);

                    List<Currency> result = new ArrayList<>();
                    this.graph = new GraphUtils();
                    List<String> currencyNamesList = Stream.of(commandMap.get("RATE").split(","))
                            .collect(Collectors.toList());
                    for (String currencyName : currencyNamesList) {
                        List<Currency> intermediateResult = new ArrayList<>();
                        LocalDate currentDate = LocalDate.now();
                        if (commandMap.get("DATE") != null) {
                            if ("TOMORROW".equals(commandMap.get("DATE"))) {
                                currentDate = currentDate.plusDays(1L);
                                intermediateResult.add(forecasting.forecastingSpecificDateRate(currencyName, currentDate));
                            } else {
                                try {
                                    LocalDate specificDate = LocalDate.parse(commandMap.get("DATE"), Constants.FORMATTER);
                                    intermediateResult.add(forecasting.forecastingSpecificDateRate(currencyName, specificDate));
                                } catch (DateTimeParseException e) {
                                    throw new WrongCommandException("Wrong date!");
                                }
                            }
                        } else {
                            intermediateResult = forecasting.forecastingSeveralRates(currencyName, commandMap.get("PERIOD"));
                        }
                        graph.setData(intermediateResult, currencyName);
                        result.addAll(intermediateResult);
                    }

                    switch (commandMap.get("OUTPUT")) {
                        case "LIST":
                            execute(SendMessage.builder()
                                    .chatId(message.getChatId())
                                    .text(result.stream()
                                            .map((cur) -> String.format("%s - %.2f", cur.getDate().format(PRINT_FORMATTER), cur.getRate()))
                                            .collect(Collectors.joining("\n")))
                                    .build());
                            break;
                        case "GRAPH":
                            InputFile lineChartResult = new InputFile(graph.getCurrencyRatesAsGraph());
                            execute(SendPhoto.builder()
                                    .chatId(message.getChatId())
                                    .photo(lineChartResult)
                                    .build());
                            break;
                        default:
                            throw new WrongCommandException("Wrong output!");
                    }
                } catch (CurrencyNotFoundException | WrongCommandException e) {
                    log.error(commandLine + " - " + e.getMessage());
                    try {
                        execute(SendMessage.builder()
                                .chatId(message.getChatId())
                                .text(e.getMessage())
                                .build());
                    } catch (TelegramApiException ex) {
                        log.error(e.getMessage());
                        throw new RuntimeException(ex);
                    }
                } catch (TelegramApiException e) {
                    log.error(e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void getForecastingCurrencyRates() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
