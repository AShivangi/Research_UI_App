package com.example.shivangi.messaging_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageListActivity extends AppCompatActivity {
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private EditText mMessageText;
    final SimpleDateFormat dateFormat = new SimpleDateFormat("KK:mm a");
    Intent returnIntent = new Intent();


    public void setWindowParams() {
        WindowManager.LayoutParams wlp = getWindow().getAttributes();
        wlp.dimAmount = 0;
        wlp.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        getWindow().setAttributes(wlp);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupwindow);
        setWindowParams();

        mMessageRecycler = findViewById(R.id.reyclerview_message_list);
        final List<BaseMessage> messageList = new ArrayList<>();
        if(Main.msg.equals("MOBHI1")) {
            final MediaPlayer m = MediaPlayer.create(this, R.raw.incoming);
            messageList.add(new BaseMessage("Sara", "Me", "How long will you be?",
                    dateFormat.format(new Date())));
            m.start(); } else if(Main.msg.equals("MOBRU1")) {
            final MediaPlayer m = MediaPlayer.create(this, R.raw.incoming);
            messageList.add(new BaseMessage("Sara", "Me", "How much longer will it take?",
                    dateFormat.format(new Date())));
            m.start();
        } else if(Main.msg.equals("MOBRU2")) {
            final MediaPlayer m = MediaPlayer.create(this, R.raw.incoming);
            messageList.add(new BaseMessage("Sara", "Me", "Pepperoni or cheese pizza?",
                    dateFormat.format(new Date())));
            m.start();
        }
        setTitle(messageList.get(0).getSender());

        mMessageAdapter = new MessageListAdapter(this, messageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);

        mMessageText = findViewById(R.id.edittext_chatbox);
        mMessageText.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                long tt_click = System.currentTimeMillis();
                returnIntent.putExtra("time2",tt_click);
                setResult(Activity.RESULT_OK,returnIntent);
                return false;
            }
        });

        Button mSendButton = findViewById(R.id.button_chatbox_send);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager =
                        (InputMethodManager) view.getContext().
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(
                        MessageListActivity.this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                String messageText = mMessageText.getText().toString();
                BaseMessage message = new BaseMessage("Me", "shivangi",
                        messageText,dateFormat.format(new Date()));
                messageList.add(message);
                mMessageAdapter.notifyDataSetChanged();

                final MediaPlayer m = MediaPlayer.create(MessageListActivity.this, R.raw.sent);
                m.start();

                returnIntent.putExtra("result",messageText);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });


        ImageButton voice = findViewById(R.id.button_voice);
        voice.setVisibility(Main.voice_switch? View.VISIBLE : View.GONE);
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
                long tt_click = System.currentTimeMillis();
                returnIntent.putExtra("time",tt_click);
                setResult(Activity.RESULT_OK,returnIntent);
                startActivityForResult(intent,200);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200){
            if(resultCode == RESULT_OK && data != null){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                mMessageText.setText(result.get(0));
            }
        }
    }
}