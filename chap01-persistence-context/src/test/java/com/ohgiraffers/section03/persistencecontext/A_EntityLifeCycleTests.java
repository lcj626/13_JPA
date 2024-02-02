package com.ohgiraffers.section03.persistencecontext;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class A_EntityLifeCycleTests {

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

    /*
    * 영속성 컨텍스트는 엔티티 매니저가 엔티티 객체를 저장하는 공간으로 엔티티 객체를 보관하고 관리한다
    * 엔티티 매니저가 생성될 때 하나의 영속성 컨텍스트가 만들어 진다.
    *
    * 엔티티 생ㅁㅇ주기
    * 비영속, 영속, 준영속, 삭제 상태
    * */

    @Test
    public void 비영속_테스트(){
        //given
        Menu foundMenu = entityManager.find(Menu.class, 11);

        //객체만 생성하면 영속성 컨텍스트나 데이터베이스와 관련 없는 비영속 상태이다.
        Menu newMenu = new Menu();
        newMenu.setMenuCode(foundMenu.getMenuCode());
        newMenu.setMenuName(foundMenu.getMenuName());
        newMenu.setMenuPrice(foundMenu.getMenuPrice());
        newMenu.setCategoryCode(foundMenu.getCategoryCode());
        newMenu.setOrderableStatus(foundMenu.getOrderableStatus());

        Menu copyMenu = foundMenu;
        //when
        boolean isTrue = (foundMenu == newMenu);
        boolean result = copyMenu == foundMenu;
        //then
        Assertions.assertFalse(isTrue);

        Assertions.assertTrue(result);
    }

    @Test
    void 영속성_연속_조회_테스트(){
        /*
        * 엔티티 매니저가 영속성 컨텍스트에 엔티티 객체를 저장(persist)하면 영혹성 컨텍스트가 엔티티 객체를 관리하게 되고
        * 이를 영속 상태라고 한다. Find(), jpql을 사용한 조회도 영속 상태가 된다.
        * */
        //given
        Menu foundMenu1 = entityManager.find(Menu.class, 11); // 여기서 영속화 시킴 founMenu에 전달
        Menu foundMenu2 = entityManager.find(Menu.class, 11);

        //when
        boolean isTrue = (foundMenu1 == foundMenu2);

        //then
        Assertions.assertTrue(isTrue);
    }

    @Test
    void 영속성_객체_추가_테스트(){

        //Menu Entity 의 @GeneratedValue(strategy=GenerationType.IDENTITY) 설정을 잠시 주석하고 테스트 수행
        // - 이거 있으면 임의로 코드값을 넣으니까 우리가 임의로 지정해서 테스트 하려면 주석 처리

        // Given
        Menu menuToRegist = new Menu();
        menuToRegist.setMenuCode(500);
        menuToRegist.setMenuName("수박죽");
        menuToRegist.setMenuPrice(10000);
        menuToRegist.setCategoryCode(1);
        menuToRegist.setOrderableStatus("Y");

        //when
        entityManager.persist(menuToRegist); //영속성 컨텍스트에 집어넣는다 커밋을 할 지 롤백을 할 지 기준(db 아직 반영 x)
        Menu foundMenu = entityManager.find(Menu.class, 500); // db에서 찾아오는데 그 기준을 persist기준(db에 요청을 안날림- 쿼리가 없는 이유)
        // 즉, db에는 500번 자료가 없지만 persist(영속화)에는 있는 상태이다. 이를 db에 반영하고 싶으면 commit을 하면 된다.
        // 만약 존재하는걸 처리한다면 최신상태로 update 효과
        boolean isTrue = (menuToRegist == foundMenu);

        Assertions.assertTrue(isTrue);
    }

    @Test
    void 준영속성_detach_테스트(){
        //given
        Menu foundMenu = entityManager.find(Menu.class, 11); // 쿼리 날라감1
        Menu foundMenu1 = entityManager.find(Menu.class, 12); // 쿼리 날라감2

        /*
        * 영속성 컨텍스트가 관리하던 엔티티 객체를 관리하지 않는 상태가 된다면 준영속 상태가 된다.
        * 그 중 Detach는 특정 엔티티만 준영속 상태로 만든다
        * */
        //when
        entityManager.detach(foundMenu1); // foundMenu1 준영속(잠시 관리하지 않는것-remove가 아님)

        foundMenu.setMenuPrice(5000);
        foundMenu1.setMenuPrice(5000);

        Assertions.assertEquals(5000, entityManager.find(Menu.class, 11).getMenuPrice());

        entityManager.merge(foundMenu1); // 영속화 시키는것
        Assertions.assertEquals(5000, entityManager.find(Menu.class, 12).getMenuPrice());
    }

    @Test
    void 준영속성_clear_테스트(){

        //given
        Menu foundMenu1 = entityManager.find(Menu.class, 11);
        Menu foundMenu2 = entityManager.find(Menu.class, 12);
        // 영속성 컨테스트 11,12가 들어감

        //when
        entityManager.clear();// 영속성 초기화

        foundMenu2.setMenuPrice(5000);
        foundMenu1.setMenuPrice(5000);
        //초기화 되서 일반 자바 값을 바꾼거나 다름없음

        Assertions.assertNotEquals(5000, entityManager.find(Menu.class, 11).getMenuPrice());
        Assertions.assertNotEquals(5000, entityManager.find(Menu.class, 12).getMenuPrice());
        // 11과 12를 다시 불러움
    }

    @Test
    void close_테스트(){

        //given
        Menu foundMenu1 = entityManager.find(Menu.class, 11);
        Menu foundMenu2 = entityManager.find(Menu.class, 12);

        //when
        entityManager.close();

        foundMenu2.setMenuPrice(5000);
        foundMenu1.setMenuPrice(5000);

        //then
        // 영속성 컨텍스트를 닫았기 때문에 다시 만들기 전까지는 사용할 수 없다.
        Assertions.assertEquals(5000, entityManager.find(Menu.class, 11).getMenuPrice());
        Assertions.assertEquals(5000, entityManager.find(Menu.class, 12).getMenuPrice());
    }


    @Test
    public void 삭제_remove_테스트(){

        /*
        * remove : 엔티티를 영속성 컨텍스트 및 데이터베이스에서 삭제한다ㅣ.
        * 단, 트랜젝션을 제어하지 않으면 영구 반영되지는 않는다.
        * 트랜잭션을 커밋하는 순간 영속성 컨텍스트에서 관리하는 엔티티 객체가 데이터베이스에서 반영되게 한다(이를 flush라고 한다.)
        * Flush: 영속성 컨텍스트의 변경 내용을 데이터베이스에 동기화 하는 작업(등록, 수정, 삭제한 엔티티를 데이터베이스에서 반영)
        *
        * */

        //given
        Menu foundMenu = entityManager.find(Menu.class, 2);

        //EntityTransaction transaction = entityManager.getTransaction();

        //when
        //transaction.begin();

        entityManager.remove(foundMenu); // 영속성 제거

        //transaction.commit();

        Menu refoundMenu = entityManager.find(Menu.class, 2); // 영속성 다시 찾음 (commit 안함 지울예정) Null값으로 초기화 한다음 refoundMenu에 담음

        Assertions.assertEquals(2, foundMenu.getMenuCode());
        Assertions.assertEquals(null, refoundMenu);
    }

    /*
    * 병합(merge): 파라미터로 넘어온 준영속 엔티티 객체의 식별 값으로 1차 캐시에서 엔티티 객체를 조회한다.
    * 만약 1차 캐시에 엔티티가 없으면 데이터베이스에서 엔티티를 조회하고 1차 캐시에 저장한다.
    * 조회한 영속 엔티티 객체에 준영속 상태의 엔티티 객체의 값을 병합한 뒤 영속 엔티티 객체를 반환한다.
    * 혹은 조회할 수 없는 데이터의 경우 새로 생성해서 병합한다.(save or update)
    * */
    @Test
    void 병합_merge_수정_테스트(){
        //given
        Menu menuToDetach = entityManager.find(Menu.class, 3);
        entityManager.detach(menuToDetach);

        //when
        menuToDetach.setMenuName("수박죽");
        Menu refoundMenu = entityManager.find(Menu.class, 3); // 준영속 돼서 수박죽으로 바꾼 3은 빠지고 새로운 3을 기존 db에서 다시 가져와서 refoundMenu에 담음

        //준영속 엔티티와 영속 엔티티의 해쉬코드는 다른 상태다.
        System.out.println(menuToDetach.hashCode());
        System.out.println(refoundMenu.hashCode());

        entityManager.merge(menuToDetach); // 준영속 객체가 기준이 돼서 덮어 씌움
        //then
        Menu meredMenu = entityManager.find(Menu.class, 3);
        Assertions.assertEquals("수박죽", meredMenu.getMenuName());
    }

    @Test
    void 병합_merge_삽입_테스트(){
        //given
        Menu menuToDetach = entityManager.find(Menu.class, 3); //쿼리 날라감
        entityManager.detach(menuToDetach); //준영속화

        //when
        menuToDetach.setMenuCode(999);//변경 -> 준영속 상태 상실 -> 나중에 다시 쿼리 조회해야 함
        menuToDetach.setMenuName("수박죽"); // 변경 -> 만약 id 가 db에 있어서 가져올 수 있다면 기존 db에 있던 동일 id가 있다면 해당 id의 name을 "수박죽"으로 덮어 씌운다.

        //EntityTransaction transaction = entityManager.getTransaction();
        // transaction.begin();



        entityManager.merge(menuToDetach);// 합쳐야 되는데 기존에 3번이었던 걸 999번으로 바꿨으니 비교할 수가 없음 호적조회하는데 기록이 없어서 안됨 알 수 없는 상태
        // 확인을 위해 쿼리 다시 불러옴
        //영속성에 없으면 db에서 찾아야 하는데 db에도 999가 없음 db랑 정합성이 어긋남 영속성 안됨
        // 만약 select 해왔는데 있으면 "수박죽"으로 덮어씌움. 그러나 id 999가 없기 때문에 덮어 씌우지 못함

        //transaction.commit(); // autoincrement 하면 999 항목 "수박죽"에 insert 된다 but autoincrement가 있으면 그냥 마지막 id 숫자 바로 다음거 자동 배정


        //then
        Menu mergedMenu = entityManager.find(Menu.class, 999); // 위에서 merge가 거부되어 영속화 실패한 거니까 쿼리 한번 더 날림 -> mergedMenu => Null 나옴
        Assertions.assertEquals("수박죽", mergedMenu.getMenuName()); // 그래서 여기 에러남(id기준인데 999 id가 없음)
    }


}
