import entity.Book;
import repository.BookRepository;
import service.BookService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        BookRepository bookRepository = new BookRepository();
        BookService bookService = new BookService(bookRepository);

        // Buscar libros por palabra clave
        String keyword = "Ja";
        List<Book> results = bookService.searchBooksByKeyword(keyword);
        // Mostrar resultados
        if (results.isEmpty()) {
            System.out.println("No se encontraron libros con la palabra clave: " + keyword);
        } else {
            System.out.println("Libros con la palabra clave: " + keyword);
            results.forEach(book ->
                    System.out.println("ID: " + book.getId() +
                            ", Título: " + book.getTitle() +
                            ", Autor: " + book.getAuthor()));
        }
    }
}

