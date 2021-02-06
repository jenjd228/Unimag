package com.example.unimag.ui.catalog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.unimag.R;
import com.example.unimag.ui.DTO.CatalogDTO;
import com.example.unimag.ui.GlobalVar;

import java.util.ArrayList;
import java.util.List;

public final class CustomGridAdapter extends BaseAdapter {

    private static List<CatalogDTO> listData;
    private LayoutInflater layoutInflater;
    private static CustomGridAdapter instance;

    public CustomGridAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        listData = new ArrayList<>();
    }

    public static CustomGridAdapter setContext(Context context) {
        if (instance == null) {
            instance = new CustomGridAdapter(context);
        }
        return instance;
    }

    public static CustomGridAdapter getInstance() {
        if (instance != null) {
            return instance;
        }
        return null;
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

    @SuppressLint({"InflateParams", "SetTextI18n"})
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.catalog_item_layout, null);
            holder = new ViewHolder();
            holder.flagView = convertView.findViewById(R.id.imageView_flag);
            holder.productTitle = convertView.findViewById(R.id.textView_productTitle);
            holder.productDescription = convertView.findViewById(R.id.textView_productDescription);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CatalogDTO catalogDTO = listData.get(position);
        holder.productTitle.setText(catalogDTO.getTitle());
        holder.productDescription.setText("" + catalogDTO.getPrice());

        //int imageId = this.getMipmapResIdByName("image");
        Glide.with(convertView).load(catalogDTO.getMainImage()).into(holder.flagView);


        return convertView;
    }

    // Find Image ID corresponding to the name of the image (in the directory mipmap).
    /*public int getMipmapResIdByName(String resName)  {
        String pkgName = context.getPackageName();

        // Return 0 if not found.
        int resID = context.getResources().getIdentifier(resName , "mipmap", pkgName);
        Log.i("CustomGridView", "Res Name: "+ resName+"==> Res ID = "+ resID);
        return resID;
    }*/
    public void cleanList() {
        listData.clear();
        notifyDataSetChanged();
    }

    public void addList(List<CatalogDTO> listData2) {
        listData.addAll(listData2);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        ImageView flagView;
        TextView productTitle;
        TextView productDescription;
    }

}