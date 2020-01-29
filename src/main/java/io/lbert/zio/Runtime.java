package io.lbert.zio;


import io.lbert.Either;
import io.lbert.zio.ZIO;

public class Runtime {

  public static <R, E, A, B> Either<E, B> attempt(ZIO<R, E, A> zio) {

    Either<E, B> result = null;
    switch (zio.getTag()) {
      case SUCCEED:
        ZIO.Succeed<A, E> succeed = (ZIO.Succeed<A, E>) zio;
        result = (Either<E, B>) Either.right(succeed.getValue());
        break;
      case FAIL:
        ZIO.Fail<E, A> fail = (ZIO.Fail<E, A>) zio;
        result = Either.left(fail.getError());
        break;
      case MAP:
        ZIO.Map<R, E, A, B> map = (ZIO.Map<R, E, A, B>) zio;
        Either<E, A> mr = attempt(map.getZio());
        result = mr.rMap(map.getMapping());
        break;
      case FLAT_MAP:
        ZIO.FlatMap<R, E, A, B> flatMap = (ZIO.FlatMap<R, E, A, B>) zio;
        Either<E, A> fmr = attempt(flatMap.getZio());
        result = fmr.rFlatMap(r -> attempt(flatMap.getMapping().apply(r)));
        break;
      case EFFECT_TOTAL:
        ZIO.EffectTotal<A> effect = (ZIO.EffectTotal<A>) zio;
        var a = Try.of(effect.getEffect()).toEither();
        result = (Either<E, B>) a;
        break;
      case EFFECT_ASYNC:
        ZIO.EffectAsync<A, E> effectAsync = (ZIO.EffectAsync<A, E>) zio;
        Either<E, A> effectResult;
        effectAsync.getRegister().accept(consumer -> {
          Either<E, A> b = attempt(consumer);
        });
        result = null;
        break;
    }


    return result;
  }
}
