package facades;

import entities.Member;
import entities.Role;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.*;

class MemberFacadeTest {

    private static EntityManagerFactory emf;
    private static MemberFacade facade;
    Role userR, adminR;
    Member m1, m2, m3;

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = MemberFacade.getUserFacade(emf);
    }

    @BeforeEach
    void setUp() {
        userR = new Role("user");
        adminR = new Role("admin");
        m1 = new Member("member1", "test1");
        m2 = new Member("member2", "test2");
        m3 = new Member("member3", "test3");
        m1.addRole(userR);
        m2.addRole(userR);
        m3.addRole(userR);

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            // Delete old data
            em.createQuery("DELETE FROM Member").executeUpdate();
            em.createQuery("DELETE FROM Role").executeUpdate();
            // Start with fresh data
            em.persist(userR);
            em.persist(adminR);
            em.persist(m1);
            em.persist(m2);
            em.persist(m3);
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
        Member actualMember = facade.createMember(new Member("testCreate", "createTest"));
        Member expectedMember = new Member("testCreate", "createTest");
        expectedMember.addRole(userR);
        assertThat(actualMember.getRoleList(), hasItem(userR));
    }
}