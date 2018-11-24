package org.am061.java.guava.creational;

import java.util.ArrayList;

public class FactoryMethod {

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

    public void main() {
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
