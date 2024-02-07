package com.ohgiraffers.section03.bidirection;


import com.ohgiraffers.section02.onetomany.CategoryAndMenu;
import jakarta.persistence.*;


@Entity(name = "bidirection_menu")
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


    @JoinColumn(name = "category_code") // 연관을 맺은 상대편 컬럼 기술
    @ManyToOne
    private Category categoryCode; // 필드명을 관계맺는 상대방에게 걸어줌


    @Column(name = "orderable_status")
    private String orderableStatus;



    public Menu() {
    }

    public Menu(int menuCode, String menuName, int menuPrice, Category categoryCode, String orderableStatus) {
        this.menuCode = menuCode;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.categoryCode = categoryCode;
        this.orderableStatus = orderableStatus;
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

    public String getOrderableStatus() {
        return orderableStatus;
    }

    public void setOrderableStatus(String orderableStatus) {
        this.orderableStatus = orderableStatus;
    }

    @Override
    public String toString() {
        return " \n Menu{" +
                "menuCode=" + menuCode +
                ", menuName='" + menuName + '\'' +
                ", menuPrice=" + menuPrice +
                ", orderableStatus='" + orderableStatus + '\'' +
                '}';
    }
}
