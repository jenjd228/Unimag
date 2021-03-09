package com.example.unimag.ui.OrderDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.unimag.MainActivity;
import com.example.unimag.R;
import com.example.unimag.ui.DTO.Order2ProductDTO;
import com.example.unimag.ui.DTO.OrdersDTO;
import com.example.unimag.ui.DTO.PayDTO;
import com.example.unimag.ui.Order.GridAdapterOrder;
import com.example.unimag.ui.Request.GetRequest;
import com.example.unimag.ui.SqLite.DataDBHelper;
import com.example.unimag.ui.ThreadCheckingConnection;
import com.example.unimag.ui.pay.RegisterOrderFragmentArgs;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

public class OrderDetailsFragment extends Fragment {

    private GridView gridView;

    private DataDBHelper dataDbHelper;

    private String secureKod;

    private String list;

    private GridAdapterForOrderDetails gridAdapterForOrderDetails;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataDbHelper = new DataDBHelper(getActivity());
        secureKod = dataDbHelper.getSecureKod(dataDbHelper);
        dataDbHelper.close();
        list = RegisterOrderFragmentArgs.fromBundle(requireArguments()).getList();
    }

    @SneakyThrows
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        new ThreadCheckingConnection(getParentFragmentManager(), requireContext());

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Информация о заказе");
        actionBar.setDisplayHomeAsUpEnabled(false);

        View root = inflater.inflate(R.layout.order_details_fragment, container, false);

        gridView = root.findViewById(R.id.grid_view_order_details);
        gridView.setAdapter(gridAdapterForOrderDetails = new GridAdapterForOrderDetails(this.getContext(), new ArrayList<>()));

                List<Order2ProductDTO> participantJsonList;
                ObjectMapper objectMapper = new ObjectMapper();
                participantJsonList = objectMapper.readValue(list, new TypeReference<List<Order2ProductDTO>>() {
                });
        System.out.println(participantJsonList+"Это лист");
                gridAdapterForOrderDetails.addList(participantJsonList);

        return root;
    }

    @Override
    public void onDestroyView() {
        ((MainActivity)getActivity()).addInStack(MainActivity.TAB_PERSONAL_AREA, this);
        super.onDestroyView();
    }
}
