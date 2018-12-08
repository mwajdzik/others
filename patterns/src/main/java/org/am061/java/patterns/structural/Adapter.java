package org.am061.java.patterns.structural;

import org.am061.java.patterns.DesignPattern;

import java.util.List;

import static java.lang.String.join;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;

/*
    Stara część aplikacji zwraca użytkowników w postaci tablicy napisów.
    Nowa część wymaga użycia listy potrzebnej do zapisu pliku CSV.
    Proszę napisać adapter który dokona konwersji, a następnie wyświetlić na terminalu klientów.


    1,Adam,Nowak
    2,Katarzyna,Kowalska
    3,Wojciech,Jankowski
*/
public class Adapter implements DesignPattern {

    class Users {
        String[][] getUsers() {
            return new String[][]{
                    new String[]{"1", "Adam", "Nowak"},
                    new String[]{"2", "Katarzyna", "Kowalska"},
                    new String[]{"3", "Wojciech", "Jankowski"}
            };
        }
    }

    interface ClientInterface {
        List<String> getClients();
    }

    public class ClientAdapter implements ClientInterface {

        private Users users;

        ClientAdapter(Users users) {
            this.users = users;
        }

        public List<String> getClients() {
            return of(users.getUsers())
                    .map(user -> join(",", user))
                    .collect(toList());
        }
    }

    @Override
    public void run() {
        Users users = new Users();
        ClientInterface adapter = new ClientAdapter(users);
        List<String> clients = adapter.getClients();
        clients.forEach(System.out::println);
    }
}