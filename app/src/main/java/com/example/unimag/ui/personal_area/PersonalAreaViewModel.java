package com.example.unimag.ui.personal_area;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

//ViewModel - класс, позволяющий Activity и фрагментам сохранять необходимые им объекты живыми при повороте экрана.
//Не храните в ViewModel ссылки на Activity, фрагменты, View и пр. Это может привести к утечкам памяти.

public class PersonalAreaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PersonalAreaViewModel() { //Это нам НЕ НУЖНО, нужен только закомментированный код
        mText = new MutableLiveData<>();
        //mText.setValue("This is personal area fragment");
    }

    //MutableLiveData<String> data;
    //
    //Когда Activity захочет получить данные, оно вызовет именно этот метод.
    //public LiveData<String> getData() {       //Создание LiveData для подгрузки данных
    //    if (data == null) {                   //Проверяем были ли раньше подгружены данные
    //        data = new MutableLiveData<>();   //Если нет - то загружаем только один раз
    //        loadData();
    //    }
    //    return data;                          //Возврат полученных данных
    //}

    //private void loadData() {                              //Функция подгрузки данных (независимая)
    //    dataRepository.loadData(new Callback<String>() {   //dateRepositiry - наш репозиторий
    //        @Override
    //        public void onLoad(String s) {                 //Собственно загрузка данных
    //            data.postValue(s);
    //        }
    //    });
    //}

    public LiveData<String> getText() {
        return mText;
    }
}