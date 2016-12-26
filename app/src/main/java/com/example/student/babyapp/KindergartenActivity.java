package com.example.student.babyapp;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;


public class KindergartenActivity extends AppCompatActivity {
    String TAG;

    Context context;

    //JSON 관련
    URL url;                             //JSON 요청 주소
    String json;                        //json String형
    JSONObject object;             //json JsonObject형
    JSONArray array;                  //json ArrayList형

    ArrayList<Kindergarten> list_kindergarten;

    ListView listView_kindergarten;
    ListAdapter listAdapter;

    LinearLayout view_kindergarten;
    View view_detail;
    PopupWindow popupWindow_detail;

    TextView name_Kindergarten;
    TextView address_kindergarten;
    TextView total_kids;
    TextView num_teacher;
    TextView tel_kindergarten;
    TextView num_CCTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kindergarten);

        init();

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                listView_kindergarten.setAdapter(listAdapter);

            }
        };

        new Thread() {
            String encoded_json = null;

            public void run() {
                json = getJson();
                parseJson(json);

                handler.sendEmptyMessage(1);
                Log.d(TAG, "list_kindergarten 길이 :" + Integer.toString(list_kindergarten.size()));
            }

        }.start();

    }

    public void init() {
        TAG = this.getClass().getName();
        context = this;

        list_kindergarten = new ArrayList<Kindergarten>();
        listView_kindergarten = (ListView) findViewById(R.id.listView_kindergarten);

        popupWindow_detail = new PopupWindow(context);

        view_kindergarten = (LinearLayout) this.findViewById(R.id.activity_kindergarten);
        view_detail = getLayoutInflater().inflate(R.layout.popup_detail, null);

        name_Kindergarten = (TextView) view_detail.findViewById(R.id.name_kindergarten);
        address_kindergarten = (TextView) view_detail.findViewById(R.id.address_kindergarten);
        total_kids = (TextView) view_detail.findViewById(R.id.total_kids);
        num_teacher = (TextView)  view_detail.findViewById(R.id.num_teacher);
        tel_kindergarten = (TextView) view_detail.findViewById(R.id.tel_kindergarten);
        num_CCTV = (TextView) view_detail.findViewById(R.id.num_CCTV);

        listView_kindergarten.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(KindergartenActivity.this, list_kindergarten.get(i).getName_kindergarten(), Toast.LENGTH_SHORT).show();

                Kindergarten kindergarten = list_kindergarten.get(i);

                popupWindow_detail.setContentView(view_detail);
                popupWindow_detail.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow_detail.setTouchable(true);
                popupWindow_detail.setFocusable(true);
                //popupWindow_detail.setOutsideTouchable(true);
                popupWindow_detail.showAtLocation(view_kindergarten, Gravity.CENTER, 0, 0);

                name_Kindergarten.setText(kindergarten.getName_kindergarten());
                address_kindergarten.setText("주소  "+kindergarten.getAddress_kindergarten());
                total_kids.setText("정원수  "+kindergarten.getTotal_kids());
                num_teacher.setText("보육교직원수  "+kindergarten.getNum_teacher());
                tel_kindergarten.setText("전화번호  "+kindergarten.getTel_kindergarten());
                num_CCTV.setText("CCTV 설치수  "+kindergarten.getNum_CCTV());

            }
        });

        listAdapter = new ListAdapter(this, list_kindergarten);

    }

    public String getJson() {
        String key = "zNUxHwqZV0QfCgDkhHtNKqyPfsEEHmNfp0%2FO0zFHfmg1sujkxk%2FJVxf4qml60BaH219L795Fhlwx7vuiGAFahg%3D%3D";
        String api_address = "http://api.data.go.kr/openapi/0c9e6948-e327-404b-89bf-2506d4684c1c";

        String string_url = api_address + "?serviceKey=" + key + "&s_page=1" + "&s_list=10000" + "&type=json" + "&numOfRows=1" + "&pageNo=1";

        try {
            Log.d(TAG, "JSON 요청 주소 " + string_url);
            url = new URL(string_url);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("GET");

            int responseCode = urlConn.getResponseCode();
            Log.d(TAG, Integer.toString(responseCode));
            if (responseCode != HttpURLConnection.HTTP_OK) return null;

            BufferedReader buffR = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "euc-kr"));
            String input = null;
            StringBuffer sb = new StringBuffer();

            while ((input = buffR.readLine()) != null) {
                sb.append(input);
            }
            return sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void parseJson(String json) {
        try {
            array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                if (array.get(i) == null) {
                    break;
                }
                JSONObject obj_kindergarten = array.getJSONObject(i);
                Kindergarten kindergarten = new Kindergarten();
                kindergarten.setName_kindergarten(obj_kindergarten.getString("어린이집명"));
                kindergarten.setAddress_kindergarten(obj_kindergarten.getString("소재지도로명주소"));
                kindergarten.setLocation_kindergarten(obj_kindergarten.getString("시도명") + " " + obj_kindergarten.getString("시군구명"));
                kindergarten.setTotal_kids(obj_kindergarten.getString("정원수"));
                kindergarten.setNum_teacher(obj_kindergarten.getString("보육교직원수"));
                kindergarten.setTel_kindergarten(obj_kindergarten.getString("어린이집전화번호"));
                kindergarten.setNum_CCTV(obj_kindergarten.getString("CCTV설치수"));
                list_kindergarten.add(kindergarten);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
