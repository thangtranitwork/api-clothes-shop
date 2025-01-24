package com.clothes.noc.repository;

import com.clothes.noc.entity.CartItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {
    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem ci WHERE ci.cart.user.id = :userId AND ci.productVariant.id = :variantId")
    int removeItem(String userId, String variantId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem ci WHERE ci.cart.user.id = :userId")
    int removeAllItem(String userId);

    @Modifying
    @Transactional
    @Query("UPDATE CartItem ci SET ci.quantity = :quantity WHERE ci.cart.user.id = :userId AND ci.productVariant.id = :variantId")
    int updateQuantity(String userId, String variantId, int quantity);

    @Query(value = "SELECT ci.quantity FROM CartItem ci WHERE ci.cart.user.id = :userId AND ci.productVariant.id = :variantId")
    Optional<Integer> getQuantityByUserIdAndVariantId(String userId, String variantId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO cart_item (id, cart_id, product_variant_id, quantity) VALUES (:id, :cartId, :variantId, :quantity)", nativeQuery = true)
    int addCartItem(String id, String cartId, String variantId, int quantity);
}

