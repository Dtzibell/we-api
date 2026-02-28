package usr.dtzi.items.filter;

import usr.dtzi.items.Equipment;

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
  public List<Equipment> filter(double rollEfficiency) {
    List<Equipment> fEq = new ArrayList<>();
    for (Equipment e : this.eq) {
      var eff = ItemBounds.getRollEfficiency(e);
      if (eff > rollEfficiency) {
        fEq.add(e);
      }
    }
    return fEq;
  }
}

