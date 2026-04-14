package cz.uhk.timetable.utils.impl;

import com.google.gson.Gson;
import cz.uhk.timetable.model.LocationTimeTable;
import cz.uhk.timetable.utils.TimetableProvider;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class StagTimetableProvider  implements TimetableProvider {
    @Override
    public LocationTimeTable read(String building, String room) {
        //1. pripravit URL pro nacteni ze STAGU
        var url = "https://stag-demo.uhk.cz/ws/services/rest2/rozvrhy/getRozvrhByMistnost" +
                "?semestr=LS&budova=%s&mistnost=%s&outputFormat=JSON".formatted(building,room);
        //2. Pripojit se k serveru
        try{
           URL server = new URL(url);
            //3. Vyrobit instanci Gson parseru
            Gson gson = new Gson();
            //4. Nacist data rozvrhu pomoci
            return gson.fromJson(new InputStreamReader(server.openStream()),
                          LocationTimeTable.class
            );
        }
        catch (MalformedURLException ex)
        {
            ex.printStackTrace();
            throw new RuntimeException(ex);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
