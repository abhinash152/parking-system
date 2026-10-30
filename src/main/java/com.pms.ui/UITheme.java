package com.pms.ui;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import java.awt.*;

/**
 * Central place for the app's colors and fonts, so every screen
 * looks consistent. Change the values here once and the whole
 * app updates.
 */
public class UITheme {

    // Official-looking blue palette
    public static final Color PRIMARY_DARK  = new Color(13, 42, 84);    // deep navy - headers
    public static final Color PRIMARY       = new Color(21, 101, 192);  // main blue - buttons
    public static final Color PRIMARY_LIGHT = new Color(66, 165, 245);  // accent blue
    public static final Color BACKGROUND    = new Color(240, 245, 250); // soft background
    public static final Color TEXT_DARK     = new Color(30, 30, 30);

    // Fonts
    public static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_BODY    = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BUTTON  = new Font("Segoe UI", Font.BOLD, 13);

    /** Call this once, at app startup, before any screen opens. */
    public static void apply() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
        }

        // Nimbus theme hooks - this is what actually turns buttons/selections blue
        UIManager.put("nimbus.base", PRIMARY);
        UIManager.put("nimbus.blueGrey", new Color(200, 214, 229));
        UIManager.put("control", BACKGROUND);
        UIManager.put("text", TEXT_DARK);
        UIManager.put("nimbus.selectionBackground", PRIMARY_LIGHT);
        UIManager.put("nimbus.selectedText", Color.WHITE);

        // Fonts everywhere
        UIManager.put("defaultFont", new FontUIResource(FONT_BODY));
        UIManager.put("Label.font", new FontUIResource(FONT_BODY));
        UIManager.put("Button.font", new FontUIResource(FONT_BUTTON));
        UIManager.put("TextField.font", new FontUIResource(FONT_BODY));
        UIManager.put("PasswordField.font", new FontUIResource(FONT_BODY));
        UIManager.put("ComboBox.font", new FontUIResource(FONT_BODY));
        UIManager.put("Table.font", new FontUIResource(FONT_BODY));
        UIManager.put("TableHeader.font", new FontUIResource(FONT_HEADING));
        UIManager.put("TitledBorder.font", new FontUIResource(FONT_HEADING));

        UIManager.put("Button.background", new ColorUIResource(PRIMARY));
        UIManager.put("Button.foreground", new ColorUIResource(Color.WHITE));
    }

    /** A reusable dark-blue header banner you can drop into the top of any screen. */
    public static JPanel buildHeader(String titleText) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_DARK);
        header.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JLabel title = new JLabel(titleText);
        title.setFont(FONT_TITLE);
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);

        return header;
    }
}