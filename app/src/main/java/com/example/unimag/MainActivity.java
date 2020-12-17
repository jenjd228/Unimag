package com.example.unimag;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.unimag.ui.SqLite.DataDBHelper;
import com.example.unimag.ui.sort.GlobalSort;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private DataDBHelper dataDbHelper;

    private String secureKod;

    GlobalSort globalSort = new GlobalSort();

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
                R.id.navigation_catalog, R.id.navigation_partner_program, R.id.navigation_basket, R.id.myCabinetFragment)
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

    /*@Override
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

    private void qwe(FragmentManager fragmentManager){
        for (Fragment es : fragmentManager.getFragments()){
            System.out.println(es.isVisible());
           if(es.isVisible()){
               es.getParentFragmentManager().popBackStack();
           }
            qwe(es.getChildFragmentManager());
        }
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        /*System.out.println("-----------------------------------");
        System.out.println(getSupportFragmentManager().getFragments().get(0).getChildFragmentManager().getFragments().get(0).getClass().getName());
        System.out.println(getSupportFragmentManager().getFragments().get(0).getChildFragmentManager().getBackStackEntryCount());
        FragmentManager e = getSupportFragmentManager().getFragments().get(0).getChildFragmentManager();
        qwe(e);
        System.out.println("-----------------------------------");*/
        switch (item.getItemId()) {
            case R.id.navigation_catalog:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.navigation_catalog);
                break;
            case R.id.navigation_partner_program:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.navigation_partner_program);
                break;
            case R.id.navigation_basket:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.navigation_basket);
                break;
            case R.id.myCabinetFragment: {

                dataDbHelper = new DataDBHelper(this);
                secureKod = dataDbHelper.getSecureKod(dataDbHelper);
                dataDbHelper.close();

                if (secureKod == null) {
                    Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.loginFragment);
                } else {
                    Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.myCabinetFragment);
                }
                break;
            }
        }

        return true;
    }
}
