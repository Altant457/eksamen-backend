package facades;

import entities.Role;
import entities.User;
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

class UserFacadeTest {

    private static EntityManagerFactory emf;
    private static UserFacade facade;
    Role userR, adminR;
    User u1, u2, u3;

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = UserFacade.getUserFacade(emf);
    }

    @BeforeEach
    void setUp() {
        userR = new Role("user");
        adminR = new Role("admin");
        u1 = new User("user1", "test1");
        u2 = new User("user2", "test2");
        u3 = new User("user3", "test3");
        u1.addRole(userR);
        u2.addRole(userR);
        u3.addRole(userR);

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            // Delete old data
            em.createQuery("DELETE FROM User").executeUpdate();
            em.createQuery("DELETE FROM Role").executeUpdate();
            // Start with fresh data
            em.persist(userR);
            em.persist(adminR);
            em.persist(u1);
            em.persist(u2);
            em.persist(u3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    void getVeryfiedUser() throws AuthenticationException {
        User actualUser = facade.getVeryfiedUser("user1", "test1");
        assertEquals(u1, actualUser);
        assertThat(actualUser.getRoleList(), hasItem(userR));
    }

    @Test
    void createUser() {
        User actualUser = facade.createUser(new User("testCreate", "createTest"));
        User expectedUser = new User("testCreate", "createTest");
        expectedUser.addRole(userR);
        assertThat(actualUser.getRoleList(), hasItem(userR));
    }
}