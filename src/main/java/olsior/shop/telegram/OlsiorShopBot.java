package olsior.shop.telegram;

import olsior.shop.telegram.processor.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class OlsiorShopBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String username;
    @Value("${telegram.bot.token}")
    private String token;

    private Processor processor;

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        processor.process(update);
    }

    @Autowired
    private void setProcessor(Processor processor){
        this.processor = processor;
    }

}
