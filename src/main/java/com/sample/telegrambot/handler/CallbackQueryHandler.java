package com.sample.telegrambot.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class CallbackQueryHandler implements Handler<CallbackQuery> {
  @Override
  public SendMessage choose(CallbackQuery callbackQuery) {
    return SendMessage.builder()
        .chatId(String.valueOf(callbackQuery.getMessage().getChatId()))
        .text("Unsupported")
        .build();
  }
}
