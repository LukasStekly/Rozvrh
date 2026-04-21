package cz.uhk.timetable.gui;

import cz.uhk.timetable.model.LocationTimeTable;
import cz.uhk.timetable.utils.TimetableProvider;
import cz.uhk.timetable.utils.impl.MockTimeTableProvider;
import cz.uhk.timetable.utils.impl.StagTimetableProvider;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

public class TimetableFrame extends JFrame {
    private LocationTimeTable timetable;
    private TimetableProvider provider = new StagTimetableProvider();
    private JTable tabTimetable;

    public TimetableFrame(){
        super("FIM Rozvrhy");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
        initGui();
    }

    private void initGui() {
        timetable = provider.read("J", "J22");

        tabTimetable = new JTable(new TimetableModel());

        tabTimetable.setAutoCreateRowSorter(true);

        add(new JScrollPane(tabTimetable), BorderLayout.CENTER);


        pack();
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
