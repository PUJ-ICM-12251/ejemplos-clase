package com.example.firebase.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class People {
    private String firstName;
    private String lastName;
    private int age;
    private double height;
    private double weight;
    private Address currentAddress;

    public People () { }
}
