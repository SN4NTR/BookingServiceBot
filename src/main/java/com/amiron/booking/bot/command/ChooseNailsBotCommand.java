package com.amiron.booking.bot.command;

import com.amiron.booking.bot.util.MessageBuilder;
import com.amiron.booking.master.model.Master;
import com.amiron.booking.master.service.MasterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static com.amiron.booking.bot.command.BotCommandPattern.CHOOSE_MASTER;
import static com.amiron.booking.bot.command.BotCommandPattern.CHOOSE_NAILS;
import static com.amiron.booking.bot.util.KeyboardBuilder.buildInlineKeyboardMarkup;
import static com.amiron.booking.bot.util.MessageBuilder.buildSendPhotoMessage;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Component
public class ChooseNailsBotCommand extends BotCommand<CallbackQuery> {

    private final MasterService masterService;

    @Override
    public BotCommandPattern getCommandPattern() {
        return CHOOSE_NAILS;
    }

    @Override
    public List<? extends PartialBotApiMethod<?>> execute(@NotNull final CallbackQuery callbackQuery) {
        final Message message = callbackQuery.getMessage();
        return buildResponseToMessage(message);
    }

    private List<? extends PartialBotApiMethod<?>> buildResponseToMessage(final Message message) {
        final Long chatId = message.getChatId();
        final Integer messageId = message.getMessageId();
        final List<Master> masters = masterService.getAll();

        final EditMessageText chooseMasterMessage = buildChooseMasterMessage(chatId, messageId);
        final List<SendPhoto> mastersInfoMessages = buildMastersInfoMessages(chatId, masters);

        List messages = new ArrayList();
        messages.add(chooseMasterMessage);
        messages.addAll(mastersInfoMessages);
        return messages;
    }

    private EditMessageText buildChooseMasterMessage(final Long chatId, final Integer messageId) {
        return MessageBuilder.buildEditedMessageText(chatId, messageId, "Please choose master:", null);
    }

    private List<SendPhoto> buildMastersInfoMessages(final Long chatId, final List<Master> masters) {
        return masters.stream()
                .map(master -> buildMasterInfoMessage(chatId, master))
                .collect(toList());
    }

    private SendPhoto buildMasterInfoMessage(final Long chatId, final Master master) {
        final String masterInfoText = buildMasterInfoText(master);
        final byte[] masterPhoto = master.getPhoto();
        final String masterId = master.getId().toString();
        final InlineKeyboardMarkup keyboardMarkup = buildInlineKeyboardMarkup("Book master", format(CHOOSE_MASTER.getPatternTemplate(), masterId));
        return buildSendPhotoMessage(chatId, masterPhoto, masterInfoText, keyboardMarkup);
    }

    private String buildMasterInfoText(final Master master) {
        final String firstName = master.getFirstName();
        final String lastName = master.getLastName();
        return format("Name: %s %s", firstName, lastName);
    }
}
