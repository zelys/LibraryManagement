package repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import entity.*;

import java.time.LocalDate;
import java.util.List;

public class LoanRepository {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("userSystemPU");

    public Loan findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Loan.class, id);
        } finally {
            em.close();
        }
    }

    public Loan createLoan(Book book, User user) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            // Verificar disponibilidad
            if (book.getAvailableCopies() <= 0) {
                throw new IllegalStateException("No hay copias disponibles para préstamo");
            }

            // Reducir copias disponibles
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            em.merge(book);

            // Crear préstamo
            Loan loan = new Loan();
            loan.setBook(book);
            loan.setUser(user);
            loan.setLoanDate(LocalDate.now());
            loan.setReturnDate(LocalDate.now().plusDays(14)); // Préstamo de 14 días
            loan.setStatus(Loan.LoanStatus.ACTIVE);

            em.persist(loan);

            transaction.commit();
            return loan;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error al crear préstamo", e);
        } finally {
            em.close();
        }
    }

    public void returnBook(Loan loan) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            // Actualizar estado del préstamo
            loan.setStatus(Loan.LoanStatus.RETURNED);

            // Incrementar copias disponibles
            Book book = loan.getBook();
            book.setAvailableCopies(book.getAvailableCopies() + 1);

            em.merge(loan);
            em.merge(book);

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error al devolver libro", e);
        } finally {
            em.close();
        }
    }

    public List<Loan> findOverdueLoans() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT l FROM Loan l WHERE l.returnDate < CURRENT_DATE AND l.status = :status",
                            Loan.class)
                    .setParameter("status", Loan.LoanStatus.ACTIVE)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
