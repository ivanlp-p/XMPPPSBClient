package com.example.ivan.xmpppsbclient.login.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.ivan.xmpppsbclient.R;
import com.example.ivan.xmpppsbclient.databinding .ActivityLoginBinding;
import com.example.ivan.xmpppsbclient.login.presenter.LoginPresenterImpl;
import com.example.ivan.xmpppsbclient.userslist.view.MainActivity;

/**
 * Created by I.Laukhin on 21.01.2017.
 */

public class LoginActivity
        extends MvpAppCompatActivity
        implements LoginView
{
    @InjectPresenter
    LoginPresenterImpl loginPresenter;

    private static final String IS_FIRST_LOGGIN = "is_first_loggin";

    private ActivityLoginBinding binding;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        if (loginPresenter.canFindUser()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = binding.inputLogin.getText().toString();
                String password = binding.inputPassword.getText().toString();

                loginPresenter.getAccess(login, password);
            }
        });
    }

    @Override
    public void startUserListActivity() {
        progressDialog.dismiss();

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

    @Override
    public void showProgressDialog() {
        binding.btnSignIn.setEnabled(false);

        progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.progress_dialog_message));
        progressDialog.show();
    }
}
