package com.bloodmatch.bloodlink;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {
   private ViewPager viewPager;
   private CardView cardView;
   private LinearLayout dotsLayout;

   private TextView[] dots;
   private TextView skip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        skip= findViewById(R.id.skipTextView);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        cardView = findViewById(R.id.nextCard);
        dotsLayout = findViewById(R.id.dotsLayout);
        Onboarding_Adapter adapter = new Onboarding_Adapter(this);
        viewPager.setAdapter(adapter);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity2.this, MainActivity3.class));
            }
        });
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1,true);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                dotsFunction(position);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position <3) {
                        viewPager.setCurrentItem(position+1,true);

                    }
                   else {
                        startActivity(new Intent(MainActivity2.this, MainActivity3.class));
                        finish();
                    }
                }
            });
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        dotsFunction(0);

    }

    private void dotsFunction(int pos) {
        dots = new TextView[4];
        dotsLayout.removeAllViews();

        for (int i=0; i<dots.length;i++ ){
            dots[i]= new TextView(this);
            dots[i].setText(Html.fromHtml("_"));
            dots[i].setTextColor(getColor(R.color.black));
            dots[i].setTextSize(40);
            
            dotsLayout.addView(dots[i]);
        }
        if (dots.length>0) {
            dots[pos].setTextColor(getColor(R.color.red));
            dots[pos].setTextSize(40);
            
        }

    }
}