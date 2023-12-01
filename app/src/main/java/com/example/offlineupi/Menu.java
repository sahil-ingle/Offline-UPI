package com.example.offlineupi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.offlineupi.databinding.ActivityMenuBinding;

public class Menu extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    ActivityMenuBinding binding;

    private static final String UID = "*99";
    private String changeAccount = "*4";

    private String language = "*2";
    private String remark = "*1#";

    private String selectedLang = "*1#";

    private Spinner spinner;
    private static final String[] paths = {"English", "हिंदी (Hindi)", "தமிழ் (Tamil)", "മലയാളം (Malayalam)", "ಕನ್ನಡ (Kannada)", "తెలుగు (Telugu)"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Menu.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                selectedLang = "*1#";
                break;
            case 1:
                selectedLang = "*2#";
                break;
            case 2:
                selectedLang = "*3#";
                break;
            case 3:
                selectedLang = "*4#";
                break;
            case 4:
                selectedLang = "*5#";
                break;
            case 5:
                selectedLang = "*6#";
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }


    public void onLangChangeClick(View view){
        String dialString = UID + changeAccount + language + selectedLang;
        dialPhoneNumber(dialString);
    }
    public void onChangeBankAccount(View view){
        String dialString = UID + changeAccount + remark;
        dialPhoneNumber(dialString);
    }

    private void dialPhoneNumber(String dial) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + Uri.encode(dial)));
        startActivity(intent);
    }
}