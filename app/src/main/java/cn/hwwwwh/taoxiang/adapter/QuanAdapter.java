package cn.hwwwwh.taoxiang.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.hwwwwh.taoxiang.R;
import cn.hwwwwh.taoxiang.model.bean.QuanBean;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by 97481 on 2017/10/3/ 0003.
 */
public class QuanAdapter extends RecyclerView.Adapter<QuanAdapter.FishViewHolder> {


    private List<QuanBean.QuanDataBean> data;
    private LayoutInflater inflater;
    private Context context;
    OnItemClickListener mOnItemClickListener;

    public QuanAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public FishViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FishViewHolder holder = new FishViewHolder(inflater.inflate(
                R.layout.quan_rv_item, parent, false));
        return holder;
    }


    @Override
    public void onBindViewHolder(final FishViewHolder holder, int position) {
        holder.bindData(data.get(position));
        if (mOnItemClickListener != null) {
            RxView.clicks(holder.itemView)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            int pos = holder.getLayoutPosition();
                            mOnItemClickListener.onItemClick(holder.itemView, pos, data.get(pos).getCoupon_link(),data.get(pos).getGoods_id()+"");
                        }
                    });

            RxView.longClicks(holder.itemView)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            int pos = holder.getLayoutPosition();
                            mOnItemClickListener.OnItemLongClick(holder.itemView, pos);
                            return ;
                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        } else {
            return data.size();
        }
    }


    public void addMoreData(List<QuanBean.QuanDataBean> data) {
        if (data != null) {
                this.data.addAll(this.data.size(), data);
                notifyItemInserted(this.data.size());
        }
    }

    /**
     * 设置全新的数据集合，如果传入null，则清空数据列表（第一次从服务器加载数据，或者下拉刷新当前界面数据表）
     *
     * @param data
     */
    public void setData(List<QuanBean.QuanDataBean> data) {
        if (data != null) {
            this.data = data;
        } else {
            this.data.clear();
        }
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position, String couponLink,String id);

        void OnItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    class FishViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.pic)
        ImageView pic;
        @BindView(R.id.D_title)
        TextView DTitle;
        @BindView(R.id.Price)
        TextView Price;
        @BindView(R.id.Org_Price)
        TextView OrgPrice;
        @BindView(R.id.coupon_value)
        TextView coupon_value;
        @BindView(R.id.sales)
        TextView sales;
        @BindView(R.id.biaozhi)
        ImageView biaozhi;

        public FishViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindData(QuanBean.QuanDataBean quanDataBean) {

            DTitle.setText(quanDataBean.getGoods_short_title());
            Price.setText("￥" + quanDataBean.getGoods_finalprice());
            OrgPrice.setText("￥" + quanDataBean.getGoods_price());
            OrgPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            SharedPreferences sharedPreferences=context.getSharedPreferences("setting",Context.MODE_PRIVATE);
            boolean wifi_switch = sharedPreferences.getBoolean("wifi_switch", true);
            if(!wifi_switch) {
                Glide.with(context).load(quanDataBean.getGoods_pic()).placeholder(R.drawable.loadpic).into(pic);
            }else{
                if(isWifi(context))
                    Glide.with(context).load(quanDataBean.getGoods_pic()).placeholder(R.drawable.loadpic).into(pic);
                else
                    Glide.with(context).load(quanDataBean.getGoods_pic()+"_340x340.jpg").placeholder(R.drawable.loadpic).into(pic);
            }
            coupon_value.setText(quanDataBean.getCoupon_price()+"");
            sales.setText(quanDataBean.getGoods_sales()+"");
            if(quanDataBean.getIs_tmall()==1){
                Glide.with(context).load(R.drawable.tmall).into(biaozhi);
            }else{
                Glide.with(context).load(R.drawable.taobao).into(biaozhi);
            }
        }

        private boolean isWifi(Context mContext) {
            ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
            return false;
        }
    }
}
