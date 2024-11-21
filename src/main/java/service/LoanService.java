package service;

import entity.*;
import repository.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class LoanService {
    private LoanRepository loanRepository;
    private BookRepository bookRepository;
    private UserRepository userRepository;

    public LoanService(LoanRepository loanRepository,
                       BookRepository bookRepository,
                       UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public Loan borrowBook(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId);
        User user = userRepository.findById(userId);

        if (book == null || user == null) {
            throw new IllegalArgumentException("Libro o usuario no encontrado");
        }

        return loanRepository.createLoan(book, user);
    }

    public void returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId);
        loanRepository.returnBook(loan);
    }

    public void checkOverdueLoans() {
        List<Loan> overdueLoans = loanRepository.findOverdueLoans();
        for (Loan loan : overdueLoans) {
            // Lógica de notificación o penalización
            loan.setStatus(Loan.LoanStatus.OVERDUE);
        }
    }

    public void processReturn(Long loanId) {
        Loan loan = loanRepository.findById(loanId);

        // Calcular días de retraso
        long daysLate = ChronoUnit.DAYS.between(loan.getReturnDate(), LocalDate.now());

        if (daysLate > 0) {
            // Si hay retraso, calcular multa ($100 por día)
            double fine = daysLate * 100;
            // Generar comprobante
            System.out.println("Cargo por devolución: " + fine);
        }
    }
}
