package com.example.unimag.ui.pay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.unimag.R;
import com.example.unimag.ui.ThreadCheckingConnection;
import com.example.unimag.ui.catalog.CatalogFragment;

public class RegisterOrderFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_register_order, container, false);

        new ThreadCheckingConnection(getFragmentManager(), savedInstanceState).execute(); //Если дисконект

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button button_register_order = getView().findViewById(R.id.button_register_order);
        button_register_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Временно
                Toast toast = Toast.makeText(RegisterOrderFragment.this.getContext(),
                        "Покупка оплачена!\nСтатус доставки можно посмотреть в личном кабинете", Toast.LENGTH_LONG);
                toast.show();

                //Временно
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(RegisterOrderFragment.this.getId(), new CatalogFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

    }

}
