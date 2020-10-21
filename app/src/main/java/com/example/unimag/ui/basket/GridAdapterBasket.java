package com.example.unimag.ui.basket;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.unimag.R;
import com.example.unimag.ui.DTO.BasketProductDTO;
import com.example.unimag.ui.GlobalVar;
import com.example.unimag.ui.Request.AddRequest;
import com.example.unimag.ui.Request.DeleteRequest;
import com.example.unimag.ui.SqLite.DataDBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GridAdapterBasket  extends BaseAdapter {

    private List<BasketProductDTO> listData;
    private List<BasketProductDTO> listProductForPay = new ArrayList<>();;
    private LayoutInflater layoutInflater;
    private Context context;
    private String secureKod = null;
    private DataDBHelper dataDbHelper;


    public GridAdapterBasket(Context aContext, List<BasketProductDTO> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    public View getView(int position, View convertView, ViewGroup parent) {
        dataDbHelper = new DataDBHelper(context);
        secureKod = dataDbHelper.getSecureKod(dataDbHelper);
        dataDbHelper.close();

        GridAdapterBasket.ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.basket_product_item_layout, null);
            holder = new GridAdapterBasket.ViewHolder();
            holder.basketLayout = convertView.findViewById(R.id.basket_product);
            holder.imageView = convertView.findViewById(R.id.image_basket_product);
            holder.productPrice = convertView.findViewById(R.id.price_basket_product);
            holder.productTitle= convertView.findViewById(R.id.title_basket_product);
            holder.productDescription = convertView.findViewById(R.id.description_basket_product);
            holder.itemCheckBox = convertView.findViewById(R.id.checkBoxBasket);
            holder.deleteButton = convertView.findViewById(R.id.deleteButton);
            holder.addProductCountButton = convertView.findViewById(R.id.addProductCountButton);
            holder.deleteProductCountButton = convertView.findViewById(R.id.deleteProductCountButton);
            holder.productCount = convertView.findViewById(R.id.productCount);
            convertView.setTag(holder);
        } else {
            holder = (GridAdapterBasket.ViewHolder) convertView.getTag();
        }

        BasketProductDTO product = this.listData.get(position);
        holder.productTitle.setText(product.getTitle());
        holder.productDescription.setText("" + product.getDescriptions());
        holder.productPrice.setText("" + product.getPrice()*product.getCount());
        holder.productCount.setText("" + product.getCount());

        Integer id = product.getProductId();

        if(position%2==0) {
            holder.basketLayout.setBackgroundColor(Color.parseColor("#dadfe0"));
        }
        //int imageId = this.getMipmapResIdByName("image");

        //holder.imageView.setImageResource(imageId);
        Glide.with(convertView).load("http://"+ GlobalVar.ip +":8080/upload/"+product.getImageName()).into(holder.imageView);

        holder.deleteButton.setOnClickListener(v -> {
            DeleteRequest deleteRequest = new DeleteRequest(secureKod,product.getProductId(),"deleteBasketProduct");
            deleteRequest.execute();
            try {
                if(deleteRequest.get().equals("ok")){
                    Toast toast = Toast.makeText(context,
                            "Товар удален!", Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    Toast toast = Toast.makeText(context,
                            "Ошибка!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            listData.remove(product);
            notifyDataSetChanged();
        });

        holder.addProductCountButton.setOnClickListener(v -> {
            if (secureKod==null){

            }else {
                AddRequest addRequest = new AddRequest(product.getProductId(),secureKod,"addOneProductToBasket");
                addRequest.execute();
                try {
                    if(addRequest.get().equals("ok")){
                        Toast toast = Toast.makeText(context,
                                "Товар добавлен!", Toast.LENGTH_SHORT);
                        toast.show();
                        listData.get(position).setCount(product.getCount()+1);
                        notifyDataSetChanged();
                    } else {
                        Toast toast = Toast.makeText(context,
                                "Ошибка!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } catch (ExecutionException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });

        holder.deleteProductCountButton.setOnClickListener(v -> {
            if (listData.get(position).getCount()-1 ==0){

            }else {
                if (secureKod!=null){
                    DeleteRequest deleteRequest = new DeleteRequest(secureKod ,product.getProductId(),"deleteOneProductFromBasket");
                    deleteRequest.execute();
                    try {
                        if(deleteRequest.get().equals("ok")){
                            Toast toast = Toast.makeText(context,
                                    "Товар удален!", Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            Toast toast = Toast.makeText(context,
                                    "Ошибка!", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    } catch (ExecutionException | InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                listData.get(position).setCount(product.getCount()-1);
                notifyDataSetChanged();
            }
        });


        holder.itemCheckBox.setOnClickListener(
                v -> {
                    Toast toast = Toast.makeText(context,
                            "Выбран товар с id = " + id+" - "+holder.itemCheckBox.isChecked(), Toast.LENGTH_SHORT);
                    toast.show();
                    if (holder.itemCheckBox.isChecked()) {
                        listProductForPay.add(product);
                    }else {
                        listProductForPay.remove(product);
                    }
                }
        );
        return convertView;
    }

    // Find Image ID corresponding to the name of the image (in the directory mipmap).
    public int getMipmapResIdByName(String resName)  {
        String pkgName = context.getPackageName();

        // Return 0 if not found.
        int resID = context.getResources().getIdentifier(resName , "mipmap", pkgName);
        Log.i("CustomGridView", "Res Name: "+ resName+"==> Res ID = "+ resID);
        return resID;
    }

    public void addList(List<BasketProductDTO> listData){
        this.listData.addAll(listData);
        notifyDataSetChanged();
    }

    public boolean findProductInBasketList(Integer id){
        for (BasketProductDTO basketProduct : listData){
            if (basketProduct.getProductId().equals(id)){
                return true;
            }
        }
        return false;
    }

    public List<BasketProductDTO> getProductForPayList(){
        return listProductForPay;
    }

    static class ViewHolder {
        ImageView imageView;

        TextView productTitle;
        TextView productDescription;
        TextView productPrice;
        TextView productCount;

        LinearLayout basketLayout;

        CheckBox itemCheckBox;

        Button deleteButton;
        Button addProductCountButton;
        Button deleteProductCountButton;
    }

}