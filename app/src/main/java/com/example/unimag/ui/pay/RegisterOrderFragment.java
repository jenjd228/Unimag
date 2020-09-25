package com.example.unimag.ui.pay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.unimag.R;
import com.example.unimag.ui.DTO.PayDTO;
import com.example.unimag.ui.SqLite.DataDBHelper;
import com.example.unimag.ui.ThreadCheckingConnection;
import com.example.unimag.ui.catalog.CatalogFragment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

public class RegisterOrderFragment extends Fragment {

    private GridView gridView;

    private GridAdapterForPay gridAdapterForPay;

    private DataDBHelper dataDbHelper;

    private String secureKod;

    private String list;

   public RegisterOrderFragment(){

    }

   /* public RegisterOrderFragment(String list){
        this.list = list;
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataDbHelper = new DataDBHelper(getActivity());
        secureKod = dataDbHelper.getSecureKod(dataDbHelper);
        dataDbHelper.close();
        list = RegisterOrderFragmentArgs.fromBundle(requireArguments()).getList();
    }

    @SneakyThrows
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_order, container, false);

        new ThreadCheckingConnection(getFragmentManager(), savedInstanceState).execute(); //Если дисконект

        gridView = view.findViewById(R.id.grid_view_order);
        if (list!=null){
            List<PayDTO> participantJsonList;
            ObjectMapper objectMapper = new ObjectMapper();
            participantJsonList = objectMapper.readValue(list, new TypeReference<List<PayDTO>>(){});
            gridView.setAdapter(gridAdapterForPay = new GridAdapterForPay(this.getContext(), new ArrayList<>()));

            gridAdapterForPay.addList(participantJsonList);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button button_register_order = requireView().findViewById(R.id.button_register_order);
        TextView totalMoney = requireView().findViewById(R.id.money);
        totalMoney.setText(String.valueOf(gridAdapterForPay.getTheCostOfProducts()));

        button_register_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Временно
                Toast toast = Toast.makeText(RegisterOrderFragment.this.getContext(),
                        "Покупка оплачена!\nСтатус доставки можно посмотреть в личном кабинете", Toast.LENGTH_LONG);
                toast.show();

                //Временно
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(RegisterOrderFragment.this.getId(), new CatalogFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

    }

}
