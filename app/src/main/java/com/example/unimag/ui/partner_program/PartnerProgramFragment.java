package com.example.unimag.ui.partner_program;

import android.app.Notification;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.unimag.R;
import com.example.unimag.ui.DTO.PartnerProgramDTO;
import com.example.unimag.ui.Request.GetRequest;
import com.example.unimag.ui.ThreadCheckingConnection;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

public class PartnerProgramFragment extends Fragment {

    GridView gridView; //RecyclerView со списком всех партнеров


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_partner_program, container, false);

        new ThreadCheckingConnection(getFragmentManager(), savedInstanceState).execute(); //Если дисконект

        return root;
    }


    @SneakyThrows
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Находим GridView
        gridView = requireView().findViewById(R.id.recycler_view_partner_program);
        GetRequest getRequest = new GetRequest("getPartner");
        getRequest.execute();
        //Тестовый список партнеров
        ArrayList<PartnerProgramDTO> dataList;

        ObjectMapper objectMapper = new ObjectMapper();
        dataList = objectMapper.readValue(getRequest.get(), new TypeReference<List<PartnerProgramDTO>>(){});

        //Создаем адаптер, исходя из нашего списка партнеров
        GridAdapterPartnerProgram partnerAdapter = new GridAdapterPartnerProgram(this.getContext(), dataList);

        //Передаем в GridView наш адаптер с данными
        gridView.setAdapter(partnerAdapter);


        //Устанавливаем Listener для элементов GridView
        gridView.setOnItemClickListener((a, v, position, id) -> {
            //Получаем данный элемент
            Object item = gridView.getItemAtPosition(position);
            PartnerProgramDTO partner = (PartnerProgramDTO) item;

            PartnerProgramFragmentDirections.ActionPartnerProgramFragmentToInformationAboutPartnerFragment action =
                    PartnerProgramFragmentDirections.actionPartnerProgramFragmentToInformationAboutPartnerFragment(partner.getImageName(), partner.getTitle(),
                            partner.getDescription(),
                            partner.getPrice(),
                            partner.getId());

            Navigation.findNavController(v).navigate(action);
        });

    }

}
