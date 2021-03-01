package com.example.unimag;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.unimag.ui.LoginFragment;
import com.example.unimag.ui.SqLite.DataDBHelper;
import com.example.unimag.ui.basket.BasketFragment;
import com.example.unimag.ui.catalog.CatalogFragment;
import com.example.unimag.ui.partner_program.PartnerProgramFragment;
import com.example.unimag.ui.personal_area.MyCabinetFragment;
import com.example.unimag.ui.sort.GlobalSort;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.EmptyStackException;
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
    public static final String TAB_PERSONAL_AREA = "PERSONAL_AREA";

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
        mStacks.put(TAB_PERSONAL_AREA, new Stack<Fragment>());
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new CatalogFragment()).addToBackStack(null).commit();

        mStacksBundle = new HashMap<String, Stack<Bundle>>();
        mStacksBundle.put(TAB_CATALOG, new Stack<Bundle>());
        mStacksBundle.put(TAB_PARTNER_PROGRAM, new Stack<Bundle>());
        mStacksBundle.put(TAB_BASKET, new Stack<Bundle>());
        mStacksBundle.put(TAB_PERSONAL_AREA, new Stack<Bundle>());

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
                break;
            case R.id.navigation_basket:
                selectedTab(TAB_BASKET);
                break;
            case R.id.myCabinetFragment: {
                selectedTab(TAB_PERSONAL_AREA);
                break;
            }
        }

        return true;
    }


    private void selectedTab(String tabId)
    {
        lastCurrentTab = mCurrentTab;
        mCurrentTab = tabId;

        /*//Фича как в вк (нажат тот же пункт меню)
        if (lastCurrentTab == mCurrentTab) {
            //Исключительный случай для оригинального пользователя, нажавшего на каталог дважды
            if (mCurrentTab.equals(TAB_CATALOG)) {
                //Если был каталог - устанавливаем позицию скроллинга вверху, иначе - достаем из стека каталог
                if (mStacks.get(TAB_CATALOG).size() == 0) {
                    ((GridView)getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1).getView().findViewById(R.id.gridView)).setSelection(0);
                    return;
                } else {
                    navigateIn(TAB_CATALOG, mStacks.get(TAB_CATALOG).lastElement(), mStacksBundle.get(tabId).lastElement());
                    deleteFromStack(tabId);
                    return;
                }
            } else {
                clearStack(tabId);
            }
        }*/

        //Если в первый раз жмем
        if(mStacks.get(tabId).size() == 0){
            //Смотрим куда нажали (каталог и лк - исключительные случаи)
            if(tabId.equals(TAB_CATALOG)){
                navigateIn(tabId, new CatalogFragment(), new Bundle());
            }else if(tabId.equals(TAB_PARTNER_PROGRAM)){
                navigateIn(tabId, new PartnerProgramFragment(), new Bundle()); //Переходим на фрагмент
            }else if(tabId.equals(TAB_BASKET)){
                navigateIn(tabId, new BasketFragment(), new Bundle());
            } else if(tabId.equals(TAB_PERSONAL_AREA)){
                dataDbHelper = new DataDBHelper(this);
                secureKod = dataDbHelper.getSecureKod(dataDbHelper);
                dataDbHelper.close();

                if (secureKod == null) {
                    navigateIn(tabId, new LoginFragment(), new Bundle());
                } else {
                    navigateIn(tabId, new MyCabinetFragment(), new Bundle());
                }
            }

        } else {
            //Иначе если там не первая страница - то делаем те же действия, только в конце еще и удаляем последнюю запись из стека только что выбранного пункта меню
            navigateIn(tabId, mStacks.get(tabId).lastElement(), mStacksBundle.get(tabId).lastElement());
            deleteFromStack(tabId);
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
        ft.commitNowAllowingStateLoss();

        //После удаления фрагмента вызывается дестрой, в котором сохраняется запись в стек
        //Эту запись нужно удалить
        deleteFromStack(mCurrentTab);

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
        try {
            mStacks.get(tag).pop();
            mStacksBundle.get(tag).pop();
        } catch (EmptyStackException e) {
            //Оно выскакивает только в ЛК из-за clearStack в onCreateView фрагментов Логин и МайКабинет
        }
    }
    public void clearStack(String tag) {
        mStacks.get(tag).clear();
        mStacksBundle.get(tag).clear();
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
