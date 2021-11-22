package com.example.retrovolley.Retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.retrovolley.R;
import com.example.retrovolley.activity.RetrofitActivity;
import com.example.retrovolley.activity.VolleyActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void actionRetrofit(View view){
        Intent retrofit = new Intent(this, RetrofitActivity.class);
        startActivity(retrofit);
    }
    public void actionVolley(View view){
        Intent volley = new Intent(this, VolleyActivity.class);
        startActivity(volley);
    }
}