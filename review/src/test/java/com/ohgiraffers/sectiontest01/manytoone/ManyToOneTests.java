package com.ohgiraffers.sectiontest01.manytoone;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class ManyToOneTests {
    ;

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
    void 다대일_연관관계_객체지향쿼리_사용한_카테고리_이름_조회_테스트(){

        //given
        int menuCode = 12;

        //when
        Menu foundMenu = entityManager.find(Menu.class, menuCode);
        Category menuCategory = foundMenu.getCategoryCode();



        Assertions.assertNotNull(menuCategory);
        System.out.println("menuCategory : " + menuCategory);
    }

}


