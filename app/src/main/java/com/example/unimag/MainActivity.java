package com.example.unimag;
import android.os.Bundle;
import android.view.MenuItem;


import com.example.unimag.ui.SqLite.DataDBHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class MainActivity extends AppCompatActivity implements  BottomNavigationView.OnNavigationItemSelectedListener {
    //private DataDBHelper dataDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //dataDbHelper = new DataDBHelper(this);
        //String secureKod = dataDbHelper.getSecureKod(dataDbHelper);
        //dataDbHelper.close();
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

        navView.setOnNavigationItemSelectedListener(this);


    }
    //onNavigationItemSelected


   /* binding.navView.setNavigationItemSelectedListener(this)

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.about_item -> navController.navigate(R.id.aboutFramgent)
            R.id.settings_item -> navController.navigate(R.id.settingsFramgent)
        }

        return true
    }*/

   /* @Override
    public void onBackPressed() {
        if (!recursivePopBackStack(getSupportFragmentManager())) {
            super.onBackPressed();
        }
    }

    private boolean recursivePopBackStack(FragmentManager fragmentManager) {
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment != null && fragment.isVisible()) {
                if (recursivePopBackStack(fragment.getChildFragmentManager())) {
                    return true;
                }
            }
        }

        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
            return true;
        }

        return false;
    }*/

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        System.out.println("Нажата кнопка меню");
        switch (item.getItemId()) {
            case R.id.navigation_catalog: Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.navigation_catalog);break;
            case R.id.navigation_news: Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.navigation_news);break;
            case R.id.navigation_basket: Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.navigation_basket);break;
            case R.id.navigation_personal_area: Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.navigation_personal_area);break;
        }

        return true;
    }
}
