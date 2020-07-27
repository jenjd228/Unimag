package com.example.unimag;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import DTO.ProductDTO;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Передача каждого идентификатора меню в виде набора идентификаторов
        //поскольку каждое меню следует рассматривать как пункты назначения верхнего уровня.

        //Создание переменно navView для всей нижней панели с меню
        BottomNavigationView navView = findViewById(R.id.nav_view);
        //?Связывание пунктов меню с их id фрагментами (mobile_navigation.XML)
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_catalog, R.id.navigation_news, R.id.navigation_basket, R.id.navigation_personal_area)
                .build();
        //??Создание области действия нашего контроллера для фрагмента nav_host_fragment(в activity_main.xml) - для всего экрана
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //?Установка (привязка) контроллера к нужным XML-фрагментам в файле mobile_navigation.XML
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        //Связывание (привязка) контроллера навигации и меню
        NavigationUI.setupWithNavController(navView, navController);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //FragmentManager manager = this.getSupportFragmentManager();
        //manager.popBackStack("Catalog",FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
