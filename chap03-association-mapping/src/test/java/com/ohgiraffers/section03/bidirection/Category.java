package com.ohgiraffers.section03.bidirection;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/*
* 양방향 매핑에서 어느 한 쪽이 연관 관계의 주인이 되면, 주인이 아닌 쪽에서는 속성을 지정해주어야 한다.
* 이때, 연관 관계의 주인이 아닌 객체 MappedBy를 써서 연관 관계 주인 객체의 필드명을 매핑 시켜 놓으면 양방향 관계를 적용할 수 있다.
* */

@Entity(name = "bidrection_category")
@Table(name = "tbl_category")
public class Category {

    @Id
    @Column(name = "category_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryCode;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "ref_category_code")
    private Integer refCategoryCode;

    public Category() {
    }



    @OneToMany(mappedBy = "categoryCode", cascade = CascadeType.PERSIST) // menu의 categoryCode에서 가져옴 mappedBy = 주인이 아닌 쪽에 붙인다 -> 연관관계 주인이 아님 =>조회만 가능
    private List<Menu> menuList = new ArrayList<>(); // 조회만 가능 조회를 위한 용도 연관되어 있지만 따로 있다.

    public Category(int categoryCode, String categoryName, Integer refCategoryCode) {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        this.refCategoryCode = refCategoryCode;
    }

    public int getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(int categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getRefCategoryCode() {
        return refCategoryCode;
    }

    public void setRefCategoryCode(Integer refCategoryCode) {
        this.refCategoryCode = refCategoryCode;
    }


    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {

        List<Menu> list = new ArrayList<>();
        for (Menu m: menuList){
            m.setCategoryCode(this);
            list.add(m);
        }
        this.menuList = list;
    }

    @Override
    public String toString() {
        return "CategoryAndMenu{" +
                "categoryCode=" + categoryCode +
                ", categoryName='" + categoryName + '\'' +
                ", refCategoryCode=" + refCategoryCode +
                ", menuList=" + menuList +
                '}';
    }
}
