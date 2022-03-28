package com.sample.telegrambot.gateway;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageSender {

  void sendMessage(Message sendMessage);

  void sendMessageWithButtons(String chatId);
}
