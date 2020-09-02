package com.example.unimag.ui.partner_program;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unimag.R;
import com.example.unimag.ui.DTO.PartnerProgramDTO;
import com.example.unimag.ui.ProductFragment;
import com.example.unimag.ui.ThreadCheckingConnection;
import com.example.unimag.ui.catalog.CatalogFragment;

import java.util.ArrayList;

public class PartnerProgramFragment extends Fragment {

    GridView gridView; //RecyclerView со списком всех партнеров


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_partner_program, container, false);

        new ThreadCheckingConnection(getFragmentManager(), savedInstanceState).execute(); //Если дисконект

        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Находим GridView
        gridView = getView().findViewById(R.id.recycler_view_partner_program);

        //Тестовый список партнеров
        ArrayList<PartnerProgramDTO> dataList = new ArrayList<PartnerProgramDTO>();

        PartnerProgramDTO p1 = new PartnerProgramDTO();
        p1.setTitle("Adidas");
        p1.setDescription("Скидка 10% на первую покупку\n(Акция действет ежемесячно)");
        p1.setImageName("adidas.png");
        p1.setPrice(200);

        PartnerProgramDTO p2 = new PartnerProgramDTO();
        p2.setTitle("Red Bull");
        p2.setDescription("Скидка 5%\nПри подписке на срок более 3х месяцев скидка 8% на любую покупку");
        p2.setImageName("red_bull.png");
        p2.setPrice(150);

        PartnerProgramDTO p3 = new PartnerProgramDTO();
        p3.setTitle("Nike");
        p3.setDescription("Верните до 15% при покупке от 3000 рублей\nАкция дейстительна до 27.09.2020");
        p3.setImageName("nike.png");
        p3.setPrice(250);

        dataList.add(p1);
        dataList.add(p2);
        dataList.add(p3);

        //Создаем адаптер, исходя из нашего списка партнеров
        GridAdapterPartnerProgram partnerAdapter = new GridAdapterPartnerProgram(this.getContext(), dataList);

        //Передаем в GridView наш адаптер с данными
        gridView.setAdapter(partnerAdapter);


        //Устанавливаем Listener для элементов GridView
        gridView.setOnItemClickListener((a, v, position, id) -> {
            //Получаем данный элемент
            Object item = gridView.getItemAtPosition(position);
            PartnerProgramDTO partner = (PartnerProgramDTO) item;

            //Настраиваем переход
            FragmentManager manager = PartnerProgramFragment.this.getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(PartnerProgramFragment.this.getId(), new InformationAboutPartnerFragment(
                    partner.getImageName(),
                    partner.getTitle(),
                    partner.getPrice(),
                    partner.getDescription()));
            transaction.addToBackStack(null);
            transaction.commit();
        });

    }

}
