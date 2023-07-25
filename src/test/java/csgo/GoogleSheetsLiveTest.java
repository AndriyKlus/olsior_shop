package csgo;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import olsior.shop.google.sheets.SheetsServiceUtil;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GoogleSheetsLiveTest {
    private static Sheets sheetsService;
    private static final String SPREADSHEET_ID = "1-UvdD9Z-cItc7HZsyowOLhJ2YJXC5paxgs1DQtQbbqg";

    @BeforeClass
    public static void setup() throws GeneralSecurityException, IOException {
        sheetsService = SheetsServiceUtil.getSheetsService();
    }

    @Test
    public void whenWriteSheet_thenReadSheetOk() throws IOException {
        ValueRange appendBody = new ValueRange()
                .setValues(List.of(
                        Arrays.asList("Total", "heh")));
        AppendValuesResponse appendResult = sheetsService.spreadsheets().values()
                .append(SPREADSHEET_ID, "A1", appendBody)
                .setValueInputOption("USER_ENTERED")
                .setInsertDataOption("INSERT_ROWS")
                .setIncludeValuesInResponse(true)
                .execute();

        ValueRange total = appendResult.getUpdates().getUpdatedData();
        assertThat(total.getValues().get(0).get(1)).isEqualTo("65");
    }
}