package com.example.offlineupi;

import static com.example.offlineupi.getStartedActivity.LANG_KEY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.offlineupi.databinding.ActivityToPhoneBinding;

import java.util.Locale;

public class toPhone extends AppCompatActivity {

    ActivityToPhoneBinding binding;
    private static final int REQUEST_PHONE_CALL = 1;
    private static final String UID = "*99";
    private static final String sendMoney = "*1";
    private static final String toMobile = "*1";

    private static final String reCheck = "*1#";

    public boolean isFirstTime = true;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityToPhoneBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String myValue = sharedPref.getString(LANG_KEY, "en");
        setLocale(myValue);

        SharedPreferences prefs = getSharedPreferences("contact_number", MODE_PRIVATE);
        String contactNumber = prefs.getString("contactNumber", "" );

        if(contactNumber != null){
            String contactNumber2 = contactNumber.replaceAll("[^0-9]", "");

            if (contactNumber2.length() > 10 && contactNumber2.startsWith("91")) {
                contactNumber2 = contactNumber2.substring(2); // Remove "+91"
            }

            binding.phoneNumber.setText(contactNumber2);
            SharedPreferences preferences = getSharedPreferences("contact_number", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(toPhone.this, MainActivity.class);
        startActivity(i);
    }


    public void onContactsClick(View view){
        Intent i = new Intent(this, ContactsActivity.class);
        startActivity(i); // Start ContactsActivity for result
    }

    public void onPayButtonClick(View view) {
        String phoneNumber = binding.phoneNumber.getText().toString().trim();
        String amount = binding.amount.getText().toString().trim();

        if (phoneNumber.isEmpty() || amount.isEmpty()) {
            if (phoneNumber.isEmpty()) {
                Toast.makeText(this, "Please enter a phone number.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please enter the amount", Toast.LENGTH_SHORT).show();
            }
        } else if (phoneNumber.length() != 10) {
            Toast.makeText(this, "Phone number should be 10 digits.", Toast.LENGTH_SHORT).show();
        } else {
            try {
                double amountValue = Double.parseDouble(amount);
                if (amountValue <= 0) {
                    Toast.makeText(this, "Amount should be greater than 0", Toast.LENGTH_SHORT).show();
                }else if (amountValue > 5000){
                    Toast.makeText(this, "Amount should be less than 5000.", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Construct the dial string
                    String dialString = UID + sendMoney + toMobile + "*" + phoneNumber + "*" + amount + reCheck;

                    // Check for CALL_PHONE permission
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        // If permission is granted, make the call
                        dialPhoneNumber(dialString);
                    } else {
                        // Request permission to make the call
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                    }
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid amount format.", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + Uri.encode(dial)));
        startActivity(intent);
    }


    public void setLocale(String Lang) {
        Locale newLocale = new Locale(Lang);
        Locale currentLocale = getResources().getConfiguration().locale;

        if (!newLocale.equals(currentLocale)) {
            Locale.setDefault(newLocale);

            Resources resources = getResources();
            Configuration configuration = resources.getConfiguration();
            configuration.setLocale(newLocale);
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());

            // Restart the activity to apply the language change
            recreate();
        }
    }
}
