package com.example.unimag.ui.partner_program;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.unimag.MainActivity;
import com.example.unimag.R;
import com.example.unimag.ui.DTO.PartnerProgramDTO;
import com.example.unimag.ui.Request.GetRequest;
import com.example.unimag.ui.ThreadCheckingConnection;
import com.example.unimag.ui.sort.SortFragment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

public class PartnerProgramFragment extends Fragment {

    GridView gridView; //RecyclerView со списком всех партнеров


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_partner_program, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false); //Убираем стрелочку назад

        new ThreadCheckingConnection(getParentFragmentManager(), requireContext());

        return root;
    }


    @SneakyThrows
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Находим GridView
        gridView = requireView().findViewById(R.id.recycler_view_partner_program);
        try {
            GetRequest getRequest = new GetRequest(requireContext(), getFragmentManager(), "getPartner");
            getRequest.execute();
            //Тестовый список партнеров
            ArrayList<PartnerProgramDTO> dataList;

            ObjectMapper objectMapper = new ObjectMapper();
            dataList = objectMapper.readValue(getRequest.get(), new TypeReference<List<PartnerProgramDTO>>() {
            });

            //Создаем адаптер, исходя из нашего списка партнеров
            GridAdapterPartnerProgram partnerAdapter = new GridAdapterPartnerProgram(this.getContext(), dataList);

            //Передаем в GridView наш адаптер с данными
            gridView.setAdapter(partnerAdapter);


            //Устанавливаем Listener для элементов GridView
            gridView.setOnItemClickListener((a, v, position, id) -> {
                //Получаем данный элемент
                Object item = gridView.getItemAtPosition(position);
                PartnerProgramDTO partner = (PartnerProgramDTO) item;

                PartnerProgramFragmentDirections.ActionNavigationPartnerProgramToInformationAboutPartnerFragment action =
                        PartnerProgramFragmentDirections.actionNavigationPartnerProgramToInformationAboutPartnerFragment(partner.getImageName(), partner.getTitle(),
                                partner.getDescription(),
                                partner.getPrice(),
                                partner.getId());

                //((MainActivity)getActivity()).navigateIn(MainActivity.TAB_PARTNER_PROGRAM, new InformationAboutPartnerFragment(), action.getArguments());
            });

        } catch (Exception e) {
            e.getMessage();
        }

    }

    /*@Override
    public void onDestroyView() {
        ((MainActivity)getActivity()).addInStack(MainActivity.TAB_PARTNER_PROGRAM, this);
        super.onDestroyView();
    }*/

}
