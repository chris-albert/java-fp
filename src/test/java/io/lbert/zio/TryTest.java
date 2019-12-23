package io.lbert.zio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TryTest {

  @Test
  public void getSuccessIfNoException() throws Exception {
    var res = Try.of(() -> 10);
    assertEquals(Try.success(10), res);
    assertTrue(res.isSuccess());
  }

  @Test
  public void getFailureIfException() throws Exception {
    var exception = new RuntimeException("hi");
    var res = Try.of(() -> {throw exception;});
    assertEquals(Try.failure(exception), res);
    assertTrue(res.isFailure());
  }

  @Test
  public void mapOverSuccess() throws Exception {
    var res = Try.of(() -> 10).map(i -> i + 10);
    assertEquals(Try.success(20), res);
    assertTrue(res.isSuccess());
  }

  @Test
  public void doNotMapOverFailure() throws Exception {
    var exception = new RuntimeException("hi");
    var res = Try.of(() -> {throw exception;}).map((i) -> 10);
    assertEquals(Try.failure(exception), res);
    assertTrue(res.isFailure());
  }

  @Test
  public void flatMapOverSuccess() throws Exception {
    var res = Try.of(() -> 10).flatMap(i -> Try.of(() -> i + 10));
    assertEquals(Try.success(20), res);
    assertTrue(res.isSuccess());
  }

  @Test
  public void doNotFlatMapOverFailure() throws Exception {
    var exception = new RuntimeException("hi");
    var res = Try.of(() -> {throw exception;}).flatMap((i) -> Try.success(10));
    assertEquals(Try.failure(exception), res);
    assertTrue(res.isFailure());
  }

  @Test
  public void mapErrorOverError() throws Exception {
    var exception = new RuntimeException("hi");
    var otherException = new RuntimeException("other");
    var res = Try.of(() -> {throw exception;}).mapError((e) -> otherException);
    assertEquals(Try.failure(otherException), res);
    assertTrue(res.isFailure());
  }
}