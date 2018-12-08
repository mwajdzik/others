package org.am061.java.patterns.structural;

import org.am061.java.patterns.DesignPattern;

/*
    Proszę napisać program, który będzie ochraniać dostęp do danych z użyciem wzorca proxy.
    Obiekt z wrażliwymi danymi powinien być tworzony jedynie w momencie wpisania prawidłowego hasła ("dobrehaslo").

    ProxyClient: Access denied

    ProxyClient: Initialized
    RealClient: Initialized
    data from Proxy Client = WSEI data
 */
public class Proxy implements DesignPattern {

    interface Client {
        String getData();
    }

    class RealClient implements Client {

        private String data;

        RealClient() {
            System.out.println("RealClient: Initialized");
            data = "WSEI data";
        }

        @Override
        public String getData() {
            return data;
        }
    }

    class ProxyClient implements Client {

        private static final String PASSWORD = "dobrehaslo";
        private final boolean authenticated;
        private final RealClient client;

        ProxyClient(String password) {
            if (PASSWORD.equals(password)) {
                System.out.println("ProxyClient: Initialized");
                authenticated = true;
                client = new RealClient();
            } else {
                authenticated = false;
                client = null;
            }
        }

        @Override
        public String getData() {
            if (authenticated) {
                return "data from Proxy Client = " + client.getData();
            }

            return "ProxyClient: Access denied";
        }
    }

    @Override
    public void run() {
        ProxyClient proxy1 = new ProxyClient("zlehaslo");
        System.out.println(proxy1.getData());

        System.out.println();

        ProxyClient proxy2 = new ProxyClient("dobrehaslo");
        System.out.println(proxy2.getData());
    }
}
