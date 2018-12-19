package com.example.qichaoqun.myapplication;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author qichaoqun
 * @create 2018/12/19
 * @Describe
 */
public class MainActivity extends AppCompatActivity {

    public static final int RESPONSE_CODE = 200;
    public static final int WHAT = 100;
    private ListView mMusicListView;
    /**
     * 创建handler 用于更新ui
     */
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(WHAT == msg.what){
                //如果刚才放松的内容就去更新ui
                ArrayList<Music> musics = (ArrayList<Music>) msg.obj;
                mMusicListView.setAdapter(new MusicAdapter(MainActivity.this,musics));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件
        mMusicListView = findViewById(R.id.music_list_view);
        //加载数
        loadData();
    }

    private void loadData() {
        //音乐路径
        final String path = "http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&calback=&from=webapp_music&method=baidu.ting.billboard.billList&type=1&size=10&offset=0";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(path);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setRequestMethod("GET");
                    int responseCode = connection.getResponseCode();
                    if(RESPONSE_CODE == responseCode){
                        //已经访问成功
                        InputStream inputStream = connection.getInputStream();
                        //将字节流转换为字符流
                        InputStreamReader reader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(reader);
                        String line = null;
                        StringBuilder content = new StringBuilder();
                        while((line = bufferedReader.readLine()) != null){
                            content.append(line);
                        }
                        Log.i("获取到的数据为：：：", "run:"+content);
                        getMusicList(content);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void getMusicList(StringBuilder content){
                try {
                    JSONObject jsonObject = new JSONObject(content.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("song_list");
                    ArrayList<Music> musicList = new ArrayList<>();
                    for (int i = 0;i < jsonArray.length();i++){
                        JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                        Music music = new Music();
                        String title = jsonObject1.getString("title");
                        music.setTitle(title);
                        String singer = jsonObject1.getString("author");
                        music.setSinger(singer);
                        String image = jsonObject1.getString("album_500_500");
                        music.setImage(image);
                        musicList.add(music);
                    }
                    Log.i("集合的长度为：：：", "getMusicList: "+musicList.size());
                    //发消息给handler 让其更新更新ui
                    Message message = Message.obtain();
                    message.obj = musicList;
                    message.what = WHAT;
                    mHandler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
