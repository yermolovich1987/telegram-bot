package com.sample.telegrambot.gateway;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.K;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;

@Slf4j
//@Component
// @RequiredArgsConstructor
// TODO How to implement localization.
public class DefaultMessageSender implements MessageSender {
  private AbsSender sender;

  @Autowired
  void setSender(AbsSender sender) {
    this.sender = sender;
  }

  @Override
  public void sendMessage(SendMessage message) {
    try {
      sender.execute(message);
    } catch (TelegramApiException e) {
      log.error("Failed to send response failed message!");
    }
  }

  @Override
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
      sender.execute(sendMessage);
    } catch (TelegramApiException e) {
      log.error("Failed to send response failed message!");
    }
  }
}
