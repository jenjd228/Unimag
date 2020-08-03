package com.example.unimag.ui.Product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.unimag.R;
import com.example.unimag.ui.DTO.OrderDTO;
import com.example.unimag.ui.DTO.ProductDTO;
import com.example.unimag.ui.ProductFragment;
import com.example.unimag.ui.SqLite.DataDBHelper;
import com.example.unimag.ui.ThreadCheckingConnection;


import java.util.ArrayList;
import java.util.List;


public class OrderFragment extends Fragment {

    private GridAdapterOrder gridAdapterOrder;

    private DataDBHelper dataDbHelper;

    private String secureKod = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataDbHelper = new DataDBHelper(getActivity());
        secureKod = dataDbHelper.getSecureKod(dataDbHelper);
        dataDbHelper.close();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        new ThreadCheckingConnection(getFragmentManager(), savedInstanceState).execute();
        View root = inflater.inflate(R.layout.fragment_orders, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //CreateAndSendRequast createAndSendRequast = new CreateAndSendRequast(secureKod);
        //createAndSendRequast.execute();

        List<OrderDTO> products = new ArrayList<>();
        //Создание нашего списка товаров с помощью функции getList()
        final GridView gridView = (GridView) getView().findViewById(R.id.grid_view_orders);
        //Установление адаптера (что-то вроде прохождения по всем элементам)
        gridView.setAdapter(gridAdapterOrder = new GridAdapterOrder(this.getContext(),products));

        //Установка onClickListenera для каждого элемента item
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = gridView.getItemAtPosition(position);
                ProductDTO productDTO = (ProductDTO) o;
                FragmentManager manager = OrderFragment.this.getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(OrderFragment.this.getId(), new ProductFragment(productDTO.getImageName(), productDTO.getTitle(), productDTO.getDescriptions(), productDTO.getPrice(), productDTO.getId()));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}
