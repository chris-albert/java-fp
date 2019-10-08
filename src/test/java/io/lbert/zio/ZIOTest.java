package io.lbert.zio;

import org.junit.jupiter.api.Test;

public class ZIOTest {

    @Test
    public void mapOnSuccessShouldReturnResult() {

        UIO<Integer> uio = ZIO.succeed(10);

    }
}
