package facades;

import entities.Assignment;
import entities.DinnerEvent;
import entities.Member;
import entities.Role;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.*;

class MemberFacadeTest {

    private static EntityManagerFactory emf;
    private static MemberFacade facade;
    Role userR, adminR;
    Member m1, m2, m3;
    DinnerEvent d1, d2, d3;
    Assignment a1, a2, a3;

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = MemberFacade.getUserFacade(emf);
    }

    @BeforeEach
    void setUp() {
        userR = new Role("member");
        adminR = new Role("admin");
        m1 = new Member("member1", "test1", "Test street 1", "+45 12 34 56 78", "member1@mail.dk", 1993);
        m2 = new Member("member2", "test2", "Test street 2", "+45 21 43 65 87", "member2@mail.dk", 1997);
        m3 = new Member("member3", "test3", "Test street 3", "+45 87 65 43 21", "member3@mail.dk", 2001);
        m1.addRole(userR);
        m2.addRole(userR);
        m3.addRole(userR);
        d1 = new DinnerEvent(new Timestamp(System.currentTimeMillis()), "Roma", "Pizza", new BigDecimal(200));
        d2 = new DinnerEvent(new Timestamp(System.currentTimeMillis()), "Rønne", "Pasta", new BigDecimal(150));
        d3 = new DinnerEvent(new Timestamp(System.currentTimeMillis()), "Aakirkeby", "Beans", new BigDecimal(100));
        a1 = new Assignment(m1, "fname", String.format("Email: %s\nTlf nr: %s", m1.getEmail(), m1.getPhone()));
        a2 = new Assignment(m2, "fname", String.format("Email: %s\nTlf nr: %s", m2.getEmail(), m2.getPhone()));
        a3 = new Assignment(m3, "fname", String.format("Email: %s\nTlf nr: %s", m3.getEmail(), m3.getPhone()));
        a1.setDinnerEvent(d1);
        a2.setDinnerEvent(d2);
        a3.setDinnerEvent(d3);
        a1.addMember(m2);
        a2.addMember(m1);

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            // Delete old data
            em.createQuery("DELETE FROM Assignment").executeUpdate();
            em.createQuery("DELETE FROM Member").executeUpdate();
            em.createQuery("DELETE FROM Role").executeUpdate();
            em.createQuery("DELETE FROM DinnerEvent").executeUpdate();
            // Start with fresh data
            em.persist(userR);
            em.persist(adminR);
            em.persist(m1);
            em.persist(m2);
            em.persist(m3);
            em.persist(d1);
            em.persist(d2);
            em.persist(d3);
            em.persist(a1);
            em.persist(a2);
            em.persist(a3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    void getVeryfiedUser() throws AuthenticationException {
        Member actualMember = facade.getVeryfiedMember("member1", "test1");
        assertEquals(m1, actualMember);
        assertThat(actualMember.getRoleList(), hasItem(userR));
    }

    @Test
    void createUser() {
        Member expectedMember = new Member("testCreate", "createTest", "Made Street 3", "+45 78 56 34 12", "cmember@mail.dk", 1990);
        Member actualMember = facade.createMember(expectedMember);
        expectedMember.addRole(userR);
        assertThat(actualMember.getRoleList(), hasItem(userR));
    }

    @Test
    void getAllEvents() {
        List<DinnerEvent> actualList = facade.getAllEvents();
        assertThat(actualList, containsInAnyOrder(d1, d2, d3));
    }
}