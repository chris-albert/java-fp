package io.lbert.zio;

import io.lbert.Either;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RuntimeTest {

  @Test
  public void attemptOnSuccessShouldBeRight() {
    ZIO<Object, Object, Integer> uio = ZIO.succeed(10);
    assertEquals(Runtime.attempt(uio), Either.right(10));
  }

  @Test
  public void attemptOnFailShouldBeLeft() {
    ZIO<Object, Integer, Object> uio = ZIO.fail(10);
    assertEquals(Runtime.attempt(uio), Either.left(10));
  }

  @Test
  public void mapOnSuccessShouldMapValue() {
    ZIO<Object, Object, Integer> uio = ZIO.succeed(10);
    ZIO<Object, Object, Integer> newUIO = uio.map((i) -> i + 10);
    assertEquals(Runtime.attempt(newUIO), Either.right(20));
  }

  @Test
  public void mapOnSuccessShouldMapValueToDifferentType() {
    ZIO<Object, Object, Integer> uio = ZIO.succeed(10);
    ZIO<Object, Object, String> newUIO = uio.map(Object::toString);
    assertEquals(Runtime.attempt(newUIO), Either.right("10"));
  }

  @Test
  public void mapOnFailureShouldNotApplyMap() {
    ZIO<Object, Integer, Object> uio = ZIO.fail(10);
    ZIO<Object, Integer, String> newIO = uio.map(Object::toString);
    assertEquals(Runtime.attempt(newIO), Either.left(10));
  }

  @Test
  public void fromEitherShouldBeRight() {
    ZIO<Object, Integer, String> zio = ZIO.fromEither(Either.right("hi"));
    assertEquals(Runtime.attempt(zio), Either.right("hi"));
  }

  @Test
  public void fromEitherShouldBeLeft() {
    ZIO<Object, Integer, String> zio = ZIO.fromEither(Either.left(10));
    assertEquals(Runtime.attempt(zio), Either.left(10));
  }

  @Test
  public void effectShouldBeRightIfSuccess() {
    ZIO<Object, Throwable, Integer> zio = ZIO.effect(() -> 10);
    assertEquals(Runtime.attempt(zio), Either.right(10));
  }

  @Test
  public void effectShouldBeLeftIfFailure() {
    var re = new RuntimeException("hi");
    ZIO<Object, Throwable, Integer> zio = ZIO.effect(() -> {throw re;});
    assertEquals(Runtime.attempt(zio), Either.left(re));
  }

  @Test
  public void flatMapOnSuccessShouldMapValue() {
    ZIO<Object, Object, Integer> uio = ZIO.succeed(10);
    ZIO<Object, Object, Integer> newUIO = uio.flatMap((i) -> ZIO.succeed(i + 10));
    assertEquals(Runtime.attempt(newUIO), Either.right(20));
  }

  @Test
  public void flatMapOnFailureShouldNotMapValue() {
    ZIO<Object, Integer, Object> uio = ZIO.fail(10);
    ZIO<Object, Integer, Integer> newUIO = uio.flatMap(i -> ZIO.succeed(10));
    assertEquals(Runtime.attempt(newUIO), Either.left(10));
  }
}

