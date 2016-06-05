package net.mccomput3rfr3ak.telegramuserqr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public class GenerateQr {
    private static String TELEGRAM_LINK = "http://telegram.me/";

    public static BufferedImage getQrWithOverlay(String username) {

        try {
            BufferedImage qrCode = generateQrCode(username);
            BufferedImage overlay = ImageIO.read(TelegramUserQr.class.getClassLoader().getResource("t_logo.png"));

            double deltaHeight = qrCode.getHeight() - overlay.getHeight();
            double deltaWidth  = qrCode.getWidth()  - overlay.getWidth();

            BufferedImage combined = new BufferedImage(qrCode.getHeight(), qrCode.getWidth(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphic = (Graphics2D) combined.getGraphics();
            graphic.drawImage(qrCode, 0, 0, null);
            graphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            graphic.drawImage(overlay, (int)Math.round(deltaWidth/2), (int)Math.round(deltaHeight/2), null);

            return combined;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static BufferedImage generateQrCode(String username){
        String userLink = TELEGRAM_LINK + username;
        int size = 300;

        try {

            Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            // Now with zxing version 3.2.1 you could change border size (white border size to just 1)
            hintMap.put(EncodeHintType.MARGIN, 0); /* default = 4 */
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix byteMatrix = qrCodeWriter.encode(userLink, BarcodeFormat.QR_CODE, size, size, hintMap);
            int CrunchifyWidth = byteMatrix.getWidth();
            BufferedImage image = new BufferedImage(CrunchifyWidth, CrunchifyWidth, BufferedImage.TYPE_INT_RGB);
            image.createGraphics();

            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth);
            graphics.setColor(Color.BLACK);

            for (int i = 0; i < CrunchifyWidth; i++) {
                for (int j = 0; j < CrunchifyWidth; j++) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }

            return image;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
}
