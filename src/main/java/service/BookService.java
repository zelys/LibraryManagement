package service;

import entity.Book;
import repository.BookRepository;

import java.util.List;

public class BookService {

    public BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> searchBooksByKeyword(String keyword) {
        return bookRepository.findBooksByKeyword(keyword);
    }
}

