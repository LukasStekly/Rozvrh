package cz.uhk.timetable.utils;

import cz.uhk.timetable.model.LocationTimeTable;

public interface TimetableProvider {
    LocationTimeTable read(String building, String room);
}
