package com.example.unimag.ui.catalog;

import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.unimag.R;
import com.example.unimag.ui.CategoryFragment;
import com.example.unimag.ui.DTO.ProductDTO;
import com.example.unimag.ui.ProductFragment;
import com.example.unimag.ui.Request.GetRequest;
import com.example.unimag.ui.ThreadCheckingConnection;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import lombok.SneakyThrows;

public class CatalogFragment extends Fragment {

    private CatalogViewModel catalogViewModel;

    private GridView gridView;

    private Integer currentNumberList = 0;

    private CustomGridAdapter customGridAdapter;

    private GetRequest getRequest;

    private Boolean isEnd = false; //Переменная отвечающая за "товары закончились"

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SneakyThrows
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        new ThreadCheckingConnection(getFragmentManager(), savedInstanceState).execute();
        catalogViewModel =
                ViewModelProviders.of(this).get(CatalogViewModel.class);
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);
        gridView = view.findViewById(R.id.gridView);

        try {
            getRequest = new GetRequest(currentNumberList,"getList");
            getRequest.execute();

            if (customGridAdapter == null){
                gridView.setAdapter(customGridAdapter = new CustomGridAdapter(this.getContext(), new ArrayList<>()));

                String otvet = getRequest.get();

                if(otvet.equals("Error!")){
                    Toast toast = Toast.makeText(getContext(),
                            "Товары закончились!", Toast.LENGTH_SHORT);
                    toast.show();

                } else {
                    List<ProductDTO> participantJsonList;
                    ObjectMapper objectMapper = new ObjectMapper();
                    participantJsonList = objectMapper.readValue(otvet, new TypeReference<List<ProductDTO>>() {
                    });
                    customGridAdapter.addList(participantJsonList);
                    currentNumberList++;
                }
            }else {
                gridView.setAdapter(customGridAdapter);
            }
        }catch (Exception e){e.getMessage();}


        gridView.setOnItemClickListener((a, v, position, id) -> {
            Object o = gridView.getItemAtPosition(position);
            ProductDTO productDTO = (ProductDTO) o;

            FragmentManager manager = CatalogFragment.this.getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(CatalogFragment.this.getId(), new ProductFragment(productDTO.getImageName(), productDTO.getTitle(), productDTO.getDescriptions(), productDTO.getPrice(), productDTO.getId()));
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView viewCategory = getView().findViewById(R.id.text_category); //Кнопка категории и сортировки
        ImageView loading = getView().findViewById(R.id.view_loading); //Элемент анимации во фаргменте каталога
        AnimationDrawable animation = (AnimationDrawable) loading.getBackground(); //Говорим что фон - это анимация


        //Listener для кнопки с категорией
        viewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(CatalogFragment.this.getId(), new CategoryFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        //Автообновление каталога
        gridView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                //Если товары еще не закончились
                if (!isEnd) {
                    View lastElement = (View) gridView.getChildAt(gridView.getChildCount() - 1); //Определяем последний товар

                    //Обработка возможного исключения с GridView
                    int xLastElement;
                    if (gridView.getChildCount() == 0) {
                        xLastElement = 0;
                    } else {
                        xLastElement = lastElement.getBottom();
                    }

                    int diff = (xLastElement - (gridView.getHeight() + gridView.getScrollY())); //Определяем разницу в пикселях

                    //Если разница равна нулю - добавляем товары
                    if (diff == 0) {

                        //Проигрыш анимации
                        loading.setVisibility(View.VISIBLE); //Делаем видимым ImageView
                        animation.setOneShot(false); //Зацикливаем анимацию
                        animation.start(); //Начинаем проигрывать анимацию

                        getRequest = new GetRequest(currentNumberList, "getList");
                        getRequest.execute();
                        try {
                            String otvet = getRequest.get();

                            if (otvet.equals("Error!")) {
                                isEnd = true; //Товары закончились
                                //Завершение анимации
                                animation.stop(); //Останавливаем анимацию
                                loading.setVisibility(View.INVISIBLE); //Делаем невидимым ImageView
                            } else {
                                List<ProductDTO> participantJsonList;
                                ObjectMapper objectMapper = new ObjectMapper();
                                participantJsonList = objectMapper.readValue(otvet, new TypeReference<List<ProductDTO>>() {
                                });
                                customGridAdapter.addList(participantJsonList);
                                currentNumberList++;
                                //Завершение анимации
                                animation.stop(); //Останавливаем анимацию
                                loading.setVisibility(View.INVISIBLE); //Делаем невидимым ImageView
                            }
                        } catch (Exception ex) {
                            ex.getStackTrace();
                        }

                    }

                }
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy");
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("onPause");
    }
}