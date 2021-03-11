package com.example.textwork;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class InfoAboutClientActivity extends AppCompatActivity {
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_about_client);
        ActionBar actionBar = getSupportActionBar();
        linearLayout = (LinearLayout) findViewById(R.id.info);
        TextView first_name_text = (TextView)findViewById(R.id.first_name);
        TextView last_name_text = (TextView)findViewById(R.id.last_name);
        TextView country_text = (TextView)findViewById(R.id.country);
        TextView description_text = (TextView)findViewById(R.id.description);
        String first_name = getIntent().getStringExtra("first_name");
        String last_name = getIntent().getStringExtra("last_name");
        String country = getIntent().getStringExtra("country");
        String description = getIntent().getStringExtra("description");
        first_name_text.setText(first_name);
        last_name_text.setText(last_name);
        description_text.setText(Html.fromHtml(description));
        description_text.setMovementMethod(LinkMovementMethod.getInstance());
        country_text.setText(country);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.toolbar_title);
        TextView title = (TextView) actionBar.getCustomView().findViewById(R.id.tvTitle);
        title.setText(getIntent().getStringExtra("user_name"));
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_remove:
                Integer id = getIntent().getIntExtra("id",-1);
                jsonDelete(id);
                Toast toast = Toast.makeText(getApplicationContext(),
                        "User has been deleted", Toast.LENGTH_SHORT);
                toast.show();
                MainActivity.buttonId = id;
                this.finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    private void jsonDelete(int id){
        String json;
        try {
            InputStream fileJson ;
            if(new File(getFilesDir()+"/clients.json").exists()){
                fileJson = openFileInput("clients.json");
            }else {
                fileJson = getAssets().open("clients.json");

            }
            int size = fileJson.available();
            byte[] buffer = new byte[size];
            fileJson.read(buffer);
            fileJson.close();
            json = new String(buffer,"UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("clients");
            JSONArray copyJsonArray = new JSONArray();
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject userInfo = jsonArray.getJSONObject(i);
                if(userInfo.getInt("id") != id) {
                    copyJsonArray.put(userInfo);
                }
            }

            JSONObject newJsonObject = new JSONObject();
            newJsonObject.put("clients",copyJsonArray);
            FileOutputStream file = openFileOutput("clients.json",MODE_PRIVATE);
            file.write(newJsonObject.toString().getBytes());
            file.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

}