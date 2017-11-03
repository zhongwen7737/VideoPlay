package com.videoplay;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.videoplay.homepage.HomePageFragment;
import com.videoplay.me.MeFragment;
import com.videoplay.underline.UnderLineFragment;
import com.videoplay.utils.StringUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FragmentManager fragmentManager;
    private HomePageFragment fragment_hp;
    private MeFragment fragment_me;
    private UnderLineFragment fragment_ul;
    private TextView tv_hp,tv_ul,tv_me;
    private int color_fmenu_normal,color_fmenu_focus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        color_fmenu_normal = getResources().getColor(
                R.color.main_tabmenu_text_normal);
        color_fmenu_focus = getResources().getColor(
                R.color.main_tabmenu_text_focus);
        initView();
        setTabSelection(0);
        initListener();
    }

    private void initListener() {
        tv_hp.setOnClickListener(this);
        tv_me.setOnClickListener(this);
        tv_ul.setOnClickListener(this);
    }

    private void initView() {
        tv_hp=(TextView)findViewById(R.id.tv_hp);
        tv_ul=(TextView)findViewById(R.id.tv_ul);
        tv_me=(TextView)findViewById(R.id.tv_me);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_hp:
                setTabSelection(0);
                break;
            case R.id.tv_ul:
                setTabSelection(1);
                break;
            case R.id.tv_me:
                setTabSelection(2);
                break;
        }
    }

    private void setTabSelection(int i) {
            // 每次选中之前先清楚掉上次的选中状态
            clearSelection();
            // 开启一个Fragment事务
            FragmentTransaction transaction = getFM().beginTransaction();
            // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
            hideFragments(transaction);
            switch (i){
                case 0:
                    tv_hp.setTextColor(color_fmenu_focus);
                    StringUtil.setDrawbleTop(this,tv_hp,R.mipmap.homepage_smallred);
                    if(fragment_hp ==null){
                        fragment_hp = new HomePageFragment();
                        transaction.add(R.id.rl_above,fragment_hp);
                    }else {
                        transaction.show(fragment_hp);
                    }
                    break;
                case 1:
                    tv_ul.setTextColor(color_fmenu_focus);
                    StringUtil.setDrawbleTop(this,tv_ul,R.mipmap.underline_small);
                    if(fragment_ul ==null){
                        fragment_ul = new UnderLineFragment();
                        transaction.add(R.id.rl_above,fragment_ul);
                    }else {
                        transaction.show(fragment_ul);
                    }
                    break;
                case 2:
                    tv_me.setTextColor(color_fmenu_focus);
                    StringUtil.setDrawbleTop(this,tv_me,R.mipmap.me_smallred);
                    if(fragment_me ==null){
                        fragment_me = new MeFragment();
                        transaction.add(R.id.rl_above,fragment_me);
                    }else {
                        transaction.show(fragment_me);
                    }
                    break;
                default:
                    break;
            }
            transaction.commitAllowingStateLoss();
        }


    private FragmentManager getFM() {
        if (fragmentManager == null)
            fragmentManager =getFragmentManager();
        return fragmentManager;

    }

    private void hideFragments(FragmentTransaction transaction) {
        if(fragment_hp!=null){
            transaction.hide(fragment_hp);
        }
        if(fragment_ul!=null){
            transaction.hide(fragment_ul);
        }
        if(fragment_me!=null){
            transaction.hide(fragment_me);
        }
    }

    private void clearSelection() {
        tv_hp.setTextColor(color_fmenu_normal);
        tv_ul.setTextColor(color_fmenu_normal);
        tv_me.setTextColor(color_fmenu_normal);
        StringUtil.setDrawbleTop(this,tv_hp,R.mipmap.homepage_smallwhite);
        StringUtil.setDrawbleTop(this,tv_ul,R.mipmap.underline_small);
        StringUtil.setDrawbleTop(this,tv_me,R.mipmap.me_smallwhite);

    }
}
