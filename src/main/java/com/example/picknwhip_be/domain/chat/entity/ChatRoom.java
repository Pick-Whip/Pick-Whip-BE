package com.example.picknwhip_be.domain.chat.entity;

import com.example.picknwhip_be.domain.review.service.user.entity.User;
import com.example.picknwhip_be.domain.shop.entity.Shop;
import com.example.picknwhip_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat_rooms")
public class ChatRoom extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long chatRoomId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id")
  private User customer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "shop_id")
  private Shop shop;

  private Long lastMessageId;

  public void updateLastMessage(Long messageId) {
    this.lastMessageId = messageId;
  }
}
