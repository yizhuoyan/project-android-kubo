package com.liuyongmei.kubo.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.liuyongmei.kubo.MyApplication;
import com.liuyongmei.kubo.R;
import com.liuyongmei.kubo.common.ToastUtils;
import com.liuyongmei.kubo.controller.dialog.SpectrumDataDialog;
import com.liuyongmei.kubo.model.AppService;
import com.liuyongmei.kubo.model.SyncMessageListener;
import com.liuyongmei.kubo.model.datamodel.KuboData;
import com.liuyongmei.kubo.model.datamodel.SpectrumKuboData;
import com.liuyongmei.kubo.model.datamodel.SyncMessage;
import com.liuyongmei.kubo.view.PortDetailBaseView;
import com.liuyongmei.kubo.view.PortDetailProgressView;
import com.liuyongmei.kubo.view.PortListView;
import com.liuyongmei.kubo.view.PortSpectrumChartView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener,SyncMessageListener {
    public static final String SWITCH_LOGIN_TAG="switch-login";
    private PortListView portListView;
    private PortSpectrumChartView portChartView;
    private PortDetailProgressView portDetailProgressView;
    private PortDetailBaseView portDetailBaseView;
    private TextView currentIpView;
    private Button tBtn;
    private int currentPort;
    private SpectrumDataDialog spectrumDataDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initNavigationView();
        this.initContentView();
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
        View headView=navigationView.getHeaderView(0);
        currentIpView= (TextView) headView.findViewById(R.id.current_ip_view);
    }

    private void initContentView(){
        //设置当前ip
        currentIpView.setText(AppService.getInstance().currentIp);
        portListView= (PortListView) findViewById(R.id.port_list);
        portChartView = (PortSpectrumChartView) findViewById(R.id.chart);
        portDetailProgressView =(PortDetailProgressView) findViewById(R.id.port_detail_progress);
        portDetailBaseView =(PortDetailBaseView) findViewById(R.id.port_detail_base);
        tBtn= (Button) findViewById(R.id.chart_t_btn);
        tBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //发送获取谱图数据请求
        AppService.getInstance().addReceiveDataListener(KuboData.PORTS_SPECTRUM, this.portChartView);
        AppService.getInstance().addReceiveDataListener(KuboData.PORTS_SPECTRUM, this.portListView);
        AppService.getInstance().addReceiveDataListener(KuboData.PORTS_SPECTRUM, this);

        //对分析进度数据感兴趣
        AppService.getInstance().addReceiveDataListener(KuboData.PORT_ANALYZE_PROGRESS,this.portDetailProgressView);
        AppService.getInstance().addReceiveDataListener(KuboData.PORT_PARAMETER,this.portDetailBaseView);


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            AppService.getInstance().sendSpectrumData();
            //发送分析进度请求一次,然后等待推送
            AppService.getInstance().sendProgressCommand();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //发送获取谱图数据请求
        AppService.getInstance().removeReceiveDataListener(KuboData.PORTS_SPECTRUM, this.portChartView);
        AppService.getInstance().removeReceiveDataListener(KuboData.PORTS_SPECTRUM, this.portListView);
        //对分析进度数据感兴趣
        AppService.getInstance().removeReceiveDataListener(KuboData.PORT_ANALYZE_PROGRESS,this.portDetailProgressView);
        AppService.getInstance().removeReceiveDataListener(KuboData.PORT_PARAMETER,this.portDetailBaseView);

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.d("xxx","onSaveInstanceState");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.getApplication().onTerminate();
    }

    public void switchView(int port){
        currentPort=port;
        //切换图表
        portChartView.switchView(port);
        //改变标题
        this.setTitle("端口"+(port+1));
        //切换详情
        portDetailBaseView.switchView(port);
        //切换进度
        portDetailProgressView.switchView(port);
    }
    private long lastBackPressedTime;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //两次退出
            if(System.currentTimeMillis()-lastBackPressedTime<500){
                super.onBackPressed();
            }else {
                ToastUtils.shortShow(this,"双击退出");
                lastBackPressedTime=System.currentTimeMillis();
            }

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_gas_control:
                showGasControlActivity();
                break;

        }
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
            case R.id.nav_gas_control:
                showGasControlActivity();
                break;
            case R.id.nav_disconnection://退出
                this.finish();
                break;
        }
        return true;
    }
    private void showGasControlActivity(){
        startActivity(new Intent(this,GasControlActivity.class));
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent!=null){
            //如果是关闭应用标记，则销毁
            if(intent.getBooleanExtra(MyApplication.EXIT,false)){
                //手动触发application的终止方法，统一出口
                this.getApplication().onTerminate();
                this.finish();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chart_t_btn:{
                if(spectrumDataDialog==null){
                    spectrumDataDialog=new SpectrumDataDialog(this);
                }
                spectrumDataDialog.show(spectDatas.get(currentPort));
                break;
            }
        }
    }
    private List<SpectrumKuboData> spectDatas=new ArrayList<>(8);
    @Override
    public void onReceive(SyncMessage message) {
        if(message instanceof SpectrumKuboData){
            SpectrumKuboData data=(SpectrumKuboData)message;
            int port=data.port;
            if(spectDatas.size()>port){
                spectDatas.set(port,data);
            }
            spectDatas.add(data);
        }
    }
}
