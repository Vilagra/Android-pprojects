package com.example.sql_orm;

import com.activeandroid.ActiveAndroid;
import com.example.sql_orm.entity.Position;

/**
 * Created by Vilagra on 15.02.2017.
 */

public class Positions {
    private String[] position_name = {"Директор", "Программер",
            "Бухгалтер", "Охранник"};
    private int[] position_salary = {15000, 13000, 10000, 8000};
    public Position[] positions = new Position[position_name.length];

    public void fill() {
        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < position_name.length; i++) {
                positions[i] = new Position(position_name[i],position_salary[i]);
                positions[i].save();
            }
            ActiveAndroid.setTransactionSuccessful();
        }finally {
            ActiveAndroid.endTransaction();
        }

    }



}
