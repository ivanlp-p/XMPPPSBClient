package com.example.ivan.xmpppsbclient.chat.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.ivan.xmpppsbclient.R;
import com.example.ivan.xmpppsbclient.chat.presenter.ChatPresenterImpl;
import com.example.ivan.xmpppsbclient.databinding.ActivityChatBinding;
import com.example.ivan.xmpppsbclient.enrities.MessageDB;
import com.example.ivan.xmpppsbclient.enrities.RosterEntryDecorator;

import java.util.List;

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

    private static final String EXTRA_CONTACT_ROSTER_ENTRY = "extra_contact_roster_entry";

    private ActivityChatBinding binding;

    private RosterEntryDecorator contactRosterEntry;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);

        setSupportActionBar(binding.chatToolbar);

        binding.chatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatPresenter.sendMessage(binding.chatView.getInputText());
                binding.chatView.setInputText("");
            }
        });

        Intent intent = getIntent();

        if (intent.getSerializableExtra(EXTRA_CONTACT_ROSTER_ENTRY) instanceof RosterEntryDecorator) {
            contactRosterEntry = (RosterEntryDecorator) intent.getSerializableExtra(EXTRA_CONTACT_ROSTER_ENTRY);
        }


        getSupportActionBar().setTitle(contactRosterEntry.getName());

        chatPresenter.loadHistory(contactRosterEntry);
        chatPresenter.getChatWithUser(contactRosterEntry);
    }

    @Override
    public void showHistory(List<MessageDB> messages, User user, User contact) {
        for (MessageDB message : messages) {
            if (message.isIncoming() == true) {
                Message incomingMessage = new Message.Builder()
                        .setUser(contact)
                        .setRightMessage(false)
                        .setMessageText(message.getMessageText())
                        .build();

                binding.chatView.send(incomingMessage);
            } else {
                Message sendMessage = new Message.Builder()
                        .setUser(user)
                        .setRightMessage(true)
                        .setMessageText(message.getMessageText())
                        .build();

                binding.chatView.send(sendMessage);
            }
        }
    }

    @Override
    public void showSendingMessage(User user, String message) {
        Message sendMessage = new Message.Builder()
                .setUser(user)
                .setRightMessage(true)
                .setMessageText(message)
                .build();

        binding.chatView.send(sendMessage);
    }

    @Override
    public void showIncomingMessage(User contact, String message) {
        Message incomingMessage = new Message.Builder()
                .setUser(contact)
                .setRightMessage(false)
                .setMessageText(message)
                .build();

        binding.chatView.send(incomingMessage);
    }
}
