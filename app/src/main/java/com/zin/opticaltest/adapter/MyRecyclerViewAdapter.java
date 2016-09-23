package com.zin.opticaltest.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zin.opticaltest.R;
import com.zin.opticaltest.entity.TestBank;

import java.util.List;

/**
 * Created by ZIN on 2016/3/23.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>{
    /*
     *1.继承RecyclerViewAdapter类，需重写 onCreateViewHolder，onBindViewHolder，getItemCount
     * 三个方法
     */
    /**
     * 为RecyclerView添加item的点击事件
     * 1.在MyAdapter中定义接口,模拟ListView的OnItemClickListener：
     * 2. 声明一个这个接口的变量( OnItemClickListener mListener;)，并注册监听（ itemView.setOnClickListener）
     * 3.接口的onItemClick()中的v.getTag()方法，这需要在onBindViewHolder()方法中设置和item相关的数据
     * 4.最后暴露给外面的调用者，定义一个设置Listener的方法（setOnItemClickListener）：
     */
    private List<TestBank> datas = null;
    private OnItemClickListener mListener;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.paperlist_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.OnItemClick(v, (TestBank) view.getTag());//注意这里使用getTag方法获取数据
                }
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      /*  String string=datas.get(position);
        holder.setTvTitle(string) ;*/
        holder.tvTitle.setText(datas.get(position).getTitle());
        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(datas.get(position));

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
    /**
     * @param datas 展示的数据
     */
    public  MyRecyclerViewAdapter(List<TestBank> datas){
        this.datas=datas;
    }
    /**
    *自定义的ViewHolder，持有每个Item的的所有界面元素
    */
    static  class  ViewHolder extends  RecyclerView.ViewHolder{
        TextView tvTitle;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle= (TextView) itemView.findViewById(R.id.tvTitle);

        }
       /* public  void  setTvTitle(String t){
            tvTitle.setText(t);
        }*/

    }
    /**
    * RecyclerView的Item的监听器接口
    *
    */
    public interface OnItemClickListener
    {
         void OnItemClick(View view,TestBank data);
    }
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mListener = listener;
    }
}
