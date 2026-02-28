package usr.dtzi.items.filter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import usr.dtzi.helpers.PredicateDuo;
import usr.dtzi.items.Equipment;

public class ItemBounds {

  // static Map<String, PredicateDuo<Integer>> bounds = new HashMap<>();
  static ObjectMapper mapper = new ObjectMapper();
  static TypeReference<HashMap<String, HashMap<String, HashMap<String,
    HashMap<String, Integer>>>>> typeRef = new TypeReference<>() {};

  public static HashMap<String, HashMap<String, 
         HashMap<String, Integer>>> bounds = mapper
           .readValue(new File("item_bounds.json"), typeRef).get("items");


    // To get the items roll efficiency:
    // - aboveMin = (minRoll - stat)
    // - rollEff = (aboveMin/(maxRoll - minRoll)


    // would be nice to make a function that removes
    // the magic numbers here, but they will likely remain
    // static. These values are top 15% of the available values 
    // for an equipment type.
    // bounds.put("knife", new PredicateDuo<>(ItemBounds.geq(37), ItemBounds.geq(5)));
    // bounds.put("gun", new PredicateDuo<>(ItemBounds.geq(59), ItemBounds.geq(10)));
    // bounds.put("rifle", new PredicateDuo<>(ItemBounds.geq(87), ItemBounds.geq(15)));
    // bounds.put("sniper", new PredicateDuo<>(ItemBounds.geq(117), ItemBounds.geq(20)));
    // bounds.put("tank", new PredicateDuo<>(ItemBounds.geq(155), ItemBounds.geq(29)));
    // bounds.put("jet", new PredicateDuo<>(ItemBounds.geq(263), ItemBounds.geq(39)));
    // bounds.put("helmet1", new PredicateDuo<>(ItemBounds.geq(9)));
    // bounds.put("helmet2", new PredicateDuo<>(ItemBounds.geq(19)));
    // bounds.put("helmet3", new PredicateDuo<>(ItemBounds.geq(29)));
    // bounds.put("helmet4", new PredicateDuo<>(ItemBounds.geq(39)));
    // bounds.put("helmet5", new PredicateDuo<>(ItemBounds.geq(57)));
    // bounds.put("helmet6", new PredicateDuo<>(ItemBounds.geq(77)));
    // bounds.put("chest1", new PredicateDuo<>(ItemBounds.geq(5)));
    // bounds.put("chest2", new PredicateDuo<>(ItemBounds.geq(10)));
    // bounds.put("chest3", new PredicateDuo<>(ItemBounds.geq(15)));
    // bounds.put("chest4", new PredicateDuo<>(ItemBounds.geq(20)));
    // bounds.put("chest5", new PredicateDuo<>(ItemBounds.geq(29)));
    // bounds.put("chest6", new PredicateDuo<>(ItemBounds.geq(39)));
    // bounds.put("pants1", new PredicateDuo<>(ItemBounds.geq(5)));
    // bounds.put("pants2", new PredicateDuo<>(ItemBounds.geq(10)));
    // bounds.put("pants3", new PredicateDuo<>(ItemBounds.geq(15)));
    // bounds.put("pants4", new PredicateDuo<>(ItemBounds.geq(20)));
    // bounds.put("pants5", new PredicateDuo<>(ItemBounds.geq(29)));
    // bounds.put("pants6", new PredicateDuo<>(ItemBounds.geq(39)));
    // bounds.put("boots1", new PredicateDuo<>(ItemBounds.geq(5)));
    // bounds.put("boots2", new PredicateDuo<>(ItemBounds.geq(10)));
    // bounds.put("boots3", new PredicateDuo<>(ItemBounds.geq(15)));
    // bounds.put("boots4", new PredicateDuo<>(ItemBounds.geq(20)));
    // bounds.put("boots5", new PredicateDuo<>(ItemBounds.geq(29)));
    // bounds.put("boots6", new PredicateDuo<>(ItemBounds.geq(39)));
    // bounds.put("gloves1", new PredicateDuo<>(ItemBounds.geq(5)));
    // bounds.put("gloves2", new PredicateDuo<>(ItemBounds.geq(10)));
    // bounds.put("gloves3", new PredicateDuo<>(ItemBounds.geq(15)));
    // bounds.put("gloves4", new PredicateDuo<>(ItemBounds.geq(20)));
    // bounds.put("gloves5", new PredicateDuo<>(ItemBounds.geq(29)));
    // bounds.put("gloves6", new PredicateDuo<>(ItemBounds.geq(39)));
    // }
    //
  public static int getSkill1MaxRoll(String itemCode){
    return ItemBounds.bounds.get(itemCode).get("skill1").get("maxRoll");
  }

  public static int getSkill2MaxRoll(String itemCode){
    return ItemBounds.bounds.get(itemCode).get("skill2").get("maxRoll");
  }

  public static int getSkill1MinRoll(String itemCode){
    return ItemBounds.bounds.get(itemCode).get("skill1").get("minRoll");
  }

  public static int getSkill2MinRoll(String itemCode){
    return ItemBounds.bounds.get(itemCode).get("skill2").get("minRoll");
  }

  public static double getRollEfficiency(Equipment eq) {
    if (eq.getSkill2() == null) {
      return ItemBounds.getOneStatRollEfficiency(eq);
    } else return ItemBounds.getTwoStatRollEfficiency(eq);
  }

  private static double getOneStatRollEfficiency(Equipment eq) {
    double eff;
    var maxRoll = ItemBounds.getSkill1MaxRoll(eq.itemCode());
    var minRoll = ItemBounds.getSkill1MinRoll(eq.itemCode());
    var skill1 = eq.getSkill1();
    var aboveMin = skill1 - minRoll;
    eff = (double) aboveMin / (maxRoll - minRoll);
    return eff;
  }

  private static double getTwoStatRollEfficiency(Equipment eq) {
    double eff;
    var maxRoll1 = ItemBounds.getSkill1MaxRoll(eq.itemCode());
    var minRoll1 = ItemBounds.getSkill1MinRoll(eq.itemCode());
    var maxRoll2 = ItemBounds.getSkill2MaxRoll(eq.itemCode());
    var minRoll2 = ItemBounds.getSkill2MinRoll(eq.itemCode());

    var skill1 = eq.getSkill1();
    var skill2 = eq.getSkill2();

    var aboveMin1 = skill1 - minRoll1;
    var aboveMin2 = skill2 - minRoll2;
    var eff1 = (double) aboveMin1 / (maxRoll1 - minRoll1);
    var eff2 = (double) aboveMin2 / (maxRoll2 - minRoll2);

    eff = (eff1 + eff2) / 2;
    return eff;
  }


  

  // /** 
  //  * @param   eq  an Equipment
  //  * @return  whether the {@link Equipment} is better than 
  //  *          {@link ItemBounds#bounds}.
  //  */
  // public static boolean isWithin(Equipment eq) {
  //   PredicateDuo<Integer> predicates = bounds.get(eq.itemCode());
  //   return predicates.test(eq.getSkill1(), eq.getSkill2());
  // }
  //
  // /** 
  //  * @param   x
  //  * @return  a function that consumes a y and returns whether x is 
  //  *          larger or equal then y.
  //  */
  // private static Predicate<Integer> geq(int x) {
  //   return ((y) -> x <= y);
  // }
}
