package com.example.unimag.ui.basket;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.drm.DrmStore;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.unimag.R;
import com.example.unimag.ui.DTO.BasketProductDTO;
import com.example.unimag.ui.DTO.PayDTO;
import com.example.unimag.ui.Product.GridAdapterOrder;
import com.example.unimag.ui.DTO.ProductDTO;
import com.example.unimag.ui.ProductFragment;
import com.example.unimag.ui.Request.AddRequest;
import com.example.unimag.ui.Request.GetRequest;
import com.example.unimag.ui.SqLite.DataDBHelper;
import com.example.unimag.ui.ThreadCheckingConnection;
import com.example.unimag.ui.pay.GridAdapterForPay;
import com.example.unimag.ui.register.RegisterFragmentDirections;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.SneakyThrows;


public class BasketFragment extends Fragment {

    private DataDBHelper dataDbHelper;

    private BasketViewModel basketViewModel;

    private GridView gridView;

    private String secureKod = null;

    private GridAdapterBasket gridAdapterBasket;

    private GridAdapterOrder gridAdapterOrder;


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

        TextView buttonReady = requireView().findViewById(R.id.button_oformlenie_order);
        buttonReady.setOnClickListener(e -> {

            List<BasketProductDTO> products = new ArrayList<>();
            List<PayDTO> payDTOList = new ArrayList<>();
            if (gridAdapterBasket.getProductList()!=null){
                products = gridAdapterBasket.getProductList();
                for (BasketProductDTO basketProductDTO : products){
                    payDTOList.add(new PayDTO(basketProductDTO.getImageName(),basketProductDTO.getPrice(),basketProductDTO.getTitle(),basketProductDTO.getCount()));
                }
            }

            //Navigation.findNavController(requireView()).navigate(R.id.action_navigation_basket_to_registerOrderFragment);
            if (products.size()!=0){

                String list = new Gson().toJson(payDTOList);
                System.out.println(list + "-----");
                BasketFragmentDirections.ActionNavigationBasketToRegisterOrderFragment action = BasketFragmentDirections.actionNavigationBasketToRegisterOrderFragment(list);
                Navigation.findNavController(requireView()).navigate(action);

                /*StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0;i<products.size();i++){
                    if (i == products.size()-1){
                        stringBuilder.append(products.get(i).getProductId());
                        break;
                    }
                    stringBuilder.append(products.get(i).getProductId()).append(",");
                }
                Navigation.findNavController(requireView()).navigate(R.id.action_navigation_basket_to_registerOrderFragment);
                if (stringBuilder.length()!=0){
                    AddRequest addRequest = new AddRequest(stringBuilder.toString(),secureKod,"addToOrders");
                    addRequest.execute();

                    /*Toast toast = Toast.makeText(getContext(),
                            "Заказ оформлен!", Toast.LENGTH_LONG);
                    toast.show();*/
                //}
            }else {
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

        try{
            GetRequest getRequest = new GetRequest(secureKod,"getBasketList");
            getRequest.execute();
            String result = getRequest.get();
            if (result.equals("BAD_REQUEST") || result.equals("Error!")){

            }else {
                List<BasketProductDTO> participantJsonList;
                ObjectMapper objectMapper = new ObjectMapper();
                participantJsonList = objectMapper.readValue(getRequest.get(), new TypeReference<List<BasketProductDTO>>(){});
                gridAdapterBasket.addList(participantJsonList);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


        gridView.setOnItemClickListener((a, v, position, id) -> {
            Object o = gridView.getItemAtPosition(position);
            ProductDTO productDTO = (ProductDTO) o;
            /*Toast.makeText(getActivity(), "Selected :"
                    + " " + product +" "+id, Toast.LENGTH_LONG).show();*/
            BasketFragmentDirections.ActionNavigationBasketToProductFragment action = BasketFragmentDirections.actionNavigationBasketToProductFragment(productDTO.getImageName(), productDTO.getTitle(), productDTO.getDescriptions(), productDTO.getPrice(), productDTO.getId());
            Navigation.findNavController(v).navigate(action);

        });

        return view;
    }
}

