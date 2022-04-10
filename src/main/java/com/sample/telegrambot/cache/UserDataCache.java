package com.sample.telegrambot.cache;

import com.sample.telegrambot.domain.BotUser;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserDataCache implements Cache<BotUser> {
  // TODO Replace with proper cache or DB.
  private final Map<Long, BotUser> users;

  public UserDataCache() {
    this.users = new HashMap<>();
  }

  @Override
  public void add(BotUser data) {
    if (data.getId() == null) {
      throw new IllegalArgumentException("Uninitialized user");
    }

    users.put(data.getId(), data);
  }

  @Override
  public void remove(BotUser botUser) {
    users.remove(botUser);
  }

  @Override
  public BotUser findById(Long id) {
    return users.get(id);
  }

  @Override
  public Collection<BotUser> getAll() {
    return users.values();
  }
}
