package io.lbert;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class OptionTest {

    @Test
    public void mappingOverSomeShouldReturnNewSome() {
        List<String> list = new ArrayList<>();
        var some = (Option.Some<Integer>) Option.some(10);
        var newSome = (Option.Some<Integer>) some.map(e -> {
            list.add("inside some.map");
            return e + 10;
        });
        assertEquals(some.value, 10);
        assertEquals(newSome.value, 20);
        assertEquals(list.size(), 1);
    }

    @Test
    public void mappingOverNoneShouldDoNothing() {
        List<String> list = new ArrayList<>();
        Option<Integer> none = Option.none();
        Option<Integer> newNone = none.map(e -> {
            list.add("inside none.map");
            return e + 10;
        });
        assertEquals(none, newNone);
        assertEquals(list.size(), 0);
    }

    @Test
    public void twoNonesShouldBeTheSame() {
        assertEquals(Option.none(), Option.none());
        assert(Option.none() == Option.none());
    }

    @Test
    public void twoANoneAndSomeShouldNotBeEqual() {
        assertNotEquals(Option.none(), Option.some(10));
    }

    @Test
    public void twoSomesShouldBeEqual() {
        assertEquals(Option.some(10), Option.some(10));
    }

    @Test
    public void differentSomesShouldBeNotEqual() {
        assertNotEquals(Option.some(11), Option.some(10));
    }

    @Test
    public void getOrElseOnSomeShouldntBeCalled() {
        List<String> list = new ArrayList<>();
        var some = Option.some(10);
        Function<Integer, Integer> lambda = i -> {
            list.add("inside lambda");
            return i + 10;
        };
        var res = some.getOrElse(() -> lambda.apply(10));

        assertEquals(res, 10);
        assertEquals(list.size(), 0);
    }

    @Test
    public void getOrElseOnNoneShouldBeCalled() {
        List<String> list = new ArrayList<>();
        var none = Option.none();
        Function<Integer, Integer> lambda = i -> {
            list.add("inside lambda");
            return i;
        };
        var res = none.getOrElse(() -> lambda.apply(100));

        assertEquals(res, 100);
        assertEquals(list.size(), 1);
    }

    @Test
    public void ofWithAValueShouldBeSome() {
        assertEquals(Option.of(10), Option.some(10));
    }

    @Test
    public void ofWithNullShouldBeNone() {
        assertEquals(Option.of(null), Option.none());
    }
}