package com.sam.lib.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sam.lib.etv.ExpandTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecycleView = (RecyclerView) findViewById(R.id.rv);


        mRecycleView.setLayoutManager(new LinearLayoutManager(this));

        mRecycleView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mRecycleView.setAdapter(new Adapter(randomData()));

    }

    static class Adapter extends RecyclerView.Adapter<ViewHolder> implements View.OnClickListener {

        private List<Model> mList;

        Adapter(List<Model> list) {
           this.mList = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_etv, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mContent.setTag(position);
            holder.mContent.setText(mList.get(position));

        }

        @Override
        public int getItemCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public void onClick(View v) {
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ExpandTextView mContent;

        public ViewHolder(View itemView) {
            super(itemView);
            mContent = (ExpandTextView) itemView.findViewById(R.id.xtv);
        }
    }

    static class Model implements ExpandTextView.IWrap {

        private String content;

        private int state = ExpandTextView.State.STATE_INIT;

        public Model(String content) {
            this.content = content;
        }

        @Override
        public String getIWrapText() {
            return content;
        }

        @Override
        public int getIWrapState() {
            return state;
        }

        @Override
        public void setIWrapState(int state) {
            this.state = state;
        }
    }

    private ArrayList<Model> randomData() {
        String content = "一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十";
        int length = content.length();
        Random random = new Random(System.currentTimeMillis());
        ArrayList<Model> data = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            int s = random.nextInt(length);
            data.add(new Model(i + "    :  " + s + content.substring(0, s)));
        }
        return data;
    }


}
