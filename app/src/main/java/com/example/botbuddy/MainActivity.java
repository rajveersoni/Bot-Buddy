package com.example.botbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText editText;
    private final String bot_key = "bot";
    private final String user_key = "user";
    private ArrayList<ChatsModel> chatsModelArrayList;
    private ChatRVAdapter chatRVAdapter;
    private FloatingActionButton sendButton;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.idRVChats);
        editText = findViewById(R.id.etMessage);
        sendButton = findViewById(R.id.send);
        textView = findViewById(R.id.appear);
        chatsModelArrayList = new ArrayList<>();
        chatRVAdapter = new ChatRVAdapter(chatsModelArrayList,this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(chatRVAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setVisibility(View.GONE);
                if (editText.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter your message", Toast.LENGTH_SHORT).show();
                    return;
                }
                getResponse(editText.getText().toString());
                editText.setText("");
            }
        });

    }
    private void getResponse(String message){
        chatsModelArrayList.add(new ChatsModel(message,user_key));
        chatRVAdapter.notifyDataSetChanged();
        String url = "http://api.brainshop.ai/get?bid=177910&key=KfFMM7CjvjNCHFj7&uid=[uid]&msg=" + message;
        String BASE_URL = "http://api.brainshop.ai/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<MsgModel> call = retrofitAPI.getMessage(url);
        call.enqueue(new Callback<MsgModel>() {
            @Override
            public void onResponse(Call<MsgModel> call, Response<MsgModel> response) {
                if (response.isSuccessful()){
                    MsgModel model = response.body();
                    assert model != null;
                    chatsModelArrayList.add(new ChatsModel(model.getCnt(),bot_key));
                    chatRVAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MsgModel> call, Throwable t) {
                chatsModelArrayList.add(new ChatsModel("Please revert your question",bot_key));
                chatRVAdapter.notifyDataSetChanged();
            }
        });
    }
}