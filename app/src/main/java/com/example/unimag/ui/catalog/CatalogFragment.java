package com.example.unimag.ui.catalog;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.unimag.R;
import com.example.unimag.ui.CreateAndSendRequest;
import com.example.unimag.ui.DTO.ProductDTO;
import com.example.unimag.ui.ProductFragment;
import com.example.unimag.ui.Request.GetRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;


import lombok.SneakyThrows;

public class CatalogFragment extends Fragment {

    private CatalogViewModel catalogViewModel;

    private GridView gridView;

    private Integer currentNumberList = 0;

    private CustomGridAdapter customGridAdapter;

    private GetRequest getRequest;

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
        catalogViewModel =
                ViewModelProviders.of(this).get(CatalogViewModel.class);
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);
        gridView = view.findViewById(R.id.gridView);

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button b = requireView().findViewById(R.id.button_add_elements);
        b.setOnClickListener(e -> {
            getRequest = new GetRequest(currentNumberList,"getList");
            getRequest.execute();
            try {
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
            }catch (Exception ex){
                ex.getStackTrace();
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