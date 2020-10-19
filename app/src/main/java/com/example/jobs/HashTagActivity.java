package com.example.jobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HashTagActivity extends AppCompatActivity {

    EditText hashText;
    ListView usersList;
    String text;
    int length,index,countCharacters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hash_tag);

        hashText = findViewById(R.id.hashText);
        usersList = findViewById(R.id.usersList);
        HashText();
    }

    public void HashText() {

        hashText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    text = hashText.getText().toString();
                    length = text.length();

                    if (text.contains("#")) {
                        index = text.indexOf('#');
                        countCharacters = 0;
                        for (int i = (index + 1); ; i++) {
                            if ((text.charAt(i)) != ' ') {
                                countCharacters = countCharacters + 1;
                            }
                        }
                    }
                    SpannableString spannableString = new SpannableString(text);
                    ForegroundColorSpan foregroundColorSpanBlue = new ForegroundColorSpan(Color.BLUE);
                    spannableString.setSpan(foregroundColorSpanBlue , index , countCharacters , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    hashText.setText(spannableString);
                }
            }
        });
    }
        /*this.hashText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                text =  editable.toString();
                Pattern pattern =   Pattern.compile("[#][a-zA-Z0-9-.]");
                Matcher m = pattern.matcher(text);
                int cursorPosition =hashText.getSelectionStart();
                while (m.find())  {
                    if (cursorPosition >= m.start()   &&  cursorPosition <= m.end())    {
                        final int s =  m.start() + 1;
                        final int e =  m.end();
                        ClickableSpan clickableSpan = new ClickableSpan() {
                            @Override
                            public void onClick(@NonNull View widget) {
                               *//* Intent intent = new Intent(HashTagActivity.this , Demo.class);
                                startActivity(intent);*//*
                            }
                        };
                        SpannableString spannableString = new SpannableString(text);
                        ForegroundColorSpan foregroundColorSpanBlue = new ForegroundColorSpan(Color.BLUE);
                        spannableString.setSpan(foregroundColorSpanBlue , m.start() , m.end() , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        hashText.setText(spannableString);
                    }
                }
            }
        });
    }*/
}