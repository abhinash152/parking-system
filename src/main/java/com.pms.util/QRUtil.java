package com.pms.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.UUID;

public class QRUtil {

    public static String generateBookingCode(int bookingSlotId, int userId) {
        return "PMS-" + userId + "-" + bookingSlotId + "-" +
                UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static ImageIcon generateQRImage(String content, int size) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
            return new ImageIcon(image);
        } catch (WriterException e) {
            throw new RuntimeException("Could not generate QR code", e);
        }
    }
}