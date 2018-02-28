package cn.hwwwwh.taoxiang.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ali.auth.third.ui.context.CallbackContext;
import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcDetailPage;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;
import com.jakewharton.rxbinding2.view.RxMenuItem;
import com.jakewharton.rxbinding2.view.RxView;
import com.mob.ums.UMSSDK;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import cn.hwwwwh.taoxiang.DemoTradeCallback;
import cn.hwwwwh.taoxiang.R;
import cn.hwwwwh.taoxiang.base.BaseActivity;
import cn.hwwwwh.taoxiang.base.RxBus;
import cn.hwwwwh.taoxiang.dagger.DaggerPresenterComponent;
import cn.hwwwwh.taoxiang.dagger.module.PresenterModule;
import cn.hwwwwh.taoxiang.presenter.RequestCollectPre;
import cn.hwwwwh.taoxiang.presenter.WebViewIsCollectPre;
import cn.hwwwwh.taoxiang.utils.ToastUtils;
import cn.hwwwwh.taoxiang.view.iface.IRequestCollectView;
import cn.hwwwwh.taoxiang.view.iface.IWebViewCollectView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 阿里百川电商
 * 项目名称：阿里巴巴电商SDK
 * 开发团队：阿里巴巴百川商业化团队
 * 阿里巴巴电商SDK答疑群号：1200072507
 * <p/>
 * 功能说明：WebView代理页面
 */
public class AliSdkWebViewProxyActivity extends BaseActivity implements IWebViewCollectView,IRequestCollectView{

    @BindView(R.id.toolbar_wb)
    Toolbar toolbar;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Inject
    WebViewIsCollectPre webViewIsCollectPre;
    @Inject
    RequestCollectPre requestCollectPre;

    //清除tmall页面广告
    private static String noAdTmall="javascript:function setTop(){if(document.querySelector('#J_smartjump').style.display !='none'){document.querySelector('#J_smartjump').style.display=\"none\";document.querySelector('#content.pt44').style.paddingTop='0px';}}window.setInterval(setTop, 0);";
    //清除淘宝广告js
    protected static String noAdTaoBao="javascript:function setTaobao(){if(document.querySelector('body').style.paddingTop !=\"0px\"){document.querySelector('body').style.paddingTop=\"0px\";document.querySelector('.app-detail').style.zIndex='2';document.querySelector('.main_layout').style.backgroundColor='white';}} window.setInterval(setTaobao, 0);";
    //清除淘宝评价广告
    protected static String NoAdTaoBaoPingjia="javascript:function setTaobaoPingjia(){if(document.querySelector('#J_ratePop').style.zIndex != '2'){document.querySelector('body').style.paddingTop =\"0px\";document.querySelector('#J_ratePop').style.zIndex='2',document.querySelector('#J_ratePop').style.position='absolute'}}  window.setInterval(setTaobaoPingjia, 0);";


    private Map<String, String> exParams = new HashMap<>();
    private String goodID;
    private String couponLink;
    AlibcTaokeParams alibcTaokeParams;
    private boolean isGoodPage=true;
    private WebChromeClient webChromeClient;
    private WebViewClient webViewClient;
    private MenuItem menuItem_collect;
    private MenuItem menuItem_link;
    private boolean isCoolect=false;
    private RxBus rxBus;
    private boolean isVaild;
    private int couponType;
    private String fuligou;

    //登录须重写onActivityResult方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        CallbackContext.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        //调用了AlibcTrade.show方法的Activity都需要调用AlibcTradeSDK.destory()
        AlibcTradeSDK.destory();
        rxBus.unSubscribe(this);
        super.onDestroy();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_web;
    }

    @Override
    protected void init() {
        toolbar.setTitle("购买");
        toolbar.setTitleTextAppearance(this,R.style.ToolbarTitle);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.tomato));
        }
        initToolbar();
        //商品加载
        WebSettings s = webView.getSettings();
        s.setBuiltInZoomControls(true);
        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setSavePassword(true);
        s.setSaveFormData(true);
        s.setJavaScriptEnabled(true);
        // enable navigator.geolocation
        s.setGeolocationEnabled(true);
        s.setCacheMode(WebSettings.LOAD_DEFAULT);
        // enable Web Storage: localStorage, sessionStorage
        s.setDomStorageEnabled(true);
        webViewClient=new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d("taoxiangTest","onPageStart");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("taoxiangTest","onPageFinished");
//                webView.loadUrl(noAdTaoBao);
//                webView.loadUrl(NoAdTaoBaoPingjia);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        };

        progressBar.setMax(100);
        webChromeClient=new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                // java.lang.NullPointerException: Attempt to invoke virtual method 'void android.widget.ProgressBar.setProgress(int)' on a null object reference
                if(progressBar!=null) {
                    progressBar.setProgress(newProgress);
                    if (newProgress == 100) {
                        Log.d("taoxiangTest","onPageFinished：100");
                        progressBar.setVisibility(View.INVISIBLE);
                        webView.loadUrl(noAdTmall);
                        //Toast.makeText(mContext, "加载结束...", Toast.LENGTH_SHORT).show();
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        //Toast.makeText(mContext, "加载中...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        alibcTaokeParams = new AlibcTaokeParams(); // 若非淘客taokeParams设置为null即可
        alibcTaokeParams.adzoneid = "136690366";
        alibcTaokeParams.pid = "mm_56584098_37534934_136690366";
        // alibcTaokeParams.subPid = "mm_56584098_37534934_136690366";
        alibcTaokeParams.extraParams = new HashMap<>();
        alibcTaokeParams.extraParams.put("taokeAppkey", "24642765");
        //showGood();
        showCoupon();
        //是否收藏
        DaggerPresenterComponent.builder().presenterModule(new PresenterModule(this)).build().inject(this);
        initCollect();
        initRxBus();
    }


    public void initToolbar(){
        RxToolbar.itemClicks(toolbar)
                .subscribe(new Consumer<MenuItem>() {
                    @Override
                    public void accept(final MenuItem item) throws Exception {
                        int id = item.getItemId();
                        //noinspection SimplifiableIfStatement
                        if (id == R.id.action_coupon) {

                        }
                        if (id == R.id.action_refresh) {
                            webView.reload();
                        }
                        if(id==R.id.action_collect){

                        }
                    }
                });
    }

    private void initCollect(){
        if(!UMSSDK.getLoginUserId().isEmpty() && !UMSSDK.getLoginUserToken().isEmpty())
            webViewIsCollectPre.isCollect(UMSSDK.getLoginUserId(),goodID,null);
        else
            setUnCollect();
    }

    private void initRxBus() {
        rxBus = RxBus.getIntanceBus();
        registerRxBus(Boolean.class, new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean isLogin) throws Exception {
                if(isLogin){
                    webViewIsCollectPre.isCollect(UMSSDK.getLoginUserId(), goodID, new WebViewIsCollectPre.CallBack() {
                        @Override
                        public void initSuccess() {
                            requestCollectPre.requestCollect(UMSSDK.getLoginUserId(),goodID,null,-1);
                        }
                    });

                }
            }
        });
    }

    //注册
    public <T> void registerRxBus(Class<T> eventType, Consumer<T> action) {
        Disposable disposable = rxBus.doSubscribe(eventType, action, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                //Log.e("NewsMainPresenter", throwable.toString());
            }
        });
        rxBus.addSubscription(this,disposable);
    }

    @Override
    protected void initBundleData() {
         goodID=getIntent().getExtras().getString("GoodID");
         couponLink=getIntent().getExtras().getString("CouponLink");
         isVaild=getIntent().getExtras().getBoolean("isVaild",false);
         couponType=getIntent().getExtras().getInt("CouponType",0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.web, menu);
        menuItem_collect=menu.findItem(R.id.action_collect);
        menuItem_link=menu.findItem(R.id.action_coupon);
        if(isVaild){
            menuItem_collect.setVisible(false);
            menuItem_link.setVisible(false);
        }
        RxMenuItem.clicks(menuItem_link)
                .throttleFirst(1000,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                       /* if(isGoodPage) {
                            isGoodPage=false;
                            menuItem_link.setIcon(R.drawable.ic_shop);
                            menuItem_link.setTitle("前往商品");
                            showCoupon();
                        }
                        else {
                            isGoodPage=true;
                            menuItem_link.setIcon(R.drawable.ic_coupon);
                            menuItem_link.setTitle("优惠券");
                            showGood();
                        }*/
                        showGood();
                    }
                });
        RxMenuItem.clicks(menuItem_collect)
                .throttleFirst(1000,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if(!UMSSDK.getLoginUserId().isEmpty() &&!UMSSDK.getLoginUserToken().isEmpty()){
                            requestCollectPre.requestCollect(UMSSDK.getLoginUserId(),goodID,null,-1);
                        }else{
                            Intent intent=new Intent(AliSdkWebViewProxyActivity.this,LoginActivity.class);
                            intent.putExtra("isNeedFinish",false);
                            startActivity(intent);
                        }
                    }
                });
        return true;
    }


    public void showGood(){
        //AlibcBasePage alibcBasePage = new AlibcDetailPage(goodID);
        AlibcBasePage alibcBasePage = new AlibcDetailPage(goodID);
        AlibcShowParams alibcShowParams = new AlibcShowParams(OpenType.Native, false);
        AlibcTrade.show(AliSdkWebViewProxyActivity.this,webView, webViewClient, webChromeClient,alibcBasePage,alibcShowParams,alibcTaokeParams,exParams, new DemoTradeCallback());
    }

    public void showCoupon(){
        AlibcBasePage alibcBasePage = new AlibcPage(couponLink);
        AlibcShowParams alibcShowParams = new AlibcShowParams(OpenType.H5, false);
        AlibcTrade.show(AliSdkWebViewProxyActivity.this,webView, webViewClient, webChromeClient,alibcBasePage,alibcShowParams,alibcTaokeParams,exParams, new DemoTradeCallback());
    }

    @Override
    public void setCollect() {
        if(menuItem_collect!=null){
            isCoolect=true;
            menuItem_collect.setIcon(R.drawable.ic_favourites_filled_star_yellow);
            menuItem_collect.setTitle("取消收藏");
        }
    }

    @Override
    public void setUnCollect() {
        if(menuItem_collect!=null){
            isCoolect=false;
            menuItem_collect.setIcon(R.drawable.ic_favourites_filled_border_star_white);
            menuItem_collect.setTitle("收藏");
        }
    }


    @Override
    public void requestSuccess(String msg, View view,int pos) {
        if(isCoolect){
            isCoolect=false;
            setUnCollect();
        }else{
            isCoolect=true;
            setCollect();
        }
        ToastUtils.showToast(this,msg);
    }

    @Override
    public void requestFail(String msg) {
        ToastUtils.showToast(this,msg);
    }

}
