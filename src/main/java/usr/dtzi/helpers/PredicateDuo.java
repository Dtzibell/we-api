package usr.dtzi.helpers;
import java.util.function.Predicate;

public class PredicateDuo<T extends Number> {

  Predicate<T> firstPredicate;
  Predicate<T> secondPredicate;

  public PredicateDuo (Predicate<T> first, Predicate<T> second) {
    firstPredicate = first;
    secondPredicate = second;
  }

  public PredicateDuo (Predicate<T> first) {
    firstPredicate = first;
    secondPredicate = isNull();
  }

  public boolean test(T x, T y) {
    return firstPredicate.test(x) && secondPredicate.test(y);
  }
   
  private Predicate<T> isNull() {
    return ((x) -> x == null);
  }
}
