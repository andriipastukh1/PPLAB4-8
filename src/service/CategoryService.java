package service;

import java.io.Serializable;
import java.util.*;


public class CategoryService implements Serializable {
    private static final long serialVersionUID = 1L;



    private Map<String, List<String>> categories = new LinkedHashMap<>();
    private static CategoryService instance = new CategoryService();



    private CategoryService() {
        seedDefaults();


    }

    public static CategoryService getInstance() { return instance; }

    private void seedDefaults() {

        categories.put("INCOME", new ArrayList<>(Arrays.asList(
                "Salary (primary)",
                "Salary (secondary)",
                "Author royalties",


                "Other income"
        )));

        categories.put("BENEFIT", new ArrayList<>(Arrays.asList(
                "Child allowance",
                "Material aid",


                "Tax relief",
                "Other benefit"
        )));
    }

    public Set<String> getMainCategories() {
        return Collections.unmodifiableSet(categories.keySet());


    }

    public List<String> getSubcategories(String mainCategory) {
        List<String> l = categories.get(mainCategory);


        return l == null ? Collections.emptyList() : Collections.unmodifiableList(l);


    }

    public boolean addSubcategory(String mainCategory, String subcategory) {
        if (mainCategory == null || subcategory == null) return false;


        categories.putIfAbsent(mainCategory, new ArrayList<>());


        List<String> list = categories.get(mainCategory);


        if (list.contains(subcategory)) return false;


        list.add(subcategory);


        return true;
    }

    public boolean removeSubcategory(String mainCategory, String subcategory) {
        List<String> list = categories.get(mainCategory);
        if (list == null) return false;




        return list.remove(subcategory);
    }

    public Map<String, List<String>> getAll() {


        return Collections.unmodifiableMap(categories);
    }
}
