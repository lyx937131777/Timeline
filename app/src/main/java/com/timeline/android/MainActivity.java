package com.timeline.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.timeline.android.db.Article;
import com.timeline.android.util.HttpUtil;
import com.timeline.android.util.LogUtil;
import com.timeline.android.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
{

//    private Article[] articles = {new Article(0,"123456@qq.com","吕轶霄","123456","",new Date()),
//            new Article(1,"","孙海英","123456789123456789","",new Date()),
//            new Article(2,"","李钦","123456789123456789123456789","",new Date()),
//            new Article(3,"","姜宁康","1234567891234567891234567891111111111111" +
//                    "1111111111111111111111111111111111111111111111111111","",new Date()),
//            new Article(4,"","赵慧","12345678912345678912345678911111111111111" +
//                    "111111111111111111111111111111111111111111111111111111111111" +
//                    "11111111111111111111111111111111111111111111111111111111111111" +
//                    "111111111111111111111111111111111111111111111111111111111111111","",new Date()),
//            new Article(5,"","卜天明","《算法导论》自第一版出版以来，已经成为世界范围内广" +
//                    "泛使用的大学教材和专业人员的标准参考手册。本书全面论述了算法的内容，从一定深度上涵盖了算法的诸" +
//                    "多方面，同时其讲授和分析方法又兼顾了各个层次读者的接受能力。各章内容自成体系，可作为独立单元学习。所" +
//                    "有算法都用英文和伪码描述，使具备初步编程经验的人也可读懂。全书讲解通俗易懂，且不失深度和数学上的严谨" +
//                    "性。第二版增加了新的章节，如算法作用、概率分析与随机算法、线性编程等，几乎对第一版的各个部分都作" +
//                    "了大量修订。","",new Date())};

    private List<Article> articleList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ArticleAdapter articleAdapter;
    private SwipeRefreshLayout swipeRefresh;
    private SharedPreferences pref;
    private CardView moreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null)
//        {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }

        recyclerView =  findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        articleAdapter = new ArticleAdapter(articleList);
        recyclerView.setAdapter(articleAdapter);
        refreshArticle();

        moreButton = findViewById(R.id.more_card);
        moreButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                moreArticle();
            }
        });

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                refreshArticle();
            }
        });

    }

    private void moreArticle()
    {
        String address = HttpUtil.LocalAddress + "/article/more";
        int articleID = articleList.get(articleList.size()-1).getArticleID();
        HttpUtil.moreRequest(address,articleID , 8, new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                e.printStackTrace();
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(MainActivity.this, "服务器连接错误", Toast
                                .LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                final String responsData = response.body().string();
                LogUtil.e("MainActivity", responsData);
                if (Utility.checkMessage(responsData).equals("true"))
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            moreArticle(responsData);
                            articleAdapter.notifyDataSetChanged();
                        }
                    });
                } else
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(MainActivity.this, "传输出错", Toast
                                    .LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private void refreshArticle()
    {
        String address = HttpUtil.LocalAddress + "/article/refresh";
        HttpUtil.refreshRequest(address, 0,8, new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                e.printStackTrace();
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(MainActivity.this, "服务器连接错误", Toast
                                .LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                final String responsData = response.body().string();
                LogUtil.e("MainActivity", responsData);
                if (Utility.checkMessage(responsData).equals("true"))
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            initArticle(responsData);
                            articleAdapter.notifyDataSetChanged();
                            swipeRefresh.setRefreshing(false);
                        }
                    });
                } else
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(MainActivity.this, "传输出错", Toast
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.push_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.push:
                Intent intent = new Intent(MainActivity.this, PushActivity.class);
                intent.putExtra("type","push");
                startActivityForResult(intent,1);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case 1:
                if(resultCode == RESULT_OK)
                {
                    refreshArticle();
                }
                break;
        }
    }
}
