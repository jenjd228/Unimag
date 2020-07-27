package com.example.unimag.ui.catalog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.unimag.R;
import com.example.unimag.ui.DTO.ProductDTO;

import java.util.List;

public class CustomGridAdapter  extends BaseAdapter {

    private List<ProductDTO> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public CustomGridAdapter(Context aContext,  List<ProductDTO> listData) {
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

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.catalog_item_layout, null);
            holder = new ViewHolder();
            holder.flagView = (ImageView) convertView.findViewById(R.id.imageView_flag);
            holder.productTitle= (TextView) convertView.findViewById(R.id.textView_productTitle);
            holder.productDescription = (TextView) convertView.findViewById(R.id.textView_productDescription);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ProductDTO productDTO = this.listData.get(position);
        holder.productTitle.setText(productDTO.getTitle());
        holder.productDescription.setText("" + productDTO.getPrice());

        //int imageId = this.getMipmapResIdByName("image");
        Glide.with(convertView).load("http://192.168.31.143:8080/upload/"+ productDTO.getImageName()).into(holder.flagView);


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

    public void addList(List<ProductDTO> listData){
        this.listData.addAll(listData);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        ImageView flagView;
        TextView productTitle;
        TextView productDescription;
    }

}