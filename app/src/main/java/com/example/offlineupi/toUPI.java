package com.example.offlineupi;// ... (existing imports)

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.offlineupi.databinding.ActivityToUpiBinding;

import android.Manifest;


public class toUPI extends AppCompatActivity {

    ActivityToUpiBinding binding;
    private static final int REQUEST_PHONE_CALL = 1;
    private static final int REQUEST_CLIPBOARD_PERMISSION = 2;
    private static final int REQUEST_ACCESSIBILITY_PERMISSION = 3;
    private static final String UID = "*99";
    private static String sendMoney = "*1";
    private static String toUPI = "*3#";

    private ClipboardManager clipboardManager;
    private String upiID;

    private WindowManager windowManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityToUpiBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
    }

    public void onPayButtonClick(View view) {
        String upiID = binding.upiID.getText().toString().trim();
        String dialString = UID + sendMoney + toUPI ;
        if (!upiID.isEmpty()) {
            // Check for CALL_PHONE permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                // If permission is granted, make the call
                dialPhoneNumber(dialString);
            } else {
                // Request permission to make the call
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
            }
        } else {
            if (upiID.isEmpty()) {
                Toast.makeText(this, "Please enter a UPI ID.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PHONE_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, make the call
                onPayButtonClick(null);
            } else {
                Toast.makeText(this, "Permission denied. Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dialPhoneNumber(String dial) {
        String upiID = binding.upiID.getText().toString().trim();
        copyToClipboard(upiID);
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + Uri.encode(dial)));
        startActivity(intent);
    }

    private void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
    }



}
