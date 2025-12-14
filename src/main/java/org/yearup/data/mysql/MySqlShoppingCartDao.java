package org.yearup.data.mysql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@Repository
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao
{
    @Autowired
    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        ShoppingCart cart = new ShoppingCart();

        String sql = "SELECT sc.user_id, sc.product_id, sc.quantity, " +
                "p.product_id, p.name, p.price, p.category_id, p.description, " +
                "p.sub_category, p.stock, p.featured, p.image_url " +
                "FROM shopping_cart sc " +
                "JOIN products p ON sc.product_id = p.product_id " +
                "WHERE sc.user_id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            ResultSet row = statement.executeQuery();
            while (row.next()) {
                ShoppingCartItem item = new ShoppingCartItem();
                Product product = new Product();
                product.setProductId(row.getInt("product_id"));
                product.setName(row.getString("name"));
                product.setPrice(row.getBigDecimal("price"));
                product.setCategoryId(row.getInt("category_id"));
                product.setDescription(row.getString("description"));
                product.setSubCategory(row.getString("sub_category"));
                product.setStock(row.getInt("stock"));
                product.setFeatured(row.getBoolean("featured"));
                product.setImageUrl(row.getString("image_url"));

                item.setProduct(product);
                item.setQuantity(row.getInt("quantity"));
                item.setDiscountPercent(BigDecimal.ZERO);
                cart.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cart;
    }

    @Override
    public void addItem(int userId, int productId) {
        String sql = "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?, ?, 1)";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setInt(2, productId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateItem(int userId, int productId, int quantity) {
        String sql = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, quantity);
            statement.setInt(2, userId);
            statement.setInt(3, productId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addProduct(int userId, int productId) {
        // FIX: implement POST behavior per PDF:
        // if not in cart -> insert qty=1; else -> increment qty by 1 :contentReference[oaicite:2]{index=2}
        String selectSql = "SELECT quantity FROM shopping_cart WHERE user_id = ? AND product_id = ?";
        String insertSql = "INSERT INTO shopping_cart(user_id, product_id, quantity) VALUES (?, ?, 1)";
        String updateSql = "UPDATE shopping_cart SET quantity = quantity + 1 WHERE user_id = ? AND product_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement select = connection.prepareStatement(selectSql))
        {
            select.setInt(1, userId);
            select.setInt(2, productId);
            try (ResultSet rs = select.executeQuery())
            {
                if (rs.next())
                {
                    try (PreparedStatement update = connection.prepareStatement(updateSql))
                    {
                        update.setInt(1, userId);
                        update.setInt(2, productId);
                        update.executeUpdate();
                    }
                }
                else
                {
                    try (PreparedStatement insert = connection.prepareStatement(insertSql))
                    {
                        insert.setInt(1, userId);
                        insert.setInt(2, productId);
                        insert.executeUpdate();
                    }
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateProduct(int userId, int productId, int quantity) {
        // FIX: implement PUT behavior (PDF labels PUT optional/bonus) :contentReference[oaicite:3]{index=3}
        // Only update if item already exists; do nothing otherwise.
        String existsSql = "SELECT 1 FROM shopping_cart WHERE user_id = ? AND product_id = ?";
        String updateSql = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement exists = connection.prepareStatement(existsSql))
        {
            exists.setInt(1, userId);
            exists.setInt(2, productId);
            try (ResultSet rs = exists.executeQuery())
            {
                if (!rs.next()) return;
                try (PreparedStatement update = connection.prepareStatement(updateSql))
                {
                    update.setInt(1, quantity);
                    update.setInt(2, userId);
                    update.setInt(3, productId);
                    update.executeUpdate();
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearCart(int userId) {
        // FIX: implement DELETE cart: delete all items for user
        String sql = "DELETE FROM shopping_cart WHERE user_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    // FIX: local mapper so this class does not depend on ProductDao
    private static Product mapProduct(ResultSet rs) throws SQLException
    {
        int productId = rs.getInt("product_id");
        String name = rs.getString("name");
        BigDecimal price = rs.getBigDecimal("price");
        int categoryId = rs.getInt("category_id");
        String description = rs.getString("description");
        String subCategory = rs.getString("subcategory");
        int stock = rs.getInt("stock");
        boolean featured = rs.getBoolean("featured");
        String imageUrl = rs.getString("image_url");
        return new Product(productId, name, price, categoryId, description, subCategory, stock, featured, imageUrl);
    }
}