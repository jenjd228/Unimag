package com.example.unimag.ui.catalog;

import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.unimag.R;
import com.example.unimag.ui.DTO.CatalogDTO;
import com.example.unimag.ui.Request.GetRequest;
import com.example.unimag.ui.ThreadCheckingConnection;
import com.example.unimag.ui.sort.GlobalSort;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import lombok.SneakyThrows;

public class CatalogFragment extends Fragment {

    private GridView gridView;

    private Integer currentNumberList = 0;

    private GetRequest getRequest;

    private Boolean isEnd = false; //Переменная отвечающая за "товары закончились"

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (CustomGridAdapter.getInstance() != null) {
            CustomGridAdapter.getInstance().cleanList();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SneakyThrows
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        new ThreadCheckingConnection(getParentFragmentManager(), requireContext());

        //CatalogViewModel catalogViewModel = ViewModelProviders.of(this).get(CatalogViewModel.class);
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);
        gridView = view.findViewById(R.id.gridView);

        if (CustomGridAdapter.getInstance() == null) {
            CustomGridAdapter.setContext(requireContext());
        }

        try {

            if (GlobalSort.getInstance().getUpdateFlag()) {
                CustomGridAdapter.getInstance().cleanList();
                currentNumberList = 0;
                GlobalSort.getInstance().setUpdateFlag(false);
                isEnd = false;
            }

            if (CustomGridAdapter.getInstance().getCount() == 0) {
                getRequest = new GetRequest(requireContext(), getParentFragmentManager(), currentNumberList, "getList");
                getRequest.execute();
                String response = getRequest.get();

                if (response.equals("Error!")) {
                    Toast toast = Toast.makeText(getContext(),
                            "Товары закончились!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    List<CatalogDTO> participantJsonList;
                    ObjectMapper objectMapper = new ObjectMapper();
                    participantJsonList = objectMapper.readValue(response, new TypeReference<List<CatalogDTO>>() {
                    });
                    CustomGridAdapter.getInstance().addList(participantJsonList);
                    currentNumberList++;
                    System.out.println("Лист установлен");
                    gridView.setAdapter(CustomGridAdapter.getInstance());
                }
            } else {
                gridView.setAdapter(CustomGridAdapter.getInstance());
            }

        } catch (Exception e) {
            e.getMessage();
        }


        gridView.setOnItemClickListener((a, v, position, id) -> {
            Object o = gridView.getItemAtPosition(position);
            CatalogDTO catalogDTO = (CatalogDTO) o;

            CatalogFragmentDirections.ActionNavigationCatalogToProductFragment2 action = CatalogFragmentDirections.actionNavigationCatalogToProductFragment2(catalogDTO.getMainImage(), catalogDTO.getTitle(), catalogDTO.getDescriptions(), catalogDTO.getPrice(), catalogDTO.getHash(), catalogDTO.getCategory(), catalogDTO.getListImage());
            Navigation.findNavController(v).navigate(action);
        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView viewCategory = requireView().findViewById(R.id.text_category); //Кнопка категории и сортировки
        ImageView loading = requireView().findViewById(R.id.view_loading); //Элемент анимации во фаргменте каталога
        AnimationDrawable animation = (AnimationDrawable) loading.getBackground(); //Говорим что фон - это анимация


        //Listener для кнопки с категорией
        viewCategory.setOnClickListener(v -> {
            NavDirections action = CatalogFragmentDirections.actionNavigationCatalogToSortFragment2();
            Navigation.findNavController(v).navigate(action);
        });

        //Автообновление каталога
        gridView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            //Если товары еще не закончились
            if (!isEnd) {
                View lastElement = gridView.getChildAt(gridView.getChildCount() - 1); //Определяем последний товар

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

                    getRequest = new GetRequest(requireContext(), getFragmentManager(), currentNumberList, "getList");
                    getRequest.execute();
                    try {
                        String otvet = getRequest.get();

                        //Делаем невидимым ImageView
                        //Останавливаем анимацию
                        if (otvet.equals("Error!")) {
                            isEnd = true; //Товары закончились
                            //Завершение анимации
                        } else {
                            List<CatalogDTO> participantJsonList;
                            ObjectMapper objectMapper = new ObjectMapper();
                            participantJsonList = objectMapper.readValue(otvet, new TypeReference<List<CatalogDTO>>() {
                            });
                            CustomGridAdapter.getInstance().addList(participantJsonList);
                            currentNumberList++;
                            //Завершение анимации
                        }
                        animation.stop(); //Останавливаем анимацию
                        loading.setVisibility(View.INVISIBLE); //Делаем невидимым ImageView
                    } catch (Exception ex) {
                        ex.getStackTrace();
                    }

                }

            }
        });


    }
}