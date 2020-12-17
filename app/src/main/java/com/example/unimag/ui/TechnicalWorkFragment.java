package com.example.unimag.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.unimag.R;

public class TechnicalWorkFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false); //Убираем стрелочку назад

        View root = inflater.inflate(R.layout.fragment_technical_work, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().findViewById(R.id.nav_view).setVisibility(View.INVISIBLE); //Убираем меню

        ImageView imageView = requireView().findViewById(R.id.image_technical_work);
        Animation animation = new AnimationUtils().loadAnimation(TechnicalWorkFragment.this.getContext(), R.anim.animation_rotate);
        imageView.startAnimation(animation);
    }
}
