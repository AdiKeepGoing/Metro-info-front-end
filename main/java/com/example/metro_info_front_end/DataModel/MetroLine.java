package com.example.metro_info_front_end.DataModel;

import java.util.List;

public class MetroLine {
    private String lineNumber;
    private List<MetroStation> stations;

    public String getLineNumber() {
        return lineNumber;
    }

    public List<MetroStation> getStations() {
        return stations;
    }
}
