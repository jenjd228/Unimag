package com.example.unimag.ui.pay;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.unimag.R;
import com.example.unimag.SimpleExampleActivity;
import com.example.unimag.ui.DTO.PayDTO;
import com.example.unimag.ui.DTO.UserDTO;
import com.example.unimag.ui.Request.GetRequest;
import com.example.unimag.ui.SqLite.DataDBHelper;
import com.example.unimag.ui.TechnicalWorkFragment;
import com.example.unimag.ui.ThreadCheckingConnection;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.SneakyThrows;

public class RegisterOrderFragment extends Fragment {

    private GridView gridView;

    private GridAdapterForPay gridAdapterForPay;

    private DataDBHelper dataDbHelper;

    private String secureKod;

    private String list;

    private ArrayList<String> listPickUpPoints = new ArrayList<>();

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

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false); //Убираем стрелочку назад

        new ThreadCheckingConnection(getFragmentManager(), requireContext()); //Проверка на подключение к интернету

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

    @SneakyThrows
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button button_register_order = requireView().findViewById(R.id.button_register_order);
        TextView totalMoney = requireView().findViewById(R.id.money);
        Spinner spinner = (Spinner) requireView().findViewById(R.id.spinnerPickUpPoint); //Ищем спиннер
        totalMoney.setText(String.valueOf(gridAdapterForPay.getTheCostOfProducts()));

        try {
            GetRequest getPickUpPointRequest = new GetRequest(requireContext(),getFragmentManager(), "getPickUpPointList");
            getPickUpPointRequest.execute();

            GetRequest getUser = new GetRequest(requireContext(),getFragmentManager(), secureKod, "getUser");
            getUser.execute();

            String userS = getUser.get();

            List<Integer> idProductList = new ArrayList<>();
            gridAdapterForPay.getProductList().forEach(object -> idProductList.add(object.getProductId()));

            List<String> listPickUpPoints = new ObjectMapper().readValue(getPickUpPointRequest.get(), new TypeReference<List<String>>() {
            });

            UserDTO user = new UserDTO();
            user.setEmail("");

            if (!userS.equals("Error!")) {
                user = new ObjectMapper().readValue(getUser.get(), new TypeReference<UserDTO>() {
                });
            }

            String[] arrayPickUpPoints = listPickUpPoints.toArray(new String[0]);

            //Настраиваем адаптер
            AdapterForSpinner adapter = new AdapterForSpinner(requireContext(), R.layout.spinner_row, arrayPickUpPoints);
            spinner.setAdapter(adapter);
            spinner.setSelection(0);

            UserDTO finalUser = user;

            button_register_order.setOnClickListener(v -> {
                //NavDirections action = RegisterOrderFragmentDirections.actionRegisterOrderFragmentToSimpleExampleActivity();
                //Navigation.findNavController(v).navigate(action);

                if (secureKod != null) {
                    Intent intent = new Intent(getActivity(), SimpleExampleActivity.class);
                    intent.putExtra("Amount", totalMoney.getText());
                    intent.putExtra("IdProductList", idProductList.toString());
                    intent.putExtra("secureKod", secureKod);
                    intent.putExtra("pickUpPoint", spinner.getSelectedItem().toString());
                    intent.putExtra("email", finalUser.getEmail());

                    startActivity(intent);
                }

            });

        } catch (IOException e) {
            e.getMessage();
        }

    }




    /** АДАПТЕР ДЛЯ СПИННЕРА
     *
     *
     *
     * */
    public class AdapterForSpinner extends ArrayAdapter<String> {

        String[] array; //Массив с пунктами выдачи заказов

        //Конструктор класса
        public AdapterForSpinner(Context context, int textViewResourceId,
                                 String[] objects) {
            super(context, textViewResourceId, objects);
            array = objects;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {

            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return getCustomView(position, convertView, parent);
        }


        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.spinner_row_pick_up_point, parent, false);
            TextView label = (TextView) row.findViewById(R.id.row_spinner_pick_up_point);
            label.setText(array[position].toString());

            return row;
        }

    }

}
