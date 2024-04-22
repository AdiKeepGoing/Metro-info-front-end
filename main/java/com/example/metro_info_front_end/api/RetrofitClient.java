package com.example.metro_info_front_end.api;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.logging.HttpLoggingInterceptor;
public class RetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://192.168.169.206:8080/";
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // 创建 OkHttpClient 并添加日志拦截器
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(loggingInterceptor);
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build()) // 使用带有日志拦截器的 OkHttpClient
                    .build();
        }
        return retrofit;
    }
}
