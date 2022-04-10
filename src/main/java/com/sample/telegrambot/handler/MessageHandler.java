package com.sample.telegrambot.handler;

import com.sample.telegrambot.cache.Cache;
import com.sample.telegrambot.domain.BotUser;
import com.sample.telegrambot.domain.Position;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@Component
public class MessageHandler implements Handler<Message> {
  private final Cache<BotUser> cache;

  public MessageHandler(Cache<BotUser> cache) {
    this.cache = cache;
  }

  @Override
  public SendMessage choose(Message message) {
    BotUser user = cache.findById(message.getChatId());
    if (user != null) {
      switch (user.getPosition()) {
          // TODO Ввести номер телефону
        case INPUT_NAME:
          user.setFullName(message.getText());
          return SendMessage.builder()
              .text("Натисніть на кнопку, щоб відправити номер телефону.")
              .chatId(String.valueOf(message.getChatId()))
              .replyMarkup(
                  ReplyKeyboardMarkup.builder()
                      .keyboardRow(
                          new KeyboardRow() {
                            {
                              add(
                                  KeyboardButton.builder()
                                      .text("Выдправити номер")
                                      .requestContact(true)
                                      .build());
                            }
                          })
                      .build())
              .build();
        case INPUT_PHONE_NUMBER:
          if (message.hasContact()) {
            user.setPhoneNumber(message.getContact().getPhoneNumber());
            user.setPosition(Position.NONE);
            return SendMessage.builder()
                .chatId(String.valueOf(message.getChatId()))
                .text("<b>Тут будуть рекомендації для</b> " + user.getFullName())
                .parseMode("HTML")
                .build();
          }
        default:
          return SendMessage.builder()
              .text("Ця дій не підтримується")
              .chatId(String.valueOf(message.getChatId()))
              .build();
      }
    }

    if (message.hasText()) {
      if (message.getText().equals("/reg")) {
        cache.add(generateUserFromMessage(message));
        return SendMessage.builder()
            .chatId(String.valueOf(message.getChatId()))
            .text("Введіть свій ПІБ")
            .build();
      }
    }

    return SendMessage.builder()
        .text("Dead end")
        .chatId(String.valueOf(message.getChatId()))
        .build();
  }

  private BotUser generateUserFromMessage(Message message) {
    BotUser user = new BotUser();
    user.setUserName(message.getFrom().getUserName());
    user.setFullName(message.getFrom().getFirstName() + message.getFrom().getLastName());
    user.setId(message.getChatId());
    user.setPosition(Position.INPUT_NAME);
    return user;
  }
}
