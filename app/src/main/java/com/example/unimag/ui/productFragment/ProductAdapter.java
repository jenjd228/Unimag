package com.example.unimag.ui.productFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.unimag.R;
import com.example.unimag.ui.GlobalVar;

public class ProductAdapter extends PagerAdapter {

    private Context context;
    private String[] listImageName; //Лист с названиями картинок
    private TextView numberStr;
    private ImageView imageView;


    //Конструктор класса
    public ProductAdapter(Context context, String[] listImageName) {
        this.context = context;
        this.listImageName = listImageName;
    }


    //Возвращает число элементов
    @Override
    public int getCount() {
        return listImageName.length;
    }


    //Возвращает true если все гуд
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    //Заполняет наш image_product.xml
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.image_product, container, false);

        numberStr = itemView.findViewById(R.id.number_str_product);
        imageView = itemView.findViewById(R.id.image_product_view);

        numberStr.setText(String.valueOf(position + 1) + "/" + String.valueOf(getCount()));
        //?????????????
        System.out.println(position);
        Glide.with(container).load("http://"+ GlobalVar.ip +":8080/upload/"+ listImageName[position]).into(imageView);

        container.addView(itemView);

        return itemView;
    }


    //Очищает после каждого скроллинга
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }

}
