package com.titan.ycslzy.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.titan.ycslzy.R;

import java.util.List;


/**
 * 选择照片的adapter
 * Created by li on 2017/10/18.
 */

public class RecyclerViewPicAdapter extends RecyclerView.Adapter<RecyclerViewPicAdapter.ViewHolder> implements View.OnClickListener{


    private Context mContext;
    private PicOnclick picOnclick;
    private List<String> picList;
    private int mColumnWidth=100;

    public RecyclerViewPicAdapter(Context context, List<String> list, int mColumnWidth){
        this.mContext = context;
        this.picList = list;
        this.mColumnWidth = mColumnWidth;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_image, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String title = mContext.getResources().getString(R.string.serverhost);
        String path = picList.get(position);
        Bitmap bitmap = null;
        if(path.contains(title)){
            //网络图片
            Picasso.with(mContext).load(path).into(holder.img_pic);
        }else{
            bitmap = BitmapFactory.decodeFile(path);
            holder.img_pic.setImageBitmap(bitmap);
        }
        holder.img_pic.setOnClickListener(this);
        holder.img_pic.setId(position);
        holder.itemView.setId(position);

        holder.img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return picList.size();
    }

    @Override
    public void onClick(View v) {
        if(picOnclick != null){
            int id = v.getId();
            picOnclick.setPicOnclick(v, id);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img_pic;
        ImageView img_close;

        public ViewHolder(View itemView) {
            super(itemView);

            img_pic = (ImageView) itemView.findViewById(R.id.imageView_pic);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mColumnWidth,mColumnWidth);
            img_pic.setLayoutParams(params);
            params.setMargins(10,10,10,10);
            img_close = (ImageView) itemView.findViewById(R.id.imageView_close);
        }
    }

    /**
     * 图片点击回调接口
     */
    public interface PicOnclick{
        void setPicOnclick(View item, int position);
    }

    /**
     * 调用此方法，实现点击
     * @param onclick
     */
    public void setPicOnclick(PicOnclick onclick){
        this.picOnclick = onclick;
    }

}

