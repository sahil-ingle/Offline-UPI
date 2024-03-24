package com.example.offlineupi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.offlineupi.EncryptionHelper;
import com.example.offlineupi.MainActivity;

public class EnterPinActivity extends AppCompatActivity {

    private EditText pinEditText1, pinEditText2, pinEditText3, pinEditText4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pin);

        pinEditText1 = findViewById(R.id.customEditText1);
        pinEditText2 = findViewById(R.id.customEditText2);
        pinEditText3 = findViewById(R.id.customEditText3);
        pinEditText4 = findViewById(R.id.customEditText4);

        setEditTextFocusChangeListener(pinEditText1, null, pinEditText2);
        setEditTextFocusChangeListener(pinEditText2, pinEditText1, pinEditText3);
        setEditTextFocusChangeListener(pinEditText3, pinEditText2, pinEditText4);
        setEditTextFocusChangeListener(pinEditText4, pinEditText3, null);

        setBackKeyListener(pinEditText2, pinEditText1);
        setBackKeyListener(pinEditText3, pinEditText2);
        setBackKeyListener(pinEditText4, pinEditText3);


        Button forgetPinButton = findViewById(R.id.forgetPinButton);
        forgetPinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open Set Pin Activity or perform desired action
                startActivity(new Intent(EnterPinActivity.this, useOfSecurityQuestion.class));
            }
        });

        validatePin();



    }

    private void setEditTextFocusChangeListener(final EditText currentEditText, final EditText previousEditText, final EditText nextEditText) {
        currentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1 && nextEditText != null) {
                    nextEditText.requestFocus();
                } else if (s.length() == 0 && previousEditText != null) {
                    if (start == 0) { // Check if deletion is happening at the beginning
                        previousEditText.requestFocus();
                        previousEditText.setSelection(previousEditText.getText().length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setBackKeyListener(final EditText currentEditText, final EditText previousEditText) {
        currentEditText.setOnKeyListener((view, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN && TextUtils.isEmpty(currentEditText.getText())) {
                previousEditText.requestFocus();
                return true;
            }
            return false;
        });
    }

    private void validatePin() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String encryptedPin = preferences.getString("PIN", "");

        findViewById(R.id.validateButton).setOnClickListener(view -> {
            String enteredPin = pinEditText1.getText().toString() +
                    pinEditText2.getText().toString() +
                    pinEditText3.getText().toString() +
                    pinEditText4.getText().toString();

            if (enteredPin.length() != 4) {
                Toast.makeText(EnterPinActivity.this, "Please enter a 4-digit PIN", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Log.d("EnterPinActivity", "Encrypted PIN: " + encryptedPin);
                String storedPin = EncryptionHelper.decrypt(encryptedPin);
                Log.d("EnterPinActivity", "Decrypted PIN: " + storedPin);
                if (enteredPin.equals(storedPin)) {
                    startActivity(new Intent(EnterPinActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(EnterPinActivity.this, "Incorrect PIN", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("EnterPinActivity", "Error decrypting PIN: " + e.getMessage());
                Toast.makeText(EnterPinActivity.this, "Error decrypting PIN", Toast.LENGTH_SHORT).show();
            }
        });
    }
}