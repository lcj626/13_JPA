package com.ohgiraffers.springjpa.order.infra;

import com.ohgiraffers.springjpa.menu.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class MenuFindImpl implements MenuFind{


    @Autowired
    private MenuService menuService;

    @Override
    public Integer findMenu(int menuCode) {

//        if(Objects.isNull(findCode)){
//            return null;
//        } 의미 없음

        Integer findCode = menuService.findMenuCode(menuCode);

        return findCode;
    }
}

