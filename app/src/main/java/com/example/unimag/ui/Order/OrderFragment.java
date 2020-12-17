package com.example.unimag.ui.Order;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.unimag.R;
import com.example.unimag.ui.DTO.OrdersDTO;
import com.example.unimag.ui.DTO.ProductDTO;
import com.example.unimag.ui.Request.GetRequest;
import com.example.unimag.ui.SqLite.DataDBHelper;
import com.example.unimag.ui.ThreadCheckingConnection;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;


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

    @SuppressLint("ShowToast")
    @SneakyThrows
    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        new ThreadCheckingConnection(getParentFragmentManager(), requireContext());

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false); //Убираем стрелочку назад

        View root = inflater.inflate(R.layout.fragment_orders, container, false);

        gridView = root.findViewById(R.id.grid_view_orders);
        gridView.setAdapter(gridAdapterOrder = new GridAdapterOrder(this.getContext(), new ArrayList<>()));

        try {
            GetRequest getRequest = new GetRequest(requireContext(), getFragmentManager(), secureKod, "getOrdersList");
            getRequest.execute();
            String result = getRequest.get();

            if (result.isEmpty()) {

            } else {
                List<OrdersDTO> participantJsonList;
                ObjectMapper objectMapper = new ObjectMapper();
                participantJsonList = objectMapper.readValue(getRequest.get(), new TypeReference<List<OrdersDTO>>() {
                });
                gridAdapterOrder.addList(participantJsonList);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gridView.setOnItemClickListener((a, v, position, id) -> {

            Object o = gridView.getItemAtPosition(position);
            ProductDTO productDTO = (ProductDTO) o;

        });

    }
}
