package org.am061.java.guava.creational;

import org.am061.java.guava.Pattern;

import java.util.ArrayList;

/**
 * Proszę napisać program dzięki któremu czytelnicy mogą kupować książki.
 * Mamy 2 typy czytelników:
 * - normalnego (genericReader) - kupuje 1 książkę (encyklopedię),
 * - nałogowego (bookReader) - kupuje 3 kiążki (przygodowe, fantastykę oraz horror).
 * <p>
 * Proszę każdym z czytelników kupić odpowiednie książki i je wyświetlić.
 * <p>
 * Encyclopedia
 * <p>
 * AdventureBookReader
 * TreasureIsland
 * <p>
 * FantasyBookReader
 * LordOfTheRings
 * <p>
 * HorrorBookReader
 * Dracula
 */
public class FactoryMethod implements Pattern {

    abstract class Book {

        abstract String getName();
    }

    public class Encyclopedia extends Book {

        @Override
        String getName() {
            return "Encyclopedia";
        }
    }

    public class TreasureIsland extends Book {

        @Override
        String getName() {
            return "TreasureIsland";
        }
    }

    public class LordOfTheRings extends Book {

        @Override
        String getName() {
            return "LordOfTheRings";
        }
    }

    public class Dracula extends Book {

        @Override
        String getName() {
            return "Dracula";
        }
    }

    // ---

    public abstract class BookReader {

        private Book book;

        BookReader() {
            book = buyBook();
        }

        Book getBook() {
            return book;
        }

        public abstract Book buyBook();

        // #DisplayOwnedBooks() // to string ;)
    }

    public class GenericBookReader extends BookReader {

        @Override
        public Book buyBook() {
            return new Encyclopedia();
        }
    }

    public class AdventureBookReader extends BookReader {

        @Override
        public Book buyBook() {
            return new TreasureIsland();
        }
    }

    public class FantasyBookReader extends BookReader {

        @Override
        public Book buyBook() {
            return new LordOfTheRings();
        }
    }

    public class HorrorBookReader extends BookReader {

        @Override
        public Book buyBook() {
            return new Dracula();
        }
    }

    // ---

    public void run() {
        BookReader genericReader = new GenericBookReader();
        Book book = genericReader.buyBook();
        System.out.println(book.getName());

        System.out.println("\n/* ********************************** */\n");

        ArrayList<BookReader> bookReaderList = new ArrayList<BookReader>();
        bookReaderList.add(new AdventureBookReader());
        bookReaderList.add(new FantasyBookReader());
        bookReaderList.add(new HorrorBookReader());

        for (BookReader bookReader : bookReaderList) {
            System.out.println(bookReader.getClass().getSimpleName());
            System.out.println(bookReader.getBook().getName());
        }
    }
}
