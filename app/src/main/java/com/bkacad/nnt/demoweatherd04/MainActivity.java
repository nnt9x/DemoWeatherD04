package com.bkacad.nnt.demoweatherd04;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;


import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "";
    private EditText edtCity;
    private ImageView imgIcon;
    private TextView tvTemp, tvMainWeather;

    private String buildUrlOpenWeather(String city) {
        city = city.toLowerCase();
        return String.format(
                "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric&lang=vi"
                , city, API_KEY);
    }

    private String buildUrlIcon(String icon) {
        return String.format(
                "https://openweathermap.org/img/wn/%s@4x.png"
                , icon);
    }

    private void initView() {
        edtCity = findViewById(R.id.edtCity);
        imgIcon = findViewById(R.id.imgIcon);
        tvMainWeather = findViewById(R.id.tvMainWeather);
        tvTemp = findViewById(R.id.tvTemp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void searchCityWeather(View view) {
        // B1:  Lấy dữ liệu từ Edittext
        String city = edtCity.getText().toString();
        if (city.isEmpty()) {
            edtCity.setError("Không để trống");
            return;
        }
        // B2: Chuẩn bị câu lệnh
        String myURL = buildUrlOpenWeather(city);
        Log.d("myURL", myURL);

        // B3: Gửi request -> OpenWeather
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, myURL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Thành công
                        // B4: Kết quả -> xử lý kết quả và hiển thị
                        try {
                            Log.d("myData", response.toString());
                            // Thời tiết chính
                            String mainWeather =
                                    response.getJSONArray("weather")
                                            .getJSONObject(0)
                                            .getString("description");
                            tvMainWeather.setText(mainWeather);

                            // Nhiệt độ
                            String temp = response.getJSONObject("main")
                                    .getString("temp");
                            tvTemp.setText(temp);

                            // icon
                            String icon = response.getJSONArray("weather")
                                    .getJSONObject(0)
                                    .getString("icon");


                            // Tạo ra đường dẫn đó
                            String iconURL = buildUrlIcon(icon);
                            Log.d("myData",iconURL);
                            Glide.with(MainActivity.this)
                                    .load(iconURL)
                                    .into(imgIcon);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Khi request lỗi
                        Toast.makeText(MainActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                        edtCity.setText("");
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);


    }
}