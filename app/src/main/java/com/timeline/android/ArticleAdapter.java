package com.timeline.android;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.timeline.android.db.Article;

import java.text.SimpleDateFormat;
import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder>
{
    private Context mContext;
    private List<Article> mArticleList;

    static  class ViewHolder extends RecyclerView.ViewHolder
    {
        View articleView;
        ImageView articleImg;
        TextView articleAuthor;
        TextView articleContent;
        TextView articleDate;
        public ViewHolder(View itemView)
        {
            super(itemView);
            articleView = itemView;
            articleImg = itemView.findViewById(R.id.article_img);
            articleAuthor = itemView.findViewById(R.id.article_author);
            articleContent = itemView.findViewById(R.id.article_content);
            articleDate = itemView.findViewById(R.id.article_date);
        }
    }
    public ArticleAdapter(List<Article> articleList)
    {
        mArticleList = articleList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if (mContext == null)
        {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.article_item,parent,false);
        final ArticleAdapter.ViewHolder holder = new ArticleAdapter.ViewHolder(view);
        holder.articleView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int position = holder.getAdapterPosition();
                Article article = mArticleList.get(position);
                Intent intent = new Intent(mContext,ArticleActivity.class);
                intent.putExtra("article",article);
                ((MainActivity)mContext).startActivityForResult(intent,1);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Article article = mArticleList.get(position);
        holder.articleAuthor.setText(article.getNickname());
        holder.articleContent.setText(article.getContent());
//        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
//        holder.articleDate.setText(ft.format(article.getDate()));
        holder.articleDate.setText(article.getTimeStamp());
        Glide.with(mContext).load(article.getImageURL()).into(holder.articleImg);
    }

    @Override
    public int getItemCount()
    {
        return mArticleList.size();
    }
}
