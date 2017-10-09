package cn.hwwwwh.taoxiang.view.activity;

import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.hwwwwh.taoxiang.CoustomView.SpacesItemDecoration;
import cn.hwwwwh.taoxiang.R;
import cn.hwwwwh.taoxiang.adapter.TqgTmrAdapter;
import cn.hwwwwh.taoxiang.api.ApiUrls;
import cn.hwwwwh.taoxiang.base.BaseActivity;
import cn.hwwwwh.taoxiang.dagger.DaggerPresenterComponent;
import cn.hwwwwh.taoxiang.dagger.module.PresenterModule;
import cn.hwwwwh.taoxiang.model.bean.TqgTmrData;
import cn.hwwwwh.taoxiang.presenter.DownloadTqgTmrDataPre;
import cn.hwwwwh.taoxiang.utils.ToastUtils;
import cn.hwwwwh.taoxiang.view.iface.ITmrTqgDataView;

public class TmrActivity extends BaseActivity implements ITmrTqgDataView {

    @BindView(R.id.toolbar_tmr)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;


    @Inject
         public DownloadTqgTmrDataPre downloadTqgTmrDataPre;

    TqgTmrAdapter adapter;
    int page=1;
    int type=1;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_tmr;
    }

    @Override
    protected void init() {
        toolbar.setTitle("明日预告");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
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
        DaggerPresenterComponent.builder().presenterModule(new PresenterModule(this)).build().inject(this);
        initRecycleView();
        loadTmrPre();
        setUpRefresh();
    }

    void loadTmrPre(){
        downloadTqgTmrDataPre.loadTmrData(ApiUrls.tqgApiUrl,type,page);
    }

    void setUpRefresh(){
        //设置 Header 为 Material风格
        refreshLayout.setRefreshHeader(new WaterDropHeader(this));
        //refreshLayout.setRefreshHeader(new FunGameHitBlockHeader(this));
        //refreshLayout.setPrimaryColors(Color.CYAN);
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page=1;
                loadTmrPre();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page=page+1;
                loadTmrPre();
            }
        });
    }

    @Override
    protected void initBundleData() {

    }

    private void initRecycleView() {
        adapter = new TqgTmrAdapter(this);
        //必须指定adaoter
        recyclerView.setAdapter(adapter);
        //必须指定layoutmanager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置item之间的间隔
       // SpacesItemDecoration decoration=new SpacesItemDecoration(10);
        //recyclerView.addItemDecoration(decoration);
        // adapter.setData(mDatas);
    }

    @Override
    public void setTmrData(List<TqgTmrData.TqgTmrDataBean> list) {
        if(page==1){
            adapter.setData(list);
        }else{
            adapter.addMoreData(list);
        }
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadmore();
    }

    @Override
    public void setFailView(String msg) {
        if(page!=1){
            page=page-1;
        }
        ToastUtils.showToast(this,msg);
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadmore();
    }
}
