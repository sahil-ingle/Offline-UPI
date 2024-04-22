package com.example.offlineupi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import com.example.offlineupi.databinding.ActivityOfflineRegisterBinding;

public class offlineRegister extends AppCompatActivity {

    ActivityOfflineRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOfflineRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.continueBtn.setOnClickListener(view1 -> showLogoutConfirmationDialog());

        binding.setUpBtn.setOnClickListener(view12 -> {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + Uri.encode("*99#")));
            startActivity(intent);
        });

    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Continue");
        builder.setMessage("Did you complete the setup?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // User clicked Yes, logout
            startActivity(new Intent(offlineRegister.this, SetPinActivity.class));
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            // User clicked No, do nothing
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}