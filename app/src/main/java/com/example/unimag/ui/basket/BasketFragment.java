package com.example.unimag.ui.basket;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.unimag.R;
import com.example.unimag.ui.DTO.BasketProductDTO;
import com.example.unimag.ui.DTO.PayDTO;
import com.example.unimag.ui.DTO.ProductDTO;
import com.example.unimag.ui.Request.GetRequest;
import com.example.unimag.ui.SqLite.DataDBHelper;
import com.example.unimag.ui.ThreadCheckingConnection;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;


public class BasketFragment extends Fragment {

    private DataDBHelper dataDbHelper;

    private BasketViewModel basketViewModel;

    private GridView gridView;

    private String secureKod = null;

    private GridAdapterBasket gridAdapterBasket;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataDbHelper = new DataDBHelper(getActivity());
        secureKod = dataDbHelper.getSecureKod(dataDbHelper);
        dataDbHelper.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GridView gridView = requireView().findViewById(R.id.grid_view_basket);

        TextView buttonReady = requireView().findViewById(R.id.button_oformlenie_order);

        if (gridAdapterBasket.getCount() == 0) {
            @SuppressLint("UseCompatLoadingForDrawables") Drawable drawable = requireActivity().getResources().getDrawable(R.drawable.unnamed);
            gridView.setBackground(drawable);
        } else {
            gridView.setBackground(null);
        }

        buttonReady.setOnClickListener(e -> {
            List<BasketProductDTO> products = new ArrayList<>();

            List<PayDTO> payDTOIdList = new ArrayList<>();

            if (gridAdapterBasket.getProductForPayList() != null) {
                products = gridAdapterBasket.getProductForPayList();
                for (BasketProductDTO basketProductDTO : products) {
                    payDTOIdList.add(new PayDTO(basketProductDTO.getProductId(), basketProductDTO.getImageName(), basketProductDTO.getPrice(), basketProductDTO.getTitle(), basketProductDTO.getCount()));
                }
            }

            if (products.size() != 0) {
                String list = new Gson().toJson(payDTOIdList);
                NavDirections action = BasketFragmentDirections.actionNavigationBasketToRegisterOrderFragment(list);
                Navigation.findNavController(requireView()).navigate(action);

            } else {
                Toast toast = Toast.makeText(getContext(),
                        "Вы не выбрали товар!", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    @SneakyThrows
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        new ThreadCheckingConnection(getFragmentManager(), savedInstanceState).execute();
        basketViewModel =
                ViewModelProviders.of(this).get(BasketViewModel.class);
        View view = inflater.inflate(R.layout.fragment_basket, container, false);
        gridView = view.findViewById(R.id.grid_view_basket);

        gridView.setAdapter(gridAdapterBasket = new GridAdapterBasket(this.getContext(), new ArrayList<>()));

        try {
            GetRequest getRequest = new GetRequest(secureKod, "getBasketList");
            getRequest.execute();
            String result = getRequest.get();
            if (result.equals("BAD_REQUEST") || result.equals("Error!")) {
                Toast toast = Toast.makeText(getContext(),
                        "Ошибка!", Toast.LENGTH_LONG);
                toast.show();
            } else {
                List<BasketProductDTO> participantJsonList;
                ObjectMapper objectMapper = new ObjectMapper();
                participantJsonList = objectMapper.readValue(getRequest.get(), new TypeReference<List<BasketProductDTO>>() {
                });
                gridAdapterBasket.addList(participantJsonList);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        gridView.setOnItemClickListener((a, v, position, id) -> {
            Object o = gridView.getItemAtPosition(position);
            ProductDTO productDTO = (ProductDTO) o;

            BasketFragmentDirections.ActionNavigationBasketToProductFragment action = BasketFragmentDirections.actionNavigationBasketToProductFragment(productDTO.getImageName(), productDTO.getTitle(), productDTO.getDescriptions(), productDTO.getPrice(), productDTO.getId(), productDTO.getCategory(), productDTO.getListImage());
            Navigation.findNavController(v).navigate(action);
        });

        return view;
    }
}

