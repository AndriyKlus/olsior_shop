package olsior.shop.telegram.handler;

import olsior.shop.google.sheets.SheetsService;
import olsior.shop.telegram.cache.BotUserCache;
import olsior.shop.telegram.cache.Cache;
import olsior.shop.telegram.db.TShirtDB;
import olsior.shop.telegram.domain.*;
import olsior.shop.telegram.service.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.*;

import static olsior.shop.google.sheets.SheetsService.*;

@Component
public class MessageHandler implements Handler<Message> {

    private SendMessageService sendMessageService;

    private final Cache<BotUser> cache;

    private final String UKRAINE = "Україна";

    public MessageHandler() {
        cache = BotUserCache.getBotUserCache();
    }

    @Override
    public void choose(Message message) {
        if (message.hasText()) {
            BotUser botUser = findBotUserOrCreate(message);

            switch (message.getText()) {
                case "/start":
                    sendMessageService.sendGreetingMessage(message);
                    return;
                case "Мерч Olsishop \uD83D\uDC55":
                case "Продовжити покупки \uD83D\uDC55":
                    botUser.setPosition(Position.ADD_SHIRT);
                    sendMessageService.sendTShirtsMessage(message);
                    sendMessageService.sendTShirtsProducts(message);
                    return;
                case "Розмірна сітка \uD83D\uDCCF":
                    sendMessageService.sendSizeGridMessage(message);
                    return;
                case "Переглянути корзину \uD83E\uDDFA":
                    sendMessageService.sendCartInfo(botUser, message);
                    botUser.setPosition(Position.ADD_PRODUCT);
                    return;
                case "Обрати інший розмір \uD83D\uDCCF":
                    sendMessageService.sendMessageAboutSize(message);
                    return;
                case "Повернутись до вибору товару ◀️":
                case "Повернутись назад ◀️":
                case "Назад ◀️":
                    botUser.setPosition(Position.ADD_PRODUCT);
                    sendMessageService.sendGoBack(message);
                    return;
                case "Повернутись до вибору ◀️":
                    botUser.setPosition(Position.ADD_SHIRT);
                    sendMessageService.sendGoBackToShirts(message);
                    return;
                case "Змінити спосіб оплати ⬅️":
                    botUser.setPosition(Position.PAYMENT_METHOD);
                    sendMessageService.sendChoosePaymentMethod(message);
                    return;
                case "Залишити заявку на цей розмір \uD83D\uDCCB":
                    botUser.setPosition(Position.ADD_PRODUCT);
                    saveOrderApplication(botUser);
                    sendMessageService.sendApplicationAccepted(message);
                    return;
                case "Нагороди за бали каналу Twitch \uD83D\uDC8E":
                    sendMessageService.sendGiftsUrls(message);
                    return;
                case "Хочу нагороду \uD83C\uDF81":
                    sendMessageService.sendInstructionQuestion(message);
                    return;
                case "Як це зробити ❓":
                    sendMessageService.sendInstruction(message);
                    return;
                case "Потрібна допомога ❓":
                    sendMessageService.sendHelp(message);
                    return;
                case "Я все зробив ❕":
                    if (Objects.isNull(botUser.getTwitchNickname()) || botUser.getTwitchNickname().isEmpty()) {
                        sendMessageService.sendInputTwitchUsername(message);
                        botUser.setPosition(Position.TWITCH_USERNAME);
                        return;
                    } else {
                        sendMessageService.sendTwitchGiftsProducts(message);
                    }
                    break;
                case "Перейти до оформлення замовлення \uD83D\uDCC3":
                    sendMessageService.sendCartInfoWithConfirming(botUser, message);
                    return;
                case "Підтверджую ✅":
                case "Ввести інші дані ◀️":
                    botUser.setPosition(Position.PLACE);
                    sendMessageService.sendChoosePlace(message);
                    return;
                case "Допомога ❔":
                    botUser.setPosition(Position.PROBLEM);
                    sendMessageService.sendProblem(message);
                    return;
                case "Додати коментар \uD83D\uDCC4":
                    sendMessageService.sendInfo(message);
                    botUser.setPosition(Position.INFO_ABOUT_DELIVERY);
                    return;
                case "Виникла проблема/питання ❓":
                    sendMessageService.sendQuestionProblem(message);
                    return;
                default:
                    if (message.getText().contains("Видалити"))
                        deleteProductFromCart(message);
                    break;
            }


            switch (botUser.getPosition()) {
                case ADD_SHIRT:
                    addTShirtToPurchase(message, botUser);
                    sendMessageService.sendMessageAboutSize(message);
                    break;

                case SIZE:
                    saveItemToCart(message, botUser);
                    break;

                case TWITCH_USERNAME:
                    botUser.setTwitchNickname(message.getText());
                    botUser.setPosition(Position.ADD_PRODUCT);
                    sendMessageService.sendTwitchGiftsProducts(message);
                    break;

                case STICKER:
                    if (getNumberOfSticker(message.getText()) != 0) {
                        botUser.getTwitchGiftsCart().get(botUser.getTwitchGiftsCart().size() - 1).getStickersNames().add(message.getText());
                        if (botUser.getTwitchGiftsCart().get(botUser.getTwitchGiftsCart().size() - 1).getStickersNames().size() == 3) {
                            botUser.setPosition(Position.ADD_PRODUCT);
                            sendMessageService.sendTwitchPrizeAccepted(message);
                        } else
                            sendMessageService.sendChooseSticker(message, botUser.getTwitchGiftsCart().get(botUser.getTwitchGiftsCart().size() - 1).getStickersNames().size() + 1);
                    } else {
                        sendMessageService.sendNoSticker(message);
                        break;
                    }
                    break;

                case PLACE:
                    if (message.getText().contains(UKRAINE)) {
                        botUser.setCountry(message.getText().substring(0, message.getText().length() - 5));
                        botUser.setPosition(Position.FULL_NAME);
                        sendMessageService.sendInputFullName(message);
                    } else if (message.getText().equals("По світу \uD83C\uDF0D")) {
                        botUser.setCountry("Abroad");
                        botUser.setPosition(Position.COUNTRY);
                        sendMessageService.sendInputCountry(message);
                    }
                    break;

                case FULL_NAME:
                    botUser.setFullName(message.getText());
                    if (botUser.getCountry().equals(UKRAINE)) {
                        botUser.setPosition(Position.ADDRESS);
                        sendMessageService.sendInputCity(message);
                    } else {
                        botUser.setPosition(Position.ADDRESS);
                        sendMessageService.sendInputAddress(message);
                    }
                    break;

                case COUNTRY:
                    botUser.setCountry(message.getText());
                    botUser.setPosition(Position.FULL_NAME);
                    sendMessageService.sendInputFullNameEng(message);
                    break;

                case ADDRESS:
                    botUser.setAddress(message.getText());
                    if (botUser.getCountry().equals(UKRAINE)) {
                        botUser.setPosition(Position.POST_OFFICE);
                        sendMessageService.sendInputPostOffice(message);
                    } else {
                        botUser.setPosition(Position.PHONE_NUMBER);
                        sendMessageService.sendInputPhoneNumber(message);
                    }
                    break;

                case POST_OFFICE:
                    botUser.setPostOffice(message.getText());
                    botUser.setPosition(Position.PHONE_NUMBER);
                    sendMessageService.sendInputPhoneNumber(message);
                    break;

                case PHONE_NUMBER:
                    botUser.setPhoneNumber(message.getText());
                    botUser.setPosition(Position.CONFIRMATION);
                    sendMessageService.sendConfirmationForm(botUser, message);
                    break;

                case INFO_ABOUT_DELIVERY:
                    botUser.setInfoAboutDelivery(message.getText());
                    botUser.setPosition(Position.CONFIRMATION);
                    sendMessageService.sendCommentAccepted(message);
                    break;

                case PAYMENT_METHOD:
                    botUser.setPaymentMethod(message.getText().substring(0, message.getText().length() - 3));
                    if (botUser.getPaymentMethod().equals("Онлайн оплата")) {
                        botUser.setPosition(Position.ONLINE_PAYMENT);
                        sendMessageService.sendOnlinePayment(message, botUser);
                    } else {
                        saveProductOrderImposedPaymentUkraine(botUser, message);
                    }
                    break;

                case CONFIRMATION:
                    if (Objects.nonNull(botUser.gettShirtsCart()) && !botUser.gettShirtsCart().isEmpty() && botUser.getCountry().equals(UKRAINE)) {
                        botUser.setPosition(Position.PAYMENT_METHOD);
                        sendMessageService.sendChoosePaymentMethod(message);
                    }
                    if (Objects.nonNull(botUser.getTwitchGiftsCart()) && !botUser.getTwitchGiftsCart().isEmpty()) {
                        saveGiftOrder(botUser, message);
                    }
                    if (!botUser.getCountry().equals(UKRAINE)) {
                        botUser.setPosition(Position.ONLINE_PAYMENT);
                        botUser.setPaymentMethod("Після отримання накладеним платежем");
                        botUser.setPostOffice("-");
                        sendMessageService.sendOnlinePayment(message, botUser);
                    }

                    break;

                case ONLINE_PAYMENT:
                    sendMessageService.sendWaitingForReceipt(message);
                    break;

                case PAYMENT_CONFIRMATION:
                    if (message.getText().startsWith("Так")) {
                        saveProductOrderWithOnlineConfirmation(botUser, message);
                    } else if (message.getText().startsWith("Ні")) {
                        saveProductOrderWithNoOnlineConfirmation(botUser, message);
                    }
                    break;

                case PROBLEM:
                    botUser.setPosition(Position.ADD_PRODUCT);
                    sendMessageService.sendProblemAccepted(message);
                    sendMessageService.sendProblemToAdmin(message, botUser);
                    break;

                default:
                    break;
            }

            if (message.getText().contains("Сповістити")) {
                sendMessageService.sendNotifyToUser(message);
            }

        } else if (message.hasContact()) {
            BotUser botUser = findBotUserOrCreate(message);
            botUser.setPhoneNumber(message.getContact().getPhoneNumber());
            botUser.setPosition(Position.CONFIRMATION);
            sendMessageService.sendConfirmationForm(botUser, message);
        } else if (message.hasPhoto() || message.hasDocument()) {
            processReceipt(message);
        }

    }

    private void addTShirtToPurchase(Message message, BotUser botUser) {
        TShirt tShirt = TShirtDB.getTShirts().stream()
                .filter(tShirt1 -> message.getText().contains(tShirt1.getName()))
                .findFirst()
                .orElse(null);
        assert tShirt != null;
        TShirtPurchase tShirtPurchase = new TShirtPurchase();
        tShirtPurchase.setName(tShirt.getName());
        tShirtPurchase.setMaterial(tShirt.getMaterial());
        tShirtPurchase.setPrice(tShirt.getPrice());
        tShirtPurchase.setPhotoUrls(tShirtPurchase.getPhotoUrls());

        botUser.settShirtPurchase(tShirtPurchase);
        botUser.setPosition(Position.SIZE);
    }

    private void saveItemToCart(Message message, BotUser botUser) {
        TShirtPurchase tShirtPurchase = botUser.gettShirtPurchase();
        int numberOfItems = getNumberOfItemsForSize(tShirtPurchase.getName(), message.getText());
        tShirtPurchase.setSize(message.getText());
        if (numberOfItems <= 0) {
            sendMessageService.sendNoSizeForItem(message);
            return;
        }
        if (botUser.gettShirtsCart() == null)
            botUser.settShirtsCart(new ArrayList<>(List.of(tShirtPurchase)));
        else
            botUser.gettShirtsCart().add(tShirtPurchase);
        botUser.setPosition(Position.ADD_PRODUCT);

        sendMessageService.sendItemWasSavedToCart(message);
    }

    private void deleteProductFromCart(Message message) {
        BotUser botUser = cache.findBy(message.getChatId());
        int point = Integer.parseInt(Arrays.stream(message.getText().split("\\.")).findFirst().orElse(""));
        if (point <= botUser.gettShirtsCart().size())
            botUser.gettShirtsCart().remove(point - 1);
        else
            botUser.getTwitchGiftsCart().remove(point - botUser.gettShirtsCart().size() - 1);
        sendMessageService.sendProductDeleted(message);
        sendMessageService.sendCartInfo(botUser, message);
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

    private void processReceipt(Message message) {
        BotUser botUser = findBotUserOrCreate(message);
        if (botUser.getPosition().equals(Position.ONLINE_PAYMENT)) {
            try {
                sendMessageService.sendPhotoAccepted(message);
                botUser.setPosition(Position.PAYMENT_CONFIRMATION);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void clearCart(BotUser botUser) {
        botUser.setPosition(Position.ADD_PRODUCT);
        botUser.setInfoAboutDelivery("Немає");
        botUser.setTwitchGiftsCart(new ArrayList<>());
        botUser.settShirtsCart(new ArrayList<>());
        botUser.settShirtPurchase(null);
        botUser.setPaymentMethod(null);
        botUser.setPaymentConfirmation(false);
    }

    private void saveProductOrderWithNoOnlineConfirmation(BotUser botUser, Message message) {
        if (updateNumberOfItems(botUser) && addProductsToSpreadSheets(botUser)) {
            sendMessageService.sendOnlinePaymentWithoutConfirmation(message);
            sendMessageService.sendMessageToAdmin(botUser, false);
            clearCart(botUser);
        } else {
            sendMessageService.sendWrongAddingOrder(message);
        }
        botUser.setPosition(Position.ADD_PRODUCT);
    }

    private void saveProductOrderWithOnlineConfirmation(BotUser botUser, Message message) {
        botUser.setPaymentConfirmation(true);
        if (updateNumberOfItems(botUser) && addProductsToSpreadSheets(botUser)) {
            sendMessageService.sendOnlinePaymentWithConfirmation(message);
            sendMessageService.sendMessageToAdmin(botUser, true);
            clearCart(botUser);
        } else {
            sendMessageService.sendWrongAddingOrder(message);
        }
        botUser.setPosition(Position.ADD_PRODUCT);
    }

    private void saveProductOrderImposedPaymentAbroad(BotUser botUser, Message message) {
        if (updateNumberOfItems(botUser) && addProductsToSpreadSheets(botUser)) {
            sendMessageService.sendAbroadConfirmation(message);
            sendMessageService.sendMessageToAdmin(botUser, true);
            clearCart(botUser);
        } else {
            sendMessageService.sendWrongAddingOrder(message);
        }
        botUser.setPosition(Position.ADD_PRODUCT);

    }

    private void saveProductOrderImposedPaymentUkraine(BotUser botUser, Message message) {
        if (updateNumberOfItems(botUser) && addProductsToSpreadSheets(botUser)) {
            sendMessageService.sendPostOfficeConfirmation(message);
            sendMessageService.sendMessageToAdmin(botUser, true);
            clearCart(botUser);
        } else {
            sendMessageService.sendWrongAddingOrder(message);
        }
        botUser.setPosition(Position.ADD_PRODUCT);
    }

    private void saveGiftOrder(BotUser botUser, Message message) {
        if (updateNumberOfItems(botUser) && addProductsToSpreadSheets(botUser)) {
            sendMessageService.sendPostOfficeConfirmation(message);
            sendMessageService.sendMessageToAdmin(botUser, false);
            clearCart(botUser);
        } else {
            sendMessageService.sendWrongAddingOrder(message);
        }
        botUser.setPosition(Position.ADD_PRODUCT);
    }

    private boolean updateNumberOfItems(BotUser botUser) {
        botUser.gettShirtsCart().forEach(shirt -> updateNumberOfShirts(shirt.getName(), shirt.getSize()));
        botUser.getTwitchGiftsCart().stream()
                .filter(gift -> Objects.isNull(gift.getStickersNames()))
                .forEach(gift -> updateNumberOfGifts(gift.getName()));
        for (TwitchGift twitchGift : botUser.getTwitchGiftsCart()) {
            if (twitchGift.getStickersNames() != null)
                twitchGift.getStickersNames().forEach(SheetsService::updateNumberOfStickers);
        }
        /*botUser.getTwitchGiftsCart().stream()
                .map(TwitchGift::getStickersNames)
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .forEach(SheetsService::updateNumberOfStickers);*/
        return true;

    }

    @Autowired
    private void setSendMessageService(SendMessageService sendMessageService) {
        this.sendMessageService = sendMessageService;
    }

}
