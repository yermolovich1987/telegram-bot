package com.sample.telegrambot.processor;

import com.sample.telegrambot.handler.CallbackQueryHandler;
import com.sample.telegrambot.handler.MessageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class DefaultProcessor implements Processor {
  private final CallbackQueryHandler callbackQueryHandler;
  private final MessageHandler messageHandler;

  @Override
  public SendMessage executeMessage(Message message) {
    return messageHandler.choose(message);
  }

  @Override
  public SendMessage executeCallBackQuery(CallbackQuery callbackQuery) {
    return callbackQueryHandler.choose(callbackQuery);
  }
}
