package com.example.ivan.xmpppsbclient.chat.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.ivan.xmpppsbclient.R;
import com.example.ivan.xmpppsbclient.chat.presenter.ChatPresenterImpl;
import com.example.ivan.xmpppsbclient.databinding.ActivityChatBinding;

import jp.bassaer.chatmessageview.models.Message;
import jp.bassaer.chatmessageview.models.User;

/**
 * Created by I.Laukhin on 21.01.2017.
 */

public class ChatActivity
        extends MvpAppCompatActivity
        implements ChatView
{
    @InjectPresenter
    ChatPresenterImpl chatPresenter;

    private static final String EXTRA_USER_ID = "extra_user_id";
    private static final String EXTRA_USER_NAME = "extra_user_name";

    private ActivityChatBinding binding;

    private User me;
    private User you;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);

        setSupportActionBar(binding.chatToolbar);

        me = new User(0, "Manager", BitmapFactory.decodeResource(getResources(), R.drawable.face_1));
        you = new User(1, "Operator", BitmapFactory.decodeResource(getResources(), R.drawable.face_2));

        binding.chatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatPresenter.sendMessage(binding.chatView.getInputText());
                binding.chatView.setInputText("");
            }
        });

        Intent intent = getIntent();

        String userJid = intent.getStringExtra(EXTRA_USER_ID);
        String userName = intent.getStringExtra(EXTRA_USER_NAME);

        getSupportActionBar().setTitle(userName);

        chatPresenter.getChatWithUser(userJid);
    }

    @Override
    public void showSendingMessage(String message) {
        Message sendMessage = new Message.Builder()
                .setUser(me)
                .setRightMessage(true)
                .setMessageText(message)
                .build();

        binding.chatView.send(sendMessage);
    }

    @Override
    public void showIncomingMessage(String message) {
        Message incomingMessage = new Message.Builder()
                .setUser(you)
                .setRightMessage(false)
                .setMessageText(message)
                .build();

        binding.chatView.send(incomingMessage);
    }
}
