package com.example.sql_orm.entity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Vilagra on 13.02.2017.
 */
@Table(name = "Positions")
public class Position extends Model {
    @Column(name = "Name")
    public String name;
    @Column(name = "Salary")
    public int sallary;

    public Position() {
    }

    public Position(String name, int sallary) {
        this.name = name;
        this.sallary = sallary;
    }
}
