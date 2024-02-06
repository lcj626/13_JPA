package com.ohgiraffers.section01.manytoone;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class ManyToOneAssociationTests {

    /*
    * Association Mapping은 Entity 클래스 간의 관계를 매핑하는 것을 의미한다.
    * 이를 통해 객체를 이용해 데이터베이스의 테이블 간의 관계를 매핑할 수 있다.
    *
    *
    * 다중성에 의한 분류
    * 연관 관계가 있는 객체 관계에서는 실제로 연관을 가지는 객체의 수에 따라 분류된다.
    *
    * -N:1(ManyToOne) 연관관계
    * -1:N(OneToMany) 연관관계
    * -1:1(OneToOne) 연관관계
    * -N:N(ManyToMany) 연관관계 - 중복 데이터가 나오게 되기 때문에 사실상 쓸 일 없다
    *
    * 방향에 따른 분류
    * 테이블의 연관 관계는 외래 키를 이용하여 양방향 관계의 특징을 가진다.
    * 참조에 의한 객체의 연관 관계는 단방향이다.
    * 객체간의 연관 관계를 양방향으로 만들고 싶은 경우 반대 쪽에서도 필드를 추가해서 참조를 보관하면 된다.
    * 하지만 엄밀하게 이는 양방향 관계가 아니라 단방향 관계 2개로 볼 수 있다.
    *
    * -단방향 연관관계
    * -양방향 연관관계
    * */

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
    * ManyToOne 은 다수의 엔티티가 하나의 엔티티를 참조하는 상황에서 사용된다.
    * 예를 들어 하나의 카테고리가 여러 개의 메뉴를 가질 수 있는 상황에서 메뉴 엔티티가 카테고리 엔테테를 참조하는 것이다.
    * 이 때 메뉴 엔티티가 Many, 카테고리 엔티티가 One이 된다.
    * */

    // 연관 관계를 가지는 엔티티를 조회하는 방법은 객체 그래프 탐색(객체 연관 관계를 사용한 조회), 객체 지향 쿼리(jpql) 사용이 있다.

    @Test
    void 다대일_연관관계_객체_그래프_탐색을_이용한_조회_테스트(){
        //given
        int menuCode = 15;

        //when
        MenuAndCategory foundMenu = entityManager.find(MenuAndCategory.class, menuCode);
        Category menuCategory = foundMenu.getCategory();

        Assertions.assertNotNull(menuCategory);
        System.out.println("menuCategory : " +menuCategory);
    }

    /*
    * JPQL은 java persistence query language의 약자로, 객체 지향 쿼리 언어 중 하나이다.
    * 객체 지향 모델에 맞게 작성된 커리를 통해, 엔티티 객체를 대상으로 검색, 검색 결과를 토대로 객체를 조작할 수 있다.
    * join 문법이 sql과는 다소 차이가 있지만 직접 쿼리를 작성할 수 있는 문법을 제공한다.
    * 주의할 점은 FROM 절에 기술할 테이블명에는 반드시 엔티티명이 작성되어야 한다.
    * */
    @Test
    void 다대일_연관관계_객체지향쿼리_사용한_카테고리_이름_조회_테스트(){

        //given
        String jpql = "SELECT c.categoryName FROM menu_and_category m JOIN m.category c WHERE m.menuCode = 15";

        //when
        String category = entityManager.createQuery(jpql, String.class).getSingleResult();

        //then
        Assertions.assertNotNull(category);
        System.out.println("category = " + category);
    }


    /*
    * Commit()을 할 경우 컨텍스트 내에 저장된 영속성 객체를 insert 하는 쿼리가 동작 된다.
    * 단, 카테고리가 존재하는 값이 아니므로 부모 테이블(tbl_category)에 값이 먼저 들어있어야 그 카테고리를 참조하는 자식 테이블에 넣을 수 있다.
    * 이 때 필요한 것은 @ManyToOne 어노테이션에 영속성 전이 설정을 해주는 것이다.
    * 영속성 전이랑 특정 엔티티를 영속화 할 때 연관된 언티티도 함꼐 영속화 한다는 의미이다.
    * cascade = Cascade.PERSIST 를 설정하면 MenuAndCategory 엔팉이를 영속화 할 때 Category 엔티티도 함께 영속화 하게 된다.
    * */

    @Test
    void 다대일_연관관계_객체_삽입_테스트(){

        MenuAndCategory menuAndCategory = new MenuAndCategory();
        menuAndCategory.setMenuCode(99999);
        menuAndCategory.setMenuName("죽방멸치빙수");
        menuAndCategory.setMenuPrice(30000);

        Category category = new Category();
        category.setCategoryCode(33333);
        category.setCategoryName("신규카테고리");
        category.setRefCategoryCode(null);
        // 영속성에 등록이 되어있지 않은 상태 - db 에 변경사항 업데이트 안됨

        // Category category1 = entityManager.find(Category.class, 333333);

        menuAndCategory.setCategory(category);
        menuAndCategory.setOrderableStatus("Y");

        //when
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        entityManager.persist(menuAndCategory);
        entityTransaction.commit();

        //then
        MenuAndCategory foundMenuAndCategory = entityManager.find(MenuAndCategory.class, 99999);
        Assertions.assertEquals(99999, foundMenuAndCategory.getMenuCode());
        Assertions.assertEquals(33333, foundMenuAndCategory.getCategory().getCategoryCode());
    }


    @Test
    @DisplayName("카테고리 추가")
    void 카테고리_추가(){ //persist
        Category category = new Category();
        category.setCategoryName("영속성 삭제 카테고리");

        MenuAndCategory menuAndCategory = new MenuAndCategory();
        menuAndCategory.setMenuPrice(1000);
        menuAndCategory.setMenuName("영속성 삭제 메뉴");
        menuAndCategory.setOrderableStatus("Y");
        menuAndCategory.setCategory(category);


        entityManager.persist(menuAndCategory);
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityTransaction.commit();
        System.out.println(category);
        System.out.println(menuAndCategory);
    }


    @Test
    @DisplayName("cascade Remove")
    void 영속성_삭제_테스트(){

        MenuAndCategory menuAndCategory = entityManager.find(MenuAndCategory.class,100002);

        entityManager.remove(menuAndCategory); //연관관계 맺고 있는 카테고리까지 지울거냐

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        // 자식에서 부모를 삭제하려고 하는 경우 다른 자식이 부모를 참조하고 있다면 에러가 발생된다.
        transaction.commit();
        MenuAndCategory deletMenu = entityManager.find(MenuAndCategory.class,100002);
        Category foundCategory = entityManager.find(Category.class, 33336);

        System.out.println(deletMenu); // 삭제되서 null 뜸
        System.out.println(foundCategory); // 삭제되서 null 뜸

        //cascade remove - 나랑 관계를 맺고 있는 자식까지 삭제할거냐
    }


    @Test
    @DisplayName("REFRESH 테스트")
    void Refresh_테스트(){
        // 부모의 상태를 최신 상태로 변경하는 경우 자식도 함께 최신 상태로 변경한다.
        /*
         * 동시성 문제 해결: 여러 사용자가 동시에 작업할 때 다른 사용자가 변경한 데이터가 현재 작업 중인 사용자에게 반영되어야 할 때 사용
         * */
        MenuAndCategory menuAndCategory = entityManager.find(MenuAndCategory.class,20); // menu 20번 최신화
        System.out.println("====== Refresh =========");
        entityManager.refresh(menuAndCategory);

        //해당 값을 항상 최신 상태로 조회(팀워크 시 필요 팀플에 따라 팀원마다 최신 기준이 다를 수 있어서 db기준 최신으로 refresh)
        //위 코드에서 cascade refresh 하면 카테고리 최신상태를 먼저 불러오고 메뉴 최신상태를 불러온다
     }

    @Test
    @DisplayName("Merge insert 테스트")
    void Merge_insert_테스트(){

        // Merge는 새로운 메뉴와 카테고리를 생성하는데 새로운 메뉴와 카테고리가 데이터베이스에 없는 경우
        // 새로운 엔티티로 추가되고 이미 존재하는 경우 추가한다.
        MenuAndCategory menuAndCategory = new MenuAndCategory();
        menuAndCategory.setMenuPrice(15000);
        menuAndCategory.setMenuName("merge insert 메뉴");
        menuAndCategory.setOrderableStatus("Y");
        Category category = new Category();
        category.setCategoryName("merge카테고리");

        menuAndCategory.setCategory(category);

        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        MenuAndCategory mergeMenu = entityManager.merge(menuAndCategory); // 자동으로 pk 생성
        System.out.println("=================");
        System.out.println(mergeMenu);
        System.out.println("=================");

        entityTransaction.commit(); // 머지한 다음 커밋하니까 id 못찾는 문제 안생김

        System.out.println(mergeMenu);

        // 없는 값을 병합시키는 테스트
        // 카테고리가 먼저 등록되고 메뉴 등록 두가지 상태 합체 - cascade merge 아니면 오류남
    }

    @Test
    @DisplayName("Merge update 테스트")
    void Merge_update_테스트(){

        // Merge는 새로운 메뉴와 카테고리를 생성하는데 새로운 메뉴와 카테고리가 데이터베이스에 없는 경우
        // 새로운 엔티티로 추가되고 이미 존재하는 경우 추가한다.
        MenuAndCategory menuAndCategory = new MenuAndCategory();
        menuAndCategory.setMenuPrice(15000);
        menuAndCategory.setMenuName("merge update 메뉴");
        menuAndCategory.setOrderableStatus("Y");

        Category category = entityManager.find(Category.class,33337);
        category.setCategoryName("merge update 카테고리");

        menuAndCategory.setCategory(category); // 먼저 카테고리 영속성에 집어 넣음

        EntityTransaction entityTransaction = entityManager.getTransaction();

        entityTransaction.begin();
        MenuAndCategory mergeMenu = entityManager.merge(menuAndCategory);
        entityTransaction.commit();
        System.out.println(mergeMenu);

        // category 테이블의 내용을 수정하고 커밋 했을때 menu의 카테고리 코드랑 category 테이블의 카테고리 코드가 다르니 menu에서도 update 문을 날린다
    }

    @Test
    @DisplayName("Detach 테스트")
    void detach_테스트(){
        // Detach의 경우 해당 엔터티를 영속성 컨텍스트에서 관리하지 않겠다고 하는 것이다.
        // 그러나 해당 관계를 맺고있는 엔터티의 수정이 생기는 경우 해당 엔터티는 관리 중이기 때문에 함께 관계를 가지고 간다.
        // 이러한 문제를 해결하기 위해 CascadeType를 detach로 설정하면 관계 요소도 함께 영속성에서 관리하지 않겠다는 것이다.
        MenuAndCategory menuAndCategory = entityManager.find(MenuAndCategory.class, 100004); //먼저 db 갖고옴
        menuAndCategory.setMenuName("변경함"); // menu 변경
        Category category = menuAndCategory.getCategory(); // 카테고리 갖고옴
        category.setCategoryName("detach 카테고리 수정했다"); //카테고리 수정
        menuAndCategory.setCategory(category);

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.detach(menuAndCategory); // 준영속상태 만듬 (카테고리랑 연관관계 맺고있는 것도 같이)

        transaction.commit();

    }
}
