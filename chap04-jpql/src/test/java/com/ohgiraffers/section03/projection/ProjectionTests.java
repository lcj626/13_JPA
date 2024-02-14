package com.ohgiraffers.section03.projection;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

public class ProjectionTests {

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

    /*
    * 프로젝션(projection)
    * SELECT 절에 조회할 대상을 지정하는 것을 프로젝션이라고 한다.
    * (SELECT {프로젝션 대상} FROM)
    *
    * 프로젝션 대상은 4가지 방식이 있다.
    * 1. 엔티티 프로젝션
    *    원하는 객체를 바로 조회할 수 있다.
    *    조회된 엔티티는 연속성 컨테스트가 관리한다.
    *
    * 2. 임베디드 타입 프로젝션(임베디드 타입에 대한 설명은 MENUInfo 클래스에서 설명)
    *   엔티티와 거의 비슷하게 사용되며 조회의 시작점이 될 수 없다. <- FROM 절에서 사용 불가
    *   임베디드 타입은 영속성 컨테스트에서 관리되지 않는다.
    *
    * 3. 스칼라 타입 프로젝션
    *   숫자, 문자, 날짜 같은 기본 데이터 타입이다.
    *   스칼라 타입은 영속성 컨텍스트에서 관리하지 않는다.
    *
    * 4. new 명령어를 활용한 프로젝션
    *   다양한 종류의 단순 값들을 DTO로 바로 조회하는 방식으로 new 패키지명.dto명을 쓰면 해당 dto로 바로 반환받을 수 있다.
    *   new 명령어를 사용한 클래스의 객체는 엔티티가 아니므로 영속성 컨테스트에서 관리되지 않는다.
    * */
    @Test
    void 단일_엔티티_프로젝션_테스트(){
        //when
        String jpql = "SELECT m FROM menu_section03 m";
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).getResultList(); // 조회해 온 것은 영속성 컨텍스트에서 관리

        Assertions.assertNotNull(menuList); // menuList가 null이 아님을 확인

        EntityTransaction entityTransaction = entityManager.getTransaction(); // db 변화 다루는 곳
        entityTransaction.begin();
        menuList.get(1).setMenuName("test"); // 1번에 있는 메튜이름을 test로 바꾼다.
        entityTransaction.commit();
    }

    @Test
    void 양방향_연관관계_엔티티_프로젝션_테스트(){
        //given
        int menuCodeParametetr = 3;

        //hen
        String jpql = "SELECT m.category FROM bidirection_menu m WHERE m.menuCode = :menuCode"; // bidrection_menu 엔티티에서 menuCode가 주어진 값과 일치하는 엔티티의 category 속성 조회

        BiDirectionCategory categoryOfMenu = entityManager.createQuery(jpql,BiDirectionCategory.class) // BiDirectionCategory.class <- 결과를 매핑할 엔티티 클래스
                .setParameter("menuCode",menuCodeParametetr) // menuCode라는 파라미터에 menuCodeParameter(3) 값을 설정
                .getSingleResult(); // jpql 쿼리를 실행하고 결과를 단일 객체로 가지고 옴, 이 메소드는 결과가 정확히 하나인 경우에 사용

        Assertions.assertNotNull(categoryOfMenu);
        System.out.println(categoryOfMenu);

        Assertions.assertNotNull(categoryOfMenu.getMenuList());
        categoryOfMenu.getMenuList().forEach(System.out::println);
    }

    @Test
    void 임베디드_타입_프로젝션_테스트(){

        //when
        String jpql = "SELECT m.menuInfo FROM embedded_menu m"; // embedded_menu 엔티티의 menuInfo 필드를 조회.  menuInfo는 임베디드 타입인 MenuInfo의 인스턴스
        List<MenuInfo> menuInfoList = entityManager.createQuery(jpql, MenuInfo.class).getResultList(); // jpql 쿼리 실행 MenuInfo 타입으로 결과 매핑. MenuInfo 객체의 리스트가 출력

        Assertions.assertNotNull(menuInfoList);
        menuInfoList.forEach(System.out::println);
    }


    /*3. 스칼라 타입 프로젝션*/
    @Test
    void TypeQuery를_이용한_스칼라_타입_프로젝션_테스트(){

        //when
        String jpql = "SELECT c.categoryName FROM category_section03 c";
        List<String> categoryNameList = entityManager.createQuery(jpql, String.class).getResultList();

        Assertions.assertNotNull(categoryNameList);
        categoryNameList.forEach(System.out::println);
    }

    /*
     * 조회하려는 칼럼 값이 1개인 경우 TypeQuery로 반환 타입을 단일 값에 대해 지정할 수 있지만 다중 열 컬럼을 조회하는 경우 타입을 지정하지 못한다.
     * 그때 TypeQuery 대신 Query를 사용하여 Object[]로 행의 정보를 반환 받아 사용한다.
     * */
    @Test
    void Query를_이용한_스칼라_타입_프로젝션_테스트(){

        //when
        String jpql = "SELECT c.categoryCode, c.categoryName FROM category_section03 c";
        List<Object[]> categoryList = entityManager.createQuery(jpql).getResultList(); // 반환 타입 모르고 컬럼 갯수 여러개일때

        Assertions.assertNotNull(categoryList);
        categoryList.forEach(row ->{
            Arrays.stream(row).forEach(System.out::println);
        });
    }

    // 4. new 명령어를 활용한 프로젝션
    @Test
    void new_명령어를_활용한_프로젝션_테스트(){

        //when
        String jpql = "SELECT new com.ohgiraffers.section03.projection.CategoryInfo(c.categoryCode, c.categoryName) FROM category_section03 c";
        List<CategoryInfo> categoryInfoList = entityManager.createQuery(jpql, CategoryInfo.class).getResultList();

        Assertions.assertNotNull(categoryInfoList);
        categoryInfoList.forEach(System.out::println);
    }
}
