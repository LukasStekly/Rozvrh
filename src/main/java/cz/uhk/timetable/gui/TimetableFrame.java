package cz.uhk.timetable.gui;

import cz.uhk.timetable.model.LocationTimeTable;
import cz.uhk.timetable.utils.TimetableProvider;
import cz.uhk.timetable.utils.impl.MockTimeTableProvider;
import cz.uhk.timetable.utils.impl.StagTimetableProvider;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimetableFrame extends JFrame {
    public LocationTimeTable timetable;
    public TimetableProvider provider = new StagTimetableProvider();
    public JTable tabTimetable;
    private RozvrhGridFrame gridFrame = new RozvrhGridFrame();

    public TimetableFrame(){
        super("FIM Rozvrhy");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initGui();
    }

    private void initGui() {
        timetable = provider.read("J", "J22");

        tabTimetable = new JTable(new TimetableModel());
        JPanel comboPanel = new JPanel();

        tabTimetable.setAutoCreateRowSorter(true);

        add(new JScrollPane(tabTimetable), BorderLayout.CENTER);

        JLabel labelBudova = new JLabel("Budova");
        JLabel labelMistnost = new JLabel("Místnost");

        JComboBox<String> comboBoxBudovy = new JComboBox<>(new String[]{"A", "B","C", "J","F"});
        JComboBox<String> comboBoxMistnosti = new JComboBox<>(new String[]{"1","2","3","4","5","205","206", "207"});

        JButton odeslat = new JButton("Send");
        JButton btnZobrazitGrid = new JButton("Grafický rozvrh"); // PRIDANO

        comboPanel.add(labelBudova);
        comboPanel.add(comboBoxBudovy);
        comboPanel.add(labelMistnost);
        comboPanel.add(comboBoxMistnosti);
        comboPanel.add(odeslat);
        comboPanel.add(btnZobrazitGrid); // PRIDANO

        add(comboPanel, BorderLayout.NORTH);
        pack();

        odeslat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String vybranaBudova = (String) comboBoxBudovy.getSelectedItem();
                String vybranaMistnost = (String) comboBoxMistnosti.getSelectedItem();

                timetable = provider.read(vybranaBudova, vybranaBudova +  vybranaMistnost);

                AbstractTableModel model = (AbstractTableModel) tabTimetable.getModel();
                model.fireTableDataChanged();
                gridFrame.updateData(timetable);
            }
        });
        // PRIDANO: Akce pro nové tlačítko
        btnZobrazitGrid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gridFrame.updateData(timetable);
                gridFrame.setVisible(true); // Zobrazí druhé okno
            }
        });
    }

    class TimetableModel extends AbstractTableModel {

        @Override
        public String getColumnName(int column) {
            switch (column){
                case 0: return "Zkratka";
                case 1: return "Název";
                case 2: return "Den";
                case 3: return "Začátek";
                case 4: return "Konec";
                case 5: return "Učitel";
            }
            return "";
        }

        @Override
        public int getRowCount() {
            return timetable.getActivities().size();
        }

        @Override
        public int getColumnCount() {
            return 6;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            var act = timetable.getActivities().get(rowIndex);
            switch (columnIndex){
                case 0: return act.getCode();
                case 1: return act.getName();
                case 2: return act.getDay();
                case 3: return act.getStartTime();
                case 4: return act.getEndTime();
                case 5: return act.getTeacher();
            }
            return null;
        }
    }
}