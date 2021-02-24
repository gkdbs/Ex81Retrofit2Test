package com.mrhi2020.ex81retrofit2test;

public class Item {
    String name; // 이 멤버변수 이름은 json문자열의 식별자와 일치해야 함
    String msg;

    public Item() {
    }

    public Item(String name, String msg) {
        this.name = name;
        this.msg = msg;
    }
}
