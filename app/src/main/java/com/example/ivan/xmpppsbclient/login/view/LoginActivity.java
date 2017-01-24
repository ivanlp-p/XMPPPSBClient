package com.example.ivan.xmpppsbclient.login.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.example.ivan.xmpppsbclient.R;
import com.example.ivan.xmpppsbclient.common.XMPPPSBApplication;
import com.example.ivan.xmpppsbclient.databinding.ActivityLoginBinding;
import com.example.ivan.xmpppsbclient.login.presenter.LoginPresenter;
import com.example.ivan.xmpppsbclient.userslist.view.MainActivity;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

/**
 * Created by I.Laukhin on 21.01.2017.
 */

public class LoginActivity
        extends MvpActivity<LoginView, LoginPresenter>
        implements LoginView
{
    private static final String IS_FIRST_LOGGIN = "is_first_loggin";

    private ActivityLoginBinding binding;

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return ((XMPPPSBApplication) getApplication()).component().loginPresenter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        if (getPresenter().canFindUser()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = binding.inputLogin.getText().toString();
                String password = binding.inputPassword.getText().toString();

                getPresenter().getAccess(login, password);
            }
        });
    }

    @Override
    public void startUserListActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(IS_FIRST_LOGGIN, true);
        startActivity(intent);
        finish();
    }

    @Override
    public void showWarningToast() {
        Toast.makeText(this, "Неверно указан логин или пароль",
                Toast.LENGTH_SHORT).show();
    }
}
