package repository;

import jakarta.persistence.*;
import entity.Book;

import java.util.List;

public class BookRepository {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("userSystemPU");

    public Book findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Book.class, id);
        } finally {
            em.close();
        }
    }

    public List<Book> findBooksByKeyword(String keyword) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT b FROM Book b " +
                            "WHERE b.title LIKE :keyword " +
                            "OR b.author LIKE :keyword", Book.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }


}

