package com.timeline.android.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.timeline.android.RegisterActivity;
import com.timeline.android.util.CheckUtil;
import com.timeline.android.util.HttpUtil;
import com.timeline.android.util.LogUtil;
import com.timeline.android.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterPresenter
{
    private Context context;
    private CheckUtil checkUtil;


    public RegisterPresenter(Context context, CheckUtil checkUtil)
    {
        this.context = context;
        this.checkUtil = checkUtil;
    }

    public void register(String username, String password, String confirm, String nickname)
    {

        if (!checkUtil.checkRegister(username,password,confirm,nickname))
            return;
        ((RegisterActivity)context).setSyncFinished(false);
        String address = HttpUtil.LocalAddress + "/user/register";
        HttpUtil.registerRequest(address, username, password, nickname, new
                Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                e.printStackTrace();
                LogUtil.e("Register", "Faled!!!!!!!!!");
                ((RegisterActivity)context).setSyncFinished(true);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                final String responseData = response.body().string();
                LogUtil.e("Register", "源码 : " + responseData);
                if (Utility.checkMessage(responseData).equals("true"))
                {
                    ((RegisterActivity) context).runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            new AlertDialog.Builder(context)
                                    .setTitle("提示")
                                    .setMessage("注册成功！")
                                    .setPositiveButton("确定", new
                                            DialogInterface.OnClickListener()
                                            {
                                                @Override
                                                public void onClick(DialogInterface dialog, int
                                                        which)
                                                {
                                                    ((RegisterActivity) context).finish();
                                                }
                                            }).show();
                        }
                    });
                } else if (Utility.checkErrorType(responseData).equals("userID_repeated"))
                {
                    ((RegisterActivity) context).runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            new AlertDialog.Builder(context)
                                    .setTitle("提示")
                                    .setMessage("该账户已被注册！")
                                    .setPositiveButton("确定", null)
                                    .show();
                        }
                    });
                } else
                {
                    ((RegisterActivity) context).runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            new AlertDialog.Builder(context)
                                    .setTitle("提示")
                                    .setMessage("由于未知原因注册失败，请重试！")
                                    .setPositiveButton("确定", null)
                                    .show();
                        }
                    });
                }
                ((RegisterActivity)context).setSyncFinished(true);
            }
        });
    }
}
