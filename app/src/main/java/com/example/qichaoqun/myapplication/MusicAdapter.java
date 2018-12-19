package com.example.qichaoqun.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
 * @date 2018/12/19
 * 创建music的适配器 用于视图和数据相结合
 */
public class MusicAdapter extends BaseAdapter {

    public static final int RESPONSE_CODE = 200;
    private Context mContext = null;
    private ArrayList<Music> mList = null;

    public MusicAdapter(Context context,ArrayList<Music> list){
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(mContext,R.layout.music_item,null);
        ImageView imageView = convertView.findViewById(R.id.image);
        TextView music_name = convertView.findViewById(R.id.title);
        TextView music_singer = convertView.findViewById(R.id.singer);

        getImage(mList.get(position).getImage(),imageView);
        music_name.setText(mList.get(position).getTitle());
        music_singer.setText(mList.get(position).getSinger());
        return convertView;
    }

    private void getImage(final String path, final ImageView imageView){
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
                       InputStream inputStream = connection.getInputStream();
                       final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                       imageView.post(new Runnable() {
                           @Override
                           public void run() {
                               imageView.setImageBitmap(bitmap);
                           }
                       });
                   }
               } catch (MalformedURLException e) {
                   e.printStackTrace();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }).start();

    }

}
