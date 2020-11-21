package com.example.unimag.ui.productFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.unimag.R;
import com.example.unimag.ui.GlobalVar;

import java.util.List;

public class ProductAdapter extends PagerAdapter {

    private Context context;
    private List<String> listImageName; //Лист с названиями картинок
    private TextView numberStr;
    private ImageView imageView;


    //Конструктор класса
    public ProductAdapter(Context context, List<String> listImageName) {
        this.context = context;
        this.listImageName = listImageName;
    }


    //Возвращает число элементов
    @Override
    public int getCount() {
        return listImageName.size();
    }


    //Возвращает true если все гуд
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    //Заполняет наш image_product.xml
    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.image_product, container, false);

        numberStr = itemView.findViewById(R.id.number_str_product);
        imageView = itemView.findViewById(R.id.image_product_view);

        numberStr.setText((position + 1) + "/" + getCount());
        //?????????????
        System.out.println(listImageName.get(position) + "-----------------------------------");
        Glide.with(container).load("http://"+ GlobalVar.ip +":8080/upload/"+ listImageName.get(position)).into(imageView);

        container.addView(itemView);

        return itemView;
    }


    //Очищает после каждого скроллинга
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }

}
