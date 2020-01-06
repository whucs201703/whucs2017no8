package com.example.translate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.translate.litepal.TranslationHistory;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.Map;

import baidu.CallbackFun;
import baidu.TransApi;

public class TranslateDemo extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private AppCompatEditText input_box;
    private AppCompatTextView output_box;
    private AppCompatButton button_ok;
    private AppCompatTextView source_lang;
    private AppCompatTextView target_lang;
    private AppCompatImageView exchange_btn;
    private AppCompatImageView cancel_btn;
    private AppCompatTextView history_box;
    private NavigationView navView;

    private static final Map<String, String> LanguageMap = new HashMap<String, String>()
    {{
        put("中文", "zh");
        put("英语", "en");
        put("日语", "jp");
        put("韩语", "kor");
        put("法语", "fra");
        put("俄语", "ru");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_demo);

        toolbar = findViewById(R.id.toolbar);
        input_box = findViewById(R.id.input);
        drawerLayout = findViewById(R.id.drawer_layout);
        output_box = findViewById(R.id.output);
        button_ok = findViewById(R.id.translate);
        source_lang = findViewById(R.id.source_lang);
        target_lang = findViewById(R.id.target_lang);
        exchange_btn = findViewById(R.id.exchange_btn);
        cancel_btn = findViewById(R.id.cancel_btn);
        history_box = findViewById(R.id.history);
        navView = findViewById(R.id.nav_view);

        toolbar.setNavigationIcon(R.drawable.ic_menu_black);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v->{
            drawerLayout.openDrawer(GravityCompat.START);
        });

        input_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                /*do nothing*/
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                /*do nothing*/
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() != 0) {
                    cancel_btn.setVisibility(View.VISIBLE);
                } else {
                    cancel_btn.setVisibility(View.GONE);
                }
            }
        });
        button_ok.setOnClickListener(v->{
            if(input_box.getText().length() != 0) {
                String transSource = input_box.getText().toString();
                TransApi api = new TransApi("20191203000362808", "E1td1fzNFevsfB6F7j78");
                api.setTask(transSource, LanguageMap.get(source_lang.getText().toString()), LanguageMap.get(target_lang.getText().toString()));
                api.getTransResultAsync(new CallbackFun() {
                    @Override
                    public void onSuccess(String result) {
                        output_box.setText(result);
                        TranslationHistory history = new TranslationHistory();
                        history.setFrom(transSource);
                        history.setTo(result);
                        history.save();
                    }

                    @Override
                    public void onFailure() {
                        output_box.setText("翻译发生错误！");
                    }
                });

            }
        });

        exchange_btn.setOnClickListener(v->{
            exchangeLang();
        });

        source_lang.setOnClickListener(v->{
            Intent intent = new Intent(TranslateDemo.this, LangChooseActivity.class);
            startActivityForResult(intent, 1);
        });

        target_lang.setOnClickListener(v->{
            Intent intent = new Intent(TranslateDemo.this, LangChooseActivity.class);
            startActivityForResult(intent, 2);
        });

        cancel_btn.setOnClickListener(v->{
            input_box.setText("");
            output_box.setText("");
            cancel_btn.setVisibility(View.GONE);
        });

        history_box.setOnClickListener(v->{
            Intent intent = new Intent(TranslateDemo.this, TranHistoryActivity.class);
            startActivity(intent);
        });

        navView.setNavigationItemSelectedListener(item -> {
            switch(item.getItemId()) {
                case R.id.nav_dictionary:
                    Intent intent = new Intent(TranslateDemo.this, Main.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    public void exchangeLang() {
        String lang = source_lang.getText().toString();
        source_lang.setText(target_lang.getText().toString());
        target_lang.setText(lang);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if(resultCode == RESULT_OK) {
                    String lang = data.getStringExtra("language");
                    if(lang != null) {
                        if(target_lang.getText().toString().equals(lang)) {
                            exchangeLang();
                        } else {
                            source_lang.setText(lang);
                        }
                    }
                }
                break;
            case 2:
                if(resultCode == RESULT_OK) {
                    String lang = data.getStringExtra("language");
                    if(lang != null) {
                        if(source_lang.getText().toString().equals(lang)) {
                            exchangeLang();
                        } else {
                            target_lang.setText(lang);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }
}
