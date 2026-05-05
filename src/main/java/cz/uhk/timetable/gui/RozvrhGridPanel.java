package cz.uhk.timetable.gui;

import cz.uhk.timetable.model.Activity;

import javax.swing.*;
import java.awt.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class RozvrhGridPanel extends JPanel {
    private List<Activity> activities = new ArrayList<>();

    // Nastavení osy X (od 8:00 do 20:00)
    private final int START_HOUR = 8;
    private final int END_HOUR = 20;

    private final String[] DAYS = {"Po", "Út", "St", "Čt", "Pá"};

    public void setActivities(List<Activity> activities) {
        this.activities = activities != null ? activities : new ArrayList<>();
        repaint(); // Překreslí panel při změně dat
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Odsazení pro hlavičky
        int leftMargin = 50;
        int topMargin = 30;

        int drawWidth = width - leftMargin;
        int drawHeight = height - topMargin;

        int rowHeight = drawHeight / 5;
        double pixelsPerHour = (double) drawWidth / (END_HOUR - START_HOUR);

        // Vykreslení mřížky a hlaviček (Dny a Hodiny)
        g2.setColor(Color.LIGHT_GRAY);
        g2.setFont(new Font("Arial", Font.BOLD, 12));

        // Řádky (dny)
        for (int i = 0; i <= 5; i++) {
            int y = topMargin + (i * rowHeight);
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawLine(leftMargin, y, width, y);
            if (i < 5) {
                g2.setColor(Color.BLACK);
                g2.drawString(DAYS[i], 10, y + (rowHeight / 2) + 5);
            }
        }

        // Sloupce (hodiny)
        for (int i = 0; i <= (END_HOUR - START_HOUR); i++) {
            int x = leftMargin + (int) (i * pixelsPerHour);
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawLine(x, topMargin, x, height);
            g2.setColor(Color.BLACK);
            g2.drawString((START_HOUR + i) + ":00", x - 15, 20);
        }

        // Vykreslení samotných předmětů
        for (Activity act : activities) {
            int dayIndex = getDayIndex(act.getDay());
            if (dayIndex == -1 || act.getStartTime() == null || act.getEndTime() == null) continue;

            // Výpočet pozice X a šířky podle času
            double startOffsetHours = (act.getStartTime().getHour() - START_HOUR) + (act.getStartTime().getMinute() / 60.0);
            long durationMinutes = ChronoUnit.MINUTES.between(act.getStartTime(), act.getEndTime());
            double durationHours = durationMinutes / 60.0;

            int x = leftMargin + (int) (startOffsetHours * pixelsPerHour);
            int y = topMargin + (dayIndex * rowHeight) + 5; // +5 pro menší mezeru od okraje buňky
            int actWidth = (int) (durationHours * pixelsPerHour);
            int actHeight = rowHeight - 10;

            // Kreslení bloku aktivity
            g2.setColor(new Color(100, 150, 255, 200)); // Poloprůhledná modrá
            g2.fillRect(x, y, actWidth, actHeight);

            g2.setColor(Color.BLUE.darker());
            g2.drawRect(x, y, actWidth, actHeight);

            // Vypsání zkratky předmětu do bloku
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 11));
            // Ořezání textu, aby nepřetekl blok (zjednodušeně)
            g2.drawString(act.getCode(), x + 5, y + 15);
            g2.setFont(new Font("Arial", Font.PLAIN, 10));
            g2.drawString(act.getStartTime().toString(), x + 5, y + actHeight - 5);
        }
    }

    // Pomocná metoda pro převod textu dne na index řádku
    private int getDayIndex(String dayString) {
        if (dayString == null) return -1;
        String d = dayString.toLowerCase();
        if (d.startsWith("po")) return 0;
        if (d.startsWith("út") || d.startsWith("ut")) return 1;
        if (d.startsWith("st")) return 2;
        if (d.startsWith("čt") || d.startsWith("ct")) return 3;
        if (d.startsWith("pá") || d.startsWith("pa")) return 4;
        return -1;
    }
}