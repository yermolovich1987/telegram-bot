package com.sample.telegrambot.gateway;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageSender {

  void sendMessage(SendMessage sendMessage);

  void sendMessageWithButtons(String chatId);
}
