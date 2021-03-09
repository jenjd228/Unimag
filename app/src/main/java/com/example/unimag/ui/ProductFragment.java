package com.example.unimag.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.unimag.MainActivity;
import com.example.unimag.R;
import com.example.unimag.ui.Request.AddRequest;
import com.example.unimag.ui.SqLite.DataDBHelper;
import com.example.unimag.ui.productFragment.ProductAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class ProductFragment extends Fragment { //Класс шаблона страницы с продуктом

    private DataDBHelper dataDbHelper;
    private String productHash;
    private String title;
    private String descriptions;
    private Integer price;
    private String mainImage;
    private String category;
    private String secureKod = null;
    private ArrayList<String> listSizeClothes = new ArrayList<>(); //Лист с доступными размерами одежды для данной страницы
    private String listImage;

    public ProductFragment() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataDbHelper = new DataDBHelper(getActivity());
        secureKod = dataDbHelper.getSecureKod(dataDbHelper);
        dataDbHelper.close();

        productHash = ProductFragmentArgs.fromBundle(requireArguments()).getProductHash();
        title = ProductFragmentArgs.fromBundle(requireArguments()).getTitle();
        descriptions = ProductFragmentArgs.fromBundle(requireArguments()).getDescriptions();
        price = ProductFragmentArgs.fromBundle(requireArguments()).getPrice();
        mainImage = ProductFragmentArgs.fromBundle(requireArguments()).getMainImage();
        category = ProductFragmentArgs.fromBundle(requireArguments()).getCategory();
        listImage = ProductFragmentArgs.fromBundle(requireArguments()).getListImage();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Информация о товаре");
        actionBar.setDisplayHomeAsUpEnabled(false);

        new ThreadCheckingConnection(getParentFragmentManager(), requireContext());

        View root = inflater.inflate(R.layout.fragment_product, container, false);
        return root;
    }

    public void setTitleProduct() { //Функция установки значения названия продукта
        TextView titleProduct = getView().findViewById(R.id.title_product);
        titleProduct.setText(title);
    }

    public void setPriceProduct() { //Функция установки значения цены продукта
        TextView priceProduct = getView().findViewById(R.id.price_product);
        priceProduct.setText(String.valueOf(price));
    }

    public void setImageProduct() { //Функция установки изображения продукта
        //ImageView imageProduct = getView().findViewById(R.id.image_product);
        //Glide.with(getView()).load("http://"+GlobalVar.ip+":8080/upload/"+url).into(imageProduct);
        List<String> strings = new ArrayList<>();
        ProductAdapter adapter;

        if (listImage.equals(" ")) {
            strings.add(mainImage);
        } else {
            strings = Arrays.asList(listImage.split(","));
        }

        ViewPager viewPager = getView().findViewById(R.id.pagerProduct); //Находим пэйджер
        adapter = new ProductAdapter(ProductFragment.this.getContext(), strings); //Заполняем его данными о товаре
        viewPager.setAdapter(adapter); //Устанавливаем адаптер
        viewPager.setCurrentItem(0); //Устанавливаем "курсор" на первую картинку


    }

    public void setDescriptionProduct() { //Функция установки значения описания продукта
        TextView descriptionProduct = requireView().findViewById(R.id.description_product);
        descriptionProduct.setText("Описание: " + descriptions);
    }

    public void setInformationAboutProduct() { //Функция установки всей информации о продукте
        //Через idProduct связываемся и получаем нуную инфу тут
        setImageProduct();
        setTitleProduct();
        setPriceProduct();
        setDescriptionProduct();
    }

    @SuppressLint("ShowToast")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setInformationAboutProduct(); //Получение информации о продукте

        /**ВРЕМЕННО*/
        listSizeClothes.add("42");
        listSizeClothes.add("44");
        listSizeClothes.add("46");
        listSizeClothes.add("48");
        listSizeClothes.add("50");

        Button b = requireView().findViewById(R.id.button_add_basket);
        b.setOnClickListener(e -> {
            if (secureKod == null) {
                Toast toast = Toast.makeText(getContext(),
                        "Доступ к корзине открывается после создания аккаунта", Toast.LENGTH_SHORT);
                toast.show();
            } else {

                /**--------------------------------------------------------------------
                 * Если категория = одежда добавляем всплывающее окно с выбором размера
                 * --------------------------------------------------------------------*/
                if (category.equals("Clothes")) {

                    //Делаем из листа - массив
                    //Integer[] arraySizeClothes = listSizeClothes.toArray(new Integer[0]);
                    //System.out.println(arraySizeClothes[0]);
                    String[] arraySizeClothes = listSizeClothes.toArray(new String[0]);

                    //Всплывающее окно
                    Dialog dialog = new Dialog(getActivity(), R.style.Dialog); //Создаем диалоговое окно
                    dialog.setContentView(R.layout.dialog_additional_information_for_clothes); //Устанавливаем разметку в окне
                    WindowManager.LayoutParams params = dialog.getWindow().getAttributes(); //Получаем текущие атрибуты
                    //Устанавливаем атрибуты
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    params.height = getActivity().getWindowManager().getDefaultDisplay().getHeight() * 3 / 4;
                    params.gravity = Gravity.BOTTOM;
                    dialog.getWindow().setAttributes(params);

                    //Создаем спиннер для одежды
                    //Получаем экземпляр элемента Spinner
                    LayoutInflater inflater = this.getLayoutInflater(); //Для поиска вне данной разметки
                    Spinner spinner = (Spinner) dialog.findViewById(R.id.spinnerSizeClothes); //Ищем спиннер

                    //Настраиваем адаптер
                    AdapterForSpinner adapter = new AdapterForSpinner(requireContext(), R.layout.spinner_row, arraySizeClothes);
                    spinner.setAdapter(adapter);
                    spinner.setSelection(0);

                    dialog.show(); //Отображаем окно

                    //Устанавливаем Листенер для кнопки "Добавить"
                    Button button_cancel = dialog.findViewById(R.id.button_add_basket_2);
                    button_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /**Здесь должна быть отправка размера*/
                            AddRequest addRequest = new AddRequest(requireContext(), getParentFragmentManager(), productHash, secureKod, "Non", spinner.getSelectedItem().toString(), "addToBasket"); //Отправляем дополнительно размер
                            addRequest.execute();

                            try {
                                String otvet = addRequest.get();
                                showToast(otvet);
                            } catch (ExecutionException | InterruptedException ex) {
                                ex.printStackTrace();
                            }

                            dialog.dismiss();
                        }
                    });
                } else {
                    //Иначе если не одежда то просто запрос
                    AddRequest addRequest = new AddRequest(requireContext(), getParentFragmentManager(), productHash, secureKod, "Non","Non", "addToBasket");
                    addRequest.execute();
                    try {
                        String otvet = addRequest.get();
                        showToast(otvet);
                    } catch (ExecutionException | InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void showToast(String otvet) {
        if (otvet.equals("OK")) {
            Toast toast = Toast.makeText(getContext(),
                    "Товар добавлен!", Toast.LENGTH_SHORT);
            toast.show();
        } else if (otvet.equals("PRODUCT_IS_PRESENT")) {
            Toast toast = Toast.makeText(getContext(),
                    "Этот товар уже в корзине!", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Toast toast = Toast.makeText(getContext(),
                    "Ошибка!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    /**
     * АДАПТЕР ДЛЯ СПИННЕРА
     */
    public class AdapterForSpinner extends ArrayAdapter<String> {

        String[] array; //Массив с размерами одежды

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
            View row = inflater.inflate(R.layout.spinner_row, parent, false);
            TextView label = (TextView) row.findViewById(R.id.row_spinner_size_text);
            label.setText(array[position]);

            return row;
        }

    }

    @Override
    public void onDestroyView() {
        System.out.println("Destroy product");
        listSizeClothes.clear();
        ((MainActivity)getActivity()).addInStack(MainActivity.TAB_CATALOG, this);
        super.onDestroyView();
    }
}
