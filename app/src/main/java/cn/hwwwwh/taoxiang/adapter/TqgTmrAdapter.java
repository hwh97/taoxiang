package cn.hwwwwh.taoxiang.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.hwwwwh.taoxiang.R;
import cn.hwwwwh.taoxiang.model.bean.TqgTmrData;

/**
 * Created by 97481 on 2017/10/8/ 0008.
 */

public class TqgTmrAdapter extends RecyclerView.Adapter<TqgTmrAdapter.TmrViewHolder> {

    private List<TqgTmrData.TqgTmrDataBean> data;
    private LayoutInflater inflater;
    private Context context;
    onItemClickListener mOnItemClickListener;


    public TqgTmrAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public TmrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TmrViewHolder holder = new TmrViewHolder(inflater.inflate(
                R.layout.tmr_tqg_rv_item, parent, false));
        return holder;
    }


    @Override
    public void onBindViewHolder(final TmrViewHolder holder, int position) {
        holder.bindData(data.get(position));
        if(mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView,pos);
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


    public void addMoreData(List<TqgTmrData.TqgTmrDataBean> data) {
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
    public void setData(List<TqgTmrData.TqgTmrDataBean> data) {
        if (data != null) {
            this.data = data;
        } else {
            this.data.clear();
        }
        notifyDataSetChanged();
    }

    public  interface onItemClickListener{
        void onItemClick(View view,int position);

        void OnItemLongClick(View view,int position);
    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }


    class TmrViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tmr_pic)
        ImageView tmrPic;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.Price)
        TextView Price;
        @BindView(R.id.Org_Price)
        TextView OrgPrice;
        @BindView(R.id.cardView)
        CardView cardView;

        public TmrViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }


        public void bindData(TqgTmrData.TqgTmrDataBean tqgTmrData) {
            Glide.with(context).load(tqgTmrData.getTqg_pic_url()).placeholder(R.drawable.loadpic).into(tmrPic);
            cardView.setRadius(8);//设置图片圆角的半径大小
            cardView.setCardElevation(8);//设置阴影部分大小
            cardView.setContentPadding(5, 5, 5, 5);//设置图片距离阴影大小
            title.setText(tqgTmrData.getTqg_title());
            Price.setText("￥"+tqgTmrData.getTqg_zk_final_price());
            OrgPrice.setText("￥"+tqgTmrData.getTqg_reserve_price());
            OrgPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
        }
    }



}
