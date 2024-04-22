package com.example.metro_info_front_end.DataModel;
import java.util.List;

public class MetroSystem {
    private String systemName;
    private String systemCode;
    private List<MetroLine> lines;

    public String getSystemName() {
        return systemName;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public List<MetroLine> getLines() {
        return lines;
    }
}

