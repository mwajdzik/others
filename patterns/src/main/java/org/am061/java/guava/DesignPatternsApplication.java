package org.am061.java.guava;

import org.am061.java.guava.creational.FactoryMethod;
import org.am061.java.guava.creational.Prototype;

public class DesignPatternsApplication {

    public static void main(String[] args) {
        Pattern[] patterns = new Pattern[]{
                new FactoryMethod(),
                new Prototype()
        };

        System.out.println("\n------------------------------------------\n");

        for (Pattern pattern : patterns) {
            pattern.run();
            System.out.println("\n------------------------------------------\n");
        }
    }
}