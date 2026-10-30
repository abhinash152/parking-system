package com.pms.ui;

import com.pms.dao.SlotDAO;
import com.pms.model.Slot;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageSlotsFrame extends JFrame {

    private DefaultTableModel model;

    public ManageSlotsFrame() {
        setTitle("Manage Slots / Floors");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(520, 480);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(UITheme.buildHeader("Manage Slots / Floors"), BorderLayout.NORTH);

        String[] columns = {"Slot ID", "Floor", "Slot No.", "Status"};
        model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setRowHeight(24);
        table.getTableHeader().setBackground(UITheme.PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        add(scrollPane, BorderLayout.CENTER);

        JPanel addPanel = new JPanel(new FlowLayout());
        addPanel.setBackground(UITheme.BACKGROUND);
        addPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 15, 10));
        JTextField floorField = new JTextField(4);
        JTextField slotNoField = new JTextField(6);
        JButton addBtn = new JButton("Add Slot");

        addPanel.add(new JLabel("Floor No:"));
        addPanel.add(floorField);
        addPanel.add(new JLabel("Slot No (e.g. C-1):"));
        addPanel.add(slotNoField);
        addPanel.add(addBtn);
        add(addPanel, BorderLayout.SOUTH);

        loadSlots();

        addBtn.addActionListener(e -> {
            try {
                int floor = Integer.parseInt(floorField.getText().trim());
                String slotNo = slotNoField.getText().trim();
                if (slotNo.isEmpty()) throw new NumberFormatException();
                boolean ok = new SlotDAO().addSlot(floor, slotNo);
                if (ok) {
                    loadSlots();
                    floorField.setText("");
                    slotNoField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Could not add slot.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid floor number and slot label.", "Invalid input", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void loadSlots() {
        model.setRowCount(0);
        List<Slot> slots = new SlotDAO().getAllSlots();
        for (Slot s : slots) {
            model.addRow(new Object[]{s.getSlotId(), s.getFloorNo(), s.getSlotNo(), s.getStatus()});
        }
    }
}