package com.example.unimag.ui.register;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.unimag.R;
import com.example.unimag.ui.CreateAndSendRequest;
import com.example.unimag.ui.Request.CheckRequest;
import com.example.unimag.ui.SqLite.DataDBHelper;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import lombok.SneakyThrows;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterKodFragment extends Fragment {
    private View root;
    private EditText kod;
    private String email;
    private String password;

    RegisterKodFragment(String email, String password){
        this.email = email;
        this.password = password;
    }

    /*RegisterKodFragment instOf(String email, String password){
        Bundle args = new Bundle();
        args.putString("email",email);
        args.putString("password",password);
        RegisterKodFragment registerKodFragment = new RegisterKodFragment();
        registerKodFragment.setArguments(args);
    }*/

    @Override
    public void onStart() {
        super.onStart();
        kod =  getActivity().findViewById(R.id.kod);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_register_kod, container, false);
        return root;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button b = getView().findViewById(R.id.register_button_kod);
        b.setOnClickListener(e -> {
            String kod1 = kod.getText().toString();
            /*String email = getArguments() != null ? getArguments().getString("email") : null;
            String password = getArguments() != null ? getArguments().getString("password") : null;*/
            checkByKod(kod1,email,password);
        });
    }

    @SneakyThrows
    private void checkByKod(String kod, String email, String password) {

        CheckRequest checkRequest = new CheckRequest(email,kod,password,"checkByKod");
        checkRequest.execute();
        String otvet = checkRequest.get();
        if(otvet.equals("Wrong kod")){
            Toast toast = Toast.makeText(getContext(),
                    "Вы ввели неверный код!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void goToNextFragmentRegistration(){
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(this.getId(), new RegisterFragment2(email));
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
