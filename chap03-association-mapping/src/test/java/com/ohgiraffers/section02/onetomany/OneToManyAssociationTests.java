package com.ohgiraffers.section02.onetomany;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

public class OneToManyAssociationTests {

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    @BeforeEach
    public void initManager() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterAll
    public static void closeFactory() {
        entityManagerFactory.close();
    }

    @AfterEach
    public void closeManager() {
        entityManager.close();
    }


    @Test
    void 일대다_연관관계_객체_그래프_탐색을_이용한_조회_테스트(){

        int categoryCode = 10;

        CategoryAndMenu categoryAndMenu = entityManager.find(CategoryAndMenu.class, categoryCode);

        //then
        Assertions.assertNotNull(categoryAndMenu);

        System.out.println(categoryAndMenu);
    }

//    @Test
//    void 일대다_연관관계_객체_삽입_테스스(){
//
//        CategoryAndMenu categoryAndMenu = new CategoryAndMenu();
//        categoryAndMenu.setCategoryCode(955);
//        categoryAndMenu.setCategoryName("일대다 카테고리");
//        categoryAndMenu.setRefCategoryCode(null);
//
//        List<Menu> menuList = new ArrayList<>();
//        Menu menu = new Menu();
//        menu.setMenuCode(902);
//        menu.setMenuName("일대다 아이스크림");
//        menu.setMenuPrice(8700);
//        menu.setOrderableStatus("Y");
//        menu.setCategory(categoryAndMenu.getCategoryCode());
//
//        menuList.add(menu);
//
//        categoryAndMenu.setMenuList(menuList);
//
//        EntityTransaction transaction = entityManager.getTransaction();
//        transaction.begin();
//        entityManager.persist(categoryAndMenu);
//        transaction.commit();
//
//        CategoryAndMenu foundCategoryMenu = entityManager.find(CategoryAndMenu.class, 955);
//        System.out.println(foundCategoryMenu);
//    }

    @Test
    void 일대다_연관관계_객체_삽입_테스스(){ // 양방향 상관관계 맛보기

        CategoryAndMenu categoryAndMenu = new CategoryAndMenu();
        categoryAndMenu.setCategoryName("일대다 카테고리 추가 테스트");
        categoryAndMenu.setRefCategoryCode(null);

        entityManager.persist(categoryAndMenu);

        Menu menu = new Menu();
        menu.setMenuName("this 아이스크림 테스트");
        menu.setMenuPrice(50002220);
        menu.setOrderableStatus("Y");
        menu.setCategoryCode(categoryAndMenu);

        categoryAndMenu.getMenuList().add(menu);

        entityManager.persist(menu);

        //when
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityTransaction.commit();

        //then
        CategoryAndMenu foundCategoryAndMenu = entityManager.find(CategoryAndMenu.class, categoryAndMenu.getCategoryCode());
        System.out.println(foundCategoryAndMenu);
    }





}
