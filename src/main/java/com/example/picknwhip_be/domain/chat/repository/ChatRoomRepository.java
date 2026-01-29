package com.example.picknwhip_be.domain.chat.repository;

import com.example.picknwhip_be.domain.chat.entity.ChatRoom;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import com.example.picknwhip_be.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
  Optional<ChatRoom> findByCustomerAndShop(User customer, Shop shop);

  @Query("SELECT r FROM ChatRoom r WHERE r.customer = :user OR r.shop.owner = :user")
  List<ChatRoom> findAllByCustomerOrShopOwner(@Param("user") User user);

  @Query(
      "SELECT r FROM ChatRoom r "
          + "JOIN FETCH r.shop s "
          + "WHERE (r.customer = :user OR s.owner = :user) "
          + "AND (:keyword IS NULL OR s.shopName LIKE CONCAT(:keyword, '%')) "
          + "AND (:cursor IS NULL OR r.lastMessageId < :cursor OR r.lastMessageId IS NULL) "
          + "ORDER BY r.lastMessageId DESC NULLS LAST")
  List<ChatRoom> findChatRoomsWithCursor(
      @Param("user") User user,
      @Param("keyword") String keyword,
      @Param("cursor") Long cursor,
      Pageable pageable);
}
