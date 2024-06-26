package com.kampus.kbazaar.cart;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findAllByUsername(String username);

    List<CartItem> findAllByUsernameAndSku(String username, String sku);
}
