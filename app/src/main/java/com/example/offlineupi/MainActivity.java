package com.example.offlineupi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import android.provider.Settings;
import android.telephony.SubscriptionManager;





public class MainActivity extends AppCompatActivity {
    private static final String UID = "*99";
    private static final String Balance = "*3#";
    private static String sendMoney = "*1";
    private static String toBankAccount = "*5#";
    ActivityMainBinding binding;
    private static final int REQUEST_PHONE_CALL = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if (isDeviceRooted()) {
            finishAffinity();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PHONE_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                Toast.makeText(this, "Permission denied. Phone functionality may not work.", Toast.LENGTH_SHORT).show();
            }
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


    //root checker42

    public void onMenuClick(View view){
        Intent intent = new Intent(MainActivity.this, Menu.class);
        startActivity(intent);
    }

    public void onChangeSimClick(View view){
        Toast.makeText(this,"Don't Press this button Stupid !!! ",Toast.LENGTH_SHORT).show();
    }

    public void onRegisterClick(View view) {

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
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + Uri.encode(UID + sendMoney + toBankAccount)));
        startActivity(intent);
    }

    public void onCheckBalanceClick(View view){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + Uri.encode(UID + Balance)));
        startActivity(intent);
    }

}
