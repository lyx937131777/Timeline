package com.timeline.android.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.timeline.android.PushActivity;
import com.timeline.android.db.Article;
import com.timeline.android.util.HttpUtil;
import com.timeline.android.util.LogUtil;
import com.timeline.android.util.Utility;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class PushPresenter
{
    private Context context;
    private SharedPreferences pref;
    private String userID;
    private String type;
    private Article article;
    private String imagePath;

    public PushPresenter(Context context, SharedPreferences pref)
    {
        this.context = context;
        this.pref = pref;
        userID = pref.getString("userID",null);
        type = ((PushActivity)context).getIntent().getStringExtra("type");
        if(type.equals("edit"))
        {
            article = (Article)((PushActivity)context).getIntent().getSerializableExtra("article");
        }else
        {
            article = new Article(0,"","","","",new Date());
        }
    }

    public void commit()
    {
        // post json_data to server
        // if imagePath != null post imagepath ,else post profile_photo
        if(article.getContent().length()>140)
        {
            new AlertDialog.Builder(context)
                    .setTitle("提示")
                    .setMessage("字数不得大于140")
                    .setPositiveButton("确定",null)
                    .show();
            return;
        }
        if (imagePath != null)
        {
            article.setImageURL(Utility.imageToBase64(imagePath));
            if(article.getContent().equals(""))
            {
                article.setContent("发布图片");
            }
        }else
        {
            if(article.getContent().equals(""))
            {
                new AlertDialog.Builder(context)
                        .setTitle("提示")
                        .setMessage("发布内容不得为空")
                        .setPositiveButton("确定", null)
                        .show();
                return;
            }
            article.setImageURL("");
        }
        if(type.equals("edit"))
        {
            deleteArticle();
        }
        pushArticle();
    }

    private void deleteArticle()
    {
        String address = HttpUtil.LocalAddress + "/article/delete";
        HttpUtil.deleteRequest(address, article.getArticleID(), new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                final String responseData = response.body().string();
                Log.e("PushActivity:data", responseData);
                if (Utility.checkMessage(responseData).equals("true"))
                {
                    LogUtil.e("Edit!","delete success!");
                }
            }
        });
    }

    public void setContentAndImagepath(String content, String imagePath)
    {
        article.setContent(content);
        this.imagePath = imagePath;
    }

    private void pushArticle()
    {
        String address = HttpUtil.LocalAddress + "/article/push";
        HttpUtil.pushRequest(address, userID, article.getContent(), article.getImageURL(), new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                final String responseData = response.body().string();
                Log.e("PushActivity:data", responseData);
                if (Utility.checkMessage(responseData).equals("true"))
                {
                    ((PushActivity)context).runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            new AlertDialog.Builder(context)
                                    .setTitle("提示")
                                    .setMessage("发布成功")
                                    .setPositiveButton("确定", new
                                            DialogInterface.OnClickListener()
                                            {
                                                @Override
                                                public void onClick(DialogInterface
                                                                            dialog,
                                                                    int which)
                                                {
                                                    Intent intent = new Intent();
                                                    ((PushActivity)context).setResult(RESULT_OK, intent);
                                                    ((PushActivity)context).finish();
                                                }
                                            })
                                    .show();
                        }
                    });
                }
            }
        });
    }

    public Article getArticle()
    {
        return article;
    }

    public String getImagePath()
    {
        return imagePath;
    }
}
