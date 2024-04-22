package com.example.metro_info_front_end.api;

import com.example.metro_info_front_end.DataModel.MetroLine;
import com.example.metro_info_front_end.DataModel.MetroStation;
import com.example.metro_info_front_end.DataModel.MetroSystem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // 获取所有地铁系统信息
    @GET("/metro/find/system")
    Call<List<MetroSystem>> getAllMetroSystems();

    // 根据城市和线路名称查询线路上所有站点
    @GET("/metro/{systemName}/line/{lineNumber}/stations")
    Call<List<MetroStation>> getStationsBySystemAndLine(
            @Path("systemName") String systemName,
            @Path("lineNumber") String lineNumber
    );

    // 根据系统 ID 和站点名称模糊查询站点
    @GET("/metro/{sid}/find/station")
    Call<List<MetroStation>> findMetroStation(
            @Path("sid") String sid,
            @Query("stName") String stName
    );

    // 根据系统 ID 和线路编号查询线路信息
    @GET("/metro/{sid}/search/line")
    Call<List<MetroSystem>> searchByLine(
            @Path("sid") String sid,
            @Query("lNum") String lNum
    );


    // 根据系统 ID、起始站点和目标站点查询最优路线
    @GET("/metro/{sid}/search/route")
    Call<List<MetroStation>> searchShortestPath(
            @Path("sid") String sid,
            @Query("from") String from,
            @Query("to") String to
    );


    // 根据地铁系统名称查询线路信息
    @GET("/metro/find/lines/{systemName}")
    Call<List<MetroLine>> getLinesBySystemName(
            @Path("systemName") String systemName
    );

    // 根据系统名称查询对应的系统编码
    @GET("/metro/findSystemCode")
    Call<String> findSystemCodeBySystemName(@Query("systemName") String systemName);

}
