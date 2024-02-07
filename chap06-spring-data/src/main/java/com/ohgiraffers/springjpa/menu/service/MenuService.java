package com.ohgiraffers.springjpa.menu.service;

import com.ohgiraffers.springjpa.category.entity.Category;
import com.ohgiraffers.springjpa.menu.dto.MenuDTO;
import com.ohgiraffers.springjpa.menu.entity.Menu;
import com.ohgiraffers.springjpa.menu.infra.CategoryFind;
import com.ohgiraffers.springjpa.menu.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private CategoryFind categoryFind; // 결합구조 느슨하게 만들기 위해 CategoryService랑 바로 연결짓지 않고 CategoryFind로 연결

    public Object insertMenu(MenuDTO menuDTO){
        // 메뉴 이름이 존재하는가
        Menu menu = menuRepository.findByMenuName(menuDTO.getMenuName());

        if(!Objects.isNull(menu)){
            return new String(menuDTO.getMenuName() + "의 메뉴가 존재 한다");
        }

        //가격 정보 확인
        if((menuDTO.getMenuPrice() < 0)){
            return new String(menuDTO.getMenuPrice() + "이걸 팔아서 장사를 어떻게 해?");
        }

        //카테고리 코드
        Integer categoryCode = categoryFind.getCategory(menuDTO.getCategoryCode());

        if(Objects.isNull(categoryCode)){
            return new String(menuDTO.getCategoryCode() + "는 존재하지 않습니다");
        }

        Menu newMenu = new Menu();
        newMenu.setMenuName(menuDTO.getMenuName());
        newMenu.setMenuPrice(menuDTO.getMenuPrice());
        newMenu.setCategory(menuDTO.getCategoryCode());

        Menu result = menuRepository.save(newMenu);

        return result;
    }

    public Integer findMenuCode(Integer menuCode){

        Menu findMenu = menuRepository.findByMenuCode(menuCode);

        if(Objects.isNull(findMenu)){
            return null;
        }

        return findMenu.getMenuCode();


    }
}
