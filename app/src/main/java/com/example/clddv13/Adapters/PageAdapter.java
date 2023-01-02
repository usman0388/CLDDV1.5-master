package com.example.clddv13.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.clddv13.Tabs.tab1;
import com.example.clddv13.Tabs.tab2;
import com.example.clddv13.Tabs.weather_tab3;

public class PageAdapter extends FragmentPagerAdapter {
    private int numberOfTabs;
    private String User;
    private String city;
    public PageAdapter(FragmentManager fm,int num, String User,String city){
        super(fm);
        this.numberOfTabs = num;
        this.User = User;
        this.city = city;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new tab1(this.User);
        }else if(position == 1){
            return new tab2();
        }else if(position == 2){
            return new weather_tab3(city);
        }else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return  POSITION_NONE;
    }
}
