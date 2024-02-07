package com.ohgiraffers.springjpa.order.service;

import com.ohgiraffers.springjpa.menu.entity.Menu;
import com.ohgiraffers.springjpa.order.DTO.OrderAndPaymentsVo;
import com.ohgiraffers.springjpa.order.entity.MenuOrder;
import com.ohgiraffers.springjpa.order.entity.Payments;
import com.ohgiraffers.springjpa.order.infra.MenuFind;
import com.ohgiraffers.springjpa.order.repository.OrderRepository;
import com.ohgiraffers.springjpa.order.repository.PaymentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentsRepository paymentsRepository;

    @Autowired
    private MenuFind menuFind;

    @Autowired
    private PaymentsService paymentsService;

//    public MenuOrder order(Integer menuCode){
//        Integer findMenuCode = menuFind.findMenu(menuCode);
//
//        if(Objects.isNull(findMenuCode)){
//           return null;
//        }
//
//        MenuOrder menuOrder = new MenuOrder();
//        menuOrder.setMenu(findMenuCode);
//        menuOrder.setOrderDate(new Date());
//        //결제 대행사 수행하는 로직 있다고 가정
//        Payments resultPayment = paymentsService.orderPayments(0);
//        // 저ㅜ문에 결제 정보 등록
//        resultPayment.setMenuOrder(menuOrder);
//        menuOrder.getPayments().add(resultPayment);
//
//    }
}
