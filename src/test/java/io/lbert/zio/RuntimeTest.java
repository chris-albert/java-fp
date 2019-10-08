package io.lbert.zio;

import io.lbert.Either;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RuntimeTest {

    @Test
    public void attemptOnSuccessShouldBeRight() {
        UIO<Integer> uio = UIO.succeed(10);
        assertEquals(Runtime.attempt(uio), Either.right(10));
    }
}

