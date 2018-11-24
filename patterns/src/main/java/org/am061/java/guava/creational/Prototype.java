package org.am061.java.guava.creational;

import org.am061.java.guava.Pattern;

import java.util.HashMap;
import java.util.Map;

/**
 * Proszę napisać program który:
 * - dodaje produkty do sklepu,
 * - klonuje je do koszyków klientów,
 * - zmienia cenę u klienta (rabat).
 * <p>
 * <p>
 * Milk - 0.89 > 0.85
 * Bread - 1.10 > 0.99
 */
public class Prototype implements Pattern {

    abstract class ProductPrototype {

        private double price;

        ProductPrototype(double price) {
            this.price = price;
        }

        double getPrice() {
            return price;
        }

        void setPrice(double price) {
            this.price = price;
        }

        abstract ProductPrototype cloneIt();
    }

    public class Bread extends ProductPrototype {

        Bread(double price) {
            super(price);
        }

        public ProductPrototype cloneIt() {
            return new Bread(getPrice());
        }
    }

    public class Milk extends ProductPrototype {

        Milk(double price) {
            super(price);
        }

        public ProductPrototype cloneIt() {
            return new Milk(getPrice());
        }
    }

    public class Supermarket {

        private Map<String, ProductPrototype> productList = new HashMap<String, ProductPrototype>();

        void addProduct(String key, ProductPrototype productPrototype) {
            productList.put(key, productPrototype);
        }

        ProductPrototype getProduct(String key) {
            ProductPrototype prototype = productList.get(key);
            return prototype.cloneIt();
        }
    }

    // ---

    public void run() {
        Supermarket supermarket = new Supermarket();

        double milkSourcePrice = 0.89;
        double breadSourcePrice = 1.10;

        supermarket.addProduct("Milk", new Milk(milkSourcePrice));
        supermarket.addProduct("Bread", new Bread(breadSourcePrice));

        Milk clonedMilk = (Milk) supermarket.getProduct("Milk");
        clonedMilk.setPrice(0.85);

        Bread clonedBread = (Bread) supermarket.getProduct("Bread");
        clonedBread.setPrice(0.99);

        System.out.printf("Milk - %.2f > %.2f%n", milkSourcePrice, clonedMilk.getPrice());
        System.out.printf("Bread - %.2f > %.2f%n", breadSourcePrice, clonedBread.getPrice());
    }
}
