//Класс для работы с базой данных SQLite
//Здесь мы создаем или берем существующую БД, к которой обращаемся с помощью объекта класса SQLiteDatabase
//P.S. Все названия БД, названия таблиц, названия столбцов - объявлять константами,
//     чтобы их можно было потом использовать дальше в приложении
package com.example.unimag.ui.SqLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    /**
     * Константы, связанные с БД
     */
    public static final int DATABASE_VERSION = 1; //Версия БД
    public static final String DATABASE_NAME = "SQLite"; //Имя БД

    /**
     * Константы, связанные с таблицами
     */
    //Таблица "Каталог"
    public static final String TABLE_NAME_CATALOG = "Catalog"; // Имя таблицы с товарами  в каталоге
    public static final String KEY_ID_CATALOG = "_id"; //Имя столбца id товара (обязательно с подчеркивания для id) в каталоге
    public static final String KEY_CATEGORY_CATALOG = "category"; //Имя столбца с категорией товара в каталоге
    public static final String KEY_TITLE_CATALOG = "title"; //Имя столбца с названием товара в каталоге
    //public static final String KEY_DATE_CATALOG = "date"; //Имя столбца с датой добавления товара в каталоге
    public static final String KEY_PRICE_CATALOG = "price"; //Имя столбца с ценой товара в каталоге
    public static final String KEY_DESCRIPTION_CATALOG = "description"; //Имя столбца с описанием товара в каталоге
    public static final String KEY_IMAGE_NAME = "image_name"; //Имя столбца с названием фото в каталоге


    //Конструктор класса
    //На вход принимает 4 параметра:
    //    context - контекст приложения
    //    name - имя базы данных
    //    factory - курсор
    //    version - версия БД
    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION); //Курсор не используется, поэтому null
    }


    //Вызывается при первом создании БД
    //Обязательно используйте оператор "CREATE TABLE IF NOT EXISTS..." , а не "CREATE TABLE"
    @Override
    public void onCreate(SQLiteDatabase db) {

        //Выполняем запрос в SQLite, создавая таблицу TABLE_NAME_CATALOG ("Catalog")
        db.execSQL("create table if not exists " + TABLE_NAME_CATALOG + "("
                + KEY_ID_CATALOG + " integer primary key autoincrement, "
                + KEY_CATEGORY_CATALOG + " text, "
                + KEY_TITLE_CATALOG + " text, "
                + KEY_PRICE_CATALOG + " integer, "
                + KEY_DESCRIPTION_CATALOG + " text, "
                + KEY_IMAGE_NAME + " text" + ")");

    }


    //Вызывается при изменении версии БД
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Если версия БД изменилась (вышло обновление) - то удаляем прошлую БД и создаем новую
        db.execSQL("drop table if exists " + TABLE_NAME_CATALOG);
        onCreate(db);

    }


    //Вызывается при открытии БД
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

    }


    //Возвращает БД для чтения
    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }


    //Возвращает БД для чтения и записи
    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

}

