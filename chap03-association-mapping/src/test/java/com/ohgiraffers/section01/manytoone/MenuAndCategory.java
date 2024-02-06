package com.ohgiraffers.section01.manytoone;


import jakarta.persistence.*;


/*
* @JoinColunn 은 다대일 연관관계에서 사용되는 어노테이션이다.
* @Joincolum에서  사용할 수 있는 속성들은 다음과 같다.
*
* -name : 참조하는 테이블의 컬럼명을 저장한다.
* -referencedColumnName : 참조되는 컬럼명을 지정한다.
* -nullable : 참조하는 테이블의 컬럼에 nll 값을 허용할 지 여부
* -unique
* -insertable
* -updatable
* table
* foriegnkey
*
* @ManuToOne은 다대일 연관관계에서 사용되는 어노테잇연이다.
* @MenyToOne에서 사용할 수 있는 속성들은 다음과 같다
*
*
*- cascade : 연관된 엔티티의 대한 영속성 전이를 설정
*- fetch : 연관된 데이터를 로딩하는 전략을 설정
*- optianal : 연관된 에ㄴ티티가 플수인지 선택적인지 신중
* */
@Entity(name = "menu_and_category")
@Table(name = "tbl_menu")
public class MenuAndCategory {


    @Id
    @Column(name = "menu_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int menuCode;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "menu_price")
    private int menuPrice;


    /*
    * CascadeType.PERSIST
    * PERSIST : 엔티티를 영속화할 때, 연관된 하위 엔티티도 함계 유지한다.
    * Merge : 엔티티 상태를 병합 할 때 연관된 하위 엔티티도 모두 병합한다.
    * Remove : 엔티티를 제거할 때  연관된 하위 엔티티도 모두 제거한다.
    * Detach : 영속성 컨텍스트 엔티티 제거, 부모 엔티티를 Detach() 수행하면 하위 엔티티도 detach 상태가 되어 변경 사항을 반영하지 않는다
    * Refresh : 상위 엔티티를 새로고침 할 때 연관된 하위 엔티티도 모두 새로 고침
    * */
    @JoinColumn(name = "category_code")
//    @ManyToOne(cascade = CascadeType.PERSIST) //테스트 1
//    @ManyToOne(cascade = CascadeType.REMOVE) // 테스트2
//    @ManyToOne(cascade = CascadeType.REFRESH) // 테스트3
//    @ManyToOne // 테스트 3
//    @ManyToOne(cascade = CascadeType.MERGE) // 테스트4
    @ManyToOne(cascade = CascadeType.DETACH) // 테스트 5
    private Category category;

    @Column(name = "orderable_status")
    private String orderableStatus;

    public MenuAndCategory() {
    }

    public MenuAndCategory(int menuCode, String menuName, int menuPrice, Category category, String orderableStatus) {
        this.menuCode = menuCode;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.category = category;
        this.orderableStatus = orderableStatus;
    }

    public String getOrderableStatus() {
        return orderableStatus;
    }

    public void setOrderableStatus(String orderableStatus) {
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "MenuAndCategory{" +
                "menuCode=" + menuCode +
                ", menuName='" + menuName + '\'' +
                ", menuPrice=" + menuPrice +
                ", category=" + category +
                '}';
    }
}
