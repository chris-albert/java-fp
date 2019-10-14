package io.lbert;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EitherTest {

    @Test
    public void primitiveRightsShouldBeEqual() {
        assertEquals(Either.right(10), Either.right(10));
    }

    @Test
    public void primitiveLeftsShouldBeEqual() {
        assertEquals(Either.left(10), Either.left(10));
    }
}
