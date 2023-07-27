package olsior.shop.google.sheets;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.SheetsScopes;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.List;

public class GoogleAuthorizeUtil {

    private static final String CREDENTIALS_PATH = "src/main/resources/credentials.p12";

    public static Credential authorize() throws IOException, GeneralSecurityException {


        final HttpTransport TRANSPORT = new NetHttpTransport();
        final JsonFactory JSON_FACTORY = new JacksonFactory();

        return new  GoogleCredential.Builder()
                .setTransport(TRANSPORT)
                .setJsonFactory(JSON_FACTORY)
                .setServiceAccountId("olsior-shop@olsior-shop.iam.gserviceaccount.com")
                .setServiceAccountScopes(List.of(SheetsScopes.SPREADSHEETS))
                .setServiceAccountPrivateKeyFromP12File(new File(CREDENTIALS_PATH))
                .build();
    }
}