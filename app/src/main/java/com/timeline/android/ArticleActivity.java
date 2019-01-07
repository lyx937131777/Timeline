package com.timeline.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.timeline.android.dagger2.DaggerMyComponent;
import com.timeline.android.dagger2.MyComponent;
import com.timeline.android.dagger2.MyModule;
import com.timeline.android.db.Article;
import com.timeline.android.presenter.ArticlePresenter;
import com.timeline.android.util.HttpUtil;
import com.timeline.android.util.LogUtil;
import com.timeline.android.util.Utility;

import java.io.IOException;
import java.text.SimpleDateFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ArticleActivity extends AppCompatActivity
{
    private TextView authorText;
    private TextView dateText;
    private TextView contentText;
    private ImageView imageView;
    private CardView imageCard;

    private Article article;

    private ArticlePresenter articlePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        article = (Article)getIntent().getSerializableExtra("article");
        authorText = findViewById(R.id.author);
        dateText = findViewById(R.id.date);
        contentText = findViewById(R.id.content);
        imageView = findViewById(R.id.img);
        imageCard = findViewById(R.id.img_card);

        authorText.setText(article.getNickname());
//        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
//        dateText.setText(ft.format(article.getDate()));
        dateText.setText(article.getTimeStamp());
        contentText.setText(article.getContent());
        Glide.with(this).load(article.getImageURL()).into(imageView);
        if(article.getImageURL().equals("empty"))
        {
            imageCard.setVisibility(View.GONE);
        }

        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(this)).build();
        articlePresenter = myComponent.articlePresenter();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(articlePresenter.createMenu())
        {
            getMenuInflater().inflate(R.menu.edit_delete_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
            case R.id.edit:
                articlePresenter.edit();
//                Toast.makeText(this,"edit",Toast.LENGTH_LONG).show();
                break;
            case R.id.delete:
                articlePresenter.delete();
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
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
        }
    }

    public ArticlePresenter getArticlePresenter()
    {
        return articlePresenter;
    }

    public void setArticlePresenter(ArticlePresenter articlePresenter)
    {
        this.articlePresenter = articlePresenter;
    }
}
