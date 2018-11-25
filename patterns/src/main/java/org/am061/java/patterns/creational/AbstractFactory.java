package org.am061.java.patterns.creational;

import org.am061.java.patterns.DesignPattern;

/**
 * Proszę napisać aplikację, która tworzyć będzie obiekty zawierające alfabet (fragment).
 * Obsługa 3: łacinki, cyrylicy i greki.
 * Powinny działać 2 metody dla każdego: wyswietl() oraz zwroc();
 * <p>
 * W wyniku działania powinniśmy uzyskać:
 * <p>
 * abcde
 * αβγδε
 * абвгд
 */
public class AbstractFactory implements DesignPattern {

    enum AlphabetType {LATIN, GREEK, RUSSIAN}

    class FactoryMaker {
        Factory getFactory(AlphabetType alphabetType) {
            switch (alphabetType) {
                case LATIN:
                    return new LatinFactory();
                case GREEK:
                    return new GreekFactory();
                case RUSSIAN:
                    return new RussianFactory();
                default:
                    throw new UnsupportedOperationException();
            }
        }
    }

    // ---

    interface Factory {
        Alphabet createAlphabet();
    }

    class RussianFactory implements Factory {
        public Alphabet createAlphabet() {
            return new RussianAlphabet();
        }
    }

    class GreekFactory implements Factory {
        public Alphabet createAlphabet() {
            return new GreekAlphabet();
        }
    }

    class LatinFactory implements Factory {
        public Alphabet createAlphabet() {
            return new LatinAlphabet();
        }
    }

    // ---

    interface Alphabet {
        String retrieve();
    }

    class LatinAlphabet implements Alphabet {

        public String retrieve() {
            return "abcde";
        }
    }

    class GreekAlphabet implements Alphabet {

        public String retrieve() {
            return "αβγδε";
        }

    }

    class RussianAlphabet implements Alphabet {

        public String retrieve() {
            return "абвгд";
        }
    }

    // ---

    public void run() {
        FactoryMaker factoryMaker = new FactoryMaker();

        Factory latinFactory = factoryMaker.getFactory(AlphabetType.LATIN);
        Alphabet latinAlphabet = latinFactory.createAlphabet();
        System.out.println(latinAlphabet.retrieve());

        Factory greekFactory = factoryMaker.getFactory(AlphabetType.GREEK);
        Alphabet greekAlphabet = greekFactory.createAlphabet();
        System.out.println(greekAlphabet.retrieve());

        Factory russianFactory = factoryMaker.getFactory(AlphabetType.RUSSIAN);
        Alphabet russianAlphabet = russianFactory.createAlphabet();
        System.out.println(russianAlphabet.retrieve());
    }
}
