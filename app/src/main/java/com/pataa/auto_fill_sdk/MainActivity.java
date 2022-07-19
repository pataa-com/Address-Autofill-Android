package com.pataa.auto_fill_sdk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.pataa.sdk.GetPataaDetailResponse;
import com.pataa.sdk.OnAddress;
import com.pataa.sdk.Pataa;
import com.pataa.sdk.PataaAutoFillView;
import com.pataa.sdk.User;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((PataaAutoFillView) findViewById(R.id.vPataaCodeView))
                .setCurrentActivity(this)//to get the result of create pataa
                .setAddressCallBack(new OnAddress() {//to get the click events
                    @Override
                    public void onNetworkIsNotAvailable() {
                        Toast.makeText(MainActivity.this, "onNetworkIsNotAvailable", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPataaNotFound(String message) {

                    }


                    @Override
                    public void onPataaFound(User user, Pataa response) {
                        //  Toast.makeText(MainActivity.this, user.getFirst_name() + " : " + response.getFormattedAddress(), Toast.LENGTH_SHORT).show();
                        StringBuilder builder = new StringBuilder();
                        builder.append("F Name: " + user.getFirst_name() + "\n");
                        builder.append("L Name: " + user.getLast_name() + "\n");
                        builder.append("Mobile: " + user.getMobile() + "\n");
                        builder.append("Email: " + user.getEmail_id() + "\n");
                        builder.append("CountryCode: " + user.getCountry_code() + "\n");
                        builder.append("Address: " + response.getFormattedAddress() + "\n");
                        ((TextView) findViewById(R.id.result)).setText(builder.toString());

                    }

                    @Override
                    public void onError(int statusCode, String message) {
                        Toast.makeText(MainActivity.this, statusCode + " : " + message, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}