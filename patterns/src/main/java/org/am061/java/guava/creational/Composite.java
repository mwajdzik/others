package org.am061.java.guava.creational;

import org.am061.java.guava.Pattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Proszę napisać program budujący strukturę drzewiastą.
 * Podstawowa to korzeń. Elementy które mogą się pojawić to węzły i liście.
 * Korzeń i węzły mogą zawierać inne elementy, liście nie (kończą daną gałąź).
 * <p>
 * Korzeń rozpoczęcie renderowania
 * Liść 1.1 renderowanie...
 * Węzeł 2 rozpoczęcie renderowania
 * Liść 2.1 renderowanie...
 * Liść 2.2 renderowanie...
 * Liść 2.3 renderowanie...
 * Węzeł 2 zakończenie renderowania
 * Węzeł 3 rozpoczęcie renderowania
 * Liść 3.1 renderowanie...
 * Liść 3.2 renderowanie...
 * Węzeł 3.3 rozpoczęcie renderowania
 * Liść 3.3.1 renderowanie...
 * Węzeł 3.3 zakończenie renderowania
 * Węzeł 3 zakończenie renderowania
 * Korzeń zakończenie renderowania
 */
public class Composite implements Pattern {

    public interface Component {
        void render();

        Node add(Component element);
    }

    public class Leaf implements Component {

        private String name;

        Leaf(String name) {
            this.name = name;
        }

        public void render() {
            System.out.println(name + " rendering...");
        }

        public Node add(Component element) {
            throw new UnsupportedOperationException();
        }
    }

    public class Node implements Component {

        private List<Component> children = new ArrayList<Component>();
        private String name;

        Node(String name) {
            this.name = name;
        }

        public void render() {
            System.out.println(name + " starts rendering...");

            for (Component child : children) {
                child.render();
            }

            System.out.println(name + " finishes rendering...");
        }

        public Node add(Component element) {
            children.add(element);
            return this;
        }
    }

    public void run() {
        Node root = new Node("Root")
                .add(new Leaf("Leaf 1.1"))
                .add(new Node("Leaf 2")
                        .add(new Leaf("Leaf 2.1"))
                        .add(new Leaf("Leaf 2.2"))
                        .add(new Leaf("Leaf 2.3")))
                .add(new Node("Leaf 3")
                        .add(new Leaf("Leaf 3.1"))
                        .add(new Leaf("Leaf 3.2"))
                        .add(new Node("Leaf 3.3")
                                .add(new Leaf("Leaf 3.3.1"))));

        root.render();
    }
}
