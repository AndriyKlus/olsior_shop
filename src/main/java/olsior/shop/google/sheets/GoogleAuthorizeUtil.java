package olsior.shop.google.sheets;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;

import java.io.*;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.util.List;

public class GoogleAuthorizeUtil {

    private static final String CREDENTIALS_PATH = "src/main/resources/google_sheets_credentials.json";

    public static Credential authorize() throws IOException, GeneralSecurityException {
        InputStream inputStream = Files.newInputStream(new File(CREDENTIALS_PATH).toPath());
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(), new InputStreamReader(inputStream));

        List<String> scopes = List.of(SheetsScopes.SPREADSHEETS);


        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                clientSecrets, scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
                .setAccessType("offline")
                .build();

        /*FileDataStoreFactory dataStoreFactory = new FileDataStoreFactory(new File("C:/dev/ws/mainAzure/GoogleTest/res"));


        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                clientSecrets, scopes)
                .setDataStoreFactory(dataStoreFactory)
                .setAccessType("offline")
                .setApprovalPrompt("force")
                .build();*/

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }
}