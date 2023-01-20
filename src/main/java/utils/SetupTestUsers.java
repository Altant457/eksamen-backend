package utils;


import entities.Assignment;
import entities.DinnerEvent;
import entities.Member;
import entities.Role;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class SetupTestUsers {

  public static void main(String[] args) {

    EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
    EntityManager em = emf.createEntityManager();
    
    // IMPORTAAAAAAAAAANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // This breaks one of the MOST fundamental security rules in that it ships with default users and passwords
    // CHANGE the three passwords below, before you uncomment and execute the code below
    // Also, either delete this file, when users are created or rename and add to .gitignore
    // Whatever you do DO NOT COMMIT and PUSH with the real passwords

    Role memberRole = new Role("member");
    Role adminRole = new Role("admin");
    Member m1 = new Member("member", "As123456", "Test Street 4", "+45 11 22 33 44", "member@mail.dk", 1996);
    Member m2 = new Member("admin", "JK123456", "Admin Street 2", "+45 22 33 44 11", "admin@mail.dk", 2001);
    Member m3 = new Member("member2", "DQ123456", "Grove Street 14", "+45 33 44 11 22", "both@mail.dk", 2005);
    m1.addRole(memberRole);
    m2.addRole(adminRole);
    m3.addRole(memberRole);
    DinnerEvent d1 = new DinnerEvent(new Timestamp(System.currentTimeMillis()), "Roma", "Pizza", new BigDecimal(200));
    DinnerEvent d2 = new DinnerEvent(new Timestamp(System.currentTimeMillis()), "Rønne", "Pasta", new BigDecimal(150));
    DinnerEvent d3 = new DinnerEvent(new Timestamp(System.currentTimeMillis()), "Aakirkeby", "Beans", new BigDecimal(100));
    Assignment a1 = new Assignment(m1, "fname", String.format("Email: %s\nTlf nr: %s", m1.getEmail(), m1.getPhone()));
    Assignment a2 = new Assignment(m2, "fname", String.format("Email: %s\nTlf nr: %s", m2.getEmail(), m2.getPhone()));
    Assignment a3 = new Assignment(m3, "fname", String.format("Email: %s\nTlf nr: %s", m3.getEmail(), m3.getPhone()));
    a1.setDinnerEvent(d1);
    a2.setDinnerEvent(d2);
    a3.setDinnerEvent(d3);
    a1.addMember(m2);
    a2.addMember(m1);

    if(m2.getMemberPass().equals("test")|| m1.getMemberPass().equals("test")||m3.getMemberPass().equals("test"))
      throw new UnsupportedOperationException("You have not changed the passwords");

    em.getTransaction().begin();
    em.persist(memberRole);
    em.persist(adminRole);
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
    System.out.println("PW: " + m1.getMemberPass());
    System.out.println("Testing member with OK password: " + m1.verifyPassword("As123456"));
    System.out.println("Testing member with wrong password: " + m1.verifyPassword("test1"));
    System.out.println("Created TEST Members");
   
  }

}
