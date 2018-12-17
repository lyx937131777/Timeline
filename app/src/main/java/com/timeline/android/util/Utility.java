package com.timeline.android.util;

import android.text.TextUtils;
import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

    //注册界面
    public static boolean storeLoginUser(String response)
    {
        if (!TextUtils.isEmpty(response))
        {
            try
            {
                LogUtil.e("Utility", response);
                JSONObject dataObject = new JSONObject(response);
                LogUtil.e("Utility", "111111111111111111");
                if (dataObject.getString("code").equals("true"))
                {
                    LogUtil.e("Utility", "2222222222222222222222222222");
                    JSONObject userObject = dataObject.getJSONObject("data");
                    LogUtil.e("Utility", userObject.getString("userID"));
                    LogUtil.e("Utility", userObject.getString("password"));
                    LogUtil.e("Utility", userObject.getString("nickname"));
                    User user = new User();
                    user.setNickname(userObject.getString("nickname"));
                    user.setUserID(userObject.getString("userID"));
                    user.setPassword(userObject.getString("password"));
                    DataSupport.deleteAll(User.class, "userID = ?", user.getUserID());
                    user.save();
                    return true;
                }
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    //返回Json数据的code值
    public static String checkCode(String response)
    {
        try
        {
            JSONObject dataObject = new JSONObject(response);
            return dataObject.getString("code");
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

    public static ArrayList<User> storeUserResponse(String response)
    {
        if (!TextUtils.isEmpty(response))
        {
            try
            {
                ArrayList<User> users = new ArrayList<>();
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
                    DataSupport.deleteAll(User.class, "userID = ?", user.getUserID());
                    user.save();
                    users.add(user);
                }
                return users;
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        ArrayList<User> users2 = new ArrayList<>();
        return users2;
    }
}
