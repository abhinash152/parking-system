package com.pms.ui;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

/**
 * Generates a simple, professional-looking "P" parking-sign logo
 * entirely in code (no external image file needed). Used as the
 * window/taskbar icon for every screen in the app.
 */
public class AppIcon {

    public static Image get() {
        int size = 64;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Blue circle background (like the international "P" parking sign)
        g.setColor(UITheme.PRIMARY);
        g.fill(new Ellipse2D.Double(2, 2, size - 4, size - 4));

        // White border ring
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(3));
        g.draw(new Ellipse2D.Double(3, 3, size - 6, size - 6));

        // Bold white "P"
        g.setColor(Color.WHITE);
        g.setFont(new Font("Segoe UI", Font.BOLD, 38));
        FontMetrics fm = g.getFontMetrics();
        String text = "P";
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();
        g.drawString(text, (size - textWidth) / 2, (size + textHeight) / 2 - 4);

        g.dispose();
        return img;
    }
}