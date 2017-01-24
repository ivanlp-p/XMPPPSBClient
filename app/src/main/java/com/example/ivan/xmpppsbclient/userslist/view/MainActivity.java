package com.example.ivan.xmpppsbclient.userslist.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;

import com.example.ivan.xmpppsbclient.R;
import com.example.ivan.xmpppsbclient.chat.view.ChatActivity;
import com.example.ivan.xmpppsbclient.common.XMPPPSBApplication;
import com.example.ivan.xmpppsbclient.databinding.ActivityMainBinding;
import com.example.ivan.xmpppsbclient.enrities.RosterGroupDecorator;
import com.example.ivan.xmpppsbclient.userslist.presenter.MainPresenter;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

public class MainActivity
        extends MvpActivity<MainView, MainPresenter>
        implements MainView, RecyclerItemClickListener
{
    private static final String TAG = "MainActivity";
    private static final String IS_FIRST_LOGGIN = "is_first_loggin";
    private static final String EXTRA_USER_ID = "extra_user_id";
    private static final String EXTRA_USER_NAME = "extra_user_name";

    private ActivityMainBinding binding;

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return ((XMPPPSBApplication) getApplication()).component().mainPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(binding.mainToolbar);
        getSupportActionBar().setTitle("Контакты");

        Intent intent = getIntent();

        boolean isFirstLoggin = intent.getBooleanExtra(IS_FIRST_LOGGIN, false);

        if (isFirstLoggin) {
            getPresenter().getUserList();
        } else {
            getPresenter().getAccess();
        }
    }

    @Override
    public void showUserList(List<RosterGroupDecorator> rosterGroups) {
        UserListAdapter adapter = new UserListAdapter(this, rosterGroups);
        binding.contactListRv.setLayoutManager(new LinearLayoutManager(this));
        binding.contactListRv.setAdapter(adapter);

        adapter.setRecyclerItemClickListener(this);
    }

    @Override
    public void showChatWithUser(String userJid, String userName) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(EXTRA_USER_ID, userJid);
        intent.putExtra(EXTRA_USER_NAME, userName);
        startActivity(intent);
    }

    @Override
    public void onItemClickListener(String userJid, String userName) {
        getPresenter().getChatWithUser(userJid, userName);
    }
}
