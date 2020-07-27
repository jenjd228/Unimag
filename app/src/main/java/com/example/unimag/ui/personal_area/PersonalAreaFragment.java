package com.example.unimag.ui.personal_area;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.unimag.R;
import com.example.unimag.ui.CreateAndSendRequest;
import com.example.unimag.ui.Request.CheckRequest;
import com.example.unimag.ui.SqLite.DataDBHelper;
import com.example.unimag.ui.LoginFragment;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import lombok.SneakyThrows;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PersonalAreaFragment extends Fragment {
    private DataDBHelper dataDbHelper;
    private Boolean isLogin = false;
    View root;

    private com.example.unimag.ui.personal_area.PersonalAreaViewModel personalAreaViewModel;

    @SneakyThrows
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataDbHelper = new DataDBHelper(getActivity());
        String secureKod = dataDbHelper.getSecureKod(dataDbHelper);
        dataDbHelper.close();
        if (secureKod==null){
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(PersonalAreaFragment.this.getId(), new LoginFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        }
        checkBySecureKod(secureKod);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Создание View-Model для данного фрагмента (передаем данные Activity)
        personalAreaViewModel =
                ViewModelProviders.of(this).get(com.example.unimag.ui.personal_area.PersonalAreaViewModel.class);
//        //Создание View-элемента из XML-файла
        root = inflater.inflate(R.layout.fragment_personal_area, container, false);
        return root;
    }

    private void checkBySecureKod(String secureKod) throws ExecutionException, InterruptedException {
        if (secureKod==null){

        }else {
            CheckRequest checkRequest = new CheckRequest(secureKod,"checkBySecureKod");
            checkRequest.execute();
            try {
                if(checkRequest.get().equals("ok")){
                    //isLogin = true;
                    FragmentManager manager = PersonalAreaFragment.this.getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(PersonalAreaFragment.this.getId(), new MyCabinetFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(PersonalAreaFragment.this.getId(), new LoginFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
