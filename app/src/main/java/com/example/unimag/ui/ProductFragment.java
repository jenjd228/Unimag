package com.example.unimag.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.bumptech.glide.Glide;
import com.example.unimag.R;
import com.example.unimag.ui.Request.AddRequest;
import com.example.unimag.ui.SqLite.DataDBHelper;

import java.util.concurrent.ExecutionException;



public class ProductFragment extends Fragment { //Класс шаблона страницы с продуктом

    private DataDBHelper dataDbHelper;
    private Integer productId;
    private String title;
    private String descriptions;
    private Integer price;
    private String imageName;
    String secureKod = null;

    public ProductFragment(String imageName,String title, String descriptions, Integer price,Integer productId){
        this.title = title;
        this.descriptions = descriptions;
        this.price = price;
        this.productId = productId;
        this.imageName = imageName;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataDbHelper = new DataDBHelper(getActivity());
        secureKod = dataDbHelper.getSecureKod(dataDbHelper);
        dataDbHelper.close();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_product, container, false);
        return root;
    }

    public void setTitleProduct(String title){ //Функция установки значения названия продукта
        TextView titleProduct = getView().findViewById(R.id.title_product);
        titleProduct.setText(title);
    }

    public void setPriceProduct(Integer price){ //Функция установки значения цены продукта
        TextView priceProduct = getView().findViewById(R.id.price_product);
        priceProduct.setText(String.valueOf(price));
    }

    public void setImageProduct(String url){ //Функция установки изображения продукта
        //setImageURI(Uri uri) — загружает изображение по его URI
        ImageView imageProduct = getView().findViewById(R.id.image_product);

        Glide.with(getView()).load("http://192.168.31.143:8080/upload/"+url).into(imageProduct);
    }

    public void setDescriptionProduct(String description){ //Функция установки значения описания продукта
        TextView descriptionProduct = getView().findViewById(R.id.description_product);
        descriptionProduct.setText("Описание: " + description);
    }

    public void setInformationAboutProduct(){ //Функция установки всей информации о продукте
        //Через idProduct связываемся и получаем нуную инфу тут
        setImageProduct(imageName);
        setTitleProduct(title);
        setPriceProduct(price);
        setDescriptionProduct(descriptions);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setInformationAboutProduct(); //Получение информации о продукте

        Button b = getView().findViewById(R.id.button_add_basket);
        b.setOnClickListener(e -> {
            if (secureKod==null){

            }else {
                AddRequest addRequest = new AddRequest(productId,secureKod,"addToBasket");
                addRequest.execute();
                try {
                    if(addRequest.get().equals("ok")){
                        Toast toast = Toast.makeText(getContext(),
                                "Товар добавлен!", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(getContext(),
                                "Ошибка!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } catch (ExecutionException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    /*class CreateAndSendRequast extends AsyncTask<Void, Void, Response> {
        private Integer id;
        private String secureKod;
        CreateAndSendRequast(Integer id,String secureKod){
            this.id = id;
            this.secureKod = secureKod;
        }

        @Override
        protected Response doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("id", id.toString()) // A simple POST field
                    .add("secureKod",secureKod)
                    .build();
            Request request = new Request.Builder()
                    .url("http://192.168.31.143:8080/addToBasket") // The URL to send the data to
                    .post(formBody)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute(); //ответ сервера
                //System.out.println(response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response result) {
            super.onPostExecute(result);
            String otvet;
            otvet = null;
            try {
                otvet = result.body().string();
                if(otvet.equals("ok")){
                    Toast toast = Toast.makeText(getContext(),
                            "Товар добавлен!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getContext(),
                            "Ошибка!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }*/
}
