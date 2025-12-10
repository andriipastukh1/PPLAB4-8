package service;

import util.AppLogger; // <--- Імпорт логгера

import java.io.Serializable;
import java.util.*;

public class CategoryService implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, List<String>> categories = new LinkedHashMap<>();
    private static CategoryService instance = new CategoryService();

    private CategoryService() {
        seedDefaults();


        AppLogger.LOGGER.info("CategoryService initialized with default categories.");
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


        if (mainCategory == null || subcategory == null) {
            AppLogger.LOGGER.warning("CategoryService: Attempted to add null");



            return false;
        }

        categories.putIfAbsent(mainCategory, new ArrayList<>());
        List<String> list = categories.get(mainCategory);

        if (list.contains(subcategory)) {
            AppLogger.LOGGER.warning("CategoryService: Subcategory '" + subcategory + "' already exists in '" + mainCategory + "'.");
            return false;
        }

        list.add(subcategory);
        AppLogger.LOGGER.info("CategoryService: Added new subcategory '" + subcategory + "' to '" + mainCategory + "'.");
        return true;
    }

    public boolean removeSubcategory(String mainCategory, String subcategory) {
        List<String> list = categories.get(mainCategory);
        if (list == null) {
            AppLogger.LOGGER.warning("CategoryService: Cannot remove subcategory. Main category '" + mainCategory + "' not found.");
            return false;
        }

        boolean removed = list.remove(subcategory);
        if (removed) {
            AppLogger.LOGGER.info("CategoryService: Removed subcategory '" + subcategory + "' from '" + mainCategory + "'.");
        } else {
            AppLogger.LOGGER.warning("CategoryService: Failed to remove subcategory '" + subcategory + "' (not found).");
        }
        return removed;


    }



    public Map<String, List<String>> getAll() {
        return Collections.unmodifiableMap(categories);



    }
}