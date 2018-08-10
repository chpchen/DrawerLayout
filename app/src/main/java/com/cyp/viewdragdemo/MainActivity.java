package com.cyp.viewdragdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cyp.viewdragdemo.app.DrawerLayout;

public class MainActivity extends AppCompatActivity {

    private LeftMenuFragment mLeftMenuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        mLeftMenuFragment = (LeftMenuFragment) fm.findFragmentById(R.id.id_container_menu);
        if (mLeftMenuFragment == null) {
            fm.beginTransaction().add(R.id.id_container_menu, mLeftMenuFragment = new LeftMenuFragment())
                    .commit();
        }

        TextView id_content_tv = (TextView) findViewById(R.id.id_content_tv);
        final Button id_content_btn = (Button) findViewById(R.id.id_content_btn);

        final DrawerLayout vdhtest = (DrawerLayout) findViewById(R.id.vdhtest);
        if (id_content_tv != null) {
            id_content_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (!vdhtest.isShown()) {
                    vdhtest.openDrawer();
//                    }
                }
            });
        }
        if (id_content_btn != null) {
            id_content_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!vdhtest.isShown()) {
                        vdhtest.openDrawer();
                        id_content_btn.setText("关闭");
                    } else {
                        vdhtest.closeDrawer();
                        id_content_btn.setText("打开");
                    }

                }
            });
        }
    }
}
