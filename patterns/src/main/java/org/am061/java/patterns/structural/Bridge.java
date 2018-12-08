package org.am061.java.patterns.structural;

import lombok.Setter;
import org.am061.java.patterns.DesignPattern;

/*
    Aplikacja powinna umożliwiać wysyłanie różnych wiadomości.
    Mogą być systemowe lub od administratora.
    Wiadomości są różnego typu, np. mail, GG, FB.
    GG i FB zasymulujemy wyświetleniem informacji na konsoli. Maila bardzo prosto możemy wysłać.

    ------------------
    Email: Alert!
    Wykryto wirusa!

    FB: Alert!
    Wykryto wirusa!

    GG: Alert!
    Wykryto wirusa!

    Email: Życzenia
    Cześć, najlepsze życzenia z okazji dnia bez resetu serwera!
    Twój Administrator
 */
public class Bridge implements DesignPattern {

    @Setter
    public abstract class Message {

        protected ISender sender;
        protected String topic;
        protected String content;

        public abstract void send();
    }

    public class MessageSystem extends Message {

        public void send() {
            sender.sendMessage(topic, content);
        }
    }

    public class MessageUser extends Message {

        @Override
        public void send() {
            sender.sendMessage(topic, content);
        }
    }

    // ---

    interface ISender {
        void sendMessage(String topic, String content);
    }

    class Email implements ISender {

        @Override
        public void sendMessage(String topic, String content) {
            System.out.println("Email: " + topic + "\n" + content);
        }
    }

    class FB implements ISender {

        @Override
        public void sendMessage(String topic, String content) {
            System.out.println("FB: " + topic + "\n" + content);
        }
    }

    class GG implements ISender {

        @Override
        public void sendMessage(String topic, String content) {
            System.out.println("GG: " + topic + "\n" + content);
        }
    }

    // ---

    @Override
    public void run() {
        System.out.println("------------------");

        ISender email = new Email();
        ISender fb = new FB();
        ISender gg = new GG();

        MessageSystem statement = new MessageSystem();
        statement.setSender(email);
        statement.setTopic("Alert!");
        statement.setContent("Wykryto wirusa!");
        statement.send();
        System.out.println();

        statement.setSender(gg);
        statement.send();
        System.out.println();

        statement.setSender(fb);
        statement.send();
        System.out.println();

        MessageUser message = new MessageUser();
        message.setSender(email);
        message.setTopic("Życzenia");
        message.setContent("Cześć, najlepsze życzenia z okazji dnia bez resetu serwera!");

        message.send();
    }
}
