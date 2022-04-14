package com.pataa.auto_fill_sdk;

import static com.pataa.sdk.AppConstants.ON_ACT_RSLT_PATAA_DATA;
import static com.pataa.sdk.AppConstants.REQUEST_KEY_CREATE_PATAA;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.pataa.sdk.OnAddress;
import com.pataa.sdk.Pataa;
import com.pataa.sdk.PataaAutoFillView;
import com.pataa.sdk.User;

public class MainActivity extends AppCompatActivity {
    private PataaAutoFillView pataaAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pataaAddress = findViewById(com.pataa.sdk.R.id.vPataaCodeView);
        pataaAddress
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
                        Toast.makeText(MainActivity.this, user.getFirst_name() + " : " + response.getFormattedAddress(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_KEY_CREATE_PATAA && data != null && data.hasExtra(ON_ACT_RSLT_PATAA_DATA)) {
            String pc = data.getStringExtra(ON_ACT_RSLT_PATAA_DATA);
            if (pataaAddress != null) {
                pataaAddress.getPataadetail(pc);
            }
        }
    }
}