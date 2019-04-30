package org.amw061.spark;

import lombok.experimental.UtilityClass;
import scala.Tuple2;

import java.util.Scanner;

@UtilityClass
public class Utils {

    public static <A, B> Tuple2<B, A> reverseTuple(Tuple2<A, B> t) {
        return new Tuple2<>(t._2, t._1);
    }

    public static <A> Tuple2<A, Long> toCounterTuple(A t) {
        return new Tuple2<>(t, 1L);
    }

    public static void waitForEnter() {
        System.out.println("Open: http://localhost:4040");
        System.out.println("Press ENTER to exit");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }
}
