package com.example.test1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class GameActivity extends AppCompatActivity {

    View mView;
    //draw notes
    private Paint notePaint;
    //draw checkCircle
    private Paint nPaint;
    private Paint lPaint;
    LinearLayout gameLayout;
    Music music;
    //count notes that have appeared
    public int cnt;
    //notes fall
    public Thread mThread;
    public Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initData();
        initView();
        gameStart();
    }

    @SuppressLint("HandlerLeak")
    public void initData(){
        //init count
        cnt = 0;
        //init Paints
        notePaint = new Paint();
        notePaint.setColor(0xff666666);
        notePaint.setAntiAlias(true);
        nPaint = new Paint();
        nPaint.setARGB(100,246,198,123);
        nPaint.setAntiAlias(true);
        nPaint.setStyle(Paint.Style.STROKE);
        nPaint.setStrokeWidth(5);
        lPaint = new Paint();
        lPaint.setARGB(100,246,198,123);
        lPaint.setAntiAlias(true);
        lPaint.setStrokeWidth(5);
        //init music
        music = new Music();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String musicName = bundle.getString("musicName","");
        try{
            InputStream is = GameActivity.this.getClass().getClassLoader().getResourceAsStream("assets/"+"sheet.json");
            BufferedReader bufr = new BufferedReader((new InputStreamReader(is)));
            String line;
            StringBuilder builder = new StringBuilder();
            while((line = bufr.readLine())!=null){
                builder.append(line);
            }
            is.close();
            bufr.close();
            try{
                JSONObject json = new JSONObject(builder.toString());
                JSONArray notes = json.getJSONArray(musicName);
                music.appearTime = new int[notes.length()];
                music.state = new int[notes.length()];
                for(int i=0;i<notes.length();i++)
                    music.appearTime[i] = (int) notes.get(i);
            } catch (JSONException e){
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        handler = new Handler(){
            public void handleMessage(android.os.Message msg){
                mView.invalidate();
            };
        };
    }

    public void initView(){
        gameLayout = findViewById(R.id.gameLayout);
        mView = new View(this){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                canvas.drawCircle(mView.getWidth()/2,mView.getHeight()-100,60,nPaint);
                canvas.drawCircle(mView.getWidth()/2,mView.getHeight()-100,50,lPaint);
                for(int i=0;i<music.appearTime.length;i++){
                    if(music.state[i]==1){
                        canvas.drawOval(mView.getWidth()/2-50,music.position[i],mView.getWidth()/2+50,music.position[i]+100,notePaint);
                    }
                }
            }
        };
        mView.setBackgroundColor(0xff000000);
        gameLayout.addView(mView);
        gameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cnt<music.state.length&&music.state[cnt]==1){
                    if(music.position[cnt]>mView.getHeight()-210&&music.position[cnt]<mView.getHeight()-40){
                        music.state[cnt] = 2;
                        cnt++;
                    }
                }
            }
        });
    }

    public void gameStart(){
        if(mThread==null){
            mThread = new Thread(){
                @Override
                public void run() {
                    super.run();
                    long startTime =  System.currentTimeMillis();
                    long time;
                    while(true){
                        try{
                            sleep(10);
                        }catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        time = System.currentTimeMillis();
                        for(int i=cnt;i<music.appearTime.length;i++){
                            if(music.state[i]!=2&&(time-startTime)>=music.appearTime[i]&&(time-startTime)<(music.appearTime[i]+mView.getHeight()/music.velocity)*4){
                                music.state[i] = 1;
                                music.position[i] = (time-startTime-music.appearTime[i])*music.velocity/4;
                            }
                            else if(music.state[i]==2||(time-startTime)>(music.appearTime[i]+mView.getHeight()/music.velocity)*4){
                                music.state[i] = 2;
                                if(cnt<i+1)
                                    cnt=i+1;
                            }
                        }
                        handler.sendEmptyMessage(0);
                    }
                }
            };
            mThread.start();
        }
    }

}
