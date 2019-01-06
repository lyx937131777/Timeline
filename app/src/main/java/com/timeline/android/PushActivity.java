package com.timeline.android;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;


import com.bumptech.glide.Glide;
import com.timeline.android.dagger2.DaggerMyComponent;
import com.timeline.android.dagger2.MyComponent;
import com.timeline.android.dagger2.MyModule;
import com.timeline.android.db.Article;
import com.timeline.android.presenter.PushPresenter;
import com.timeline.android.util.HttpUtil;
import com.timeline.android.util.LogUtil;
import com.timeline.android.util.Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PushActivity extends AppCompatActivity
{
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
//    14197045
    //view
    private ImageView imageView;
    private EditText contentText;

    //photo
    private Dialog dialog;
    private Uri imageUri;
    private String imagePath = null;

    //message
    private Article article;
    private String type;

    private PushPresenter pushPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        imageView = findViewById(R.id.img);
        contentText = findViewById(R.id.content);

        type = getIntent().getStringExtra("type");
        if(type != null)
        {
            if(type.equals("edit"))
            {
                article = (Article)getIntent().getSerializableExtra("article");
                contentText.setText(article.getContent());
                Glide.with(this).load(article.getImageURL()).into(imageView);
                if(article.getImageURL().equals("empty"))
                {
                    Glide.with(this).load(R.drawable.temp).into(imageView);
                }
            }else
            {
                article = new Article(0,"","","","empty",new Date());
            }
        } else
        {
            new AlertDialog.Builder(PushActivity.this)
                    .setTitle("错误")
                    .setMessage("系统出错")
                    .setPositiveButton("确定", new
                            DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface
                                                            dialog,
                                                    int which)
                                {
                                    Intent intent = new Intent();
                                    setResult(RESULT_CANCELED, intent);
                                    finish();
                                }
                            })
                    .show();
        }
        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(this)).build();
        pushPresenter = myComponent.pushPresenter();

        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.show();
            }
        });

        init_dialog();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.commit_menu, menu);
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
            case R.id.commit:
                pushPresenter.setContentAndImagepath(contentText.getText().toString(),imagePath);
                pushPresenter.commit();
                break;
        }
        return true;
    }


    private void init_dialog()
    {
        dialog = new Dialog(this, R.style.AppTheme);
        View view = View.inflate(this, R.layout.dialog_bottom, null);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        //view.setMinimumHeight((int) (ScreenSizeUtils.getInstance(this).getScreenHeight() *
        // 0.23f));
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //lp.width = (int) (ScreenSizeUtils.getInstance(this).getScreenWidth() * 0.9f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        dialogWindow.setAttributes(lp);
        Button bt_dialog_camera = (Button) view.findViewById(R.id.bt_dialog_camera);
        Button bt_dialog_album = (Button) view.findViewById(R.id.bt_dialog_album);
        Button bt_dialog_cancel = (Button) view.findViewById(R.id.bt_dialog_cancel);
        bt_dialog_camera.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.e("EditUserInfo_Dialog", "click camera");

                // 创建File对象，用于存储拍照后的图片
                File outputImage = new File(getExternalCacheDir(), "profile_image.jpg");
                imagePath = outputImage.getAbsolutePath();
                try
                {
                    if (outputImage.exists())
                    {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT < 24)
                {
                    imageUri = Uri.fromFile(outputImage);
                } else
                {
                    imageUri = FileProvider.getUriForFile(PushActivity.this,
                            "com.timeline.android.fileprovider", outputImage);
                }
                // 启动相机程序
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);
                dialog.cancel();
            }
        });

        bt_dialog_album.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.e("EditUserInfo_Dialog", "click album");

                if (ContextCompat.checkSelfPermission(PushActivity.this, Manifest
                        .permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(PushActivity.this, new
                            String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else
                {
                    openAlbum();
                }
                dialog.cancel();
            }

        });
        bt_dialog_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.e("EditUserInfo_Dialog", "click cancel");
                dialog.cancel();
            }
        });
    }


    // Camera and Album
    private void openAlbum()
    {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults)
    {
        switch (requestCode)
        {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    openAlbum();
                } else
                {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK)
                {
                    try
                    {
                        // 将拍摄的照片显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
                                .openInputStream(imageUri));
//                        Log.e("camera", getContentResolver().openInputStream(imageUri).toString());
//                        Log.e("camera", "imageUri:" + imageUri.toString());
//                        Log.e("camera","imagePath:"+getImagePath(imageUri, null).toString());
                        imageView.setImageBitmap(bitmap);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK)
                {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19)
                    {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else
                    {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data)
    {
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri))
        {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority()))
            {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority()))
            {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse
                        ("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme()))
        {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme()))
        {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data)
    {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection)
    {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath)
    {
        Log.e("album", "imagePath:" + imagePath);
        if (imagePath != null)
        {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(bitmap);
        } else
        {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    public PushPresenter getPushPresenter()
    {
        return pushPresenter;
    }

    public void setPushPresenter(PushPresenter pushPresenter)
    {
        this.pushPresenter = pushPresenter;
    }

    public Dialog getDialog()
    {
        return dialog;
    }
}
