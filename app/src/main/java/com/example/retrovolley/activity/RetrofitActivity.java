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

import com.example.retrovolley.R;
import com.example.retrovolley.Retrofit.MethodHTTP;
import com.example.retrovolley.adapter.UserAdapter;
import com.example.retrovolley.model.User;
import com.example.retrovolley.Retrofit.UserResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private ListView lvUser;
    private List<User> listUser = new ArrayList<>();
//    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        lvUser = findViewById(R.id.lv_user_volley);

//        pref = getSharedPreferences(GlobalVariable.PREFERENCE_NAME, MODE_PRIVATE);
        getUserFromAPI();
        setTitle(getString(R.string.retrofit));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.retrofit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_add:
                Intent intent = new Intent(this, AddUserActivity.class);
                intent.putExtra("typeConnection", "retrofit");
                startActivity(intent);
                break;
//            case R.id.action_refresh:
//                getUserFromAPI();
//                Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();
//                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void actionRefresh(View view) {
        getUserFromAPI();
    }

    public void actionClose(View view) {
        this.finish();
    }

    private void getUserFromAPI() {
        ProgressDialog proDialog = new ProgressDialog(this);
        proDialog.setTitle("Retrofit");
        proDialog.setMessage("Silakan Tunggu");
        proDialog.show();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://192.168.1.11/halo/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        MethodHTTP client = retrofit.create(MethodHTTP.class);
        Call<UserResponse> call = client.getUser();

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                proDialog.dismiss();
                listUser = response.body().getUser_list();
                UserAdapter userAdapter = new UserAdapter(getApplicationContext(), listUser);
                lvUser.setAdapter(userAdapter);
                lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(getApplicationContext(), listUser.get(position).getUser_fullname(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.d(TAG, t.toString());
            }
        });
    }
}