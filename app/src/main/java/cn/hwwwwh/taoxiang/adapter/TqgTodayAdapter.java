package cn.hwwwwh.taoxiang.adapter;

import android.content.Context;
import android.graphics.Paint;
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
import cn.hwwwwh.taoxiang.CoustomView.CustomProgressBar;
import cn.hwwwwh.taoxiang.R;
import cn.hwwwwh.taoxiang.model.bean.TqgTodayData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by 97481 on 2017/10/3/ 0003.
 */
public class TqgTodayAdapter extends RecyclerView.Adapter<TqgTodayAdapter.FishViewHolder> {


    private List<TqgTodayData.TqgTodayDataBean> data;
    private LayoutInflater inflater;
    private Context context;
    OnItemClickListener mOnItemClickListener;

    public TqgTodayAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public FishViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FishViewHolder holder = new FishViewHolder(inflater.inflate(
                R.layout.tqg_rv_item, parent, false));
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
                            mOnItemClickListener.onItemClick(holder.itemView, pos, data.get(pos).getTqg_click_url());
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


    public void addMoreData(List<TqgTodayData.TqgTodayDataBean> data) {
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
    public void setData(List<TqgTodayData.TqgTodayDataBean> data) {
        if (data != null) {
            this.data = data;
        } else {
            this.data.clear();
        }
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, String url);

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
        @BindView(R.id.customProgressBar)
        CustomProgressBar customProgressBar;
        @BindView(R.id.Price)
        TextView Price;
        @BindView(R.id.Org_Price)
        TextView OrgPrice;

        public FishViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindData(TqgTodayData.TqgTodayDataBean tqgTodayDataBean) {
            DTitle.setText(tqgTodayDataBean.getTqg_title());
            Price.setText("￥" + tqgTodayDataBean.getTqg_zk_final_price());
            OrgPrice.setText("￥" + tqgTodayDataBean.getTqg_reserve_price());
            OrgPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            Glide.with(context).load(tqgTodayDataBean.getTqg_pic_url()).placeholder(R.drawable.loadpic).into(pic);
            customProgressBar.setMaxProgress(tqgTodayDataBean.getTqg_total_amount());
            customProgressBar.setCurProgress(tqgTodayDataBean.getTqg_sold_num());
        }
    }
}
