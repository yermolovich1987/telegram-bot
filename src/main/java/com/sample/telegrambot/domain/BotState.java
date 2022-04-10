package com.sample.telegrambot.domain;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Getter
public enum BotState {
  START() {
    @Override
    public void enter(BotContext context) {
      sendSampleMessage(context, "Welcome to the chat with bot.");
    }

    @Override
    public BotState nextState() {
      return INPUT_NAME;
    }
  },
  INPUT_NAME() {
    @Override
    public void enter(BotContext context) {
      sendSampleMessage(context, "Enter your full name please:");
    }

    @Override
    public void handleInput(BotContext context) {
      context.getUser().setFullName(context.getInput().getText());
    }

    @Override
    public BotState nextState() {
      return INPUT_PHONE_NUMBER;
    }
  },

  INPUT_PHONE_NUMBER() {
    @Override
    public void enter(BotContext context) {
      sendMarkupMessage(
          context,
          "Please, share your phone number",
          ReplyKeyboardMarkup.builder()
              .inputFieldPlaceholder("Do not enter anything...")
              .keyboardRow(
                  new KeyboardRow() {
                    {
                      add(
                          KeyboardButton.builder()
                              .text("Відправити номер")
                              .requestContact(true)
                              .build());
                    }
                  })
              .build());
    }

    @Override
    public void handleInput(BotContext context) {
      context.getUser().setPhoneNumber(context.getInput().getContact().getPhoneNumber());
    }

    @Override
    public BotState nextState() {
      return FINAL;
    }
  },
  FINAL(false) {
    @Override
    public void enter(BotContext context) {
      sendSampleMessage(
          context,
          "Thanks for registration. Your data next steps will be provided soon. Entered data: "
              + context.getUser().toUserFriendlyMessage());
    }

    @Override
    public BotState nextState() {
      return FINAL;
    }
  };

  BotState() {
    this.inputNeeded = true;
  }

  BotState(boolean inputNeeded) {
    this.inputNeeded = inputNeeded;
  }

  //  private final BotState nextState;
  private boolean inputNeeded;

  public static BotState getInitialState() {
    return START;
  }

  public static BotState byId(String stateId) {
    return BotState.valueOf(stateId);
  }

  protected void sendSampleMessage(BotContext context, String text) {
    SendMessage message =
        SendMessage.builder().chatId(context.getUser().getChatId()).text(text).build();
    try {
      context.getBot().execute(message);
    } catch (TelegramApiException e) {
      log.error("Failed to send message", e);
    }
  }

  protected void sendMarkupMessage(BotContext context, String text, ReplyKeyboardMarkup markup) {
    SendMessage message =
        SendMessage.builder()
            .chatId(context.getUser().getChatId())
            .text(text)
            .replyMarkup(markup)
            .build();
    try {
      context.getBot().execute(message);
    } catch (TelegramApiException e) {
      log.error("Failed to send message", e);
    }
  }

  public boolean isInputNeeded() {
    return inputNeeded;
  }

  public boolean isFinal() {
    return FINAL == this;
  }

  public void handleInput(BotContext context) {
    // do nothing by default
  }

  public abstract void enter(BotContext context);

  public abstract BotState nextState();
}
