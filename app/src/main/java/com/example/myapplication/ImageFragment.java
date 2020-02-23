package com.example.myapplication;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class ImageFragment extends Fragment {

    ImageView imageView;

   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            //通过参数中的布局填充获取对应布局
          View view =inflater.inflate(R.layout.fragment_image,container,false);
          imageView = view.findViewById(R.id.img_fragment);
          imageView.setImageResource(R.drawable.lessons);
             return view;
        }
}
