package com.example.offlineupi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.offlineupi.databinding.ActivityMainBinding;

import java.io.File;
import java.io.IOException;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;





public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if (isDeviceRooted()) {
            finishAffinity();
        }

    }

    //root checker
    public boolean isDeviceRooted() {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3() || isGooglePlayServicesAvailable();
    }

    private boolean checkRootMethod1() {
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    private boolean checkRootMethod2() {
        try {
            Process process = Runtime.getRuntime().exec("su");
            process.getOutputStream().close();
            int exitValue = process.waitFor();
            return exitValue == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkRootMethod3() {
        File file = new File("/system/app/Magisk.apk");
        return file.exists();
    }


    private boolean isGooglePlayServicesAvailable() {
        int availability = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        return availability != ConnectionResult.SUCCESS;
    }


    //root checker

    public void onRegisterClick(View view){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + Uri.encode("*99#")));
        startActivity(intent);
    }
    public void onPhoneClick(View view){
        Intent i = new Intent(MainActivity.this, toPhone.class);
        startActivity(i);
    }

    public void onUPIClick(View view){
        Intent i = new Intent(MainActivity.this, toUPI.class);
        startActivity(i);
    }
    public void onBankClick(View view){
        Intent i = new Intent(MainActivity.this, toBankAccount.class);
        startActivity(i);
    }

}
