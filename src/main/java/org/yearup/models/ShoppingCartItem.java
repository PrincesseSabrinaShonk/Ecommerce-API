package org.yearup.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;

public class ShoppingCartItem
{
    private Product product = null;
    private int quantity = 1;
    private BigDecimal discountPercent;
//    private BigDecimal discountPercent = BigDecimal.ZERO;   i replace this with line 11

    public ShoppingCartItem()
    {
        // do nothing
    }
    public Product getProduct()
    {
        return product;
    }

    public void setProduct(Product product)
    {
        this.product = product;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public BigDecimal getDiscountPercent()
    {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent)
    {
        this.discountPercent = discountPercent;
    }

    @JsonIgnore
    public int getProductId()
    {
        return this.product.getProductId();
    }

    public BigDecimal getLineTotal()
    {
        BigDecimal price = product.getPrice();
        BigDecimal qty = new BigDecimal(quantity);
        BigDecimal total = price.multiply(qty);
        if(discountPercent != null && discountPercent.compareTo(BigDecimal.ZERO) > 0)
        {
            BigDecimal discount = total.multiply(discountPercent);
            total = total.subtract(discount);
        }
        return total;
    }
}
//        BigDecimal basePrice = product.getPrice();
//        BigDecimal quantity = new BigDecimal(this.quantity);
//
//        BigDecimal subTotal = basePrice.multiply(quantity);
//        BigDecimal discountAmount = subTotal.multiply(discountPercent);
//
//        return subTotal.subtract(discountAmount);