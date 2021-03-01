package com.example.unimag;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.unimag.ui.SqLite.DataDBHelper;
import com.example.unimag.ui.basket.BasketFragment;
import com.example.unimag.ui.catalog.CatalogFragment;
import com.example.unimag.ui.partner_program.PartnerProgramFragment;
import com.example.unimag.ui.sort.GlobalSort;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private DataDBHelper dataDbHelper;
    private String secureKod;
    GlobalSort globalSort = new GlobalSort();

    private HashMap<String, Stack<Fragment>> mStacks;      //Стек с фрагментами
    private HashMap<String, Stack<Bundle>> mStacksBundle;  //Стек с бандлами от фрагментов

    //Названия ключей для каждого пункта меню
    public static final String TAB_CATALOG  = "TAB_CATALOG";
    public static final String TAB_PARTNER_PROGRAM  = "TAB_PARTNER_PROGRAM";
    public static final String TAB_BASKET  = "TAB_BASKET";

    private String mCurrentTab;    //Текущий выбранный пункт меню
    private String lastCurrentTab; //Предыдущий выбранный пункт меню


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



        //Первоначальная инициализация всех стеков
        mStacks = new HashMap<String, Stack<Fragment>>();
        mStacks.put(TAB_CATALOG, new Stack<Fragment>());
        mStacks.put(TAB_PARTNER_PROGRAM, new Stack<Fragment>());
        mStacks.put(TAB_BASKET, new Stack<Fragment>());
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new CatalogFragment()).addToBackStack(null).commit();

        mStacksBundle = new HashMap<String, Stack<Bundle>>();
        mStacksBundle.put(TAB_CATALOG, new Stack<Bundle>());
        mStacksBundle.put(TAB_PARTNER_PROGRAM, new Stack<Bundle>());
        mStacksBundle.put(TAB_BASKET, new Stack<Bundle>());

        //Первоначальная инициализация выбранного пункта меню
        mCurrentTab = TAB_CATALOG;

        System.out.println("____________________________");
        System.out.println(mStacks);
        System.out.println(mStacksBundle);
        System.out.println("____________________________");
    }


    @SuppressLint("RestrictedApi")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_catalog:
                selectedTab(TAB_CATALOG);
                //Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.navigation_catalog);
                break;
            case R.id.navigation_partner_program:
                selectedTab(TAB_PARTNER_PROGRAM);
                //Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.navigation_partner_program);
                break;
            case R.id.navigation_basket:
                selectedTab(TAB_BASKET);
                //Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.navigation_basket);
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


    private void selectedTab(String tabId)
    {
        lastCurrentTab = mCurrentTab;
        mCurrentTab = tabId;

        //Если в первый раз жмем
        if(mStacks.get(tabId).size() == 0){
            //Смотрим куда нажали (каталог и лк - исключительные случаи)
            if(tabId.equals(TAB_CATALOG)){
                navigateIn(tabId, new CatalogFragment(), new Bundle());
            }else if(tabId.equals(TAB_PARTNER_PROGRAM)){
                addInStack(lastCurrentTab, getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1));
                navigateIn(tabId, new PartnerProgramFragment(), new Bundle()); //Переходим на фрагмент
            }else if(tabId.equals(TAB_BASKET)){
                addInStack(lastCurrentTab, getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1));
                navigateIn(tabId, new BasketFragment(), new Bundle());
            }

        }else {
            //Иначе если там не первая страница - то делаем те же действия, только в конце еще и удаляем последнюю запись из стека только что выбранного пункта меню
            addInStack(lastCurrentTab,getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size()-1)); //Добавляем в стек предыдущий фрагмент
            navigateIn(tabId, mStacks.get(tabId).lastElement(), mStacksBundle.get(tabId).lastElement());
            deleteFromStack(tabId); //Удаляем из стека текущее
        }

        System.out.println("____________________________");
        System.out.println(mStacks);
        System.out.println(mStacksBundle);
        System.out.println("____________________________");
    }


    public void navigateIn(String tag, Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);

        System.out.println("____________________________");
        System.out.println(mStacks);
        System.out.println(mStacksBundle);
        System.out.println("____________________________");

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.nav_host_fragment, fragment);
        ft.commitNowAllowingStateLoss();
    }
    public void navigateBack() {
        Fragment fragment = mStacks.get(mCurrentTab).elementAt(mStacks.get(mCurrentTab).size() - 1);;

        //Удаляем одну запись из стеков
        deleteFromStack(mCurrentTab);

        System.out.println("____________________________");
        System.out.println(mStacks);
        System.out.println(mStacksBundle);
        System.out.println("____________________________");

        //Включаем новый фрагмент
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.nav_host_fragment, fragment);
        ft.commit();

        //После удаления фрагмента вызывается дестрой, в котором сохраняется запись в стек
        //Эту запись нужно удалить
        //...

        System.out.println("конец back");
    }
    public void addInStack(String tag, Fragment fragment){
        System.out.println("Добавлена 1 запись в стек");
        mStacks.get(tag).push(fragment);
        mStacksBundle.get(tag).push(fragment.getArguments());

        System.out.println("____________________________");
        System.out.println(mStacks);
        System.out.println(mStacksBundle);
        System.out.println("____________________________");
    }
    public void deleteFromStack(String tag) {
        mStacks.get(tag).pop();
        mStacksBundle.get(tag).pop();
    }


    @Override
    public void onBackPressed() {
        if(mStacks.get(mCurrentTab).size() == 0){
            //Если записей нет - то есть это стартовая страница, то закрываем приложение
            finish();
            return;
        } else {
            //Иначе переключаемся на прошлый фрагмент
            navigateBack();
            System.out.println("КОНЕЦ BACKPRESSED");
        }
    }


}
