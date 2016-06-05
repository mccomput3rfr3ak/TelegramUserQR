package net.mccomput3rfr3ak.telegramuserqr;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import net.glxn.qrgen.javase.QRCode;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GenerateQr {
    private static String TELEGRAM_LINK = "http://telegram.me/";

    private static int SIZE = 300;

    public static BufferedImage getQrWithOverlay(String username) {

        try {
            QRCode qrCode = generateQrCode(username);
            File file = qrCode.file();
            BufferedImage overlay = ImageIO.read(new File("t_logo.png"));

            BufferedImage qrCodeImage = ImageIO.read(file);

            double deltaHeight = SIZE - overlay.getHeight();
            double deltaWidth  = SIZE - overlay.getWidth();

            BufferedImage combined = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphic = (Graphics2D) combined.getGraphics();
            graphic.drawImage(qrCodeImage, 0, 0, null);
            graphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            graphic.drawImage(overlay, (int)Math.round(deltaWidth/2), (int)Math.round(deltaHeight/2), null);
            file.delete();

            return combined;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static QRCode generateQrCode(String username){
        String userLink = TELEGRAM_LINK + username;

        return QRCode.from(userLink).withErrorCorrection(ErrorCorrectionLevel.H).withSize(SIZE, SIZE);
    }
}
