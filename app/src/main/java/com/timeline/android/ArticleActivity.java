package com.timeline.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.timeline.android.db.Article;
import com.timeline.android.util.HttpUtil;
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

    private SharedPreferences pref;
    private Article article;

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

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        article = (Article)getIntent().getSerializableExtra("article");
        authorText = findViewById(R.id.author);
        dateText = findViewById(R.id.date);
        contentText = findViewById(R.id.content);
        imageView = findViewById(R.id.img);

        authorText.setText(article.getNickname());
//        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
//        dateText.setText(ft.format(article.getDate()));
        dateText.setText(article.timeStamp);
        contentText.setText(article.getContent());
        Glide.with(this).load(article.getImageURL()).into(imageView);

    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        article = (Article)getIntent().getSerializableExtra("article");
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        if(article.getUserID().equals(pref.getString("userID","")))
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
                Intent intent = new Intent(ArticleActivity.this,PushActivity.class);
                intent.putExtra("type","edit");
                intent.putExtra("article",article);
                startActivityForResult(intent,1);
//                Toast.makeText(this,"edit",Toast.LENGTH_LONG).show();
                break;
            case R.id.delete:
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
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    new AlertDialog.Builder(ArticleActivity.this)
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
                                                            setResult(RESULT_OK, intent);
                                                            finish();
                                                        }
                                                    })
                                            .show();
                                }
                            });
                        }
                    }
                });
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
}
