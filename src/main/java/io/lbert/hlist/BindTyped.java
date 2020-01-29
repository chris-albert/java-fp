package io.lbert.hlist;

public class BindTyped<A extends BindTyped<A>> {

  public static <H, T extends BindTyped<T>> OpenTerm<H, T> open(Class<H> clazz, T t) {
    return new OpenTerm<>(clazz, t);
  }

  public static <T extends BindTyped<T>> StringTerm<T> string(String string, T t) {
    return new StringTerm<>(string, t);
  }

  public static EndTerm end() {
    return EndTerm.END_TERM;
  }

  public static final class OpenTerm<H, T extends BindTyped<T>> extends BindTyped<OpenTerm<H, T>> {

    private final Class<H> clazz;
    private final T tail;

    private OpenTerm(Class<H> clazz, T tail) {
      this.clazz = clazz;
      this.tail = tail;
    }
  }

  public static final class StringTerm<T extends BindTyped<T>> extends BindTyped<StringTerm<T>> {

    private final String string;
    private final T tail;

    private StringTerm(String string, T tail) {
      this.string = string;
      this.tail = tail;
    }
  }

  public static final class EndTerm extends BindTyped<EndTerm> {

    private static final EndTerm END_TERM = new EndTerm();

  }
}