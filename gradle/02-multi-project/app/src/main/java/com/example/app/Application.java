package com.example.app;

import com.example.api.StringReverser;

public class Application {

    public static void main(String[] args) {
        StringReverser reverser = new StringReverser();
        System.out.println(reverser.reverse("ABBA"));
    }
}
