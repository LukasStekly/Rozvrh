package cz.uhk.timetable.gui;

import cz.uhk.timetable.model.LocationTimeTable;
import javax.swing.*;
import java.awt.*;

public class RozvrhGridFrame extends JFrame {
    private RozvrhGridPanel gridPanel;

    public RozvrhGridFrame() {
        super("Grafický přehled rozvrhu");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); // Jen skryje, nezavře celou appku
        setLocationRelativeTo(null);

        gridPanel = new RozvrhGridPanel();
        add(gridPanel, BorderLayout.CENTER);
    }

    // Tuto metodu zavoláme z hlavního okna při vyhledání nových dat
    public void updateData(LocationTimeTable timetable) {
        if (timetable != null) {
            setTitle("Rozvrh: Budova " + timetable.getBuilding() + ", místnost " + timetable.getRoom());
            gridPanel.setActivities(timetable.getActivities());
        }
    }
}