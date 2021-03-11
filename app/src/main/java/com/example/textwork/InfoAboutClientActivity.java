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

    private LinearLayout mLinearLayout;
    private TextView mTextViewFirstName;
    private TextView mTextViewLastName;
    private TextView mTextViewCountry;
    private TextView mTextViewDescription;
    private ActionBar mActionBar;
    private MenuInflater mInflater;
    private String mJsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_about_client);
        mActionBar = getSupportActionBar();
        mLinearLayout = (LinearLayout)findViewById(R.id.info);
        mTextViewFirstName = (TextView)findViewById(R.id.text_view_first_name);
        mTextViewLastName = (TextView)findViewById(R.id.text_view_last_name);
        mTextViewCountry = (TextView)findViewById(R.id.text_view_country);
        mTextViewDescription = (TextView)findViewById(R.id.text_view_description);
        String firstName = getIntent().getStringExtra("firstName");
        String lastName = getIntent().getStringExtra("lastName");
        String country = getIntent().getStringExtra("country");
        String description = getIntent().getStringExtra("description");
        mTextViewFirstName.setText(firstName);
        mTextViewLastName.setText(lastName);
        mTextViewCountry.setText(country);
        mTextViewDescription.setText(Html.fromHtml(description));
        mTextViewDescription.setMovementMethod(LinkMovementMethod.getInstance());
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setCustomView(R.layout.menu_toolbar_title);
        TextView title = (TextView) mActionBar.getCustomView().findViewById(R.id.text_view_toolbar_title);
        title.setText(getIntent().getStringExtra("userName"));
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        mInflater = getMenuInflater();
        mInflater.inflate(R.menu.activity_info_about_client, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_remove:
                Integer userId = getIntent().getIntExtra("id",-1);
                jsonDelete(userId);
                Toast toast = Toast.makeText(getApplicationContext(),"User has been deleted", Toast.LENGTH_SHORT);
                toast.show();
                MainActivity.buttonId = userId;
                this.finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    private void jsonDelete(int userId){

        try {
            InputStream fileJson ;
            if (new File(getFilesDir()+"/clients.json").exists()) fileJson = openFileInput("clients.json");
            else fileJson = getAssets().open("clients.json");
            int size = fileJson.available();
            byte[] buffer = new byte[size];
            fileJson.read(buffer);
            fileJson.close();
            mJsonString = new String(buffer,"UTF-8");
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("clients");
            JSONArray copyJsonArray = new JSONArray();
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject userInfo = jsonArray.getJSONObject(i);
                if (userInfo.getInt("id") != userId) copyJsonArray.put(userInfo);
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