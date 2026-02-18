package usr.dtzi.items.filter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import usr.dtzi.helpers.PredicateDuo;
import usr.dtzi.items.Equipment;

public class ItemBounds {

  static Map<String, PredicateDuo<Integer>> bounds = new HashMap<>();

  static {
    // would be nice to make a function that removes
    // the magic numbers here, but they will likely remain
    // static. These values are top 15% of the available values 
    // for an equipment type.
    bounds.put("knife", new PredicateDuo<>(ItemBounds.geq(37), ItemBounds.geq(5)));
    bounds.put("gun", new PredicateDuo<>(ItemBounds.geq(59), ItemBounds.geq(10)));
    bounds.put("rifle", new PredicateDuo<>(ItemBounds.geq(87), ItemBounds.geq(15)));
    bounds.put("sniper", new PredicateDuo<>(ItemBounds.geq(117), ItemBounds.geq(20)));
    bounds.put("tank", new PredicateDuo<>(ItemBounds.geq(155), ItemBounds.geq(29)));
    bounds.put("jet", new PredicateDuo<>(ItemBounds.geq(263), ItemBounds.geq(39)));
    bounds.put("helmet1", new PredicateDuo<>(ItemBounds.geq(9)));
    bounds.put("helmet2", new PredicateDuo<>(ItemBounds.geq(19)));
    bounds.put("helmet3", new PredicateDuo<>(ItemBounds.geq(29)));
    bounds.put("helmet4", new PredicateDuo<>(ItemBounds.geq(39)));
    bounds.put("helmet5", new PredicateDuo<>(ItemBounds.geq(57)));
    bounds.put("helmet6", new PredicateDuo<>(ItemBounds.geq(77)));
    bounds.put("chest1", new PredicateDuo<>(ItemBounds.geq(5)));
    bounds.put("chest2", new PredicateDuo<>(ItemBounds.geq(10)));
    bounds.put("chest3", new PredicateDuo<>(ItemBounds.geq(15)));
    bounds.put("chest4", new PredicateDuo<>(ItemBounds.geq(20)));
    bounds.put("chest5", new PredicateDuo<>(ItemBounds.geq(29)));
    bounds.put("chest6", new PredicateDuo<>(ItemBounds.geq(39)));
    bounds.put("pants1", new PredicateDuo<>(ItemBounds.geq(5)));
    bounds.put("pants2", new PredicateDuo<>(ItemBounds.geq(10)));
    bounds.put("pants3", new PredicateDuo<>(ItemBounds.geq(15)));
    bounds.put("pants4", new PredicateDuo<>(ItemBounds.geq(20)));
    bounds.put("pants5", new PredicateDuo<>(ItemBounds.geq(29)));
    bounds.put("pants6", new PredicateDuo<>(ItemBounds.geq(39)));
    bounds.put("boots1", new PredicateDuo<>(ItemBounds.geq(5)));
    bounds.put("boots2", new PredicateDuo<>(ItemBounds.geq(10)));
    bounds.put("boots3", new PredicateDuo<>(ItemBounds.geq(15)));
    bounds.put("boots4", new PredicateDuo<>(ItemBounds.geq(20)));
    bounds.put("boots5", new PredicateDuo<>(ItemBounds.geq(29)));
    bounds.put("boots6", new PredicateDuo<>(ItemBounds.geq(39)));
    bounds.put("gloves1", new PredicateDuo<>(ItemBounds.geq(5)));
    bounds.put("gloves2", new PredicateDuo<>(ItemBounds.geq(10)));
    bounds.put("gloves3", new PredicateDuo<>(ItemBounds.geq(15)));
    bounds.put("gloves4", new PredicateDuo<>(ItemBounds.geq(20)));
    bounds.put("gloves5", new PredicateDuo<>(ItemBounds.geq(29)));
    bounds.put("gloves6", new PredicateDuo<>(ItemBounds.geq(39)));
  }

  public static boolean isWithin(Equipment eq) {
    PredicateDuo<Integer> predicates = bounds.get(eq.itemCode());
    return predicates.test(eq.getSkill1(), eq.getSkill2());
  }

  private static Predicate<Integer> geq(int x) {
    return ((y) -> x <= y);
  }
}
