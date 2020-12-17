package com.example.unimag.ui;

public class Bonus {

    public float getMyBonus() { //Функция получения бонусов пользователя
        float myBonuses; //Бонусы пользователя
        myBonuses = 0; //Удалить
        //Запрос на получение бонусов из БД
        return myBonuses;
    }

    private void setBonus(float newBonus) { //Функция добавления нового значения бонусов пользователя в БД
        //Запрос к БД на установку нового значения бонусов пользователю
    }

    private float getNewBonus(float sum) { //Функция получения новых начисленных баллов за покупку
        float bonuses = 0; //Новые бонусы пользователя (начисляющиеся от суммы покупки)
        bonuses = countBonus(sum);
        return bonuses;
    }

    private float countBonus(float sum) { //Функция подсчета кол-ва полученных бонусов в зависимости от суммы покупки sum
        float bonuses = 0; //Новые бонусы пользователя (начисляющиеся от суммы покупки)

        if (sum < 500) {
            bonuses = 0;
        } else if (sum < 1000) {
            bonuses += ((float) Math.round(sum * 2)) / 100;
        } else if (sum < 1500) {
            bonuses += ((float) Math.round(sum * 3)) / 100;
        } else if (sum < 2000) {
            bonuses += ((float) Math.round(sum * 4)) / 100;
        } else if (sum >= 2000) {
            bonuses += ((float) Math.round(sum * 5)) / 100;
        }

        //setBonus(bonuses); //Добавление новых бонусов пользователю
        return bonuses;
    }
}