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

    public static Integer buttonId;

    private LinearLayout mLinearLayout;
    private ArrayList<String> mUserNames = new ArrayList<>();
    private ArrayList<String> mDescriptions = new ArrayList<>();
    private ArrayList<String> mFirstNames = new ArrayList<>();
    private ArrayList<String> mLastNames = new ArrayList<>();
    private ArrayList<String> mCountries = new ArrayList<>();
    private ArrayList<Integer> mUserIds = new ArrayList<>();
    private Drawable clientIcon;
    private Button mButtonUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonId = 0;
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mLinearLayout = (LinearLayout) findViewById(R.id.activity_main_linear_layout);
        getJson();
        for (int i = 0; i < mUserNames.size(); i++){
            mButtonUser = new Button(getApplicationContext());
            mButtonUser.setText(mUserNames.get(i));
            mButtonUser.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            mButtonUser.setId(mUserIds.get(i));
            mButtonUser.setBackgroundColor(0xFFFFFFFF);
            clientIcon = mButtonUser.getContext().getResources().getDrawable(R.mipmap.ic_contact);
            mButtonUser.setCompoundDrawablesWithIntrinsicBounds(clientIcon,null,null,null);
            mButtonUser.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT)
            );
            Integer id = mUserIds.get(i);
            String userName = mUserNames.get(i);
            String firstName = mFirstNames.get(i);
            String lastName = mLastNames.get(i);
            String country = mCountries.get(i);
            String description = mDescriptions.get(i);

            mButtonUser.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, InfoAboutClientActivity.class);
                            intent.putExtra("id",id);
                            intent.putExtra("userName", userName);
                            intent.putExtra("firstName", firstName);
                            intent.putExtra("lastName", lastName);
                            intent.putExtra("country", country);
                            intent.putExtra("description", description);
                            startActivity(intent);
                        }
                    }
            );
            mLinearLayout.addView(mButtonUser);
        }
    }

    @Override
    protected void onResume() {
        if (buttonId != 0){
            Button buttonForHiding = (Button) findViewById(buttonId);
            buttonForHiding.setVisibility(View.GONE);
            buttonId = 0;
        }
        super.onResume();
    }

    public void getJson(){
        String json;
        try {
            InputStream fileJson;
            if (new File(getFilesDir()+"/clients.json").exists())  fileJson = openFileInput("clients.json");
            else fileJson = getAssets().open("clients.json");
            int fileSize = fileJson.available();
            byte[] buffer = new byte[fileSize];
            fileJson.read(buffer);
            fileJson.close();
            json = new String(buffer,"UTF-8");
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("clients");
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject userInfo = jsonArray.getJSONObject(i);
                mUserIds.add(i,userInfo.getInt("id"));
                mUserNames.add(i,userInfo.getString("username"));
                mFirstNames.add(i,userInfo.getString("first_name"));
                mLastNames.add(i,userInfo.getString("last_name"));
                mCountries.add(i,userInfo.getString("country"));
                mDescriptions.add(i,userInfo.getString("description"));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }


}