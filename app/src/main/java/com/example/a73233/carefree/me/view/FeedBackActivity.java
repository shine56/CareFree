package com.example.a73233.carefree.me.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.a73233.carefree.R;
import com.example.a73233.carefree.baseView.BaseActivity;

public class FeedBackActivity extends BaseActivity {

    private String subwords = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        ReviseStatusBar(TRANSPARENT_BLACK);
        Button submit = findViewById(R.id.feed_back_submit);
        EditText words = findViewById(R.id.feed_back_words);
        ImageView back = findViewById(R.id.feed_back_toolbar_left);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(words.getText().toString() != null && words.getText().toString().length()!=0){
                    if(words.getText().toString().equals(subwords)){
                        showToast("不要重复提交哦");
                    }else {
                        showToast("提交成功,感谢您的回馈！");
                        subwords = words.getText().toString();
                    }
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
