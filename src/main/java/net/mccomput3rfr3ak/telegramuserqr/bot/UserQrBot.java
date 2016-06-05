package net.mccomput3rfr3ak.telegramuserqr.bot;

import net.mccomput3rfr3ak.telegramuserqr.Configuration;
import net.mccomput3rfr3ak.telegramuserqr.GenerateQr;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;


public class UserQrBot extends TelegramLongPollingBot{
    public UserQrBot() {}

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText() && message.getText().startsWith("/")) {
            switch (message.getText().substring(1)) {
                case "start":
                    sendStartMessage(message);
                    break;
                case "gen":
                    sendQrCode(message);
                    break;
                default:
                    sendUnknownCommandMessage(message);
                    break;
            }
        }
    }

    private void sendQrCode(Message message) {
        SendPhoto photo = new SendPhoto();
        photo.setChatId(message.getChatId().toString());
        photo.setReplayToMessageId(message.getMessageId());

        BufferedImage image = GenerateQr.getQrWithOverlay(message.getFrom().getUserName());

        try {
            File file = File.createTempFile("qrcode", ".png");
            ImageIO.write(image, "png", file);
            photo.setNewPhoto(file.getAbsolutePath(), "qrcode.png");
            sendPhoto(photo);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendUnknownCommandMessage(Message message) {
        SendMessage reply = new SendMessage();
        reply.enableMarkdown(true);
        reply.setChatId(message.getChatId().toString());
        reply.setReplayToMessageId(message.getMessageId());
        reply.setText("Sorry, but I don't recognize this command.");

        try {
            sendMessage(reply);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendStartMessage(Message message) {
        SendMessage reply = new SendMessage();
        reply.enableMarkdown(true);
        reply.setChatId(message.getChatId().toString());
        reply.setText("Hello " + message.getFrom().getFirstName() + ",\n"
                + "you can send me the command */gen* to generate your own qr code. "
                + "You may add an argument to create a custom QR-Code for a channel or group (eg. _/gen programmerjokes_).\n"
                + "Just send me a picture of a Telegram-QR-Code to resolve a Telegram user. ");

        try {
            sendMessage(reply);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        return Configuration.USER_QR_BOT_USERNAME;
    }

    public String getBotToken() {
        return Configuration.USER_QR_BOT_TOKEN;
    }
}
