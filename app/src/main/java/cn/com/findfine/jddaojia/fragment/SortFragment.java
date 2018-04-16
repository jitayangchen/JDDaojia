package cn.com.findfine.jddaojia.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.com.findfine.jddaojia.R;


public class SortFragment extends Fragment {

    public SortFragment() {
    }

    public static SortFragment newInstance() {
        SortFragment fragment = new SortFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sort, container, false);
    }

}
