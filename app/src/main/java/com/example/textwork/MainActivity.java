package com.example.textwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private LinearLayout linearLayout;
    ArrayList<String> userNames = new ArrayList<>();
    ArrayList<String> descriptions = new ArrayList<>();
    ArrayList<String> firstNames = new ArrayList<>();
    ArrayList<String> lastNames = new ArrayList<>();
    ArrayList<String> countries = new ArrayList<>();
    ArrayList<Integer> userIds = new ArrayList<>();
    static Integer buttonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonId = 0;
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        get_json();
        for(int i = 0; i < userNames.size(); i++){
            Button button = new Button(getApplicationContext());
            button.setText(userNames.get(i));
            button.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            button.setId(userIds.get(i));
            button.setBackgroundColor(0xFFFFFFFF);
            Drawable clientIcon = button.getContext().getResources().getDrawable(R.mipmap.ic_contact);
            button.setCompoundDrawablesWithIntrinsicBounds(clientIcon,null,null,null);
            button.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT)
            );
            String firstName = firstNames.get(i);
            String lastName = lastNames.get(i);
            String country = countries.get(i);
            String description = descriptions.get(i);
            String userName = userNames.get(i);
            Integer id = userIds.get(i);

            button.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, InfoAboutClientActivity.class);
                            intent.putExtra("id",id);
                            intent.putExtra("first_name", firstName);
                            intent.putExtra("last_name", lastName);
                            intent.putExtra("country", country);
                            intent.putExtra("description", description);
                            intent.putExtra("user_name", userName);
                            startActivity(intent);
                        }
                    }
            );
            linearLayout.addView(button);
        }
    }

    public void get_json(){
        String json;
        try {
            InputStream fileJson;
            if(new File(getFilesDir()+"/clients.json").exists()){
                fileJson = openFileInput("clients.json");
            }else {
                fileJson = getAssets().open("clients.json");
            }
            int fileSize = fileJson.available();
            byte[] buffer = new byte[fileSize];
            fileJson.read(buffer);
            fileJson.close();
            json = new String(buffer,"UTF-8");
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("clients");
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject userInfo = jsonArray.getJSONObject(i);
                userIds.add(i,userInfo.getInt("id"));
                userNames.add(i,userInfo.getString("username"));
                firstNames.add(i,userInfo.getString("first_name"));
                lastNames.add(i,userInfo.getString("last_name"));
                countries.add(i,userInfo.getString("country"));
                descriptions.add(i,userInfo.getString("description"));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        if(buttonId != 0){
            Button btn = (Button) findViewById(buttonId);
            btn.setVisibility(View.GONE);
            buttonId = 0;
        }
        super.onResume();
    }

}