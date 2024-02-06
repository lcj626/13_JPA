package com.ohgiraffers.sectiontest01.manytoone;

import jakarta.persistence.*;

@Entity(name = "test_menu_category")
@Table(name = "tbl_menu")
public class Menu {

    @Id
    @Column(name = "menu_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int menuCode;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "menu_price")
    private int menuPrice;


    @JoinColumn(name = "category_code")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Category categoryCode;


    @Column(name = "orderable_status")
    private String orederableStatus;


    public Menu() {
    }

    public Menu(int menuCode, String menuName, int menuPrice, Category categoryCode, String orederableStatus) {
        this.menuCode = menuCode;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.categoryCode = categoryCode;
        this.orederableStatus = orederableStatus;
    }

    public int getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(int menuCode) {
        this.menuCode = menuCode;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(int menuPrice) {
        this.menuPrice = menuPrice;
    }

    public Category getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(Category categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getOrederableStatus() {
        return orederableStatus;
    }

    public void setOrederableStatus(String orederableStatus) {
        this.orederableStatus = orederableStatus;

    }

    @Override
    public String toString() {
        return "Menu{" +
                "menuCode=" + menuCode +
                ", menuName='" + menuName + '\'' +
                ", menuPrice=" + menuPrice +
                ", categoryCode=" + categoryCode +
                ", orederableStatus='" + orederableStatus + '\'' +
                '}';
    }
}