package org.yearup.data;
import org.yearup.models.Category;
import java.util.List;

public interface CategoryDao  //CategoryDao defines the data access operations for Category entities.
{
    // Retrieves all categories from the database
    List<Category> getAllCategories();   // Retrieves all categories from the database.
    Category getById(int categoryId);   // Retrieves a single category by its unique ID.
    Category create(Category category); // Creates a new category in the database
    void update(int categoryId, Category category); //   Updates an existing category identified by its ID
    void delete(int categoryId);  // Deletes a category from the database.

}
