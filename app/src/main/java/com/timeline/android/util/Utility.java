package com.timeline.android.util;

import android.text.TextUtils;
import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.timeline.android.db.Article;
import com.timeline.android.db.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class Utility
{
    public static boolean handleUserResponse(String response)
    {
        if (!TextUtils.isEmpty(response))
        {
            try
            {
                DataSupport.deleteAll(User.class);
                JSONArray allUsers = new JSONArray(response);
                for (int i = 0; i < allUsers.length(); i++)
                {
                    JSONObject userObject = allUsers.getJSONObject(i);
                    User user = new User();
                    user.setNickname(userObject.getString("nickname"));
                    user.setUserID(userObject.getString("userID"));
                    user.setPassword(userObject.getString("password"));
                    Log.e("test", user.getUserID());
                    Log.e("test", user.getPassword());
                    Log.e("test", user.getNickname());
                    user.save();
                }
                return true;
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }


    //返回Json数据的message值
    public static String checkMessage(String response)
    {
        try
        {
            JSONObject dataObject = new JSONObject(response);
            return dataObject.getString("message");
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return "000";
    }

    //返回Json数据的errorType值
    public static String checkErrorType(String response)
    {
        try
        {
            JSONObject dataObject = new JSONObject(response);
            return dataObject.getString("errorType");
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return "000";
    }

    //返回Json数据的特定string值
    public static String checkString(String response, String string)
    {
        try
        {
            JSONObject dataObject = new JSONObject(response);
            return dataObject.getString(string);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return "000";
    }

    public static boolean searchUser(String response)
    {
        if (!TextUtils.isEmpty(response))
        {
            try
            {
                JSONArray allUsers = new JSONArray(response);
                for (int i = 0; i < allUsers.length(); i++)
                {
                    JSONObject userObject = allUsers.getJSONObject(i);
                    User user = new User();
                    user.setNickname(userObject.getString("nickname"));
                    user.setUserID(userObject.getString("userID"));
                    user.setPassword(userObject.getString("password"));
                    Log.e("test", user.getUserID());
                    Log.e("test", user.getPassword());
                    Log.e("test", user.getNickname());
                    user.save();
                }
                return true;
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    //refresh more
    public static List<Article> handleArticleList(String response)
    {
        if (!TextUtils.isEmpty(response))
        {
            try
            {
                JSONObject dataObject = new JSONObject(response);
                JSONArray jsonArray = dataObject.getJSONArray("articles");
                String itemsJson = jsonArray.toString();
                return new Gson().fromJson(itemsJson, new TypeToken<List<Article>>() {}.getType());
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }
}
