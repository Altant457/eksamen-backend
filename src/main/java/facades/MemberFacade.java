package facades;

import entities.DinnerEvent;
import entities.Member;
import entities.Role;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import security.errorhandling.AuthenticationException;

import java.util.List;

/**
 * @author lam@cphbusiness.dk
 */
public class MemberFacade {

    private static EntityManagerFactory emf;
    private static MemberFacade instance;

    private MemberFacade() {
    }

    /**
     * @param _emf
     * @return the instance of this facade.
     */
    public static MemberFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new MemberFacade();
        }
        return instance;
    }

    public Member getVeryfiedMember(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        Member member;
        try {
            member = em.find(Member.class, username);
            if (member == null || !member.verifyPassword(password)) {
                throw new AuthenticationException("Invalid member name or password");
            }
        } finally {
            em.close();
        }
        return member;
    }


    public Member createMember(Member member) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Role userRole = new Role("member");
            member.addRole(userRole);
            em.persist(member);
            em.getTransaction().commit();
            return member;
        } finally {
            em.close();
        }
    }

    public List<DinnerEvent> getAllEvents() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<DinnerEvent> query = em.createQuery("SELECT e FROM DinnerEvent e", DinnerEvent.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public DinnerEvent createEvent(DinnerEvent event) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(event);
            em.getTransaction().commit();
            return event;
        } finally {
            em.close();
        }
    }
}
