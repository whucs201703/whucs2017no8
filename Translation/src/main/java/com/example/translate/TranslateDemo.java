package com.example.translate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

import baidu.TransApi;

public class TranslateDemo extends AppCompatActivity {
    private AppCompatEditText input_box;
    private AppCompatTextView output_box;
    private AppCompatButton button_ok;
    private AppCompatTextView source_lang;
    private AppCompatTextView target_lang;
    private AppCompatImageView exchange_btn;

    private static final Map<String, String> LanguageMap;
    static {
        LanguageMap = new HashMap<>();
        LanguageMap.put("中文", "zh");
        LanguageMap.put("英语", "en");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_demo);

        input_box = findViewById(R.id.input);
        output_box = findViewById(R.id.output);
        button_ok = findViewById(R.id.translate);
        source_lang = findViewById(R.id.source_lang);
        target_lang = findViewById(R.id.target_lang);
        exchange_btn = findViewById(R.id.exchange_btn);

        button_ok.setOnClickListener(v->{
            if(input_box.getText().length() != 0) {
                String transSource = input_box.getText().toString();
                TransApi api = new TransApi("20191203000362808", "E1td1fzNFevsfB6F7j78");
                String transResult = api.getTransResult(transSource, LanguageMap.get(source_lang.getText().toString()), LanguageMap.get(target_lang.getText().toString()));
                output_box.setText(transResult);
            }
        });

        exchange_btn.setOnClickListener(v->{
            String lang = source_lang.getText().toString();
            source_lang.setText(target_lang.getText().toString());
            target_lang.setText(lang);
        });
    }
}
