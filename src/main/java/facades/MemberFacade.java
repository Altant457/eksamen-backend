package facades;

import entities.Member;
import entities.Role;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import security.errorhandling.AuthenticationException;

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
        em.getTransaction().begin();
        Role userRole = new Role("member");
        member.addRole(userRole);
        em.persist(member);
        em.getTransaction().commit();
        return member;
    }
}
