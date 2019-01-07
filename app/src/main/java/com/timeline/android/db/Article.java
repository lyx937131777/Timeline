package com.timeline.android.db;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.Date;

public class Article extends DataSupport implements Serializable
{
    private int articleID;
    private String userID;
    private String nickname;
    private String content;
    private String imageURL;
    private Date date;
    private String timeStamp;

    public Article(int articleID, String userID,String nickname, String content, String imageURL, Date date)
    {
        this.articleID = articleID;
        this.userID = userID;
        this.nickname =nickname;
        this.content = content;
        this.imageURL = imageURL;
        this.date = date;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public int getArticleID()
    {
        return articleID;
    }

    public void setArticleID(int articleID)
    {
        this.articleID = articleID;
    }

    public String getUserID()
    {
        return userID;
    }

    public void setUserID(String userID)
    {
        this.userID = userID;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getImageURL()
    {
        return imageURL;
    }

    public void setImageURL(String imageURL)
    {
        this.imageURL = imageURL;
    }

    public String getTimeStamp()
    {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp)
    {
        this.timeStamp = timeStamp;
    }
}
