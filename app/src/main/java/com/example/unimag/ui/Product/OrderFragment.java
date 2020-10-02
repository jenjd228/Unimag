package com.example.unimag.ui.Product;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.unimag.R;
import com.example.unimag.ui.DTO.OrderDTO;
import com.example.unimag.ui.DTO.ProductDTO;
import com.example.unimag.ui.Request.GetRequest;
import com.example.unimag.ui.SqLite.DataDBHelper;
import com.example.unimag.ui.ThreadCheckingConnection;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.ArrayList;
import java.util.List;


public class OrderFragment extends Fragment {

    private GridAdapterOrder gridAdapterOrder;

    private DataDBHelper dataDbHelper;

    private GridView gridView;

    private String secureKod = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataDbHelper = new DataDBHelper(getActivity());
        secureKod = dataDbHelper.getSecureKod(dataDbHelper);
        dataDbHelper.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Мои заказы");
        actionBar.setDisplayHomeAsUpEnabled(false);

        new ThreadCheckingConnection(getFragmentManager(), savedInstanceState).execute();

        View root = inflater.inflate(R.layout.fragment_orders, container, false);

        gridView = root.findViewById(R.id.grid_view_orders);
        gridView.setAdapter(gridAdapterOrder = new GridAdapterOrder(this.getContext(), new ArrayList<>()));

        try{
            GetRequest getRequest = new GetRequest(secureKod,"getOrdersList");
            getRequest.execute();
            String result = getRequest.get();
            if (result.equals("BAD_REQUEST") || result.equals("Error!")){

            }else {
                List<OrderDTO> participantJsonList;
                ObjectMapper objectMapper = new ObjectMapper();
                participantJsonList = objectMapper.readValue(getRequest.get(), new TypeReference<List<OrderDTO>>(){});
                gridAdapterOrder.addList(participantJsonList);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //CreateAndSendRequast createAndSendRequast = new CreateAndSendRequast(secureKod);
        //createAndSendRequast.execute();

       // List<OrderDTO> products = new ArrayList<>();
        //Создание нашего списка товаров с помощью функции getList()
        //final GridView gridView = (GridView) getView().findViewById(R.id.grid_view_orders);
        //Установление адаптера (что-то вроде прохождения по всем элементам)
        //gridView.setAdapter(gridAdapterOrder = new GridAdapterOrder(this.getContext(),products));

        //Установка onClickListenera для каждого элемента item
        ArrayList<OrderDTO> listData = new ArrayList<OrderDTO>();

        OrderDTO p1 = new OrderDTO();
        p1.setTitle("Часы ЮФУ ясень");
        p1.setImageName("29.jpg");
        p1.setDataOfOrder("02.02.2020");
        p1.setStatus("Не доставлено");
        listData.add(p1);

        GridAdapterOrder adapter = new GridAdapterOrder(this.getContext(), listData);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = gridView.getItemAtPosition(position);
                ProductDTO productDTO = (ProductDTO) o;

                /*FragmentManager manager = OrderFragment.this.getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(OrderFragment.this.getId(), new ProductFragment(productDTO.getImageName(), productDTO.getTitle(), productDTO.getDescriptions(), productDTO.getPrice(), productDTO.getId()));
                transaction.addToBackStack(null);
                transaction.commit();*/
            }
        });

    }
}
