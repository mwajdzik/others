package org.am061.java.patterns.creational;

import org.am061.java.patterns.DesignPattern;

/*
    Limits instances of class to one, eg. a resource accessor, a logging class
    Java: System, Runtime, Desktop
 */
public class Singleton implements DesignPattern {

    private static class PrintSpooler {

        // #2. final and initialized at once
        private static PrintSpooler spooler;
        private static boolean initialized = false;

        private PrintSpooler() {
        }

        private static void init() {
            initialized = true;
        }

        static synchronized PrintSpooler getInstance() {
            if (!initialized) {
                spooler = new PrintSpooler();
                init();
            }

            return spooler;
        }
    }

    @Override
    public void run() {
        PrintSpooler printSpooler = PrintSpooler.getInstance();
        System.out.println(printSpooler.toString());

        PrintSpooler anotherPrintSpooler = PrintSpooler.getInstance();
        System.out.println(anotherPrintSpooler.toString());
    }
}
