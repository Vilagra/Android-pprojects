package com.example.sql_orm.entity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Vilagra on 13.02.2017.
 */
@Table(name = "Persons")
public class Person extends Model{
    @Column(name = "Name")
    public String name;
    @Column(name = "Age")
    public int age;
    @Column(name = "Position")
    public Position position;

    public Person(int age, String name, Position position) {
        super();
        this.age = age;
        this.name = name;
        this.position = position;
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", position=" + position +
                '}';
    }

    public Person() {
        super();
    }
}
