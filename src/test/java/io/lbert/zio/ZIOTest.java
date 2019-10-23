package io.lbert.zio;

import io.lbert.Either;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ZIOTest {

    @Test
    public void mapOnSuccessShouldReturnResult() {

        UIO<Integer> uio = ZIO.succeed(10);
//        UIO<Integer> uioNew = uio.map(i -> i + 1);
        assertEquals(Runtime.attempt(uio), Either.right(10));
    }
}
