package olsior.shop.telegram.service;

import olsior.shop.telegram.db.TShirtDB;
import olsior.shop.telegram.db.TwitchGiftsDB;
import olsior.shop.telegram.domain.BotUser;
import olsior.shop.telegram.domain.TwitchGift;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class KeyboardService {

    public static ReplyKeyboardMarkup getMainMarkup(Long id) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        row1.add("Мерч Olsishop \uD83D\uDC55");
        row1.add("Нагороди за бали каналу Twitch \uD83D\uDC8E");
        row2.add("Переглянути корзину \uD83E\uDDFA");
        row2.add("Перейти до оформлення замовлення \uD83D\uDCC3");
        row3.add("Виникла проблема/питання ❓");
        if(Objects.nonNull(id) && ( id == 358029493L || id == 6354732700L || id == 237088388L))
            row3.add("Надіслати повідомлення ✉\uFE0F");
        keyboardRows.add(row1);
        keyboardRows.add(row2);
        keyboardRows.add(row3);
        markup.setKeyboard(keyboardRows);
        markup.setResizeKeyboard(true);

        return markup;
    }

    public static ReplyKeyboardMarkup getShirtsMarkup() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        row1.add("Розмірна сітка \uD83D\uDCCF");
        row2.add(TShirtDB.getTShirts().get(0).getName());
        row2.add(TShirtDB.getTShirts().get(1).getName());
        //row2.add(TShirtDB.getTShirts().get(2).getName());
        row3.add("Повернутись назад ◀️");
        row3.add("Потрібна допомога ❓");
        keyboardRows.add(row1);
        keyboardRows.add(row2);
        keyboardRows.add(row3);
        markup.setKeyboard(keyboardRows);
        markup.setResizeKeyboard(true);

        return markup;
    }

    public static ReplyKeyboardMarkup getShirtsWithNoGridMarkup() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        //KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        row1.add(TShirtDB.getTShirts().get(0).getName());
        row1.add(TShirtDB.getTShirts().get(1).getName());
        //row2.add(TShirtDB.getTShirts().get(2).getName());
        row3.add("Повернутись назад ◀️");
        row3.add("Потрібна допомога ❓");
        keyboardRows.add(row1);
        //keyboardRows.add(row2);
        keyboardRows.add(row3);
        markup.setKeyboard(keyboardRows);
        markup.setResizeKeyboard(true);

        return markup;
    }

    public static ReplyKeyboardMarkup getGiftsUrlsMarkup() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Хочу нагороду \uD83C\uDF81");
        row1.add("Назад ◀️");
        keyboardRows.add(row1);
        markup.setKeyboard(keyboardRows);
        markup.setResizeKeyboard(true);

        return markup;
    }

    public static ReplyKeyboardMarkup getInstructionQuestionMarkup() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Я все зробив ❕");
        row1.add("Як це зробити ❓");
        keyboardRows.add(row1);
        markup.setKeyboard(keyboardRows);
        markup.setResizeKeyboard(true);

        return markup;
    }

    public static ReplyKeyboardMarkup getInstructionMarkup() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Я все зробив ❕");
        row1.add("Потрібна допомога ❓");
        keyboardRows.add(row1);
        markup.setKeyboard(keyboardRows);
        markup.setResizeKeyboard(true);

        return markup;
    }

    public static ReplyKeyboardMarkup getShirtWasAddedMarkup() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Продовжити покупки \uD83D\uDC55");
        row1.add("Переглянути корзину \uD83E\uDDFA");
        keyboardRows.add(row1);
        markup.setKeyboard(keyboardRows);
        markup.setResizeKeyboard(true);

        return markup;
    }

    public static ReplyKeyboardMarkup getChooseStickerMarkup() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        TwitchGift twitchGift = TwitchGiftsDB.getTwitchGifts().get(2);
        for (int w = 0; w < twitchGift.getStickersNames().size(); w = w  + 3) {
            KeyboardRow row = new KeyboardRow();
            row.add(TwitchGiftsDB.getTwitchGifts().get(2).getStickersNames().get(w));
            if(TwitchGiftsDB.getTwitchGifts().get(2).getStickersNames().size() > w + 1)
                row.add(TwitchGiftsDB.getTwitchGifts().get(2).getStickersNames().get(w + 1 ));
            if(TwitchGiftsDB.getTwitchGifts().get(2).getStickersNames().size() > w + 2)
                row.add(TwitchGiftsDB.getTwitchGifts().get(2).getStickersNames().get(w + 2 ));
            keyboardRows.add(row);
        }
        keyboardRows.add(row1);
        row1.add("Повернутись назад ◀️");
        markup.setKeyboard(keyboardRows);
        markup.setResizeKeyboard(true);

        return markup;
    }

    public static ReplyKeyboardMarkup getSizesKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        row1.add("XS");
        row1.add("S-M");
        row1.add("L-XL");
        row2.add("Повернутись до вибору ◀️");
        keyboardRows.add(row1);
        keyboardRows.add(row2);
        markup.setKeyboard(keyboardRows);
        markup.setResizeKeyboard(true);

        return markup;
    }

    public static ReplyKeyboardMarkup getNoSizeKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        row1.add("Обрати інший розмір \uD83D\uDCCF");
        row1.add("Повернутись до вибору товару ◀️");
        row2.add("Залишити заявку на цей розмір \uD83D\uDCCB");
        keyboardRows.add(row1);
        keyboardRows.add(row2);
        markup.setKeyboard(keyboardRows);
        markup.setResizeKeyboard(true);

        return markup;
    }

    public static ReplyKeyboardMarkup getCartKeyboard(BotUser botUser) {
        if((Objects.isNull(botUser.gettShirtsCart()) || botUser.gettShirtsCart().size() == 0)
                && (Objects.isNull(botUser.getTwitchGiftsCart()) || botUser.getTwitchGiftsCart().size() == 0))
            return getMainMarkup(botUser.getId());
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Повернутись назад ◀️");
        keyboardRows.add(row1);
        int number = 1;
        if (Objects.nonNull(botUser.gettShirtsCart())) {
            for (int w = 0; w < botUser.gettShirtsCart().size(); w = w + 2) {
                KeyboardRow row = new KeyboardRow();
                row.add(number++ + ". Видалити: " + botUser.gettShirtsCart().get(w).getName());
                if (botUser.gettShirtsCart().size() > w + 1) {
                    row.add(number++ + ". Видалити: " + botUser.gettShirtsCart().get(w + 1).getName());
                }
                keyboardRows.add(row);
            }
        }
        if (Objects.nonNull(botUser.getTwitchGiftsCart())) {
            for (int w = 0; w < botUser.getTwitchGiftsCart().size(); w = w + 2) {
                KeyboardRow row = new KeyboardRow();
                row.add(number++ + ". Видалити: " + botUser.getTwitchGiftsCart().get(w).getName());
                if (botUser.getTwitchGiftsCart().size() > w + 1) {
                    row.add(number++ + ". Видалити: " + botUser.getTwitchGiftsCart().get(w + 1).getName());
                }
                keyboardRows.add(row);
            }
        }
        KeyboardRow row = new KeyboardRow();
        row.add("Перейти до оформлення замовлення \uD83D\uDCC3");
        keyboardRows.add(row);
        markup.setKeyboard(keyboardRows);
        markup.setResizeKeyboard(true);

        return markup;
    }

    public static ReplyKeyboardMarkup getConfirmationCartKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Підтверджую ✅");
        row1.add("Повернутись до вибору товару ◀️");
        keyboardRows.add(row1);
        markup.setKeyboard(keyboardRows);
        markup.setResizeKeyboard(true);

        return markup;
    }

    public static ReplyKeyboardMarkup getPlaceKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        row1.add("Україна \uD83C\uDDFA\uD83C\uDDE6");
        row1.add("По світу \uD83C\uDF0D");
        row2.add("Допомога ❔");
        row2.add("Повернутись до вибору товару ◀️");
        keyboardRows.add(row1);
        keyboardRows.add(row2);
        markup.setKeyboard(keyboardRows);
        markup.setResizeKeyboard(true);

        return markup;
    }

    public static ReplyKeyboardMarkup getBackKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Допомога ❔");
        row1.add("Повернутись до вибору товару ◀️");
        keyboardRows.add(row1);
        markup.setKeyboard(keyboardRows);
        markup.setResizeKeyboard(true);

        return markup;
    }

    public static ReplyKeyboardMarkup getBackKeyboardHelp() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Допомога ❔");
        row1.add("Повернутись до вибору товару ◀️");
        keyboardRows.add(row1);
        markup.setKeyboard(keyboardRows);
        markup.setResizeKeyboard(true);

        return markup;
    }

    public static ReplyKeyboardMarkup getPhoneNumberKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        row1.add(KeyboardButton.builder()
                .text("Надіслати номер телефону \uD83D\uDCDE")
                .requestContact(true)
                .build());
        row2.add("Повернутись до вибору товару ◀️");
        keyboardRows.add(row1);
        keyboardRows.add(row2);
        markup.setKeyboard(keyboardRows);
        markup.setResizeKeyboard(true);

        return markup;
    }

    public static ReplyKeyboardMarkup getQuestionsKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        row1.add("Пропустити ▶️");
        row2.add("Повернутись до вибору товару ◀️");
        keyboardRows.add(row1);
        keyboardRows.add(row2);
        markup.setKeyboard(keyboardRows);
        markup.setResizeKeyboard(true);

        return markup;
    }

    public static ReplyKeyboardMarkup getPaymentMethodKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        row1.add("Після отримання накладеним платежем ✉️");
        row1.add("Онлайн оплата \uD83D\uDCB3");
        row2.add("Повернутись до вибору товару ◀️");
        keyboardRows.add(row1);
        keyboardRows.add(row2);
        markup.setKeyboard(keyboardRows);
        markup.setResizeKeyboard(true);

        return markup;
    }

    public static ReplyKeyboardMarkup getConfirmationKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        row1.add("Підтвердити дані ✅");
        row2.add("Додати коментар \uD83D\uDCC4");
        row2.add("Ввести інші дані ◀️");
        keyboardRows.add(row1);
        keyboardRows.add(row2);
        markup.setKeyboard(keyboardRows);
        markup.setResizeKeyboard(true);

        return markup;
    }

    public static ReplyKeyboardMarkup getConfirmationGiftKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Підтвердити дані ✅");
        row1.add("Повернутись до вибору товару ◀️");
        keyboardRows.add(row1);
        markup.setKeyboard(keyboardRows);
        markup.setResizeKeyboard(true);

        return markup;
    }

    public static ReplyKeyboardMarkup confirmingReceiptKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        row1.add("Так \uD83D\uDFE2");
        row1.add("Ні \uD83D\uDD34");
        row2.add("Повернутись до вибору товару ◀️");
        keyboardRows.add(row1);
        keyboardRows.add(row2);
        markup.setKeyboard(keyboardRows);
        markup.setResizeKeyboard(true);

        return markup;
    }

}
