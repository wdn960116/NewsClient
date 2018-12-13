package com.wdn.newsclient.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class SmartImageView extends ImageView {

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bitmap bitmap = (Bitmap) msg.obj;
            setImageBitmap(bitmap);
        }
    };

    public SmartImageView(Context context) {
        super(context);
    }

    public SmartImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置一个网络图片并显示到控件
     *
     * @param imageUrl
     */
    public void setImageUrlAndShow(final String imageUrl) {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(imageUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    InputStream is = conn.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    Message msg = Message.obtain();
                    msg.what = 0;
                    msg.obj = bitmap;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
