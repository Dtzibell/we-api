package usr.dtzi.items.filter;

import usr.dtzi.items.Equipment;

import java.util.ArrayList;
import java.util.List;

public class ItemFilter { 

  List<Equipment> eq; 
  String quality;

  public ItemFilter(List<Equipment> eq) {
    this.eq = eq;
    this.quality = eq.get(0).itemCode();
  }

  public List<Equipment> filter() {
    var newLst = new ArrayList<Equipment>();
    for (Equipment item : this.eq) {
      if (ItemBounds.isWithin(item)) {
        newLst.add(item);
      };
    }
    return newLst;
  }
}

