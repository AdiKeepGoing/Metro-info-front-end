package com.example.metro_info_front_end.ResponseModel;

import com.example.metro_info_front_end.DataModel.MetroSystem;
import java.util.List;

public class MetroSystemResponse {
    private String status; // 响应状态，例如 "success" 或 "error"
    private List<MetroSystem> data; // 包含地铁系统信息的列表
    private String errorMessage; // 错误消息，仅在状态为 "error" 时使用

    public String getStatus() {
        return status;
    }

    public List<MetroSystem> getData() {
        return data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setData(List<MetroSystem> data) {
        this.data = data;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
