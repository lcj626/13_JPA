package com.ohgiraffers.section02.crud;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class A_EntityManagerCRUDTests {

    private static EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory(){
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    @BeforeEach
    public void initManager(){
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterAll
    static void closeFactory(){
        entityManagerFactory.close();
    }

    @AfterEach
    void closeManager(){
        entityManager.close();
    }

    @Test
    public void 메뉴코드_메뉴_조회_테스트(){

        //given
        int menuCode = 2;

        //when
        Menu foundMenu = entityManager.find(Menu.class, menuCode);

        //then
        Assertions.assertNotNull(foundMenu);
        Assertions.assertEquals(menuCode, foundMenu.getMenuCode());
        System.out.println("foundMenu : " + foundMenu);
    }


    @Test
    public void 새로운_메뉴_추가_테스트(){
        //given
        Menu menu = new Menu();
        menu.setMenuName("jap테스트 메뉴");
        menu.setMenuPrice(50000);
        menu.setCategoryCode(4); // 꼭 존재하는 카테고리 코드 넣어야 함
        menu.setOrderableStatus("Y");

        //when
        EntityTransaction entityTransaction = entityManager.getTransaction(); // 데이터베이스 상태변화를 하나로 묶은것 - transaction
        // 엔티티 매니저에서 트랜잭션 얻어옴
        entityTransaction.begin();
        try{
            entityManager.persist(menu); // 영속성 컨텍스트에 menu를 넣겠다 (db에 삽입 전 단계)
            entityTransaction.commit(); // db에 삽입
        }catch (Exception e){
            entityTransaction.rollback();
            e.printStackTrace();
        }

        Assertions.assertTrue(entityManager.contains(menu));
    }

    @Test
    public void 메뉴_이름_수정_테스트(){

        //given
        Menu menu = entityManager.find(Menu.class, 2);
        System.out.println("menu = " + menu);

        String menuNameToChange = "갈치스무디";

        //when
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        try {
            menu.setMenuName(menuNameToChange);
            entityTransaction.commit();
        }catch (Exception e){
            entityTransaction.rollback();
            e.printStackTrace();
        }

        Assertions.assertEquals(menuNameToChange, entityManager.find(Menu.class, 2).getMenuName());
    }

    @Test
    public void 메뉴_삭제하기_테스트(){
        //given
        Menu menuToRemove = entityManager.find(Menu.class, 1);

        //when
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        try {
            entityManager.remove(menuToRemove); // remove = 비영속화
            entityTransaction.commit();
        }catch (Exception e){
            entityTransaction.rollback();
            e.printStackTrace();
        }

        Menu removedMenu = entityManager.find(Menu.class, 1);
        Assertions.assertNull(removedMenu);
    }
}
