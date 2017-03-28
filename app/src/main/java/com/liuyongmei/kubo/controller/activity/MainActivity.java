package com.liuyongmei.kubo.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.liuyongmei.kubo.MyApplication;
import com.liuyongmei.kubo.R;
import com.liuyongmei.kubo.model.AppService;
import com.liuyongmei.kubo.view.PortDetailView;
import com.liuyongmei.kubo.view.PortListView;
import com.liuyongmei.kubo.view.PortSpectrumChartView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String SWITCH_LOGIN_TAG="switch-login";
    private PortListView portListView;
    private PortSpectrumChartView chartView;
    private PortDetailView portDetailView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initNavigationView();
        this.initContentView();
        this.init();
    }


    private void initNavigationView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //use the toolbar
        setSupportActionBar(toolbar);
        //hide the title
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        //get the drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //add a listener on the drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this
                ,drawer
                ,toolbar
                ,R.string.navigation_drawer_open
                ,R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawer.addDrawerListener(toggle);

        toggle.syncState();
        //add drawer item listener
        NavigationView navigationView = (NavigationView) findViewById(R.id.drawer_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initContentView(){
        portListView= (PortListView) findViewById(R.id.port_list);
        chartView= (PortSpectrumChartView) findViewById(R.id.chart);
        portDetailView= (PortDetailView) findViewById(R.id.port_detail);

    }
    private void init(){
        //发送获取谱图数据请求
        AppService.getInstance().sendSpectrumData();
    }
    public void switchView(int port){
        //切换图标
        chartView.switchView(port);
        this.setTitle("端口"+port);
        //portDetailView.
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //关闭抽屉
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        switch (item.getItemId()){
            case R.id.nav_switch_login:
                //切换登录
                Intent intent=new Intent(this,LoginActivity.class);
                intent.putExtra(SWITCH_LOGIN_TAG,true);
                this.startActivity(intent);
                break;
        }
        return true;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent!=null){
            //如果是关闭标记，则销毁
            if(intent.getBooleanExtra(MyApplication.EXIT,false)){
                this.finish();
            }
        }
    }
}
