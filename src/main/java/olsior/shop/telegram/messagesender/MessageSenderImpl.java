package olsior.shop.telegram.messagesender;

import olsior.shop.telegram.OlsiorShopBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Service
public class MessageSenderImpl implements MessageSender {

    private OlsiorShopBot olsiorShopBot;

    @Autowired
    public void setPlayerService() {

    }

    @Override
    public void sendMessage(SendMessage sendMessage) {
        try {
            olsiorShopBot.execute(sendMessage);
        } catch (TelegramApiException e) {

        }
    }

    @Override
    public void sendPhoto(SendPhoto sendPhoto) {
        try {
            olsiorShopBot.execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public File findPhoto(GetFile getFile) {
        try {
            return olsiorShopBot.execute(getFile);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public File findFile(GetFile getFile){
        try {
            return olsiorShopBot.execute(getFile);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void downloadFile(File file, java.io.File saveFile){
        try {
            olsiorShopBot.downloadFile(file, saveFile);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void sendMediaGroup(SendMediaGroup sendMediaGroup) {
        try {
            olsiorShopBot.execute(sendMediaGroup);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteMessage(DeleteMessage deleteMessage) {
        try {
            olsiorShopBot.execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void editMessageText(EditMessageText editMessageText) {
        try {
            olsiorShopBot.execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    public void setWinnerGuessBot(OlsiorShopBot olsiorShopBot) {
        this.olsiorShopBot = olsiorShopBot;
    }
}
