package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class twitter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_twitter);

            EditText editText = findViewById(R.id.editText);

            Button btnTweet = findViewById(R.id.btnTweet);
            btnTweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tweetText = editText.getText().toString();
                    setTweet(tweetText);
                }
            });
    }

    private void setTweet(String tweetText) {
        String tweetUrl = "https://twitter.com/intent/tweet?text=" + tweetText;
        Uri uri = Uri.parse(tweetUrl);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}