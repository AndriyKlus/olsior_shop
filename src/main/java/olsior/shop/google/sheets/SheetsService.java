package olsior.shop.google.sheets;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import olsior.shop.telegram.db.TShirtDB;
import olsior.shop.telegram.domain.BotUser;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    public static boolean addProductsToSpreadSheets(BotUser botUser) {
        boolean result = true;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
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

    private static List<String> getCellForShirt(String name, String size) {
        int point;
        for ( point = 0; point < TShirtDB.getTShirts().size(); point++) {
            if (name.equals(TShirtDB.getTShirts().get(point).getName()))
                break;
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
        return List.of(row + (point + 2));
    }

    private static List<String> getCellForGift(String name) {
        if(name.equals("Листівка від Olsior"))
            return List.of("B7");
        else if(name.equals("Наліпки зі смайлами каналу"))
            return List.of("B8");
        return List.of("C8");
    }

    private static List<String> getCellForSticker(String sticker) {
        switch (sticker) {
            case "РЕСПЕКОТ":
                return List.of("B9");
            case "ПРАЙД":
                return List.of("B10");
            case "НАРУТО":
                return List.of("B11");
            case "МЯВ":
                return List.of("B12");
            case "БРО":
                return List.of("B13");
            case "РЕЙДЖ":
                return List.of("B14");
            case "СІК":
                return List.of("B15");
            case "ПЛЕД":
                return List.of("B16");
            case "КЛОУН":
                return List.of("B17");
            case "БІБЛМІРА":
                return List.of("B18");
            case "ВЕСЕЛКА":
                return List.of("B19");
            case "БАВОВНА":
                return List.of("B20");
            case "АУФ":
                return List.of("B21");
            case "ЗРОЗ":
                return List.of("B22");
            case "ШЕЙХ":
                return List.of("B23");
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
            ValueRange valueRange = getRowProductApplication(botUser);
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
                        Arrays.asList(botUser.getUsername(),
                                botUser.gettShirtsCart().toString(),
                                botUser.getCountry(),
                                botUser.getFullName(),
                                botUser.getAddress(),
                                botUser.getPostOffice(),
                                botUser.getPhoneNumber(),
                                botUser.getInfoAboutDelivery(),
                                botUser.getPaymentMethod(),
                                date,
                                botUser.getId()
                        )));
    }

    private static ValueRange getRowForGiftsOrder(BotUser botUser, String date) {
        return new ValueRange()
                .setValues(List.of(
                        Arrays.asList(botUser.getUsername(),
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

    private static ValueRange getRowProductApplication(BotUser botUser) {
        return new ValueRange()
                .setValues(List.of(
                        Arrays.asList("@" + botUser.getUsername(),
                                botUser.getId(),
                                botUser.gettShirtPurchase().getName() + " Розмір: " + botUser.gettShirtPurchase().getSize()
                        )));
    }


}
