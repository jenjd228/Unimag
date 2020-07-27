package com.example.unimag.ui.Product;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.unimag.R;
import com.example.unimag.ui.DTO.BasketProductDTO;
import com.example.unimag.ui.DTO.OrderDTO;
import com.example.unimag.ui.DTO.ProductDTO;
import com.example.unimag.ui.ProductFragment;
import com.example.unimag.ui.SqLite.DataDBHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderFragment extends Fragment {

    private GridAdapterOrder gridAdapterOrder;

    private DataDBHelper dataDbHelper;

    private String secureKod = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataDbHelper = new DataDBHelper(getActivity());
        secureKod = dataDbHelper.getSecureKod(dataDbHelper);
        dataDbHelper.close();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_orders, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //CreateAndSendRequast createAndSendRequast = new CreateAndSendRequast(secureKod);
        //createAndSendRequast.execute();

        List<OrderDTO> products = new ArrayList<>();
        //Создание нашего списка товаров с помощью функции getList()
        final GridView gridView = (GridView) getView().findViewById(R.id.grid_view_orders);
        //Установление адаптера (что-то вроде прохождения по всем элементам)
        gridView.setAdapter(gridAdapterOrder = new GridAdapterOrder(this.getContext(),products));

        //Установка onClickListenera для каждого элемента item
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = gridView.getItemAtPosition(position);
                ProductDTO productDTO = (ProductDTO) o;
                FragmentManager manager = OrderFragment.this.getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(OrderFragment.this.getId(), new ProductFragment(productDTO.getImageName(), productDTO.getTitle(), productDTO.getDescriptions(), productDTO.getPrice(), productDTO.getId()));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    /*class CreateAndSendRequast extends AsyncTask<Void, Void, String> {
        String secureKod;
        CreateAndSendRequast(String secureKod){
            this.secureKod = secureKod;
        }

        @Override
        protected String doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://192.168.31.143:8080/getBasketList/"+secureKod) // The URL to send the data to
                    .get()
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute(); //ответ сервера
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            List<BasketProductDTO> participantJsonList;
            try {
                if(result.equals("Error!")){
                    Toast toast = Toast.makeText(getContext(),
                            "Пусто!", Toast.LENGTH_SHORT);
                    toast.show();

                } else {
                    ObjectMapper objectMapper = new ObjectMapper();
                    participantJsonList = objectMapper.readValue(result, new TypeReference<List<ProductDTO>>(){});
                    gridAdapterOrder.addList(participantJsonList);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/

}
