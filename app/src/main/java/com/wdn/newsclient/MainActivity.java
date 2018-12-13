package com.wdn.newsclient;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.wdn.newsclient.domain.NewsItem;
import com.wdn.newsclient.service.NewsInfoParser;
import com.wdn.newsclient.view.SmartImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int          LOAD_ERROR   =2 ;
    private static final int          LOAD_SUCCESS =1 ;
    private              LinearLayout loading;
    private              ListView     lv_news;

    private Handler        handler      =new Handler(){
         @Override
         public void handleMessage(Message msg) {
             loading.setVisibility(View.INVISIBLE);
             switch (msg.what){
                 case LOAD_SUCCESS:
                     Toast.makeText(MainActivity.this, "信息获取成功", Toast.LENGTH_SHORT).show();
                   lv_news.setAdapter(new MyNewsAdapter());
                     break;
                 case LOAD_ERROR:
                     Toast.makeText(MainActivity.this, "获取新闻信息失败", Toast.LENGTH_SHORT).show();
                     break;
                 default:
                  break;
             }
         }
     };
    private List<NewsItem> newsItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       //1.初始化界面
        initView();
        loadNewsInfo();
    }
    /**
     * 获取新闻信息
     */
    private void loadNewsInfo() {
 new Thread(){
     @Override
     public void run() {
         try {
             Thread.sleep(5000);//延时效果
             System.out.println("sa");
             URL url=new URL("http://192.168.1.6:8081/news.xml");
             HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
             httpURLConnection.setRequestMethod("GET");//大写

             //设置一个读取超时的时间
             httpURLConnection.setReadTimeout(20000);
             System.out.println("sajh");
             int code=httpURLConnection.getResponseCode();
             if (code==200){
                 //请求成功
                 InputStream is=httpURLConnection.getInputStream();
                 //要将流改为实体对象
                 newsItems = NewsInfoParser.getAllNewsInfos(is);
              //更新界面，把新闻显示到ui上
                 Message message=Message.obtain();
                 message.what=LOAD_SUCCESS;
                 handler.sendMessage(message);
             }else {
                 //请求失败
                 Message msg = Message.obtain();
             msg.what=LOAD_ERROR;
             handler.sendMessage(msg);
             }
         }catch (Exception e){
             e.printStackTrace();
             //服务器访问不成功也会发送异常
             Message msg = Message.obtain();
             msg.what=LOAD_ERROR;
             handler.sendMessage(msg);
         }
     }
     }.start();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        setContentView(R.layout.activity_main);
        loading = findViewById(R.id.loading);
        lv_news = findViewById(R.id.dynamic);
        loading.setVisibility(View.VISIBLE);
    }

    private class MyNewsAdapter extends BaseAdapter {

        private TextView tv_title;
        private TextView desc;
        private TextView type;
        private NewsItem newsitem;

        @Override
        public int getCount() {
            return newsItems.size();
        }

        @Override
        public NewsItem getItem(int position) {
            return newsItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //LayoutInflate 布局打气筒，把xml资源文件转成view
            View view = View.inflate(MainActivity.this, R.layout.news_item, null);

            System.out.println("----------------iv------------------");
            SmartImageView iv = view.findViewById(R.id.iv_image);
            tv_title = view.findViewById(R.id.tv_title);
            desc = view.findViewById(R.id.tv_desc);
            type = view.findViewById(R.id.tv_type);
            newsitem = getItem(position);
            //异步操作，创建子线程去加载图片
           iv.setImageUrlAndShow(String.valueOf(newsitem.getImagePath()));
            System.out.println(String.valueOf(newsitem.getImagePath()));
           tv_title.setText(String.valueOf(newsitem.getTitle()));
           desc.setText(String.valueOf(newsitem.getDesc()));
            String type1 = newsitem.getType();
            if (type1.equals("1")){
                type.setText("评论："+newsitem.getCommentCount());
                type.setBackgroundColor(Color.TRANSPARENT);
                type.setTextColor(Color.BLACK);
            }else if(type1.equals("2")){
                type.setText("专题："+newsitem.getCommentCount());
                type.setBackgroundColor(Color.RED);
                type.setTextColor(Color.WHITE);
            }else if(type1.equals("3")){
                type.setText("直播："+newsitem.getCommentCount());
                type.setBackgroundColor(Color.BLUE);
                type.setTextColor(Color.WHITE);
            }
            return view;

        }

    }
}
