package com.bloodmatch.bloodlink;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

public class Onboarding_Adapter extends PagerAdapter {


    private Context context;
    private LayoutInflater layoutInflater;

    public Onboarding_Adapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }
    private int titles[]={
            R.string.title1,
            R.string.title2,
            R.string.title3,
            R.string.title4
    };
    private int description[]={
            R.string.description1,
            R.string.description2,
            R.string.description3,
            R.string.description4
    };
    private int images[]={
            R.drawable.hemo,
            R.drawable.hemo10,
            R.drawable.hemo5,
            R.drawable.hem
    };

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view== (ConstraintLayout) object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
    View view= layoutInflater.inflate(R.layout.page,container,false);
        ImageView imageView= view.findViewById(R.id.image);
        TextView titleView= view.findViewById(R.id.title);
        TextView descriptionView=view.findViewById(R.id.description);
        imageView.setImageResource(images[position]);
        titleView.setText(titles[position]);
        descriptionView.setText(description[position]);

        container.addView(view);
    return  view;
    }
}
