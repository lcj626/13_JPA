package com.ohgiraffers.springjpa.order.repository;

import com.ohgiraffers.springjpa.order.entity.MenuOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<MenuOrder, Integer> {
}
