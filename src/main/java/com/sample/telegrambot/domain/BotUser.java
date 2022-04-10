package com.sample.telegrambot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BotUser {
  private Long id;
  private BotState botState;
  @Deprecated private Position position;
  // TODO Check whether we can store this info.
  private String userName;
  private String fullName;
  private String phoneNumber;

  private String region;
  private String profession;

  public String getChatId() {
    return String.valueOf(id);
  }

  // TODO Temp
  public String toUserFriendlyMessage() {
    return "Provided Data => {"
        + "fullName='"
        + fullName
        + '\''
        + ", phoneNumber='"
        + phoneNumber
        + '\''
        + '}';
  }
}
