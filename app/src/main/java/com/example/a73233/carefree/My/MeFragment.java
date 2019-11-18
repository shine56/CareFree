package com.example.a73233.carefree.My;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.a73233.carefree.Diary.DiaryListAdapter;
import com.example.a73233.carefree.MainActivity;
import com.example.a73233.carefree.R;
import com.example.a73233.carefree.Util.SpacesItemDecoration;
import com.example.a73233.carefree.db.Diary_db;

import org.litepal.LitePal;

import java.util.List;


public class MeFragment extends Fragment {
    private Activity activity;
    private RecyclerView recyclerView;
    private DiaryListAdapter adapter;
    private List<Diary_db> diary_dbs;
    private ImageView userHeadIma;
    private TextView toolbarLeft;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        // 初始化控件
        recyclerView = view.findViewById(R.id.user_recy);
        userHeadIma = view.findViewById(R.id.me_head_ima);
        toolbarLeft = view.findViewById(R.id.toolbar_left);
        activity = getActivity();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化那些时候
        diary_dbs = LitePal
                .findAll(Diary_db.class);
        //设置recycleview,指定控件布局方式，将其设置到控件对象中
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DiaryListAdapter((MainActivity) activity);
        adapter.setData(diary_dbs);
        int space = 50; //间距
        recyclerView.addItemDecoration(new SpacesItemDecoration(0,space));
        recyclerView.setAdapter(adapter);
        //初始化图片
        Glide.with(activity).load(R.drawable.user_head_ima)
                .skipMemoryCache(true) // 不使用内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(60)))
                .error(R.mipmap.find_photo_fail)
                .into(userHeadIma);
        //标题
        toolbarLeft.setText("我的");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        activity.finish();
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
