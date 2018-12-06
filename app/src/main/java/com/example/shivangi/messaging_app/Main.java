package com.example.shivangi.messaging_app;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class Main extends AppCompatActivity {

    private long startMilli;
    private long finishMilli;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button1 = findViewById(R.id.button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMilli = System.currentTimeMillis();

                startActivityForResult(
                        new Intent(Main.this, Pop.class), 0);
            }
        });

        final ImageView imageView = findViewById(R.id.imageView1);
        final List<Drawable> images = new ArrayList<>(3);
        images.add(getResources().getDrawable(R.drawable.img1));
        images.add(getResources().getDrawable(R.drawable.img2));
        images.add(getResources().getDrawable(R.drawable.img3));

        final int[] cur = {1};

        Button changeImage = findViewById(R.id.btnChangeImage);
        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageDrawable(images.get((++cur[0])%3));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200) {
            if (resultCode == RESULT_OK && data != null && data.hasExtra("result")) {
                finishMilli = System.currentTimeMillis();
                long time = finishMilli - startMilli;
                Log.d("TAG", Long.toString(time));
                Toast.makeText(getApplicationContext(), "Time from one click to next: " + time, Toast.LENGTH_LONG).show();
                File logFile = new File(getExternalCacheDir(), "time_log.txt");
                String text = data.getStringExtra("result");
                try {
                    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)));
                    out.println(Long.toString(time) + ",\"" + text + "\"");
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
