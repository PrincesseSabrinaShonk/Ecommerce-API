package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Product;
import org.yearup.data.ProductDao;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products") // Base URL for all product-related endpoints
@CrossOrigin  // Allows requests from different origins
public class ProductsController  //controller for managing products
{
    private ProductDao productDao;  //Constructor injection for ProductDao

    @Autowired
    public ProductsController(ProductDao productDao)
    {
        this.productDao = productDao;
    }


    // GET products
    // Supports optional query parameters for filtering products
    @GetMapping("")
    @PreAuthorize("permitAll()")
    public List<Product> search(@RequestParam(name="cat", required = false) Integer categoryId,
                                @RequestParam(name="minPrice", required = false) BigDecimal minPrice,
                                @RequestParam(name="maxPrice", required = false) BigDecimal maxPrice,
                                @RequestParam(name="subCategory", required = false) String subCategory
                                )
    {
        try { // Delegate filtering logic to the DAO
            return productDao.search(categoryId, minPrice, maxPrice, subCategory);
        }  catch(ResponseStatusException ex)
        {  throw ex;
        }  catch(Exception ex)
                { throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
                }
    }
    // GET /products/{id}
    // Retrieves a single product by its ID
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public Product getById(@PathVariable int id )
    {
        try {
            var product = productDao.getById(id);
            if (product == null)   // If no product exists with this ID, return 404
                throw new ResponseStatusException(HttpStatus.NOT_FOUND); // Re-throw known HTTP errors 401
            return product;
        }
        catch(ResponseStatusException ex)
            {   throw ex; }
                catch(Exception ex)
            {throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");}
        }

    // POST /products
    // Creates a new product
    // Admin-only endpoint
    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public Product addProduct(@RequestBody Product product)
    {
        try
        { // Persist the new product to the database
            return productDao.create(product);
        }
        catch(ResponseStatusException ex)
        {
            throw ex;
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    // PUT /products/{id}
    // Updates an existing product
    // Admin-only endpoint

    //This method lets an admin user update an existing product in the system.
    //It takes the product’s ID from the URL,
    // takes the new product data from the request body, and saves the changes to the database.
    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')") // first bugs was role_Admin
    public void updateProduct(@PathVariable int id, @RequestBody Product product)
    { //Calls the DAO to update the existing product in the database.
        try  // Update the product with the given ID
        { productDao.update(id, product);  } // FIX: update should call update, not create
            // productDao.create(product); ( we need to change this) i change line 83
        catch(ResponseStatusException ex)
        {
            throw ex; }
        catch(Exception ex)
        {throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");}
    }
    // DELETE /products/{id}
    // Deletes a product by ID
    // Admin-only endpoint
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProduct(@PathVariable int id)
    {
        try { // Check if the product exists before deleting
            var product = productDao.getById(id);
            if (product == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            productDao.delete(id);   // Remove product from database
        }
        catch(ResponseStatusException ex)
            {  throw ex;
            } catch(Exception ex)
            {throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
            }
        }
    }

