package com.example.unimag.ui.partner_program;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unimag.R;
import com.example.unimag.ui.DTO.PartnerProgramDTO;
import com.example.unimag.ui.ThreadCheckingConnection;

import java.util.ArrayList;

public class PartnerProgramFragment extends Fragment {

    RecyclerView recyclerView; //RecyclerView со списком всех партнеров


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_partner_program, container, false);

        new ThreadCheckingConnection(getFragmentManager(), savedInstanceState).execute(); //Если дисконект

        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Находим RecyclerView
        recyclerView = getView().findViewById(R.id.recycler_view_partner_program);
        //Указываем для него ориентацию
        recyclerView.setLayoutManager(new LinearLayoutManager(PartnerProgramFragment.this.getContext()));

        //Тестовый список партнеров
        ArrayList<PartnerProgramDTO> dataList = new ArrayList<PartnerProgramDTO>();

        PartnerProgramDTO p1 = new PartnerProgramDTO();
        p1.setTitle("Adidas");
        p1.setDescription("Скидка 10% на первую покупку\n(акция действет ежемесячно)");

        PartnerProgramDTO p2 = new PartnerProgramDTO();
        p2.setTitle("Red Bull");
        p2.setDescription("Скидка 2%");

        PartnerProgramDTO p3 = new PartnerProgramDTO();
        p3.setTitle("Nike");
        p3.setDescription("Верните до 15% при покупке от 3000 рублей\nАкция дейстительна до 27.09.2020");

        dataList.add(p1);
        dataList.add(p2);
        dataList.add(p3);

        //Создаем адаптер, исходя из нашего списка партнеров
        RecyclerAdapterPartnerProgram partnerAdapter = new RecyclerAdapterPartnerProgram(dataList);

        //Передаем в RecyclerView наш адаптер с данными
        recyclerView.setAdapter(partnerAdapter);

    }
}
