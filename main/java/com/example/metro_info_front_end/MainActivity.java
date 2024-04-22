package com.example.metro_info_front_end;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.metro_info_front_end.DataModel.MetroStation;
import com.example.metro_info_front_end.DataModel.MetroSystem;
import com.example.metro_info_front_end.api.ApiService;
import com.example.metro_info_front_end.api.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {



    private LinearLayout llOptimalRoute; // 最优路线查询界面布局
    private LinearLayout llMetroLine; // 地铁线路查询界面布局

    private AutoCompleteTextView systemName1;
    private AutoCompleteTextView systemName2;
    private AutoCompleteTextView lineName;
    private AutoCompleteTextView departureStation;
    private AutoCompleteTextView destinationStation;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 获取布局中的线性布局
        llOptimalRoute = findViewById(R.id.LL1);
        llMetroLine = findViewById(R.id.LL2);

        // 获取按钮
        Button btnOptimalRoute = findViewById(R.id.btn1);
        Button btnMetroLine = findViewById(R.id.btn2);

        // 设置按钮点击事件监听器
        btnOptimalRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示最优路线查询界面，隐藏地铁线路查询界面
                llOptimalRoute.setVisibility(View.VISIBLE);
                llMetroLine.setVisibility(View.GONE);
            }
        });

        btnMetroLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示地铁线路查询界面，隐藏最优路线查询界面
                llOptimalRoute.setVisibility(View.GONE);
                llMetroLine.setVisibility(View.VISIBLE);
            }
        });

        // 获取 AutoCompleteTextView 控件
        systemName1 = findViewById(R.id.SystemName1);
        systemName2 = findViewById(R.id.SystemName2);
        lineName = findViewById(R.id.LineName);
        departureStation = findViewById(R.id.DepartureStation);
        destinationStation = findViewById(R.id.DestinationStation);



        // 设置自动完成功能适配器
        //ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getCityData());
        ArrayAdapter<String> lineAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getLineData());
        ArrayAdapter<String> stationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getStationData());

        //systemName1.setAdapter(cityAdapter);
        //systemName2.setAdapter(cityAdapter);
        lineName.setAdapter(lineAdapter);
        departureStation.setAdapter(stationAdapter);
        destinationStation.setAdapter(stationAdapter);

        // 创建 ApiService 实例
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // 设置查询按钮点击事件监听器
        Button query1 = findViewById(R.id.query1);
        Button query2 = findViewById(R.id.query2);

        //模拟获取城市数据
        fetchCityData();


        query1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取用户输入的城市、出发站点和目标站点信息
                String city1 = systemName1.getText().toString();
                String departure = departureStation.getText().toString();
                String destination = destinationStation.getText().toString();

                Log.d("MainActivity", "发起最优路线查询：City1 = " + city1 + ", Departure = " + departure + ", Destination = " + destination);

                // 发起网络请求获取系统编码
                Call<String> systemCodeCall = apiService.findSystemCodeBySystemName(city1);
                systemCodeCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String systemCode = response.body();
                            Log.d("MainActivity", "获取系统编码成功: " + systemCode);

                            // 使用获取的系统编码发起最优路线查询
                            Call<List<MetroStation>> callShortestPath = apiService.searchShortestPath(systemCode, departure, destination);
                            callShortestPath.enqueue(new Callback<List<MetroStation>>() {
                                @Override
                                public void onResponse(Call<List<MetroStation>> call, Response<List<MetroStation>> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        Log.d("MainActivity", "最优路线查询成功");
                                        handleResponse(response);
                                    } else {
                                        Log.e("MainActivity", "最优路线查询失败");
                                        showToast("查询失败");
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<MetroStation>> call, Throwable t) {
                                    Log.e("MainActivity", "网络请求失败", t);
                                    showToast("网络请求失败");
                                }
                            });

                        } else {
                            Log.e("MainActivity", "获取系统编码失败");
                            showToast("获取系统编码失败");
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("MainActivity", "获取系统编码网络请求失败", t);
                        showToast("获取系统编码失败");
                    }
                });
            }
        });

        query2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取用户输入的城市和线路名称信息
                String city2 = systemName2.getText().toString();
                String lineName1 = lineName.getText().toString();

                Log.d("MainActivity", "发起地铁线路查询：City2 = " + city2 + ", LineName = " + lineName1);

                // 发起网络请求获取查询结果
                Call<List<MetroStation>> call = apiService.getStationsBySystemAndLine(city2, lineName1);
                call.enqueue(new Callback<List<MetroStation>>() {
                    @Override
                    public void onResponse(Call<List<MetroStation>> call, Response<List<MetroStation>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Log.d("MainActivity", "地铁线路查询成功");
                            handleResponse(response);
                        } else {
                            Log.e("MainActivity", "地铁线路查询失败");
                            showToast("查询失败");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<MetroStation>> call, Throwable t) {
                        Log.e("MainActivity", "网络请求失败", t);
                        showToast("网络请求失败");
                    }
                });
            }
        });






    }

    private void handleResponse(Response<List<MetroStation>> response) {
        if (response.isSuccessful() && response.body() != null) {
            List<MetroStation> stations = response.body();
            // 处理查询结果，更新界面或显示信息
            StringBuilder resultBuilder = new StringBuilder();
            for (MetroStation station : stations) {
                resultBuilder.append(station.getStationName()).append("\n");
            }
            // 显示查询结果
            showResult(resultBuilder.toString());
        } else {
            // 提示查询失败
            showToast("查询失败");
        }
    }

    private void showResult(String result) {
        // 在主线程（UI 线程）中更新 UI
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 获取显示结果的 TextView 控件
                TextView resultTextView = findViewById(R.id.resultTextView);
                // 将查询结果设置到 TextView 中
                resultTextView.setText(result);

                // 滚动到顶部
                resultTextView.scrollTo(0, 0);
            }
        });
    }


    private void showToast(String message) {
        // 显示 Toast 提示消息
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }


    // 模拟城市数据
    private void fetchCityData() {
        // 调用后端接口获取所有地铁系统信息（城市数据）
        Call<List<MetroSystem>> call = apiService.getAllMetroSystems();
        call.enqueue(new Callback<List<MetroSystem>>() {
            @Override
            public void onResponse(Call<List<MetroSystem>> call, Response<List<MetroSystem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MetroSystem> metroSystems = response.body();

                    // 创建存储城市名称的列表
                    List<String> cityNames = new ArrayList<>();

                    // 提取每个地铁系统的城市名称并添加到列表中
                    for (MetroSystem system : metroSystems) {
                        String cityName = system.getSystemName();
                        cityNames.add(cityName);
                    }

                    // 将城市名称列表转换为字符串数组
                    String[] cityArray = cityNames.toArray(new String[0]);

                    // 更新 AutoCompleteTextView 的适配器
                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, cityArray);
                    systemName1.setAdapter(cityAdapter);
                    systemName2.setAdapter(cityAdapter);

                    // 可以根据需要设置其他 AutoCompleteTextView 的适配器，比如线路和站点

                } else {
                    showToast("获取城市数据失败");
                }
            }

            @Override
            public void onFailure(Call<List<MetroSystem>> call, Throwable t) {
                Log.e("MainActivity", "获取城市数据失败", t);
                showToast("网络请求失败");
            }
        });
    }

    private String[] getLineData() {return new String[]{"1号线","2号线","3号线"};}

    private String[] getStationData() {return new String[]{"新百广场", "北国商城"};}
}
