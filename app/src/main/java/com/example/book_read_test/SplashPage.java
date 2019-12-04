package com.example.book_read_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashPage extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_page);
        textView = (TextView) findViewById(R.id.textView);
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.my_transistion);
        textView.startAnimation(myanim);

        final Intent i_1 = new Intent(this, LoginPage.class);
        Thread timer = new Thread(){
            public void run ()
            {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                finally {
                    startActivity(i_1);
                    finish();
                }
            }
        };

        timer.start();

    }
}
