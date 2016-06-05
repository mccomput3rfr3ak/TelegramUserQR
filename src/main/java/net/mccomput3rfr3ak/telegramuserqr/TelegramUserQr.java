package net.mccomput3rfr3ak.telegramuserqr;

import net.mccomput3rfr3ak.telegramuserqr.bot.UserQrBot;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;

public class TelegramUserQr {
    public static void main(String[] args) {

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new UserQrBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
