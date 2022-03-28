package com.sample.telegrambot.api;

import com.sample.telegrambot.gateway.MessageSender;
import lombok.extern.slf4j.Slf4j;
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
public class HelloWorldBot extends TelegramLongPollingBot {
  private final String username;
  private final String token;
//  private final MessageSender messageSender;

  public HelloWorldBot(
      @Value("${app.telegram.bot.username}") String username,
      @Value("${app.telegram.bot.token}") String token) {
    this.username = username;
    this.token = token;
//    this.messageSender = messageSender;
  }

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
    if (update.hasMessage()) {
      var msg = update.getMessage();
      if (msg.hasText()) {
        sendMessageWithButtons(String.valueOf(msg.getChatId()));
      }
    }
  }

  public void sendMessage(Message message) {
    try {
      execute(SendMessage.builder()
          .text("Отримане повідомлення:" + message.getText())
          .chatId(String.valueOf(message.getChatId())).build());
    } catch (TelegramApiException e) {
      log.error("Failed to send response failed message!");
    }
  }

  public void sendMessageWithButtons(String chatId) {
    var markup = new ReplyKeyboardMarkup();
    var keyboardRows = new ArrayList<KeyboardRow>();
    KeyboardRow keyboardRow1 = new KeyboardRow();
    KeyboardRow keyboardRow2 = new KeyboardRow();

    keyboardRow1.add("Yes");
    keyboardRow1.add("No");

    keyboardRow2.add(KeyboardButton.builder().text("Phone number").requestContact(true).build());
    keyboardRow2.add(KeyboardButton.builder().text("Location").requestLocation(true).build());

    keyboardRows.add(keyboardRow1);
    keyboardRows.add(keyboardRow2);

    markup.setKeyboard(keyboardRows);
    markup.setResizeKeyboard(true);

    SendMessage sendMessage = new SendMessage();
    sendMessage.setText("Ви вже приїхали до Германії?");
    sendMessage.setChatId(chatId);
    sendMessage.setReplyMarkup(markup);

    doExecuteSend(sendMessage);
  }

  private void doExecuteSend(SendMessage sendMessage) {
    try {
      execute(sendMessage);
    } catch (TelegramApiException e) {
      log.error("Failed to send response failed message!");
    }
  }
}
