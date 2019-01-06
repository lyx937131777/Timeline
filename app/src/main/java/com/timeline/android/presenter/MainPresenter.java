package com.timeline.android.presenter;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.timeline.android.ArticleAdapter;
import com.timeline.android.MainActivity;
import com.timeline.android.db.Article;
import com.timeline.android.util.HttpUtil;
import com.timeline.android.util.LogUtil;
import com.timeline.android.util.Utility;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainPresenter
{
    private Context context;
    private List<Article> articleList;
    private ArticleAdapter articleAdapter;
    private SwipeRefreshLayout swipeRefresh;

    public MainPresenter(Context context, List<Article> articleList, ArticleAdapter articleAdapter, SwipeRefreshLayout swipeRefresh)
    {
        this.context = context;
        this.articleList = articleList;
        this.articleAdapter = articleAdapter;
        this.swipeRefresh = swipeRefresh;
    }

    public void refreshArticle()
    {
        String address = HttpUtil.LocalAddress + "/article/refresh";
        HttpUtil.refreshRequest(address, 0,8, new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                e.printStackTrace();
                ((MainActivity)context).runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(context, "服务器连接错误", Toast
                                .LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                final String responsData = response.body().string();
                initArticle(responsData);
                if (Utility.checkMessage(responsData).equals("true"))
                {
                    ((MainActivity)context).runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            articleAdapter.notifyDataSetChanged();
                            swipeRefresh.setRefreshing(false);
                        }
                    });
                } else
                {
                    ((MainActivity)context).runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(context, "传输出错", Toast
                                    .LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    public void moreArticle()
    {
        String address = HttpUtil.LocalAddress + "/article/more";
        int articleID = articleList.get(articleList.size()-1).getArticleID();
        HttpUtil.moreRequest(address,articleID , 8, new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                e.printStackTrace();
                ((MainActivity)context).runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(context, "服务器连接错误", Toast
                                .LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                final String responsData = response.body().string();
                moreArticle(responsData);
                if (Utility.checkMessage(responsData).equals("true"))
                {
                    ((MainActivity)context).runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            articleAdapter.notifyDataSetChanged();
                        }
                    });
                } else
                {
                    ((MainActivity)context).runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(context, "传输出错", Toast
                                    .LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private void initArticle(String responsData)
    {
        articleList.clear();
        articleList.addAll(Utility.handleArticleList(responsData));
        Collections.sort(articleList, new Comparator<Article>()
        {
            @Override
            public int compare(Article o1, Article o2)
            {
                return o2.getArticleID() - o1.getArticleID();
            }
        });
    }

    private void moreArticle(String responsData)
    {
        articleList.addAll(Utility.handleArticleList(responsData));
        Collections.sort(articleList, new Comparator<Article>()
        {
            @Override
            public int compare(Article o1, Article o2)
            {
                return o2.getArticleID() - o1.getArticleID();
            }
        });
    }
}
