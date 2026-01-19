package com.example.picknwhip_be.domain.chat.repository;

import com.example.picknwhip_be.domain.chat.entity.ChatRoom;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import com.example.picknwhip_be.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
  Optional<ChatRoom> findByCustomerAndShop(User customer, Shop shop);

  @Query("SELECT r FROM ChatRoom r WHERE r.customer = :user OR r.shop.owner = :user")
  List<ChatRoom> findAllByCustomerOrShopOwner(@Param("user") User user);
}
