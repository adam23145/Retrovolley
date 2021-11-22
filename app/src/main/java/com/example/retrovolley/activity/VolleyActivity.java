package com.example.retrovolley.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.retrovolley.R;
import com.example.retrovolley.Retrofit.MethodHTTP;
import com.example.retrovolley.Retrofit.UserResponse;
import com.example.retrovolley.adapter.UserAdapter;
import com.example.retrovolley.model.User;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VolleyActivity extends AppCompatActivity {
    private final String TAG= getClass().getSimpleName();
    private ListView lvUserVolley;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);

        lvUserVolley = findViewById(R.id.lv_user_volley);
        setTitle(getString(R.string.volley));
        getUserFromAPI();
    }

    public void actionClose2(View view) {
        finish();
    }

    public void actionRefresh2(View view) {
        getUserFromAPI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.retrofit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_add:
                Intent intent = new Intent(this, AddUserActivity.class);
                intent.putExtra("typeConnection","volley");
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getUserFromAPI() {
        Gson gson = new Gson();
        String URL = "http://192.168.1.11/halo/User_Registration.php";
        ProgressDialog proDialog = new ProgressDialog(this);
        proDialog.setTitle("Volley");
        proDialog.setMessage("Silahkan Tunggu");
        proDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        proDialog.dismiss();
                        UserResponse userResponse = gson.fromJson(response.toString(), UserResponse.class);
                        if (userResponse.getCode()==200){
                            UserAdapter adapter=new UserAdapter(getApplicationContext(), userResponse.getUser_list());
                            lvUserVolley.setAdapter(adapter);
                            lvUserVolley.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Toast.makeText(getApplicationContext(), userResponse.getUser_list().get(i).getUser_fullname(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }, new Response.ErrorListener(){


            @Override
            public void onErrorResponse(VolleyError error) {
                proDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Volley Error : "+ error.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e(TAG,"Error : "+ error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }
}