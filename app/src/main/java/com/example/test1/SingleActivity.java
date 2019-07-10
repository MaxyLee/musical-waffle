package com.example.test1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class SingleActivity extends AppCompatActivity {

    Button btnMusic;

    private ViewPager vp;
    private PagerTabStrip pagerTabStrip;
    ArrayList<View> views = new ArrayList<>();

    String[] titles = {"CroatianRhapsody","大碗宽面","Unk"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        vp = findViewById(R.id.vp);
        pagerTabStrip = findViewById(R.id.tap);
        initView();
        vp.setAdapter(new MyAdapter());
        pagerTabStrip.setTabIndicatorColor(0xffc17b41);
        pagerTabStrip.setTextColor(0xffc17b41);
    }

    private void initView(){
        View v1 = getLayoutInflater().inflate(R.layout.layout1,null);
        View v2 = getLayoutInflater().inflate(R.layout.layout2,null);
        View v3 = getLayoutInflater().inflate(R.layout.layout3,null);
//        views.add(v3);
        views.add(v1);
        views.add(v2);
        views.add(v3);
//        views.add(v1);
    }




    class MyAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(views.get(position));
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View v = views.get(position);
            container.addView(v);

            switch (position){
                case 0:
                    btnMusic = findViewById(R.id.muCR);
                    btnMusic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d("fuck","clickcrrrr");
                            Intent intent = new Intent(SingleActivity.this, GameActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("musicName", "CroatianRhapsody");
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
            }
            return v;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

//        @Override
//        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){
//
//        }
    }
}
