package org.localareadelivery.distributorapp;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class Intro extends IntroActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_entry_point);


        /**
         * Standard slide (like Google's intros)
         */
        addSlide(new SimpleSlide.Builder()
                .title(R.string.title_activity_shop_home)
                .description(R.string.title_activity_add_item)
                .image(R.drawable.carrots)
                .background(R.color.blueGrey800)
                .backgroundDark(R.color.darkGreen)
                .permission(Manifest.permission.CAMERA)
                .build());

    }
}
