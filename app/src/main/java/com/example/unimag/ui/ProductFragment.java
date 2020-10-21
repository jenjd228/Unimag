package com.example.unimag.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import com.example.unimag.R;
import com.example.unimag.ui.Request.AddRequest;
import com.example.unimag.ui.SqLite.DataDBHelper;
import com.example.unimag.ui.productFragment.ProductAdapter;

import java.util.concurrent.ExecutionException;



public class ProductFragment extends Fragment { //Класс шаблона страницы с продуктом

    private DataDBHelper dataDbHelper;
    private Integer productId;
    private String title;
    private String descriptions;
    private Integer price;
    private String imageName;
    String secureKod = null;

    public ProductFragment(){

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataDbHelper = new DataDBHelper(getActivity());
        secureKod = dataDbHelper.getSecureKod(dataDbHelper);
        dataDbHelper.close();

        productId = ProductFragmentArgs.fromBundle(requireArguments()).getProductId();
        title = ProductFragmentArgs.fromBundle(requireArguments()).getTitle();
        descriptions = ProductFragmentArgs.fromBundle(requireArguments()).getDescriptions();
        price = ProductFragmentArgs.fromBundle(requireArguments()).getPrice();
        imageName = ProductFragmentArgs.fromBundle(requireArguments()).getImageName();

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Информация о товаре");
        actionBar.setDisplayHomeAsUpEnabled(false);

        new ThreadCheckingConnection(getFragmentManager(), savedInstanceState).execute();
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
        //ImageView imageProduct = getView().findViewById(R.id.image_product);
        //Glide.with(getView()).load("http://"+GlobalVar.ip+":8080/upload/"+url).into(imageProduct);

        ViewPager viewPager = getView().findViewById(R.id.pagerProduct); //Находим пэйджер
        ProductAdapter adapter = new ProductAdapter(ProductFragment.this.getContext(), new String[]{url, "5.jpg", "6.jpg"}); //Заполняем его данными о товаре
        viewPager.setAdapter(adapter); //Устанавливаем адаптер
        viewPager.setCurrentItem(0); //Устанавливаем "курсор" на первую картинку

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

    @SuppressLint("ShowToast")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setInformationAboutProduct(); //Получение информации о продукте

        Button b = getView().findViewById(R.id.button_add_basket);
        b.setOnClickListener(e -> {
            if (secureKod==null){
                Toast toast = Toast.makeText(getContext(),
                        "Доступ к корзине открывается после создания аккаунта", Toast.LENGTH_SHORT);
                toast.show();
            }else {
                AddRequest addRequest = new AddRequest(productId,secureKod,"addToBasket");
                addRequest.execute();
                try {
                    String otvet = addRequest.get();
                    System.out.println(otvet);
                    if(otvet.equals("OK")){
                        Toast toast = Toast.makeText(getContext(),
                                "Товар добавлен!", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (otvet.equals("PRODUCT_IS_PRESENT")){
                        Toast toast = Toast.makeText(getContext(),
                                "Этот товар уже в корзине!", Toast.LENGTH_SHORT);
                        toast.show();
                    }else {
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
}
