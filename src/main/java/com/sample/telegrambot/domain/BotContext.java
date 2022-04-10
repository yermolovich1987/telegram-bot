package com.sample.telegrambot.domain;

import com.sample.telegrambot.api.ChatBot;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Message;

@RequiredArgsConstructor
public class BotContext {
  private final ChatBot bot;
  private final BotUser user;
  private final Message message;

  public static BotContext of(ChatBot bot, BotUser user, Message message) {
    return new BotContext(bot, user, message);
  }

  public ChatBot getBot() {
    return bot;
  }

  public BotUser getUser() {
    return user;
  }

  public Message getInput() {
    return message;
  }
}
