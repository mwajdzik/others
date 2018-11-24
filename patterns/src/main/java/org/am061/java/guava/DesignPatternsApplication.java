package org.am061.java.guava;

import org.am061.java.guava.creational.*;

public class DesignPatternsApplication {

    public static void main(String[] args) {
        Pattern[] patterns = new Pattern[]{
                new Builder(),
                new AbstractFactory(),
                new FactoryMethod(),
                new Prototype(),
                new Composite()
        };

        System.out.println("\n------------------------------------------\n");

        for (Pattern pattern : patterns) {
            pattern.run();
            System.out.println("\n------------------------------------------\n");
        }
    }
}