package com.example.metro_info_front_end.DataModel;

import java.util.List;

public class MetroStation {
    private String stationName;
    private String stationCode;
    private Boolean isTransferStation;
    private List<String> lineNumbers;

    public String getStationName() {
        return stationName;
    }

    public String getStationCode() {
        return stationCode;
    }

    public Boolean isTransferStation() {
        return isTransferStation;
    }

    public List<String> getLineNumbers() {
        return lineNumbers;
    }
}
