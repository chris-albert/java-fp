package io.lbert;


import java.util.Optional;

public class Main {

    public static void main(String[] args ) {

        var some = Option.some(10);
        var plus = some.map(i -> i + 1);
        var outSome = plus.map(Object::toString);

        Option<Integer> none = Option.none();
        Option<String> outNone = none.map(Object::toString);

        System.out.println(outSome);
        System.out.println(outNone);


        var rigth = Either.right(10);

        var opt = Optional.of(10);
        opt.map(i -> i + 1);
        Optional<Integer> optN = Optional.empty();
    }

}
