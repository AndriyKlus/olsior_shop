package olsior.shop.telegram.service;

import olsior.shop.telegram.db.TShirtDB;
import olsior.shop.telegram.db.TwitchGiftsDB;
import olsior.shop.telegram.domain.BotUser;
import olsior.shop.telegram.domain.TShirt;
import olsior.shop.telegram.domain.TShirtPurchase;
import olsior.shop.telegram.domain.TwitchGift;
import olsior.shop.telegram.messagesender.MessageSender;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

import static olsior.shop.telegram.service.KeyboardService.*;

@Service
public class SendMessageService {

    private static MessageSender messageSender;

    public SendMessageService(MessageSender messageSender) {
        SendMessageService.messageSender = messageSender;
    }

    public void sendGreetingMessage(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Привіт, тебе вітає Olsior Shop! Що хочеш замовити?")
                .chatId(message.getChatId())
                .replyMarkup(getMainMarkup(message.getChatId()))
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendTShirtsProducts(Message message) {
        TShirtDB.getTShirts()
                .forEach(tShirt -> sendMediaGroup(message, tShirt));
        /*SendMessage msg = SendMessage.builder()
                .text("Додайте товари до корзини")
                .chatId(message.getChatId())
                .replyMarkup(getShirtsMarkup())
                .build();
        messageSender.sendMessage(msg);*/
    }

    public void sendTShirtsMessage(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Купляй ексклюзивні футболки від Olsior, поки вони не закінчилися \uD83D\uDC40")
                .chatId(message.getChatId())
                .replyMarkup(getShirtsMarkup())
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendSizeGridMessage(Message message) {
        SendPhoto msg = SendPhoto.builder()
                .chatId(message.getChatId())
                .photo(new InputFile("https://i.imgur.com/f349m2z.jpg"))
                .replyMarkup(getShirtsWithNoGridMarkup())
                .build();
        messageSender.sendPhoto(msg);
    }

    public void sendNoAvaiableNow(Message message) {
        SendPhoto msg = SendPhoto.builder()
                .photo(new InputFile("https://i.imgur.com/Hjdy0uU.jpg"))
                .caption("Ми плануємо запуск магазину мерча в серпні, а поки що ти можеш замовити наші унікальні нагороди за бали каналу твіча")
                .chatId(message.getChatId())
                .replyMarkup(getMainMarkup(message.getChatId()))
                .build();
        messageSender.sendPhoto(msg);
    }

    private void sendMediaGroup(Message message, TShirt tShirt) {
        boolean captionFirst = true;
        List<InputMedia> medias = new ArrayList<>();
        for (String url : tShirt.getUrls()) {
            String mediaName = UUID.randomUUID().toString();
            if (captionFirst) {
                medias.add(
                        InputMediaPhoto.builder()
                                .media("attach://" + mediaName)
                                .caption(tShirt.getName() + "\n\n" +
                                        tShirt.getDescription() + "\n\n" +
                                        tShirt.getMaterial() + "\n\n" +
                                        "Ціна футболки: " + tShirt.getPrice() + " грн" + "\n" +
                                        "*100 грн з кожної проданої футболки перераховуємо на ЗСУ")
                                .mediaName(mediaName)
                                .isNewMedia(true)
                                .newMediaFile(new File(url))
                                .parseMode(ParseMode.HTML)
                                .build());
                captionFirst = false;
            } else {
                medias.add(
                        InputMediaPhoto.builder()
                                .media("attach://" + mediaName)
                                .mediaName(mediaName)
                                .isNewMedia(true)
                                .newMediaFile(new File(url))
                                .parseMode(ParseMode.HTML)
                                .build());
            }
        }

        SendMediaGroup sendMediaGroup = SendMediaGroup.builder()
                .chatId(message.getChatId())
                .medias(medias)
                .build();

        messageSender.sendMediaGroup(sendMediaGroup);

    }

    public void sendMessageAboutSize(Message message) {
        SendPhoto msg = SendPhoto.builder()
                .caption("Оберіть розмір футболки")
                .photo(new InputFile("https://i.imgur.com/f349m2z.jpg"))
                .chatId(message.getChatId())
                .replyMarkup(getSizesKeyboard())
                .build();
        messageSender.sendPhoto(msg);
    }

    public void sendItemWasSavedToCart(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Чудовий вибір! Товар вже в корзині.\nЧи бажаєш замовити щось ще?")
                .chatId(message.getChatId())
                .replyMarkup(getShirtWasAddedMarkup())
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendNoSizeForItem(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Нажаль цей розмір закінчився, ви можете обрати інший розмір, іншу футболку або залишити заявку на цей розмір і з вами зв'яжуться.")
                .chatId(message.getChatId())
                .replyMarkup(getNoSizeKeyboard())
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendCartInfo(BotUser botUser, Message message) {
        String cartInfo;
        if ((Objects.isNull(botUser.gettShirtsCart()) || botUser.gettShirtsCart().size() == 0)
                && (Objects.isNull(botUser.getTwitchGiftsCart()) || botUser.getTwitchGiftsCart().size() == 0))
            cartInfo = "У тебе поки немає товарів у корзині.";
        else
            cartInfo = getTextForCartInfo(botUser);

        SendMessage msg = SendMessage.builder()
                .text(cartInfo)
                .chatId(message.getChatId())
                .replyMarkup(getCartKeyboard(botUser))
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }

    private String getTextForCartInfo(BotUser botUser) {
        StringBuilder stringBuilder = new StringBuilder();

        if (Objects.nonNull(botUser.gettShirtsCart()) && !botUser.gettShirtsCart().isEmpty()) {
            stringBuilder.append("Товари до покупки:")
                    .append("\n\n");
            int w = 1;
            int price = 0;
            for (TShirtPurchase tShirt : botUser.gettShirtsCart()) {
                stringBuilder.append(w++)
                        .append(". ")
                        .append(tShirt.getName())
                        .append(tShirt.getMaterial())
                        .append("\nРозмір: ")
                        .append(tShirt.getSize())
                        .append("\nЦіна: <b>")
                        .append(tShirt.getPrice())
                        .append("</b> грн\n\n");
                price += tShirt.getPrice();
            }
            stringBuilder.append("Загальна сума: <b>")
                    .append(price)
                    .append("</b> грн\n\n");
        }
        if (Objects.nonNull(botUser.getTwitchGiftsCart()) && !botUser.getTwitchGiftsCart().isEmpty()) {
            stringBuilder.append("Нагороди за бали каналу Twitch:")
                    .append("\n\n");
            int w = 1;
            int price = 0;
            for (TwitchGift twitchGift : botUser.getTwitchGiftsCart()) {
                stringBuilder.append(w++)
                        .append(". ")
                        .append(twitchGift.getName());
                if (Objects.nonNull(twitchGift.getStickersNames()) && !twitchGift.getStickersNames().isEmpty())
                    stringBuilder.append("\nЗначки:\n")
                            .append(twitchGift.getStickersNames().get(0))
                            .append("\n")
                            .append(twitchGift.getStickersNames().get(1))
                            .append("\n")
                            .append(twitchGift.getStickersNames().get(2));
                stringBuilder.append("\nВитрачені бали: <b>")
                        .append(twitchGift.getPoints())
                        .append("</b>\n\n");
                price += twitchGift.getPoints();
            }
            stringBuilder.append("Загальна сума: <b>")
                    .append(price)
                    .append("</b>\n\n");
        }

        return stringBuilder.toString();
    }

    public void sendGoBack(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Ок, ок, біжу")
                .chatId(message.getChatId())
                .replyMarkup(getMainMarkup(message.getChatId()))
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendGoBackToShirts(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Ок, ок, біжу")
                .chatId(message.getChatId())
                .replyMarkup(getShirtsMarkup())
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendProductDeleted(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Товар видалено із корзини")
                .chatId(message.getChatId())
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendTwitchGiftsProducts(Message message) {
        for (TwitchGift twitchGift : TwitchGiftsDB.getTwitchGifts()) {
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> inlineKeyboardMerch = new ArrayList<>();
            inlineKeyboardMerch.add(Collections.singletonList(
                            InlineKeyboardButton.builder()
                                    .text("Додати до корзини")
                                    .callbackData(twitchGift.getName().length() > 20 ? twitchGift.getName().substring(0, 21) : twitchGift.getName())
                                    .build()
                    )
            );
            inlineKeyboardMarkup.setKeyboard(inlineKeyboardMerch);
            SendPhoto merchMsg = SendPhoto.builder()
                    .chatId(message.getChatId())
                    .photo(new InputFile(twitchGift.getUrls().get(0)))
                    .caption(twitchGift.getName() + "\n" +
                            "Бали каналу: <b>" + twitchGift.getPoints() + "</b>")
                    .replyMarkup(inlineKeyboardMarkup)
                    .parseMode(ParseMode.HTML)
                    .build();
            messageSender.sendPhoto(merchMsg);
        }

        SendMessage msg = SendMessage.builder()
                .text("Обери тут ті нагороди, які ти активував на твічі, та додай їх в корзину")
                .chatId(message.getChatId())
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendInputTwitchUsername(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("А тепер напиши свій нік на твічі, щоб ми могли перевірити активовані тобою нагороди")
                .chatId(message.getChatId())
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendTwitchPrizeAccepted(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Отримання нагороди додано до кошика")
                .chatId(message.getChatId())
                .replyMarkup(getMainMarkup(message.getChatId()))
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendNoTwitchGift(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("На жаль ці предмети закінчились, вам буде повернуто бали")
                .chatId(message.getChatId())
                .replyMarkup(getMainMarkup(message.getChatId()))
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendNoTwitchGiftForAdmin(BotUser botUser) {
        SendMessage msg = SendMessage.builder()
                .text("Замовлення предемету за бали каналу:\n" +
                        botUser.getTwitchGiftsCart().get(botUser.getTwitchGiftsCart().size() - 1).getName() +
                        "\nПотрібно повнернути бали, якщо витрачені" +
                        "\nТвіч нікнейм: " + botUser.getTwitchNickname())
                .chatId("6354732700")
                .replyMarkup(getMainMarkup(botUser.getId()))
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendChooseSticker(Message message, int number) {
        String text;
        if (number == 1) {
            text = "Оберіть смайл для першого значка";
            SendPhoto msg = SendPhoto.builder()
                    .caption(text)
                    .photo(new InputFile("https://i.imgur.com/8m0qYc3.jpg"))
                    .chatId(message.getChatId())
                    .replyMarkup(getChooseStickerMarkup())
                    .build();
            messageSender.sendPhoto(msg);
            return;
        } else if (number == 2)
            text = "Оберіть смайл для другого значка";
        else
            text = "Оберіть смайл для третього значка";
        SendMessage msg = SendMessage.builder()
                .text(text)
                .chatId(message.getChatId())
                .replyMarkup(getChooseStickerMarkup())
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendNoSticker(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("На жаль зараз немає цього значка, оберіть, будь ласка, інший.")
                .chatId(message.getChatId())
                .replyMarkup(getChooseStickerMarkup())
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendCartInfoWithConfirming(BotUser botUser, Message message) {
        String cartInfo;
        ReplyKeyboardMarkup replyKeyboardMarkup;
        if ((Objects.isNull(botUser.gettShirtsCart()) || botUser.gettShirtsCart().size() == 0)
                && (Objects.isNull(botUser.getTwitchGiftsCart()) || botUser.getTwitchGiftsCart().size() == 0)) {
            cartInfo = "У тебе поки немає товарів у корзині.";
            replyKeyboardMarkup = getMainMarkup(message.getChatId());
        } else {
            cartInfo = "Підтвердіть інформацію по вашому замволенню:\n\n" + getTextForCartInfo(botUser) + "+ вартість доставки за тарифами пошти.";
            replyKeyboardMarkup = getConfirmationCartKeyboard();
        }
        SendMessage msg = SendMessage.builder()
                .text(cartInfo)
                .chatId(message.getChatId())
                .replyMarkup(replyKeyboardMarkup)
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendChoosePlace(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Оберіть місце доставки")
                .chatId(message.getChatId())
                .replyMarkup(getPlaceKeyboard())
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendQuestionProblem(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Виникла проблема із функціоналом: @just_Andriy\n" +
                        "Виникло питання щодо замовлення чи доставки: @olsioriwe")
                .chatId(message.getChatId())
                .replyMarkup(getMainMarkup(message.getChatId()))
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendInputUserId(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Введіть id користувача")
                .chatId(message.getChatId())
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendProblem(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Опишіть вашу проблему")
                .chatId(message.getChatId())
                .replyMarkup(getBackKeyboard())
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendInfo(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Напишіть свій коментар")
                .chatId(message.getChatId())
                .replyMarkup(getBackKeyboard())
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendCommentAccepted(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Коментар збережено")
                .chatId(message.getChatId())
                .replyMarkup(getConfirmationKeyboard())
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendInputFullName(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Введіть свій ПІБ")
                .chatId(message.getChatId())
                .replyMarkup(getBackKeyboard())
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendInputFullNameEng(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Введіть свій ПІБ (англійською мовою)")
                .chatId(message.getChatId())
                .replyMarkup(getBackKeyboard())
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendInputCity(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Введіть місто та область доставки")
                .chatId(message.getChatId())
                .replyMarkup(getBackKeyboard())
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendInputCountry(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Введіть країну доставки\n* Відправляємо Укрпоштою в ті країни, в які вона доставляє. Доставки в росію та білорусь немає.")
                .chatId(message.getChatId())
                .replyMarkup(getBackKeyboard())
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendInputPostOffice(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Введіть номер відділення нової пошти")
                .chatId(message.getChatId())
                .replyMarkup(getBackKeyboard())
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendInputAddress(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Введіть повну адресу та індекс (анлійською мовою)")
                .chatId(message.getChatId())
                .replyMarkup(getBackKeyboard())
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendInputPhoneNumber(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Введіть свій номер телефону")
                .chatId(message.getChatId())
                .replyMarkup(getPhoneNumberKeyboard())
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendInputQuestions(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Якщо у тебе є додаткові побажання по доставці чи питання, напишіть їх")
                .chatId(message.getChatId())
                .replyMarkup(getQuestionsKeyboard())
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendChoosePaymentMethod(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Оберіть спосіб оплати")
                .chatId(message.getChatId())
                .replyMarkup(getPaymentMethodKeyboard())
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendApplicationAccepted(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Ваша заявка прийнята, коли товар зявиться із вами зв'яжуться.")
                .chatId(message.getChatId())
                .replyMarkup(getMainMarkup(message.getChatId()))
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendGiftsUrls(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Під час перегляду стрімів Олсіора на твічі, ти накопичуєш бали каналу - Долсіори. За ці бали ти можеш активувати одну з наших фірмових нагород:\n" +
                        "\n" +
                        "- <a href='https://i.imgur.com/WeBG9aU.jpeg'>Листівка</a> - 150 000 балів каналу\n" +
                        "- <a href='https://i.imgur.com/Ryy6mHJ.jpeg'>Наліпки зі смайлами каналу</a> - 125 000 балів каналу\n" +
                        "- <a href='https://i.imgur.com/AEUxJrx.jpeg'>Набір з трьох значків зі смайлами каналу</a> - 100 000 балів каналу.\n\n" +
                        "Це фізичні нагороди, які ми відправимо тобі поштою.")
                .chatId(message.getChatId())
                .replyMarkup(getGiftsUrlsMarkup())
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendInstructionQuestion(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Для того щоб замовити нагороду, треба спочатку накопичити потрібну кількість балів каналу на твічі, а потім активувати її на сторінці нагород.\n" +
                        "\n" +
                        "Якщо потрібна з цим допомога, то в нас для тебе є інструкція.")
                .chatId(message.getChatId())
                .replyMarkup(getInstructionQuestionMarkup())
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendInstruction(Message message) {
        SendPhoto msg = SendPhoto.builder()
                .caption("Щоб все спрацювало, ти маєш бути залагованим в свій твіч аккаунт та мати потрібну для цих нагород кількість балів каналу")
                .photo(new InputFile("https://i.imgur.com/mZhMxzH.jpg"))
                .chatId(message.getChatId())
                .replyMarkup(getInstructionMarkup())
                .parseMode("HTML")
                .build();
        messageSender.sendPhoto(msg);
    }

    public void sendHelp(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Якщо у тебе щось не виходить з футболками/нагородами, напиши нам на аккаунт @olsiorshop, і ми допоможемо тобі розібратись")
                .chatId(message.getChatId())
                .replyMarkup(getMainMarkup(message.getChatId()))
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }


    public void sendConfirmationForm(BotUser botUser, Message message) {
        String form = getConfirmationForm(botUser);
        SendMessage msg = SendMessage.builder()
                .text(form)
                .chatId(message.getChatId())
                .replyMarkup(getConfirmationKeyboard())
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendConfirmationFormForGifts(BotUser botUser, Message message) {
        String form = getConfirmationForm(botUser);
        SendMessage msg = SendMessage.builder()
                .text("Підтвердіть інформацію:\n\n" + form)
                .chatId(message.getChatId())
                .replyMarkup(getConfirmationGiftKeyboard())
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendWaitingForReceipt(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Очікую скріншот квитанції...")
                .chatId(message.getChatId())
                .build();
        messageSender.sendMessage(msg);
    }


    private String getConfirmationForm(BotUser botUser) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Підтвердіть правильність ваших даних для відправки:")
                .append("\n\nПІБ: <b>")
                .append(botUser.getFullName())
                .append("</b>\nКраїна: <b>")
                .append(botUser.getCountry())
                .append("</b>\nАдреса: <b>")
                .append(botUser.getAddress())
                .append("</b>");

        if (Objects.nonNull(botUser.getPostOffice())) {
            stringBuilder.append("\nВідділення нової пошти: <b>")
                    .append(botUser.getPostOffice())
                    .append("</b>");
        } else {
            stringBuilder.append("\nАдреса: <b>")
                    .append(botUser.getAddress())
                    .append("</b>");
        }

        stringBuilder.append("\nНомер телефону: <b>")
                .append(botUser.getPhoneNumber())
                .append("</b>");

        if (Objects.nonNull(botUser.getInfoAboutDelivery()))
            stringBuilder.append("\nДодаткові побажання: ")
                    .append(botUser.getInfoAboutDelivery());

        stringBuilder.append("\n\n*доставка оплачується отримувачем по тарифам пошти");

        return stringBuilder.toString();
    }

    public void sendOnlinePayment(Message message, BotUser botUser) {
        SendMessage msg = SendMessage.builder()
                .text("Оплатити замовлення можна двома способами:\n\n" +
                        "1. Оплата переводом на картку ФОП\n" +
                        "4035 2000 4217 4885\n" +
                        "ФОП Звєрєва К. К,\n" +
                        "\n" +
                        "2. Оплата на рахунок ФОП\n" +
                        "Одержувач\n" +
                        "ФОП Звєрєва Крістіна Костянтинівна\n" +
                        "IBAN\n" +
                        "UA233220010000026000320072809\n" +
                        "ЄДРПОУ\n" +
                        "3292320247\n" +
                        "Призначення:\n" +
                        "оплата товару\n" +
                        "\n" +
                        "Після оплати надішліть сюди скріншот квитанції.\n" +
                        "\n" +
                        "Доставка за рахунок покупця, будь ласка, прорахуйте вартість доставки до вашого місця призначення: https://novaposhta.ua/delivery")
                .chatId(message.getChatId())
                .replyMarkup(getBackKeyboardHelp())
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
        sendMessageToAdmin(botUser);
    }

    public void sendPhotoAccepted(Message message) {
        if (message.hasPhoto()) {
            List<PhotoSize> photos = message.getPhoto();
            GetFile getFile = new GetFile(photos.get(photos.size() - 1).getFileId());
            org.telegram.telegrambots.meta.api.objects.File file = messageSender.findPhoto(getFile);
            messageSender.downloadFile(file, new java.io.File("\\root\\olsiorShop\\olsior_shop\\receipt\\receipt" + "_" + message.getChatId() + ".png"));
        } else {
            String uploadedFileId = message.getDocument().getFileId();
            GetFile uploadedFile = new GetFile();
            uploadedFile.setFileId(uploadedFileId);
            String uploadedFilePath = messageSender.findFile(uploadedFile).getFilePath();
            File localFile = new File("\\root\\olsiorShop\\olsior_shop\\receipt\\receipt" + "_" + message.getChatId() + ".png");
            try {
                InputStream is = new URL("https://api.telegram.org/file/bot" + "6020040632:AAHHL5uowE2IizwP16AmqkT8a7U0lhtF5_M" + "/" + uploadedFilePath).openStream();
                FileUtils.copyInputStreamToFile(is, localFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        SendMessage msg = SendMessage.builder()
                .text("Квитанція збережена.\nЧи потрібно з вами зв‘язатися для підтвердження замовлення?")
                .chatId(message.getChatId())
                .replyMarkup(confirmingReceiptKeyboard())
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendOnlinePaymentWithoutConfirmation(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Дякуємо за замовлення!\n" +
                        "\n" +
                        "Термін відправки: до 7 днів, термін доставки залежать від оператора.\n" +
                        "Ми зв'яжемось з вами, якщо треба буде щось уточнити.\n" +
                        "\n" +
                        "Якщо у тебе є запитання стосовно вашого замовлення, пишіть на @olsiorshop")
                .chatId(message.getChatId())
                .replyMarkup(getMainMarkup(message.getChatId()))
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendWrongAddingOrder(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Виникла помилка із збереженням замовлення, спробуйте, будь ласка, пізніше.")
                .chatId(message.getChatId())
                .replyMarkup(getMainMarkup(message.getChatId()))
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendAbroadConfirmation(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Дякуємо за замовлення!\n" +
                        "\n" +
                        "Термін відправки: до 7 днів, термін доставки залежать від оператора.\n" +
                        "Ми зв'яжемось з вами, якщо треба буде щось уточнити.\n" +
                        "\n" +
                        "Якщо у тебе є запитання стосовно вашого замовлення, пишіть на @olsiorshop")
                .chatId(message.getChatId())
                .replyMarkup(getMainMarkup(message.getChatId()))
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendOnlinePaymentWithConfirmation(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Дякуємо за замовлення!\n" +
                        "\n" +
                        "Термін відправки: до 7 днів, термін доставки залежать від оператора.\n" +
                        "Ми зв'яжемось з вами, якщо треба буде щось уточнити.\n" +
                        "\n" +
                        "Якщо у тебе є запитання стосовно вашого замовлення, пишіть на @olsiorshop")
                .chatId(message.getChatId())
                .replyMarkup(getMainMarkup(message.getChatId()))
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendPostOfficeConfirmation(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Дякуємо за замовлення!\n" +
                        "\n" +
                        "Термін відправки: до 7 днів, термін доставки залежать від оператора.\n" +
                        "Ми зв'яжемось з вами, якщо треба буде щось уточнити.\n" +
                        "\n" +
                        "Якщо у тебе є запитання стосовно вашого замовлення, пишіть на @olsiorshop")
                .chatId(message.getChatId())
                .replyMarkup(getMainMarkup(message.getChatId()))
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendMessageToAdmin(BotUser botUser, boolean confirmation) {
        String textMessage = "Інформація про замовлення:\n\n" + getConfirmationFormForAdmin(botUser);
        if (botUser.getCountry().equals("Україна")) {
            if (confirmation)
                textMessage += "\n\nНеобхідно підтвердити онлайн оплату";
            if (Objects.nonNull(botUser.getPaymentMethod()) && botUser.getPaymentMethod().equals("Онлайн оплата")) {
                SendDocument msg = SendDocument.builder()
                        .document(new InputFile(new File("receipt\\receipt_" + botUser.getId() + ".png")))
                        .caption(textMessage)
                        .chatId("6354732700")
                        .replyMarkup(getMainMarkup(botUser.getId()))
                        .parseMode("HTML")
                        .build();
                messageSender.sendDocument(msg);

                File file = new File("receipt\\receipt_" + botUser.getId() + ".png");
                file.delete();
            } else {
                SendMessage msg = SendMessage.builder()
                        .text(textMessage)
                        .chatId("6354732700")
                        .replyMarkup(getMainMarkup(botUser.getId()))
                        .parseMode("HTML")
                        .build();
                messageSender.sendMessage(msg);
            }
        } else {
            SendMessage msg = SendMessage.builder()
                    .text(textMessage)
                    .chatId("6354732700")
                    .replyMarkup(getMainMarkup(botUser.getId()))
                    .parseMode("HTML")
                    .build();
            messageSender.sendMessage(msg);
        }
    }

    public void sendMessageToAdmin(BotUser botUser) {
        String textMessage = "Інформація про НЕДОЗАПОВНЕНЕ замовлення:\n\n" + getConfirmationFormForAdmin(botUser);
        SendMessage msg = SendMessage.builder()
                .text(textMessage)
                .chatId("6354732700")
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }


    private StringBuilder getProductForTheAdminForm(BotUser botUser) {
        StringBuilder stringBuilder = new StringBuilder();
        if (Objects.nonNull(botUser.gettShirtsCart()) && !botUser.gettShirtsCart().isEmpty()) {
            stringBuilder.append("Товари:")
                    .append("\n");
            int w = 1;
            int sum = 0;
            for (TShirtPurchase tShirt : botUser.gettShirtsCart()) {
                stringBuilder
                        .append('\n')
                        .append(w++)
                        .append(". ")
                        .append(tShirt.getName())
                        .append("\nРозмір: ")
                        .append(tShirt.getSize())
                        .append("\nЦіна: <b>")
                        .append(tShirt.getPrice())
                        .append("</b> грн\n");
                sum += tShirt.getPrice();
            }
            stringBuilder.append("Загальна сума: ")
                    .append(sum)
                    .append("\n\n");
        }

        if (Objects.nonNull(botUser.getTwitchGiftsCart()) && !botUser.getTwitchGiftsCart().isEmpty()) {
            stringBuilder.append("Нагороди за бали каналу Twitch:")
                    .append("\n\n");
            int w = 1;
            for (TwitchGift twitchGift : botUser.getTwitchGiftsCart()) {
                stringBuilder.append("\n")
                        .append(w++)
                        .append(". ")
                        .append(twitchGift.getName());
                if (Objects.nonNull(twitchGift.getStickersNames()) && !twitchGift.getStickersNames().isEmpty())
                    stringBuilder.append("\nЗначки:\n")
                            .append(twitchGift.getStickersNames().get(0))
                            .append("\n")
                            .append(twitchGift.getStickersNames().get(1))
                            .append("\n")
                            .append(twitchGift.getStickersNames().get(2));
            }
            stringBuilder.append("\nТвіч нікнейм: ")
                    .append(botUser.getTwitchNickname());
        }
        return stringBuilder;
    }

    private String getConfirmationFormForAdmin(BotUser botUser) {
        StringBuilder stringBuilder = getProductForTheAdminForm(botUser);
        stringBuilder.append("\nІнформація про доставку:")
                .append("\n\nПІБ: <b>")
                .append(botUser.getFullName())
                .append("</b>\nТелеграм: @")
                .append(botUser.getUsername())
                .append("\nКраїна: <b>")
                .append(botUser.getCountry())
                .append("</b>\nАдреса: <b>")
                .append(botUser.getAddress())
                .append("</b>");

        if (Objects.nonNull(botUser.getPostOffice())) {
            stringBuilder.append("\nВідділення нової пошти: <b>")
                    .append(botUser.getPostOffice())
                    .append("</b>");
        } else {
            stringBuilder.append("\nАдреса: <b>")
                    .append(botUser.getAddress())
                    .append("</b>");
        }

        stringBuilder.append("\nНомер телефону: <b>")
                .append(botUser.getPhoneNumber())
                .append("</b>");

        if (Objects.nonNull(botUser.getInfoAboutDelivery()))
            stringBuilder.append("\nДодаткові побажання: ")
                    .append(botUser.getInfoAboutDelivery());

        return stringBuilder.toString();
    }

    public void sendProblemAccepted(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Дякуємо за ваше звернення, з вами зв'яжуться найближчим часом")
                .chatId(message.getChatId())
                .replyMarkup(getMainMarkup(message.getChatId()))
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendInputMessageToUser(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Введіть повідомлення для користувача")
                .chatId(message.getChatId())
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendMessageToUser(Message message, Long userId) {
        try {
            SendMessage msg = SendMessage.builder()
                    .text(message.getText())
                    .chatId(userId)
                    .parseMode("HTML")
                    .build();
            messageSender.sendMessage(msg);
            SendMessage msg2 = SendMessage.builder()
                    .text("Повідомлення було надіслано.")
                    .chatId(message.getChatId())
                    .replyMarkup(getMainMarkup(message.getChatId()))
                    .build();
            messageSender.sendMessage(msg2);
        } catch (Exception e) {
            SendMessage msg = SendMessage.builder()
                    .text("Виникла проблема з надсиланням, спробуйте знову.")
                    .chatId(message.getChatId())
                    .replyMarkup(getMainMarkup(message.getChatId()))
                    .build();
            messageSender.sendMessage(msg);
        }

    }

    public void sendProblemToAdmin(Message message, BotUser botUser) {
        String stringBuilder = "Телеграм: @" +
                botUser.getUsername() +
                "\nНомер телефону: " +
                botUser.getPhoneNumber() +
                "\nПроблема: " +
                message.getText() + "\n\n" +
                getProductForTheAdminForm(botUser);
        SendMessage msg = SendMessage.builder()
                .text(stringBuilder)
                .chatId("6354732700")
                .replyMarkup(getMainMarkup(message.getChatId()))
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }

    public void sendNotifyToUser(Message message) {
        SendMessage msg = SendMessage.builder()
                .text("Футболка, на яку ви залишали заявку знову доступна у продажі, для замовлення пишіть на @olsiorshop або замовляйте у боті.")
                .chatId(message.getText().substring(message.getText().indexOf(' ') + 1))
                .replyMarkup(getMainMarkup(message.getChatId()))
                .parseMode("HTML")
                .build();
        messageSender.sendMessage(msg);
    }

}
