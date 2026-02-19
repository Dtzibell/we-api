package usr.dtzi.items.filter;

import usr.dtzi.items.Equipment;
import java.util.function.Predicate;

import java.util.ArrayList;
import java.util.List;

public class ItemFilter { 

  List<Equipment> eq; 

  /**
   * @param eq  a list of {@link Equipment}.
   */
  public ItemFilter(List<Equipment> eq) {
    this.eq = eq;
  }

  /** 
   * @param   filter  a filter that items are passed by.
   * @return  a list containing items that pass {@code filter}.
   */
  public List<Equipment> filter(Predicate<Equipment> filter) {
    var newLst = new ArrayList<Equipment>();
    for (Equipment item : this.eq) {
      if (filter.test(item)) {
        newLst.add(item);
      };
    }
    return newLst;
  }
}

