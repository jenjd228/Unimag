package com.example.unimag.ui;

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

import java.io.IOException;
import java.util.Random;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterKodFragment extends Fragment {
    private View root;
    private EditText kod;

    @Override
    public void onStart() {
        super.onStart();
        kod =  getActivity().findViewById(R.id.kod);
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
            checkByKod(kod1);
        });
    }

    private void checkByKod(String kod){

        CreateAndSendRequast createAndSendRequast = new CreateAndSendRequast(kod);
        createAndSendRequast.execute();
    }

    private void goToNextFragmentRegistration(){
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(this.getId(), new RegisterFragment2());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    class CreateAndSendRequast extends AsyncTask<Void, Void, Response> {
        private String kod;
        CreateAndSendRequast(String kod){
            this.kod = kod;
        }

        @Override
        protected Response doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()// A simple POST field
                    .add("kod",  kod) // Another sample POST field
                    .build();
            Request request = new Request.Builder()
                    .url("http://192.168.31.143:8080/checkByKod") // The URL to send the data to
                    .post(formBody)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute(); //ответ сервера
                //System.out.println(response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response result) {
            super.onPostExecute(result);
            try {
                if(result.body().string().equals("Normal kod")){
                    goToNextFragmentRegistration();
                } else {
                    Toast toast = Toast.makeText(getContext(),
                            "Вы ввели неверный код!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        private String getKod(){
            Random random = new Random();
            int rage=9999;
            return String.valueOf(random.nextInt(rage));
        }

    }
}
