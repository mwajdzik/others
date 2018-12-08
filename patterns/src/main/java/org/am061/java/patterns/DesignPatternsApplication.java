package org.am061.java.patterns;

import org.am061.java.patterns.creational.*;
import org.am061.java.patterns.structural.Adapter;
import org.am061.java.patterns.structural.Bridge;
import org.am061.java.patterns.structural.Proxy;

public class DesignPatternsApplication {

    public static void main(String[] args) {
        DesignPattern[] patterns = new DesignPattern[]{
                new Builder(),
                new AbstractFactory(),
                new FactoryMethod(),
                new Prototype(),
                new Composite(),
                new Proxy(),
                new Bridge(),
                new Adapter(),
        };

        System.out.println("\n------------------------------------------\n");

        for (DesignPattern pattern : patterns) {
            pattern.run();
            System.out.println("\n------------------------------------------\n");
        }
    }
}