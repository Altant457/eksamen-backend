package utils;


import entities.Member;
import entities.Role;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class SetupTestUsers {

  public static void main(String[] args) {

    EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
    EntityManager em = emf.createEntityManager();
    
    // IMPORTAAAAAAAAAANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // This breaks one of the MOST fundamental security rules in that it ships with default users and passwords
    // CHANGE the three passwords below, before you uncomment and execute the code below
    // Also, either delete this file, when users are created or rename and add to .gitignore
    // Whatever you do DO NOT COMMIT and PUSH with the real passwords

    Member member = new Member("member", "As123456");
    Member admin = new Member("admin", "JK123456");
    Member both = new Member("member_admin", "DQ123456");

    if(admin.getUserPass().equals("test")|| member.getUserPass().equals("test")||both.getUserPass().equals("test"))
      throw new UnsupportedOperationException("You have not changed the passwords");

    em.getTransaction().begin();
    Role memberRole = new Role("member");
    Role adminRole = new Role("admin");
    member.addRole(memberRole);
    admin.addRole(adminRole);
    both.addRole(memberRole);
    both.addRole(adminRole);
    em.persist(memberRole);
    em.persist(adminRole);
    em.persist(member);
    em.persist(admin);
    em.persist(both);
    em.getTransaction().commit();
    System.out.println("PW: " + member.getUserPass());
    System.out.println("Testing member with OK password: " + member.verifyPassword("As123456"));
    System.out.println("Testing member with wrong password: " + member.verifyPassword("test1"));
    System.out.println("Created TEST Members");
   
  }

}
