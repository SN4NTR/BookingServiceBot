package com.amiron.booking.bot.command;

import com.amiron.booking.bot.model.BotCommand;
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

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static com.amiron.booking.bot.model.BotCommand.BOOK_NAILS;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Component
public class BookNailsCommand extends Command<CallbackQuery> {

    private final MasterService masterService;

    @Override
    public List<? extends PartialBotApiMethod<?>> execute(@NotNull final CallbackQuery callbackQuery) {
        final Long chatId = callbackQuery.getMessage().getChatId();
        final Integer messageId = callbackQuery.getMessage().getMessageId();
        final List<Master> masters = masterService.getAll();

        final EditMessageText chooseMasterMessage = buildChooseMasterMessage(chatId, messageId);
        final List<? extends SendPhoto> mastersInfoMessages = buildMastersInfoMessages(chatId, masters);

        List messages = new ArrayList();
        messages.add(chooseMasterMessage);
        messages.addAll(mastersInfoMessages);

        return messages;
    }

    @Override
    public BotCommand getCommand() {
        return BOOK_NAILS;
    }

    private EditMessageText buildChooseMasterMessage(final Long chatId, final Integer messageId) {
        return MessageBuilder.buildEditedMessageText(chatId, messageId, "Please choose master.", null);
    }

    private List<SendPhoto> buildMastersInfoMessages(final Long chatId, final List<Master> masters) {
        return masters.stream()
                .map(master -> buildMasterInfoMessage(chatId, master))
                .collect(toList());
    }

    private SendPhoto buildMasterInfoMessage(final Long chatId, final Master master) {
        final String masterInfoText = buildMasterInfoText(master);
        final byte[] photo = master.getPhoto();
        return MessageBuilder.buildSendPhotoMessage(chatId, photo, masterInfoText);
    }

    private String buildMasterInfoText(final Master master) {
        final String firstName = master.getFirstName();
        final String lastName = master.getLastName();
        return format("First name: %s\nLast name: %s\n", firstName, lastName);
    }
}
