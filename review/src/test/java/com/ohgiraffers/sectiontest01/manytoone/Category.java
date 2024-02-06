package com.ohgiraffers.sectiontest01.manytoone;

import jakarta.persistence.*;

@Entity(name = "category_test")
@Table(name = "tbl_category") // 객체와 테이블 매핑 어노테이션
public class Category {

    @Id // primary 키 설정
    @Column(name = "category_code") // 필드와 컬럼 매핑
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryCode; // categoryCode 클래스 변수가 tbl_category의 category_code와 매핑되고 이는 primary key이다

    @Column(name = "category_name")
    private String categoryName; // categoryName 클래스 변수가 tbl_category 의 category_name 항목과 매핑됨

    @Column(name = "ref_category_code")
    private Integer refCategoryName; // null값도 들어가야 하니 int보다 Integer


    public Category() {
    }

    public Category(int categoryCode, String categoryName, Integer refCategoryName) {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        this.refCategoryName = refCategoryName;
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

    public Integer getRefCategoryName() {
        return refCategoryName;
    }

    public void setRefCategoryName(Integer refCategoryName) {
        this.refCategoryName = refCategoryName;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryCode=" + categoryCode +
                ", categoryName='" + categoryName + '\'' +
                ", refCategoryName=" + refCategoryName +
                '}';
    }
}
