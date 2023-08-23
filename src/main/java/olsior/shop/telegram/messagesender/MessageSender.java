package olsior.shop.telegram.messagesender;

import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.File;

public interface MessageSender {
    void sendMessage(SendMessage sendMessage);

    void sendPhoto(SendPhoto sendPhoto);

    void sendDocument(SendDocument sendDocument);

    File findPhoto(GetFile getFile);

    File findFile(GetFile getFile);

    void downloadFile(File file, java.io.File saveFile);

    void sendMediaGroup(SendMediaGroup sendMediaGroup);

    void deleteMessage(DeleteMessage deleteMessage);

    void editMessageText(EditMessageText editMessageText);
}
