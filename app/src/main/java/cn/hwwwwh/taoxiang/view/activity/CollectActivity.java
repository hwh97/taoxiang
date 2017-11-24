package cn.hwwwwh.taoxiang.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.mob.ums.UMSSDK;
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
import cn.hwwwwh.taoxiang.R;
import cn.hwwwwh.taoxiang.adapter.CollectAdapter;
import cn.hwwwwh.taoxiang.base.BaseActivity;
import cn.hwwwwh.taoxiang.dagger.DaggerPresenterComponent;
import cn.hwwwwh.taoxiang.dagger.module.PresenterModule;
import cn.hwwwwh.taoxiang.model.bean.CollectBean;
import cn.hwwwwh.taoxiang.presenter.DeleteAllInvalidCollectPre;
import cn.hwwwwh.taoxiang.presenter.DeleteSingleInvalidCollectPre;
import cn.hwwwwh.taoxiang.presenter.DownloadCollectDataPre;
import cn.hwwwwh.taoxiang.presenter.RequestCollectPre;
import cn.hwwwwh.taoxiang.utils.ToastUtils;
import cn.hwwwwh.taoxiang.view.iface.IDeleteAllInvalidCollectView;
import cn.hwwwwh.taoxiang.view.iface.IDeleteSingleInValidCollectView;
import cn.hwwwwh.taoxiang.view.iface.IMyCollectView;
import cn.hwwwwh.taoxiang.view.iface.IRequestCollectView;

public class CollectActivity extends BaseActivity implements IMyCollectView, IDeleteSingleInValidCollectView, IRequestCollectView, IDeleteAllInvalidCollectView {

    @BindView(R.id.toolbar_collect)
    Toolbar toolbarCollect;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @Inject
    DownloadCollectDataPre downloadCollectDataPre;
    @Inject
    DeleteSingleInvalidCollectPre deleteSingleInvalidCollectPre;
    @Inject
    RequestCollectPre requestCollectPre;
    @Inject
    DeleteAllInvalidCollectPre deleteAllInvalidCollectPre;

    CollectAdapter adapter;
    int page = 1;
    int type = 1;
    @BindView(R.id.noCollect)
    ImageView noCollect;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_collect;
    }

    @Override
    protected void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.tomato));
        }
        toolbarCollect.setTitle("我的收藏");
        toolbarCollect.setTitleTextAppearance(this, R.style.ToolbarTitle);
        toolbarCollect.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbarCollect);
        toolbarCollect.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbarCollect.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        DaggerPresenterComponent.builder().presenterModule(new PresenterModule(this)).build().inject(this);
        initRecycleView();
        setUpRefresh();
        loadCollect();
    }

    @Override
    protected void initBundleData() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.collect, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_clear) {
            new AlertDialog.Builder(this).setTitle("确认").setMessage("确定清除全部无效数据吗")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteAllInvalid();
                        }
                    })
                    .setNegativeButton("取消", null).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void downloadSuccess(List<CollectBean.CollectDataBean> list) {
        if(list.size()==0 && page ==1){
            noCollect.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            noCollect.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        if (page == 1) {
            adapter.setData(list);
        } else {
            adapter.addMoreData(list);
        }
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadmore();
    }

    @Override
    public void downloadFail(String msg) {
        if(page ==1){
            noCollect.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            noCollect.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        if (page != 1) {
            page = page - 1;
        }
        ToastUtils.showToast(this, msg);
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadmore();
    }

    void deleteAllInvalid() {
        deleteAllInvalidCollectPre.deleteAllInValid(UMSSDK.getLoginUserId());
    }

    void setUpRefresh() {
        //设置 Header 为 Material风格
        refreshLayout.setRefreshHeader(new WaterDropHeader(this));
        //refreshLayout.setRefreshHeader(new FunGameHitBlockHeader(this));
        //refreshLayout.setPrimaryColors(Color.CYAN);
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                loadCollect();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page = page + 1;
                loadCollect();

            }
        });
    }

    void loadCollect() {
        refreshLayout.autoRefresh();
        downloadCollectDataPre.downloadCollect(UMSSDK.getLoginUserId(), page);
    }

    private void initRecycleView() {
        adapter = new CollectAdapter(this);
        //必须指定adaoter
        recyclerView.setAdapter(adapter);
        //必须指定layoutmanager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置item之间的间隔
        // SpacesItemDecoration decoration=new SpacesItemDecoration(10);
        //recyclerView.addItemDecoration(decoration);
        // adapter.setData(mDatas);
        adapter.setOnItemClickListener(new CollectAdapter.onItemClickListener() {

            @Override
            public void onItemClick(View view, int position, String couponLink, String id, boolean isVaild) {
                showUrl(couponLink, id, isVaild);
            }

            @Override
            public void OnItemLongClick(View view, int position) {

            }

            @Override
            public void onCancelRlClick(View view, int position, String id, boolean isVaild) {
                if (isVaild) {
                    if (adapter.getData().get(position).isCollect())
                        deleteSingleInvalidCollectPre.delete(UMSSDK.getLoginUserId(), id, view, position);
                } else {
                    requestCollectPre.requestCollect(UMSSDK.getLoginUserId(), id, view, position);
                }
            }
        });
    }

    public void showUrl(String couponLink, String id, boolean isVaild) {

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

        Intent intent = new Intent(CollectActivity.this, AliSdkWebViewProxyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("CouponLink", couponLink);
        bundle.putString("GoodID", id);
        bundle.putBoolean("isVaild", isVaild);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void deleteSuccess(String msg, View view, int pos) {
        ImageView star = (ImageView) view.findViewById(R.id.star);
        TextView collect_text = (TextView) view.findViewById(R.id.collect_text);
        adapter.getData().get(pos).setCollect(false);
        star.setImageDrawable(getResources().getDrawable(R.drawable.ic_favourites_filled_border_star_black));
        collect_text.setText("收藏");
        view.setOnClickListener(null);
        ToastUtils.showToast(getApplicationContext(), msg);
    }

    @Override
    public void deleteFail(String msg) {
        ToastUtils.showToast(getApplicationContext(), msg);
    }

    @Override
    public void requestSuccess(String msg, View view, int pos) {
        ImageView star = (ImageView) view.findViewById(R.id.star);
        TextView collect_text = (TextView) view.findViewById(R.id.collect_text);
        if (adapter.getData().get(pos).isCollect()) {
            adapter.getData().get(pos).setCollect(false);
            star.setImageDrawable(getResources().getDrawable(R.drawable.ic_favourites_filled_border_star_black));
            collect_text.setText("收藏");
        } else {
            adapter.getData().get(pos).setCollect(true);
            star.setImageDrawable(getResources().getDrawable(R.drawable.ic_favourites_filled_star_yellow));
            collect_text.setText("取消收藏");
        }

        ToastUtils.showToast(getApplicationContext(), msg);
    }

    @Override
    public void requestFail(String msg) {
        ToastUtils.showToast(getApplicationContext(), msg);
    }

    @Override
    public void deleteAllInvalidSuccess(String msg) {
        loadCollect();
        ToastUtils.showToast(getApplicationContext(), msg);
    }

    @Override
    public void deleteAllInvalidFail(String msg) {
        ToastUtils.showToast(getApplicationContext(), msg);
    }

}
