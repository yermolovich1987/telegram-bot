package com.sample.telegrambot.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface Handler<T> {
  SendMessage choose(T t);
}
