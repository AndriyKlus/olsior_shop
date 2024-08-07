package olsior.shop.google.sheets;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import olsior.shop.telegram.domain.BotUser;
import olsior.shop.telegram.domain.TShirtPurchase;
import org.apache.logging.log4j.util.Strings;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SheetsService {

    private static final Sheets sheetsService;

    static {
        try {
            sheetsService = SheetsServiceUtil.getSheetsService();
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String PRODUCTS_SPREADSHEET_ID = "1-UvdD9Z-cItc7HZsyowOLhJ2YJXC5paxgs1DQtQbbqg";
    private static final String GIFTS_SPREADSHEET_ID = "1g0-y_HFWqHrh7Pve5v25DjZzL-iYtJzXEmPuTD5JItM";
    private static final String PRODUCTS_NUMBER_SPREADSHEET_ID = "1pk20M9Z9W5nceccsCuZVWEaQvIh61GaidCOYS5w2isE";
    private static final String PRODUCTS_APPLICATION_SPREADSHEETS_ID = "1P4STSJJo0b8Z5M5YQ5Q0Vs_THYeg_SJT4pyi6eYFfUk";
    private static final String TABLE_GENERAL_SELLS = "Загальний продаж";

    public static boolean addProductsToSpreadSheets(BotUser botUser) {
        boolean result = true;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        now = now.plusHours(2L);
        if (!botUser.gettShirtsCart().isEmpty()) {
            result = addProductOrder(botUser, dtf.format(now));
            if (!result)
                return false;
        }
        if (Objects.nonNull(botUser.getTwitchGiftsCart()) && !botUser.getTwitchGiftsCart().isEmpty()) {
            result = addGiftOrder(botUser, dtf.format(now));
        }
        return result;
    }

    private static boolean addProductOrder(BotUser botUser, String date) {
        try {
            ValueRange valueRange = getRowForProductOrder(botUser, date);
            sheetsService.spreadsheets().values()
                    .append(PRODUCTS_SPREADSHEET_ID, "A1", valueRange)
                    .setValueInputOption("USER_ENTERED")
                    .setInsertDataOption("INSERT_ROWS")
                    .setIncludeValuesInResponse(true)
                    .execute();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static int getNumberOfItemsForSize(String name, String size) {
        List<String> ranges = getCellForShirt(name, size);
        String readResult;
        try {
            readResult = (String) sheetsService.spreadsheets().values()
                    .batchGet(PRODUCTS_NUMBER_SPREADSHEET_ID)
                    .setRanges(ranges)
                    .execute()
                    .getValueRanges()
                    .get(0)
                    .getValues()
                    .get(0)
                    .get(0);
            return Integer.parseInt(readResult);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getNumberOfGifts(String name) {
        List<String> ranges = getCellForGift(name);
        String readResult;
        try {
            readResult = (String) sheetsService.spreadsheets().values()
                    .batchGet(PRODUCTS_NUMBER_SPREADSHEET_ID)
                    .setRanges(ranges)
                    .execute()
                    .getValueRanges()
                    .get(0)
                    .getValues()
                    .get(0)
                    .get(0);
            return Integer.parseInt(readResult);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getNumberOfSticker(String name) {
        List<String> ranges = getCellForSticker(name);
        String readResult;
        try {
            readResult = (String) sheetsService.spreadsheets().values()
                    .batchGet(PRODUCTS_NUMBER_SPREADSHEET_ID)
                    .setRanges(ranges)
                    .execute()
                    .getValueRanges()
                    .get(0)
                    .getValues()
                    .get(0)
                    .get(0);
            return Integer.parseInt(readResult);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getNumberOfSoldItems(String name, String size) {
        String ranges =  TABLE_GENERAL_SELLS + "!" + getCellForItem(name, size);
        String readResult;
        try {
            readResult = (String) sheetsService.spreadsheets().values()
                    .batchGet(PRODUCTS_SPREADSHEET_ID)
                    .setRanges(Collections.singletonList(ranges))
                    .execute()
                    .getValueRanges()
                    .get(0)
                    .getValues()
                    .get(0)
                    .get(0);
            return Integer.parseInt(readResult);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String> getCellForShirt(String name, String size) {
        int point;
        switch (name) {
            case "Футболка «ЗРОЗ»":
                point = 2;
                break;
            case "Футболка «Лагідна Українізація»":
                point = 3;
                break;
            case "Футболка «Полапав і спить»":
                point = 4;
                break;
            case "Оверсайз худі «Вірю, я повірив»":
                point = 6;
                break;
            case "Оверсайз худі «Пасти твіча»":
                point = 7;
                break;
            default:
                point = 8;
        }
        String row = "";
        switch (size) {
            case "XS":
                row = "B";
                break;
            case "S-M":
                row = "C";
                break;
            case "L-XL":
                row = "D";
        }
        return List.of(row + (point));
    }

    private static List<String> getCellForGift(String name) {
        if(name.equals("Листівка від Olsior"))
            return List.of("B7");
        else if(name.equals("Наліпки зі смайлами каналу"))
            return List.of("B8");
        return List.of("C8");
    }

    private static String getCellForItem(String name, String size) {
        int point = 0;
        switch (name) {
            case "Футболка «ЗРОЗ»":
                point = 2;
                break;
            case "Футболка «Лагідна Українізація»":
                point = 7;
                break;
            case "Футболка «Полапав і спить»":
                point = 12;
                break;
            case "Оверсайз худі «Вірю, я повірив»":
                point = 17;
                break;
            case "Оверсайз худі «Пасти твіча»":
                point = 22;
                break;
        }
        switch (size) {
            case "S-M":
                point = point + 1;
                break;
            case "L-XL":
                point = point + 2;
                break;
        }
        return "C" + point;
    }

    private static List<String> getCellForSticker(String sticker) {
        switch (sticker) {
            case "РЕСПЕКОТ":
                return List.of("B12");
            case "ПРАЙД":
                return List.of("B13");
            case "НАРУТО":
                return List.of("B14");
            case "МЯВ":
                return List.of("B15");
            case "БРО":
                return List.of("B16");
            case "РЕЙДЖ":
                return List.of("B17");
            case "СІК":
                return List.of("B18");
            case "ПЛЕД":
                return List.of("B19");
            case "КЛОУН":
                return List.of("B20");
            case "БІБЛМІРА":
                return List.of("B21");
            case "ВЕСЕЛКА":
                return List.of("B22");
            case "БАВОВНА":
                return List.of("B23");
            case "АУФ":
                return List.of("B24");
            case "ЗРОЗ":
                return List.of("B25");
            case "ШЕЙХ":
                return List.of("B26");
        }
        return List.of("C8");
    }

    public static void updateNumberOfShirts(String name, String size) {
        List<String> cell = getCellForShirt(name, size);
        ValueRange body = new ValueRange()
                .setValues(List.of(
                        List.of(getNumberOfItemsForSize(name, size)-1)));
        try {
            sheetsService.spreadsheets().values()
                    .update(PRODUCTS_NUMBER_SPREADSHEET_ID, cell.get(0), body)
                    .setValueInputOption("USER_ENTERED")
                    .setIncludeValuesInResponse(true)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateNumberOfGifts(String name) {
        List<String> cell = getCellForGift(name);
        ValueRange body = new ValueRange()
                .setValues(List.of(
                        List.of(getNumberOfGifts(name) - 1)));
        try {
            sheetsService.spreadsheets().values()
                    .update(PRODUCTS_NUMBER_SPREADSHEET_ID, cell.get(0), body)
                    .setValueInputOption("USER_ENTERED")
                    .setIncludeValuesInResponse(true)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateNumberOfSoldShirts(String name, String size) {
        int numberOfItems = getNumberOfSoldItems(name, size);
        String cell = TABLE_GENERAL_SELLS + "!" + getCellForItem(name, size);
        ValueRange body = new ValueRange()
                .setValues(List.of(
                        List.of(numberOfItems + 1)));
        try {
            sheetsService.spreadsheets().values()
                    .update(PRODUCTS_SPREADSHEET_ID, cell, body)
                    .setValueInputOption("USER_ENTERED")
                    .setIncludeValuesInResponse(true)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateNumberOfStickers(String name) {
        List<String> cell = getCellForSticker(name);
        ValueRange body = new ValueRange()
                .setValues(List.of(
                        List.of(getNumberOfSticker(name) - 1)));
        try {
            sheetsService.spreadsheets().values()
                    .update(PRODUCTS_NUMBER_SPREADSHEET_ID, cell.get(0), body)
                    .setValueInputOption("USER_ENTERED")
                    .setIncludeValuesInResponse(true)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveOrderApplication(BotUser botUser) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            ValueRange valueRange = getRowProductApplication(botUser, dtf.format(now));
            sheetsService.spreadsheets().values()
                    .append(PRODUCTS_APPLICATION_SPREADSHEETS_ID, "A1", valueRange)
                    .setValueInputOption("USER_ENTERED")
                    .setInsertDataOption("INSERT_ROWS")
                    .setIncludeValuesInResponse(true)
                    .execute();

        } catch (Exception ignored) {
        }
    }

    private static boolean addGiftOrder(BotUser botUser, String date) {
        try {
            ValueRange valueRange = getRowForGiftsOrder(botUser, date);
            sheetsService.spreadsheets().values()
                    .append(GIFTS_SPREADSHEET_ID, "A1", valueRange)
                    .setValueInputOption("USER_ENTERED")
                    .setInsertDataOption("INSERT_ROWS")
                    .setIncludeValuesInResponse(true)
                    .execute();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static ValueRange getRowForProductOrder(BotUser botUser, String date) {
        return new ValueRange()
                .setValues(List.of(
                        Arrays.asList("✖️",
                                botUser.getUsername(),
                                botUser.gettShirtsCart().stream().map(TShirtPurchase::getStringForTable).reduce("\n", String::concat),
                                botUser.getCountry(),
                                botUser.getFullName(),
                                botUser.getAddress(),
                                botUser.getPostOffice(),
                                botUser.getPhoneNumber(),
                                Strings.isEmpty(botUser.getEmail()) ? "-" : botUser.getEmail(),
                                botUser.getInfoAboutDelivery(),
                                botUser.getPaymentMethod(),
                                date,
                                botUser.gettShirtsCart().stream().filter(item -> item.getName().contains("Футболка")).count(),
                                botUser.gettShirtsCart().stream().filter(item -> item.getName().contains("худі")).count(),
                                botUser.getId(),
                                botUser.getPaymentConfirmation()
                        )));
    }

    private static ValueRange getRowForGiftsOrder(BotUser botUser, String date) {
        return new ValueRange()
                .setValues(List.of(
                        Arrays.asList("✖️",
                                botUser.getUsername(),
                                botUser.getTwitchGiftsCartString(),
                                botUser.getCountry(),
                                botUser.getAddress(),
                                botUser.getFullName(),
                                botUser.getPostOffice(),
                                botUser.getPhoneNumber(),
                                botUser.getTwitchNickname(),
                                botUser.getInfoAboutDelivery(),
                                date,
                                botUser.getId()
                                )));
    }

    private static ValueRange getRowProductApplication(BotUser botUser, String date) {
        return new ValueRange()
                .setValues(List.of(
                        Arrays.asList("@" + botUser.getUsername(),
                                botUser.getId(),
                                botUser.gettShirtPurchase().getName() + " Розмір: " + botUser.gettShirtPurchase().getSize(),
                                date
                        )));
    }


}
