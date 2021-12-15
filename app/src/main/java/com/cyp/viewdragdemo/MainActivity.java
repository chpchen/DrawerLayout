package com.cyp.viewdragdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cyp.viewdragdemo.app.DrawerLayout;

public class MainActivity extends AppCompatActivity implements DrawerLayout.OnCloseListener {

    private RightMenuFragment mLeftMenuFragment;
    private Button openDrawerLayout_btn;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        mLeftMenuFragment = (RightMenuFragment) fm.findFragmentById(R.id.id_container_menu);
        if (mLeftMenuFragment == null) {
            fm.beginTransaction().add(R.id.id_container_menu, mLeftMenuFragment = new RightMenuFragment())
                    .commit();
        }

        TextView id_content_tv = (TextView) findViewById(R.id.id_content_tv);
        openDrawerLayout_btn = (Button) findViewById(R.id.id_content_btn);

        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.vdhtest);
        drawerLayout.setOnCloseListener(this);
        if (id_content_tv != null) {
            id_content_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer();
                    Log.i("ddd","ddd");
                }
            });
        }
        if (openDrawerLayout_btn != null) {
            openDrawerLayout_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!drawerLayout.isShown()) {
                        drawerLayout.openDrawer();
                        openDrawerLayout_btn.setText("关闭");
                    } else {
                        drawerLayout.closeDrawer();
                        openDrawerLayout_btn.setText("打开");
                    }

                }
            });
        }
    }

    @Override
    public void onClose() {
        //侧滑关闭回调事件
        openDrawerLayout_btn.setText("打开");
    }
}
