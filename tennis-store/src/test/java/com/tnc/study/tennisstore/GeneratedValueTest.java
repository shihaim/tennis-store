package com.tnc.study.tennisstore;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GeneratedValueTest {

    @PersistenceUnit
    EntityManagerFactory emf;

    @Test
    @DisplayName("GeneratedValueTest 테스트")
    void testGeneratedValue() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        Hello hello1 = new Hello();
        Hello hello2 = new Hello();
        Hello hello3 = new Hello();

        transaction.begin();

        try {
            System.out.println("""
                    -------------------------------------
                    before entity persist
                    -------------------------------------
                    """);

            em.persist(hello1);
            em.persist(hello2);
            em.persist(hello3);

            System.out.println("""
                    -------------------------------------
                    after entity persist
                    -------------------------------------
                    """);

            System.out.println("""
                    -------------------------------------
                    before transaction commit
                    -------------------------------------
                    """);
            transaction.commit();
            System.out.println("""
                    -------------------------------------
                    after transaction commit
                    -------------------------------------
                    """);

        } catch (Exception e) {
            transaction.rollback();
        } finally {
            em.close();
        }

        System.out.println("hello1.getId() = " + hello1.getId());
        System.out.println("hello2.getId() = " + hello2.getId());
        System.out.println("hello3.getId() = " + hello3.getId());
    }
}
