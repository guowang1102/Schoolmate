//package com.weiguowang.schoolmate.adapter;
//
///**
// * function:
// * Created by 韦国旺 on 2017/3/31 0031.
// * Copyright (c) 2017  All Rights Reserved.
// */
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.weiguowang.filmsky.R;
//import com.weiguowang.filmsky.domain.entity.Film;
//
//import java.util.List;
//
///**
// * Created by my on 2016/9/27.
// */
//public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {
//
//    private Context context;
//    private List<Film> list;
//
//    private OnRecyclerItemClickListener mOnItemClickListener;//单击事件
//    private onRecyclerItemLongClickListener mOnItemLongClickListener;//长按事件
//
//
//    public MyRecyclerViewAdapter(Context context, List<Film> list) {
//        this.context = context;
//        this.list = list;
//    }
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        //找到item的布局
//        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_layout, parent, false);
//        return new MyViewHolder(view);//将布局设置给holder
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    /**
//     * 绑定视图到holder,就如同ListView的getView(),但是这里已经把复用实现了,我们只需要填充数据就行,复用的时候都是调用该方法填充数据
//     */
//    @Override
//    public void onBindViewHolder(final MyViewHolder holder, final int position) {
////        //填充数据
////        holder.textView.setText(list.get(position) + "");
////        //设置单击事件
////        if (mOnItemClickListener != null) {
////            holder.textView.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    //这里是为textView设置了单击事件,回调出去
////                    //mOnItemClickListener.onItemClick(v,position);这里需要获取布局中的position,不然乱序
////                    mOnItemClickListener.onItemClick(v, holder.getLayoutPosition());
////                }
////            });
////        }
////        //长按事件
////        if (mOnItemLongClickListener != null) {
////            holder.textView.setOnLongClickListener(new View.OnLongClickListener() {
////                @Override
////                public boolean onLongClick(View v) {
////                    //回调出去
////                    mOnItemLongClickListener.onItemLongClick(v, holder.getLayoutPosition());
////                    return true;//不返回true,松手还会去执行单击事件
////                }
////            });
////        }
//
//        holder.name.setText(list.get(position).getFilmName());
//        if (mOnItemClickListener != null) {
//            holder.name.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //这里是为textView设置了单击事件,回调出去
//                    //mOnItemClickListener.onItemClick(v,position);这里需要获取布局中的position,不然乱序
//                    mOnItemClickListener.onItemClick(v, holder.getLayoutPosition());
//                }
//            });
//        }
//
//    }
//
//    class MyViewHolder extends RecyclerView.ViewHolder {
//
//        //        TextView textView;
//        ImageView imageView;
//        TextView name;
//
//        public MyViewHolder(View itemView) {
//            super(itemView);
//            imageView = (ImageView) itemView.findViewById(R.id.film_image);
////            textView = (TextView) itemView.findViewById(R.id.textView);
//            name = (TextView) itemView.findViewById(R.id.film_name);
//        }
//    }
//
//    /**
//     * 处理item的点击事件,因为recycler没有提供单击事件,所以只能自己写了
//     */
//    public interface OnRecyclerItemClickListener {
//        public void onItemClick(View view, int position);
//    }
//
//    /**
//     * 长按事件
//     */
//    public interface onRecyclerItemLongClickListener {
//        public void onItemLongClick(View view, int position);
//    }
//
//    /**
//     * 暴露给外面的设置单击事件
//     */
//    public void setOnItemClickListener(OnRecyclerItemClickListener onItemClickListener) {
//        mOnItemClickListener = onItemClickListener;
//    }
//
//    /**
//     * 暴露给外面的长按事件
//     */
//    public void setOnItemLongClickListener(onRecyclerItemLongClickListener onItemLongClickListener) {
//        mOnItemLongClickListener = onItemLongClickListener;
//    }
//
////    /**
////     * 向指定位置添加元素
////     */
////    public void addItem(int position, String value) {
////        if (position > list.size()) {
////            position = list.size();
////        }
////        if (position < 0) {
////            position = 0;
////        }
////        /**
////         * 使用notifyItemInserted/notifyItemRemoved会有动画效果
////         * 而使用notifyDataSetChanged()则没有
////         */
////        list.add(position, value);//在集合中添加这条数据
////        notifyItemInserted(position);//通知插入了数据
////    }
////
////    /**
////     * 移除指定位置元素
////     */
////    public String removeItem(int position) {
////        if (position > list.size() - 1) {
////            return null;
////        }
////        String value = list.remove(position);//所以还需要手动在集合中删除一次
////        notifyItemRemoved(position);//通知删除了数据,但是没有删除list集合中的数据
////        return value;
////    }
//
//}
