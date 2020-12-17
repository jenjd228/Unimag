package com.example.unimag.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.unimag.R;
import com.example.unimag.ui.ThreadCheckingConnection;

public class NewsFragment extends Fragment {

    private com.example.unimag.ui.news.NewsViewModel newsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        new ThreadCheckingConnection(getParentFragmentManager(), requireContext());

        newsViewModel =
                ViewModelProviders.of(this).get(com.example.unimag.ui.news.NewsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_news, container, false);
        /*final TextView textView = root.findViewById(R.id.text_catalog);
        newsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }
}
