package com.timeline.android.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;

import com.timeline.android.ArticleActivity;
import com.timeline.android.PushActivity;
import com.timeline.android.R;
import com.timeline.android.db.Article;
import com.timeline.android.util.HttpUtil;
import com.timeline.android.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class ArticlePresenter
{
    private Context context;
    private Article article;
    private SharedPreferences pref;

    public ArticlePresenter(Context context, SharedPreferences pref)
    {
        this.context = context;
        this.pref = pref;
        article = (Article) ((ArticleActivity)context).getIntent().getSerializableExtra("article");
    }

    public boolean createMenu()
    {
        if(article.getUserID().equals(pref.getString("userID","")))
        {
            return true;
        }
        return false;
    }

    public void edit()
    {
        Intent intent = new Intent(context,PushActivity.class);
        intent.putExtra("type","edit");
        intent.putExtra("article",article);
        ((ArticleActivity)context).startActivityForResult(intent,1);
    }

    public void delete()
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
                    ((ArticleActivity)context).runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            new AlertDialog.Builder(context)
                                    .setTitle("提示")
                                    .setMessage("删除成功")
                                    .setPositiveButton("确定", new
                                            DialogInterface.OnClickListener()
                                            {
                                                @Override
                                                public void onClick(DialogInterface
                                                                            dialog,
                                                                    int which)
                                                {
                                                    Intent intent = new Intent();
                                                    ((ArticleActivity)context).setResult(RESULT_OK, intent);
                                                    ((ArticleActivity)context).finish();
                                                }
                                            })
                                    .show();
                        }
                    });
                }
            }
        });
    }
}
