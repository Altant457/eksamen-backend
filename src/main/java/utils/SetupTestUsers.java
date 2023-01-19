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

    Member member = new Member("member", "As123456", "Test Street 4", "+45 11 22 33 44", "member@mail.dk", 1996);
    Member admin = new Member("admin", "JK123456", "Admin Street 2", "+45 22 33 44 11", "admin@mail.dk", 2001);
    Member both = new Member("member_admin", "DQ123456", "Grove Street 14", "+45 33 44 11 22", "both@mail.dk", 2005);

    if(admin.getMemberPass().equals("test")|| member.getMemberPass().equals("test")||both.getMemberPass().equals("test"))
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
    System.out.println("PW: " + member.getMemberPass());
    System.out.println("Testing member with OK password: " + member.verifyPassword("As123456"));
    System.out.println("Testing member with wrong password: " + member.verifyPassword("test1"));
    System.out.println("Created TEST Members");
   
  }

}
