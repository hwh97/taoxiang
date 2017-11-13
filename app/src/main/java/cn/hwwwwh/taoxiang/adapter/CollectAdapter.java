package cn.hwwwwh.taoxiang.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.hwwwwh.taoxiang.R;
import cn.hwwwwh.taoxiang.model.bean.CollectBean;
import cn.hwwwwh.taoxiang.model.bean.TqgTmrData;

/**
 * Created by 97481 on 2017/10/8/ 0008.
 */

public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.CollectHolder> {

    private List<CollectBean.CollectDataBean> data;
    private LayoutInflater inflater;
    private Context context;
    onItemClickListener mOnItemClickListener;


    public CollectAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public CollectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CollectHolder holder = new CollectHolder(inflater.inflate(
                R.layout.collect_rv_item, parent, false));
        return holder;
    }


    @Override
    public void onBindViewHolder(final CollectHolder holder, int position) {
        holder.bindData(data.get(position));
        if(mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView,pos,data.get(pos).getCollect_coupon_link(),
                            String.valueOf(data.get(pos).getCollect_goods_id()),data.get(pos).isInvalid());
                }
            });

            holder.cancel_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=holder.getLayoutPosition();
                    mOnItemClickListener.onCancelRlClick(holder.cancel_rl,pos,String.valueOf(data.get(pos).getCollect_goods_id()),data.get(pos).isInvalid());
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

    public List<CollectBean.CollectDataBean> getData() {
        if (data == null) {
            return null;
        } else {
            return data;
        }
    }

    public void addMoreData(List<CollectBean.CollectDataBean> data) {
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
    public void setData(List<CollectBean.CollectDataBean> data) {
        if (data != null) {
            this.data = data;
        } else {
            this.data.clear();
        }
        notifyDataSetChanged();
    }

    public  interface onItemClickListener{
        void onItemClick(View view, int position,String couponLink,String id,boolean isVaild);

        void OnItemLongClick(View view, int position);

        void onCancelRlClick(View view, int position, String id,boolean isVaild);
    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }


    class CollectHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.collect_pic)
        ImageView collect_pic;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.Price)
        TextView Price;
        @BindView(R.id.cardView)
        CardView cardView;
        @BindView(R.id.biaozhi)
        ImageView biaozhi;
        @BindView(R.id.endtime_tv)
        TextView endtime_tv;
//        @BindView(R.id.isVaild)
//        TextView isVaild;
        @BindView(R.id.cancel_rl)
        RelativeLayout cancel_rl;
        @BindView(R.id.star)
        ImageView star;
        @BindView(R.id.collect_text)
        TextView collect_text;


        public CollectHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }


        public void bindData(CollectBean.CollectDataBean collectDataBean) {
            Glide.with(context).load(collectDataBean.getCollect_goods_pic()).placeholder(R.drawable.loadpic).into(collect_pic);
            cardView.setRadius(8);//设置图片圆角的半径大小
            cardView.setCardElevation(8);//设置阴影部分大小
            cardView.setContentPadding(5, 5, 5, 5);//设置图片距离阴影大小
            //title.setText(collectDataBean.getCollect_goods_short_title());
            Price.setText("￥"+collectDataBean.getCollect_goods_finalprice());
            if(collectDataBean.getCollect_is_tmall()==1){
                Glide.with(context).load(R.drawable.tmall).into(biaozhi);
            }else{
                Glide.with(context).load(R.drawable.taobao).into(biaozhi);
            }
            if(collectDataBean.getCollect_coupon_end_time()!=0){
                endtime_tv.setText("券过期时间"+stampToDate(String.valueOf(collectDataBean.getCollect_coupon_end_time())));
            }
            if(collectDataBean.isInvalid()){
                //isVaild.setVisibility(View.VISIBLE);
                SpannableString spannableString = new SpannableString("【已失效】"+collectDataBean.getCollect_goods_short_title());
                StyleSpan span = new StyleSpan(Typeface.BOLD);
                spannableString.setSpan(span, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")), 0,5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                title.setText(spannableString);
            }else{
                title.setText(collectDataBean.getCollect_goods_short_title());
            }
            if(collectDataBean.isCollect()){
                star.setImageDrawable(cardView.getResources().getDrawable(R.drawable.ic_favourites_filled_star_yellow));
                collect_text.setText("取消收藏");
            }else{
                star.setImageDrawable(cardView.getResources().getDrawable(R.drawable.ic_favourites_filled_border_star_black));
                collect_text.setText("收藏");
            }
        }

        /*
         * 将时间戳转换为时间
         */
        public  String stampToDate(String s){
            String res;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            long lt = new Long(s);
            Date date = new Date(lt*1000L);
            res = simpleDateFormat.format(date);
            return res;
        }

    }



}
