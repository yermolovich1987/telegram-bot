package com.sample.telegrambot.api;

import com.sample.telegrambot.cache.Cache;
import com.sample.telegrambot.domain.BotContext;
import com.sample.telegrambot.domain.BotState;
import com.sample.telegrambot.domain.BotUser;
import com.sample.telegrambot.processor.Processor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;

@Slf4j
@Component
public class ChatBot extends TelegramLongPollingBot {
  private String username;
  private String token;
  private Processor processor;

  @Autowired private Cache<BotUser> cache;

  @Value("${app.telegram.bot.username}")
  void setUsername(String username) {
    this.username = username;
  }

  @Value("${app.telegram.bot.token}")
  void setToken(String token) {
    this.token = token;
  }

  @Autowired
  void setProcessor(Processor processor) {
    this.processor = processor;
  }

  //  public HelloWorldBot(
  //      @Value("${app.telegram.bot.username}") String username,
  //      @Value("${app.telegram.bot.token}") String token, Processor processor) {
  //    this.username = username;
  //    this.token = token;
  //    this.processor = processor;
  //  }

  @Override
  public String getBotUsername() {
    return username;
  }

  @Override
  public String getBotToken() {
    return token;
  }

  @Override
  public void onUpdateReceived(Update update) {
    if (!update.hasMessage()) return;

    final String text = update.getMessage().getText();
    final long chatId = update.getMessage().getChatId();

    BotUser user = cache.findById(chatId);

    //    if (checkIfAdminCommand(user, text))
    //      return;

    BotContext context;
    BotState state;

    if (user == null) {
      state = BotState.getInitialState();

      user = BotUser.builder().id(chatId).botState(BotState.START).build();
      cache.add(user);

      context = BotContext.of(this, user, update.getMessage());
      state.enter(context);

      log.info("New user registered: " + chatId);
    } else {
      context = BotContext.of(this, user, update.getMessage());
      state = user.getBotState();

      log.info("Update received for user in state: " + state);
    }

    state.handleInput(context);

    do {
      state = state.nextState();
      state.enter(context);
    } while (!state.isFinal() && !state.isInputNeeded());

    user.setBotState(state);
    cache.add(user);
  }

  public void sendMessage(SendMessage message) {
    try {
      execute(message);
    } catch (TelegramApiException e) {
      log.error("Failed to send response failed message!");
    }
  }

  //  public void sendMessage(Message message) {
  //    try {
  //      execute(SendMessage.builder()
  //          .text("Отримане повідомлення:" + message.getText())
  //          .chatId(String.valueOf(message.getChatId())).build());
  //    } catch (TelegramApiException e) {
  //      log.error("Failed to send response failed message!");
  //    }
  //  }
  //
  //  public void sendMessageWithButtons(String chatId) {
  //    var markup = new ReplyKeyboardMarkup();
  //    var keyboardRows = new ArrayList<KeyboardRow>();
  //    KeyboardRow keyboardRow1 = new KeyboardRow();
  //    KeyboardRow keyboardRow2 = new KeyboardRow();
  //
  //    keyboardRow1.add("Yes");
  //    keyboardRow1.add("No");
  //
  //    keyboardRow2.add(KeyboardButton.builder().text("Phone
  // number").requestContact(true).build());
  //    keyboardRow2.add(KeyboardButton.builder().text("Location").requestLocation(true).build());
  //
  //    keyboardRows.add(keyboardRow1);
  //    keyboardRows.add(keyboardRow2);
  //
  //    markup.setKeyboard(keyboardRows);
  //    markup.setResizeKeyboard(true);
  //
  //    SendMessage sendMessage = new SendMessage();
  //    sendMessage.setText("Ви вже приїхали до Германії?");
  //    sendMessage.setChatId(chatId);
  //    sendMessage.setReplyMarkup(markup);
  //
  //    doExecuteSend(sendMessage);
  //  }
  //
  //  private void doExecuteSend(SendMessage sendMessage) {
  //    try {
  //      execute(sendMessage);
  //    } catch (TelegramApiException e) {
  //      log.error("Failed to send response failed message!");
  //    }
  //  }
}
