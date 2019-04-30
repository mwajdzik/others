package org.amw061.spark.learning;

import lombok.experimental.UtilityClass;
import scala.Tuple2;

@UtilityClass
class Utils {

    static <A, B> Tuple2<B, A> reverseTuple(Tuple2<A, B> t) {
        return new Tuple2<>(t._2, t._1);
    }

    static <A> Tuple2<A, Long> toCounterTuple(A t) {
        return new Tuple2<>(t, 1L);
    }
}
