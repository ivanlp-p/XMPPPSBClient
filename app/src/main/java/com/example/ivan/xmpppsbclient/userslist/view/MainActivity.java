package com.example.ivan.xmpppsbclient.userslist.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.ivan.xmpppsbclient.R;
import com.example.ivan.xmpppsbclient.chat.view.ChatActivity;
import com.example.ivan.xmpppsbclient.databinding.ActivityMainBinding;
import com.example.ivan.xmpppsbclient.enrities.RosterEntryDecorator;
import com.example.ivan.xmpppsbclient.enrities.RosterGroupDecorator;
import com.example.ivan.xmpppsbclient.userslist.presenter.MainPresenterImpl;

import java.util.List;

public class MainActivity
        extends MvpAppCompatActivity
        implements MainView, RecyclerItemClickListener
{
    @InjectPresenter
    MainPresenterImpl mainPresenter;

    private static final String TAG = "MainActivity";
    private static final String IS_FIRST_LOGGIN = "is_first_loggin";
    private static final String EXTRA_CONTACT_ROSTER_ENTRY = "extra_contact_roster_entry";

    private ActivityMainBinding binding;
    private UserListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(binding.mainToolbar);
        getSupportActionBar().setTitle("Контакты");

        Intent intent = getIntent();

        boolean isFirstLoggin = intent.getBooleanExtra(IS_FIRST_LOGGIN, false);

        if (isFirstLoggin) {
            mainPresenter.getUserList();
        } else {
            mainPresenter.getAccess();
        }
    }

    @Override
    public void showUserList(List<RosterGroupDecorator> rosterGroups) {
        adapter = new UserListAdapter(this, rosterGroups);
        adapter.expandAllParents();
        binding.contactListRv.setLayoutManager(new LinearLayoutManager(this));
        binding.contactListRv.setAdapter(adapter);
        adapter.setRecyclerItemClickListener(this);
    }

    @Override
    public void showChatWithUser(RosterEntryDecorator contactRosterEntry) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(EXTRA_CONTACT_ROSTER_ENTRY, contactRosterEntry);
        startActivity(intent);
    }

    @Override
    public void updateUsersListWhenProcessNewChat(String userJid) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClickListener(RosterEntryDecorator contactRosterEntry) {
        mainPresenter.getChatWithUser(contactRosterEntry);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        adapter.onRestoreInstanceState(savedInstanceState);
    }
}
