package cn.hwwwwh.taoxiang.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.mob.ums.OperationCallback;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;
import com.qiangxi.checkupdatelibrary.bean.CheckUpdateInfo;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import com.wyt.searchbox.custom.IOnSearchClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.hwwwwh.taoxiang.CoustomView.CircleImageView;
import cn.hwwwwh.taoxiang.CoustomView.ForceUpdateDialog;
import cn.hwwwwh.taoxiang.CoustomView.PriceEditText;
import cn.hwwwwh.taoxiang.CoustomView.RecyclerViewScrollDetector;
import cn.hwwwwh.taoxiang.CoustomView.SearchFragment;
import cn.hwwwwh.taoxiang.CoustomView.SpacesItemDecoration;
import cn.hwwwwh.taoxiang.CoustomView.dorpmenu.MyDropDownMenu;
import cn.hwwwwh.taoxiang.R;
import cn.hwwwwh.taoxiang.adapter.QuanAdapter;
import cn.hwwwwh.taoxiang.api.ApiUrls;
import cn.hwwwwh.taoxiang.api.ApiUtils;
import cn.hwwwwh.taoxiang.base.BaseActivity;
import cn.hwwwwh.taoxiang.dagger.DaggerPresenterComponent;
import cn.hwwwwh.taoxiang.dagger.module.PresenterModule;

import cn.hwwwwh.taoxiang.manager.ActivityStackManager;
import cn.hwwwwh.taoxiang.model.bean.QuanBean;
import cn.hwwwwh.taoxiang.model.bean.QuanCryBean;
import cn.hwwwwh.taoxiang.model.bean.UpdateBean;
import cn.hwwwwh.taoxiang.presenter.DownloadQuanCryPre;
import cn.hwwwwh.taoxiang.presenter.DownloadQuanDataPre;
import cn.hwwwwh.taoxiang.utils.CleanMessageUtil;
import cn.hwwwwh.taoxiang.utils.ToastUtils;
import cn.hwwwwh.taoxiang.view.iface.IMainQuanCryVIew;
import cn.hwwwwh.taoxiang.view.iface.IMainQuanDataView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, IMainQuanCryVIew, IMainQuanDataView,View.OnClickListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.dropDownMenu)
    MyDropDownMenu dropDownMenu;
    @BindView(R.id.content_main)
    RelativeLayout contentMain;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    boolean isPermissionGrant;// 程序是否被允许持有写入权限

    @Inject
    public DownloadQuanCryPre downloadQuanCryPre;
    @Inject
    public DownloadQuanDataPre downloadQuanDataPre;

    QuanAdapter adapter;
    ArrayList<String> mDatas;
    View contentView;
    RecyclerView recyclerView;
    SmartRefreshLayout refreshLayout;
    TextView welcome;
    TextView login;
    CircleImageView headPic;
    ImageView toTop;
    PriceEditText lpEdt;
    PriceEditText mpEdt;
    ImageView noGood;
    Button btn;
    //0为默认首页，1为大牌推荐，2为每日必拍
    private int couponType =0;

    private CheckUpdateInfo mCheckUpdateInfo;
    private ForceUpdateDialog mForceUpdateDialog;

    private String headers[] = {"综合排序", "商品类别", "价格筛选"};
    private int[] types = new int[]{MyDropDownMenu.TYPE_LIST_CITY, MyDropDownMenu.TYPE_GRID, MyDropDownMenu.TYPE_CUSTOM};
    private String methods[] = {"综合排序", "按价格升序", "按价格降序", "按销量降序"};
    private String ages[] = {"更新时间", "最新更新", "综合排序"};
    private String constellations[] = {"全部类别"};
    public int page = 1;
    public String type = "0";
    public String category = "";
    public double lp = 0;
    public double mp = 100000000;
    public String keyword = "";
    Boolean isNeedUpdate = false;

    private AlibcShowParams alibcShowParams;//页面打开方式，默认，H5，Native
    private AlibcTaokeParams alibcTaokeParams = null;//淘客参数，包括pid，unionid，subPid
    private Map<String, String> exParams;//yhhpass参数
    private String shareText="最新版淘享下载地址:http://taoxiang.hwwwwh.cn/taoxiang.apk";
    private boolean wifi_switch;
    private boolean cache_switch;
    private boolean update_switch;
    private long exitTime = 0;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    //设置不支持滑动返回
    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    protected void init() {
        toolbar.setTitle("");
        toolbar.setTitleTextAppearance(this,R.style.ToolbarTitle);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        loadUpdate();

        contentView = getLayoutInflater().inflate(R.layout.contentview, null);
        dropDownMenu=(MyDropDownMenu) findViewById(R.id.dropDownMenu);
        //该监听回调只监听默认类型，如果是自定义view请自行设置监听，参照demo
        dropDownMenu.addMenuSelectListener(new MyDropDownMenu.OnDefultMenuSelectListener() {
            @Override
            public void onSelectDefaultMenu(int index, int pos, String clickstr) {
                //index:点击的tab索引，pos：单项菜单中点击的位置索引，clickstr：点击位置的字符串
                //排序
                if (index == 0) {
                    if (pos == 0) {
                        type = "0";
                    } else if (pos == 1) {
                        type = "1";
                    } else if (pos == 2) {
                        type = "2";
                    } else if (pos == 3) {
                        type = "3";
                    } else {
                        type = "0";
                    }
                    page = 1;
                    downloadTqgData();
                } else if (index == 1) {
                    if (pos == 0) {
                        category = "";
                    } else {
                        category = dealCryText(clickstr);
                    }
                    page = 1;
                    downloadTqgData();
                } else {

                }
            }
        });

        DaggerPresenterComponent.builder().presenterModule(new PresenterModule(this)).build().inject(this);
        dropDownMenu.setDropDownMenu(Arrays.asList(headers), initViewData(), contentView);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        toTop=(ImageView) findViewById(R.id.toTop);
        toTop.setOnClickListener(this);
        intiToTopBtn();
        noGood=(ImageView) findViewById(R.id.noGood);
        View headerView = navView.getHeaderView(0);
        welcome=(TextView)headerView.findViewById(R.id.welcome);
        login=(TextView)headerView.findViewById(R.id.login);
        headPic=(CircleImageView)headerView.findViewById(R.id.headPic);
        login.setOnClickListener(this);
        headPic.setOnClickListener(this);
        loadHeadBar();

        initRecycleView();
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        downloadTqgData();

        //设置 Header 为 Material风格
        refreshLayout.setRefreshHeader(new WaterDropHeader(this));
        //refreshLayout.setRefreshHeader(new FunGameHitBlockHeader(this));
        //refreshLayout.setPrimaryColors(Color.CYAN);
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                downloadTqgCryData();
                page = 1;
                recyclerView.smoothScrollToPosition(0);
                downloadTqgData();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page = page + 1;
                downloadTqgData();
            }
        });
    }

    @Override
    protected void initBundleData() {
         wifi_switch = getSharedPreferences().getBoolean("wifi_switch", true);
         cache_switch=getSharedPreferences().getBoolean("cache_switch",true);
         update_switch=getSharedPreferences().getBoolean("update_switch",true);
    }

    private void intiToTopBtn(){
        toTop.setVisibility(View.INVISIBLE);
        Glide.with(getApplicationContext()).load(R.drawable.topicon).into(toTop);
        recyclerView.addOnScrollListener(new RecyclerViewScrollDetector() {
            @Override
            public void onScrollUp() {
                if(toTop.getVisibility()==View.VISIBLE){
                    toTop.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onScrollDown() {
                if(toTop.getVisibility()!=View.VISIBLE){
                    toTop.setVisibility(View.VISIBLE);
                }
            }
            //以滑进顶部识别区1000
            @Override
            public void onScrollTop() {
                if(toTop.getVisibility()==View.VISIBLE){
                    toTop.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void setTitle(String title){
        toolbar.setTitle(title);
    }

    private void loadHeadBar(){
        if(!UMSSDK.getLoginUserId().isEmpty() &&!UMSSDK.getLoginUserToken().isEmpty()) {
            UMSSDK.getLoginUser(new OperationCallback<User>() {
                @Override
                public void onSuccess(User user) {
                    super.onSuccess(user);
                    Glide.with(MainActivity.this).load(user.avatar.get()[0]).into(headPic);
                    login.setText(user.nickname.get());
                }

                @Override
                public void onFailed(Throwable throwable) {
                    super.onFailed(throwable);
                    //Log.d("testtaoxiang","获取用户信息失败"+throwable.getMessage());
                    ToastUtils.showToast(getApplicationContext(), "获取用户信息失败");
                }
            });
        }else{
            Glide.with(getApplicationContext()).load(R.drawable.headpic).into(headPic);
            login.setText("用户登陆");
        }
    }

    private void loadUpdate(){
        ApiUtils.getTqgApi(ApiUrls.tqgApiUrl)
                .getUpdateInfo()
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UpdateBean>() {
                    Disposable d;
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        this.d =d;
                    }

                    @Override
                    public void onNext(@NonNull UpdateBean updateBean) {
                        if (getVersionCode(getApplicationContext())<updateBean.getUpdate_ver_code()) {
                            LinearLayout gallery = (LinearLayout) navView.getMenu().findItem(R.id.nav_setting).getActionView();
                            gallery.setVisibility(View.VISIBLE);
                            isNeedUpdate=true;
                            if(update_switch) {
                                mCheckUpdateInfo = new CheckUpdateInfo();
                                mCheckUpdateInfo.setAppName(updateBean.getUpdate_app_name())
                                        // .setIsForceUpdate(1)//设置是否强制更新,该方法的参数只要和服务端商定好什么数字代表强制更新即可
                                        .setNewAppReleaseTime(updateBean.getUpdate_Time())//软件发布时间
                                        // .setNewAppSize(12.3f)//单位为M
                                        .setNewAppSize(Float.parseFloat(updateBean.getUpdate_app_size()))
                                        .setNewAppUrl(updateBean.getUpdate_url())
                                        .setNewAppVersionCode(updateBean.getUpdate_ver_code())//新app的VersionCode
                                        .setNewAppVersionName(updateBean.getUpdate_ver_name())
                                        .setNewAppUpdateDesc(updateBean.getUpdate_content());
                                forceUpdateDialogClick(updateBean.isIgnore_able());
                            }
                        }
                        if(!updateBean.getShare_text().isEmpty()) {
                            shareText = updateBean.getShare_text();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        onComplete();
                    }

                    @Override
                    public void onComplete() {
                        d.dispose();
                    }
                });
    }

    public int getVersionCode(Context context){
        PackageManager packageManager=context.getPackageManager();
        PackageInfo packageInfo;
        int versionCode = 0;
        try {
            packageInfo=packageManager.getPackageInfo(context.getPackageName(),0);
            versionCode=packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    private void initRecycleView() {
        adapter = new QuanAdapter(this);
        adapter.setOnItemClickListener(new QuanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String couponLink,String id) {
                showUrl(couponLink,id);
            }

            @Override
            public void OnItemLongClick(View view, int position) {
                //ToastUtils.showToast(MainActivity.this,"ss");
            }
        });
        //必须指定adaoter
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //必须指定layoutmanager
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        //设置item之间的间隔
        SpacesItemDecoration decoration = new SpacesItemDecoration(4);
        recyclerView.addItemDecoration(decoration);

    }

    public void showUrl(String couponLink,String id) {

       /* alibcTaokeParams = new AlibcTaokeParams(); // 若非淘客taokeParams设置为null即可
        alibcTaokeParams.adzoneid = "136690366";
        alibcTaokeParams.pid = "mm_56584098_37534934_136690366";
        // alibcTaokeParams.subPid = "mm_56584098_37534934_136690366";
        alibcTaokeParams.extraParams = new HashMap<>();
        alibcTaokeParams.extraParams.put("taokeAppkey", "24642765");
        alibcShowParams = new AlibcShowParams(OpenType.H5, false);
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(MainActivity.this, "URL为空",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        AlibcTrade.show(this, new AlibcPage(url), alibcShowParams, alibcTaokeParams, exParams, new DemoTradeCallback());*/

        Intent intent=new Intent(MainActivity.this,AliSdkWebViewProxyActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("CouponLink",couponLink);
        bundle.putString("GoodID",id);
        bundle.putInt("CouponType",couponType);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private String dealCryText(String text) {
        int begin = text.indexOf("(");
        return text.substring(0, begin);
    }

    /**
     * 设置类型和数据源：
     * DropDownMenu.KEY对应类型（DropDownMenu中的常量，参考上述核心源码）
     * DropDownMenu.VALUE对应数据源：key不是TYPE_CUSTOM则传递string[],key是TYPE_CUSTOM类型则传递对应view
     */
    private List<HashMap<String, Object>> initViewData() {
        List<HashMap<String, Object>> viewDatas = new ArrayList<>();
        HashMap<String, Object> map;
        for (int i = 0; i < headers.length; i++) {
            map = new HashMap<String, Object>();
            map.put(MyDropDownMenu.KEY, types[i]);
            switch (types[i]) {
                case MyDropDownMenu.TYPE_LIST_CITY:
                    map.put(MyDropDownMenu.VALUE, methods);
                    map.put(MyDropDownMenu.SELECT_POSITION, 0);
                    break;
                case MyDropDownMenu.TYPE_LIST_SIMPLE:
                    map.put(MyDropDownMenu.VALUE, ages);
                    map.put(MyDropDownMenu.SELECT_POSITION, 0);
                    break;
                case MyDropDownMenu.TYPE_GRID:
                    map.put(MyDropDownMenu.VALUE, constellations);
                    break;
                default:
                    map.put(MyDropDownMenu.VALUE, getCustomView());
                    break;
            }
            viewDatas.add(map);
        }
        return viewDatas;
    }

    private View getCustomView() {
        View v = getLayoutInflater().inflate(R.layout.custom, null);
        lpEdt = (PriceEditText) v.findViewById(R.id.lp);
        mpEdt = (PriceEditText) v.findViewById(R.id.mp);
        btn = (Button) v.findViewById(R.id.btn);
        Observable<CharSequence> observableLp = RxTextView.textChanges(lpEdt);
        Observable<CharSequence> observableMp = RxTextView.textChanges(mpEdt);
        Observable.combineLatest(observableLp, observableMp, new BiFunction<CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean apply(@NonNull CharSequence charSequence, @NonNull CharSequence charSequence2) throws Exception {
                return isCharValid(charSequence, charSequence2);
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                btn.setEnabled(aBoolean);
            }
        });

        RxView.clicks(btn)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
//                        mDropDownMenu.setTabText(2,"自定义");//设置tab标签文字
                        hintKbTwo();
                        page = 1;
                        lp = lpEdt.getDoublePrice();
                        mp = mpEdt.getDoublePrice();
                        if (lpEdt.getText().toString().equals("")) {
                            lp = 0;
                        }
                        if (mpEdt.getText().toString().equals("")) {
                            mp = 1000000;
                        }
                        downloadTqgData();
                        dropDownMenu.closeMenu();//关闭menu
                    }
                });

        return v;
    }

    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public boolean isCharValid(CharSequence lp, CharSequence mp) {
        if (!lp.toString().equals("") && !mp.toString().equals("") && !mp.toString().startsWith(".") && !lp.toString().equals(".")) {
            if (Double.parseDouble(lp.toString()) > Double.parseDouble(mp.toString())) {
                return false;
            }
        }
        return true;
    }

    public void showSearch() {
        final SearchFragment searchFragment = SearchFragment.newInstance();
        searchFragment.setOnSearchClickListener(new IOnSearchClickListener() {

            @Override
            public void OnSearchClick(String keyword) {
                //这里处理逻辑
                page = 1;
                ((MainActivity) searchFragment.getActivity()).keyword = keyword;
                downloadTqgData();
                if(!keyword.equals("")){
                    toolbar.setTitle("“"+keyword+"“搜索结果");
                }else{
                    toolbar.setTitle("");
                }

            }
        });
        searchFragment.show(getSupportFragmentManager(), SearchFragment.TAG);
    }

    public void downloadTqgData() {
        refreshLayout.autoRefresh();
        downloadQuanDataPre.downloadQuanData(couponType,type, page, category, lp, mp, keyword);
    }

    public void downloadTqgCryData() {
        downloadQuanCryPre.loadCry(couponType);
    }

    /**
     * setForce(true);
     * 强制更新,checkupdatelibrary中提供的默认强制更新Dialog,您完全可以自定义自己的Dialog,
     */
    public void forceUpdateDialogClick(boolean isForce ) {
        mForceUpdateDialog = new ForceUpdateDialog(MainActivity.this);
        mForceUpdateDialog.setForce(isForce);
        mForceUpdateDialog.setAppSize(mCheckUpdateInfo.getNewAppSize())
                .setDownloadUrl(mCheckUpdateInfo.getNewAppUrl())
                .setTitle(mCheckUpdateInfo.getAppName() + "有更新啦")
                .setReleaseTime(mCheckUpdateInfo.getNewAppReleaseTime())
                .setVersionName(mCheckUpdateInfo.getNewAppVersionName())
                .setUpdateDesc(mCheckUpdateInfo.getNewAppUpdateDesc())
                .setFileName("taoxiang.apk")
                .setFilePath(Environment.getExternalStorageDirectory().getPath() + "/checkupdatelib").show();
    }

    //分享文字
    public void shareText() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        shareIntent.setType("text/plain");

        //设置分享列表的标题，并且每次都显示分享列表
        startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHeadBar();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        //退出activity前关闭菜单
        if (dropDownMenu.isShowing()) {
            dropDownMenu.closeMenu();
            return;
        }
        //如果是有搜索结果就返回keyword为空的结果
        if (!TextUtils.isEmpty(keyword.trim())) {
            keyword = "";
            toolbar.setTitle("");
           if(menuItem!=null){
               if(menuItem.getItemId()==R.id.nav_dp){
                   setTitle("大牌推荐");
               }else if(menuItem.getItemId()==R.id.nav_bp){
                   setTitle("每日必拍");
               }else {
                   toolbar.setTitle("");
               }
           }
            page=1;
            downloadTqgData();
            return;
        }
        if((System.currentTimeMillis()-exitTime) > 2000){
            Toast.makeText(getApplicationContext(), "再按一次退出淘享", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
            return;
        } else {
            if(cache_switch) {
                CleanMessageUtil.clearAllCache(getApplicationContext(), new CleanMessageUtil.clearSuccess() {
                    @Override
                    public void deleteSuccess() {
                        finish();
                    }

                    @Override
                    public void deleteFail() {
                        finish();
                    }
                });
            }else {
                finish();
            }
            ActivityStackManager.getManager().finishAllActivity();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
        if (id == R.id.action_search) {
            showSearch();
            return true;
        }

     /*   if (id == R.id.action_tmr) {
            Intent intent = new Intent(this, TmrActivity.class);
            startActivity(intent);
        }*/

        return super.onOptionsItemSelected(item);
    }

    private MenuItem menuItem;

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            // Handle the camera action
            this.menuItem=item;
            couponType =0;
            if(keyword.equals(""))
                setTitle("");
            else
                setTitle("“"+keyword+"“搜索结果");
            downloadTqgData();
        } else if (id == R.id.nav_collect) {
            if(!UMSSDK.getLoginUserId().isEmpty() && !UMSSDK.getLoginUserToken().isEmpty()) {
                Intent intent = new Intent(MainActivity.this, CollectActivity.class);
                startActivity(intent);
            }else{
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                intent.putExtra("isNeedFinish",false);
                startActivity(intent);
            }
            return true;
        } else if (id == R.id.nav_dp) {
            this.menuItem=item;
            couponType =1;
            if(!keyword.equals("") &&!keyword.isEmpty()){
                setTitle("“"+keyword+"“搜索结果");
            }else
                setTitle("大牌推荐");
            downloadTqgData();
        } else if (id == R.id.nav_bp) {
            this.menuItem=item;
            couponType =2;
            if(!keyword.equals("") &&!keyword.isEmpty()){
                setTitle("“"+keyword+"“搜索结果");
            }else
                setTitle("每日必拍");
            downloadTqgData();
        } else if (id == R.id.nav_share) {
            shareText();
            return true;
        }else if (id == R.id.nav_send) {
            FeedbackAPI.init(this.getApplication(), "24646100","2abd1fba85bac1a05c1545a727e4523f");
            FeedbackAPI.setBackIcon(R.drawable.ic_arrow_back_white_24dp);
            FeedbackAPI.openFeedbackActivity();
            return true;
        }else if(id==R.id.nav_setting){
            Intent intent=new Intent(MainActivity.this,SettingActivity.class);
            intent.putExtra("isNeedUpdate",isNeedUpdate);
            startActivity(intent);
            return true;
            /*ApiUtils.getTqgApi(ApiUrls.tqgApiUrl)
                    .getUpdateInfo()
                    .unsubscribeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<UpdateBean>() {
                        Disposable d;
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            this.d =d;
                        }

                        @Override
                        public void onNext(@NonNull UpdateBean updateBean) {
                            if (getVersionCode(getApplicationContext())<updateBean.getUpdate_ver_code()) {
                                LinearLayout gallery = (LinearLayout) navView.getMenu().findItem(R.id.nav_update).getActionView();
                                gallery.setVisibility(View.VISIBLE);
                                mCheckUpdateInfo = new CheckUpdateInfo();
                                mCheckUpdateInfo.setAppName(updateBean.getUpdate_app_name())
                                        // .setIsForceUpdate(1)//设置是否强制更新,该方法的参数只要和服务端商定好什么数字代表强制更新即可
                                        .setNewAppReleaseTime(updateBean.getUpdate_Time())//软件发布时间
                                        // .setNewAppSize(12.3f)//单位为M
                                        .setNewAppSize(Float.parseFloat(updateBean.getUpdate_app_size()))
                                        .setNewAppUrl(updateBean.getUpdate_url())
                                        .setNewAppVersionCode(updateBean.getUpdate_ver_code())//新app的VersionCode
                                        .setNewAppVersionName(updateBean.getUpdate_ver_name())
                                        .setNewAppUpdateDesc(updateBean.getUpdate_content());
                                forceUpdateDialogClick(updateBean.isIgnore_able());
                            }
                            if(!updateBean.getShare_text().isEmpty()) {
                                shareText = updateBean.getShare_text();
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            ToastUtils.showToast(MainActivity.this,"网络出错");
                            onComplete();
                        }

                        @Override
                        public void onComplete() {
                            d.dispose();
                        }
                    });*/
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void loadCry(List<QuanCryBean.QuanCategoryBean> quanCategoryBeen) {
        constellations = new String[quanCategoryBeen.size() + 1];
        constellations[0] = "全部类别";
        int num = 0;
        int cryPos=0;
        for (int j = 1; j < quanCategoryBeen.size() + 1; j++) {
            num = num + quanCategoryBeen.get(j - 1).getNum();
            constellations[j] = quanCategoryBeen.get(j - 1).getGoods_cat_name() + "(" + quanCategoryBeen.get(j - 1).getNum() + ")";
            if(quanCategoryBeen.get(j - 1).getGoods_cat_name() .equals(category)){
                cryPos=j;
            }
        }
        constellations[0] = "全部类别(" + num + ")";
        welcome.setText("");
        dropDownMenu.refreshGridItem(cryPos,constellations);
    }

    @Override
    public void loadCryFail(String msg) {
        ToastUtils.showToast(this, msg);
    }

    @Override
    public void setTqgTodayView(List<QuanBean.QuanDataBean> list) {
        if (page == 1) {
            adapter.setData(list);
        } else {
            adapter.addMoreData(list);
        }
        if(list.size()==0 && page ==1){
            noGood.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            noGood.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadmore();
    }

    @Override
    public void loadTqgTodayFail(String msg) {
        ToastUtils.showToast(this, msg);
        if(page ==1 ){
            noGood.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            //Bug1
            if(noGood.getVisibility()!=View.VISIBLE) {
                noGood.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
        if (page != 1) {
            page = page - 1;
        }
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadmore();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login:case R.id.headPic:
                Intent intent=null;
                if(!UMSSDK.getLoginUserToken().isEmpty() &&!UMSSDK.getLoginUserId().isEmpty()){
                    intent=new Intent(MainActivity.this,UserActivity.class);
                }else {
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.toTop:
                //if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    recyclerView.smoothScrollToPosition(0);
//                }else{
//                    recyclerView.scrollToPosition(0);
//                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //如果用户同意所请求的权限
        if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //UPDATE_DIALOG_PERMISSION_REQUEST_CODE和FORCE_UPDATE_DIALOG_PERMISSION_REQUEST_CODE这两个常量是library中定义好的
            //所以在进行判断时,必须要结合这两个常量进行判断.
            //非强制更新对话框
            //进行下载操作
            mForceUpdateDialog.download();
        } else {
            Toast.makeText(this, "请同意存储权限", Toast.LENGTH_SHORT).show();
            AskForPermission();
        }

    }

    private void AskForPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请同意存储权限");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("转去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName())); // 根据包名打开对应的设置界面
                startActivity(intent);
            }
        });
        builder.create().show();
    }

}
