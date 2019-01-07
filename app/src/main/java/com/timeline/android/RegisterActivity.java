package com.timeline.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.timeline.android.dagger2.DaggerMyComponent;
import com.timeline.android.dagger2.MyComponent;
import com.timeline.android.dagger2.MyModule;
import com.timeline.android.presenter.RegisterPresenter;
import com.timeline.android.util.CheckUtil;
import com.timeline.android.util.HttpUtil;
import com.timeline.android.util.LogUtil;
import com.timeline.android.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements OnClickListener
{
    private EditText username, password, confirm_password, nickname;
    private Button bt_username_clear;
    private Button bt_pwd_clear;
    private Button bt_confirm_pwd_clear;
    private Button bt_nickname_clear;
    private Button bt_register;
    String username_text, password_text, confirm_password_text, nickname_text;

    private RegisterPresenter registerPresenter;

    //AndroidTest
    private boolean isSyncFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(this)).build();
        registerPresenter = myComponent.registerPresenter();
//        registerPresenter = new RegisterPresenter(this, new CheckUtil(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void initView()
    {
        username = (EditText) findViewById(R.id.username);
        // 监听文本框内容变化
        username.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // 获得文本框中的用户
                username_text = username.getText().toString();
                if ("".equals(username_text))
                {
                    // 用户名为空,设置按钮不可见
                    bt_username_clear.setVisibility(View.INVISIBLE);
                } else
                {
                    // 用户名不为空，设置按钮可见
                    bt_username_clear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });
        password = (EditText) findViewById(R.id.password);
        // 监听文本框内容变化
        password.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // 获得文本框中的用户
                password_text = password.getText().toString();
                if ("".equals(password_text))
                {
                    // 用户名为空,设置按钮不可见
                    bt_pwd_clear.setVisibility(View.INVISIBLE);
                } else
                {
                    // 用户名不为空，设置按钮可见
                    bt_pwd_clear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });
        confirm_password = (EditText) findViewById(R.id.confirm_password);
        // 监听文本框内容变化
        confirm_password.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // 获得文本框中的用户
                confirm_password_text = confirm_password.getText().toString();
                if ("".equals(confirm_password_text))
                {
                    // 用户名为空,设置按钮不可见
                    bt_confirm_pwd_clear.setVisibility(View.INVISIBLE);
                } else
                {
                    // 用户名不为空，设置按钮可见
                    bt_confirm_pwd_clear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });
        nickname = (EditText) findViewById(R.id.nickname);
        // 监听文本框内容变化
        nickname.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // 获得文本框中的用户
                nickname_text = nickname.getText().toString();
                if ("".equals(nickname_text))
                {
                    // 用户名为空,设置按钮不可见
                    bt_nickname_clear.setVisibility(View.INVISIBLE);
                } else
                {
                    // 用户名不为空，设置按钮可见
                    bt_nickname_clear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });
        bt_username_clear = (Button) findViewById(R.id.bt_username_clear);
        bt_username_clear.setOnClickListener(this);
        bt_pwd_clear = (Button) findViewById(R.id.bt_pwd_clear);
        bt_pwd_clear.setOnClickListener(this);
        bt_confirm_pwd_clear = (Button) findViewById(R.id.bt_confirm_pwd_clear);
        bt_confirm_pwd_clear.setOnClickListener(this);
        bt_nickname_clear = (Button) findViewById(R.id.bt_nickname_clear);
        bt_nickname_clear.setOnClickListener(this);
        bt_register = (Button) findViewById(R.id.bt_register);
        bt_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            // 清除
            case R.id.bt_username_clear:
                username.setText("");
                break;
            case R.id.bt_pwd_clear:
                password.setText("");
                break;
            case R.id.bt_confirm_pwd_clear:
                confirm_password.setText("");
                break;
            case R.id.bt_nickname_clear:
                nickname.setText("");
                break;

            // TODO 注册按钮
            case R.id.bt_register:
                username_text = username.getText().toString();
                password_text = password.getText().toString();
                confirm_password_text = confirm_password.getText().toString();
                nickname_text = nickname.getText().toString();
                registerPresenter.register(username_text,password_text,confirm_password_text,nickname_text);
                break;

            default:
                break;
        }
    }

    public RegisterPresenter getRegisterPresenter()
    {
        return registerPresenter;
    }

    public void setRegisterPresenter(RegisterPresenter registerPresenter)
    {
        this.registerPresenter = registerPresenter;
    }

    public boolean isSyncFinished()
    {
        return isSyncFinished;
    }

    public void setSyncFinished(boolean syncFinished)
    {
        isSyncFinished = syncFinished;
    }
}

