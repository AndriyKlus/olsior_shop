package olsior.shop.telegram.handler;

import olsior.shop.telegram.cache.BotUserCache;
import olsior.shop.telegram.cache.Cache;
import olsior.shop.telegram.db.TwitchGiftsDB;
import olsior.shop.telegram.domain.*;
import olsior.shop.telegram.service.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static olsior.shop.google.sheets.SheetsService.getNumberOfGifts;

@Component
public class CallbackQueryHandler implements Handler<CallbackQuery> {

    private SendMessageService sendMessageService;
    private final Cache<BotUser> cache;

    public CallbackQueryHandler() {
        cache = BotUserCache.getBotUserCache();
    }

    @Override
    public void choose(CallbackQuery callbackQuery) {
        BotUser botUser = findBotUserOrCreate(callbackQuery.getMessage());
        addTwitchGiftToPurchase(callbackQuery, botUser);
    }

    private void addTwitchGiftToPurchase(CallbackQuery callbackQuery, BotUser botUser) {
        TwitchGift twitchGift = TwitchGiftsDB.getTwitchGifts().stream()
                .filter(twitchGift1 -> twitchGift1.getName().contains(callbackQuery.getData()))
                .findFirst()
                .orElse(null);
        assert twitchGift != null;


        if (botUser.getTwitchGiftsCart() == null)
            botUser.setTwitchGiftsCart(new ArrayList<>(List.of(new TwitchGift(twitchGift))));
        else
            botUser.getTwitchGiftsCart().add(new TwitchGift(twitchGift));

        if (twitchGift.getName().equals("Набір з трьох значків зі смайлами каналу")) {
            botUser.getTwitchGiftsCart().get(botUser.getTwitchGiftsCart().size() - 1).setStickersNames(new ArrayList<>());
            botUser.setPosition(Position.STICKER);
            sendMessageService.sendChooseSticker(callbackQuery.getMessage(), 1);
        } else {
            if (getNumberOfGifts(twitchGift.getName()) != 0) {
                sendMessageService.sendTwitchPrizeAccepted(callbackQuery.getMessage());
            } else {
                sendMessageService.sendNoTwitchGift(callbackQuery.getMessage());
                sendMessageService.sendNoTwitchGiftForAdmin(botUser);
                botUser.getTwitchGiftsCart().remove(botUser.getTwitchGiftsCart().size()-1);
            }
        }
    }

    private BotUser findBotUserOrCreate(Message message) {
        BotUser botUser = cache.findBy(message.getChatId());
        if (Objects.nonNull(botUser))
            return botUser;
        botUser = new BotUser();
        botUser.setId(message.getChatId());
        botUser.setUsername(message.getFrom().getUserName());
        botUser.setTwitchGiftsCart(new ArrayList<>());
        botUser.settShirtsCart(new ArrayList<>());
        botUser.setInfoAboutDelivery("Немає");
        botUser.setAddress("");
        botUser.setPosition(Position.ADD_PRODUCT);
        cache.add(botUser);
        return botUser;
    }

    @Autowired
    private void setSendMessageService(SendMessageService sendMessageService) {
        this.sendMessageService = sendMessageService;
    }
}