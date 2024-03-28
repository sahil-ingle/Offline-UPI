package com.example.offlineupi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.offlineupi.databinding.ActivityMenuBinding;


import java.util.Locale;

public class Menu extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ActivityMenuBinding binding;
    private SharedPreferences sharedPref;

    public static String lang = "en";
    public static final String LANG_KEY = "selected_language";

    private static final String UID = "*99";
    private String changeAccount = "*4";

    private String language = "*2";
    private String remark = "*1#";
    private String selectedLang = "*1#";

    private boolean isLanguageChange = false;

    private Spinner spinner;
    private static final String[] paths = {"English", "हिंदी (Hindi)", "தமிழ் (Tamil)", "മലയാളം (Malayalam)", "ಕನ್ನಡ (Kannada)", "తెలుగు (Telugu)"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        SharedPreferences preferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String name = preferences.getString("myName", "");
        String phone = preferences.getString("myPhone", "");
        String upiId = preferences.getString("myUPIid", "");


        if (upiId.isEmpty()) {
            // Set default value if UPI ID is empty
            upiId = "Enter UPI Id";
        }

        binding.myName.setText(name);
        binding.myPhone.setText(phone);
        binding.myUPIid.setText(upiId);

        sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Menu.this,
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        // Retrieve last selected language from SharedPreferences
        String lastSelectedLang = sharedPref.getString(LANG_KEY, "en");
        for (int i = 0; i < paths.length; i++) {
            if (paths[i].toLowerCase().contains(lastSelectedLang)) {
                spinner.setSelection(i);
                break;
            }
        }

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this, MainActivity.class);
                startActivity(i);
            }
        });

        binding.logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Menu.this, "Coming soon..", Toast.LENGTH_SHORT).show();
            }
        });

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        switch (position) {
            case 0:
                selectedLang = "*1#";
                lang = "en";
                break;
            case 1:
                selectedLang = "*2#";
                lang = "hi";
                break;
            case 2:
                selectedLang = "*3#";
                lang = "ta";
                break;
            case 3:
                selectedLang = "*4#";
                lang = "ml";
                break;
            case 4:
                selectedLang = "*5#";
                lang = "kn";
                break;
            case 5:
                selectedLang = "*6#";
                lang = "te";
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    public void onLangChangeClick(View view) {
        String dialString = UID + changeAccount + language + selectedLang;
        dialPhoneNumber(dialString);
        setLocale(lang);
        // Save selected language to SharedPreferences
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(LANG_KEY, lang);
        editor.apply();
        isLanguageChange = true;
    }

    public static String getMyString() {
        return lang;
    }

    @Override
    public void onBackPressed() {
        if (isLanguageChange){
            relaunchApp();
        }else {
            Intent i = new Intent(Menu.this, MainActivity.class);
            startActivity(i);
        }
    }


    public void onQRGenerateClick(View view){
        Intent i = new Intent(Menu.this, QRGenerateActivity.class);
        startActivity(i);
    }

    public void onChangeBankAccount(View view) {
        String dialString = UID + changeAccount + remark;
        dialPhoneNumber(dialString);
    }


    private void dialPhoneNumber(String dial) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + Uri.encode(dial)));
        startActivity(intent);
    }

    public void onEditInfoClick(View view){
        Intent i = new Intent(Menu.this, RegisterPage.class);
        startActivity(i);
    }

    public void onResetPinClick(View view){
        Intent i = new Intent(Menu.this, useOfSecurityQuestion.class);
        startActivity(i);
    }

    public void setLocale(String Lang) {
        Locale locale = new Locale(Lang);
        Locale.setDefault(locale);

        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        recreate();
    }

    public void relaunchApp() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


}
