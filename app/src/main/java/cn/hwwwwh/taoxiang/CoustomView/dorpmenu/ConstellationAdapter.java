package cn.hwwwwh.taoxiang.CoustomView.dorpmenu;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.hwwwwh.taoxiang.R;


public class ConstellationAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;
    private int checkItemPosition = 0;

    public void setCheckItem(int position) {
        checkItemPosition = position;
        notifyDataSetChanged();
    }

    public ConstellationAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_constellation_layout, null);
            convertView.setBackground(ContextCompat.getDrawable(context, R.drawable.menu_bg));
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        fillValue(position, viewHolder);
        return convertView;
    }

    void setData(int cryPos,List<String> list){
        this.list = list;
        setCheckItem(cryPos);
        notifyDataSetChanged();
    }

    private void fillValue(int position, ViewHolder viewHolder) {
        viewHolder.mText.setText(list.get(position));
        if (checkItemPosition != -1) {
            if (checkItemPosition == position) {
                viewHolder.mText.setTextColor(context.getResources().getColor(R.color.orange));
                //viewHolder.mText.setBackgroundResource(R.drawable.check_bg);
            } else {
                viewHolder.mText.setTextColor(context.getResources().getColor(com.zxl.library.R.color.drop_down_unselected));
               // viewHolder.mText.setBackgroundResource(R.drawable.uncheck_bg);
            }
        }
    }

    static class ViewHolder {
        TextView mText;

        ViewHolder(View view) {
            mText= (TextView) view.findViewById(com.zxl.library.R.id.text);
            mText.setSingleLine();
            mText.setEllipsize(TextUtils.TruncateAt.END);
        }
    }
}
