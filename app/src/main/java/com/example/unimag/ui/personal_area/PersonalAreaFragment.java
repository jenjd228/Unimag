package com.example.unimag.ui.personal_area;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.unimag.R;
import com.example.unimag.ui.Request.CheckRequest;
import com.example.unimag.ui.SqLite.DataDBHelper;
import com.example.unimag.ui.ThreadCheckingConnection;

import java.util.concurrent.ExecutionException;

import lombok.SneakyThrows;

public class PersonalAreaFragment extends Fragment {
    private DataDBHelper dataDbHelper;
    private Boolean isLogin = false;
    View root;
    String secureKod;

    private PersonalAreaViewModel personalAreaViewModel;

    @SneakyThrows
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataDbHelper = new DataDBHelper(getActivity());
        secureKod = dataDbHelper.getSecureKod(dataDbHelper);
        dataDbHelper.close();
    }

    @SneakyThrows
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkBySecureKod(secureKod);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        new ThreadCheckingConnection(getFragmentManager(), savedInstanceState).execute();
        //Создание View-Model для данного фрагмента (передаем данные Activity)
//        //Создание View-элемента из XML-файла
        root = inflater.inflate(R.layout.fragment_personal_area, container, false);
        return root;
    }

    private void checkBySecureKod(String secureKod) throws ExecutionException, InterruptedException {
        if (secureKod==null){
            Navigation.findNavController(requireView()).navigate(R.id.action_navigation_personal_area_to_loginFragment);
        }else {
            try {
            CheckRequest checkRequest = new CheckRequest(secureKod,"checkBySecureKod");
            checkRequest.execute();
                if(checkRequest.get().equals("ok")){
                    //isLogin = true;
                    Navigation.findNavController(requireView()).navigate(R.id.action_navigation_personal_area_to_myCabinetFragment);
                    System.out.println("-----------------------");
                    System.out.println(Navigation.findNavController(requireView()).getBackStackEntry(Navigation.findNavController(requireView()).getCurrentDestination().getId()).toString());
                    System.out.println("-----------------------");
                } else {
                    Navigation.findNavController(requireView()).navigate(R.id.action_navigation_personal_area_to_loginFragment);
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
