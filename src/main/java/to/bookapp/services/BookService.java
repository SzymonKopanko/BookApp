package to.bookapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

import to.bookapp.models.Book;
import to.bookapp.repositories.BookRepository;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    // GET api/books/
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // GET api/books/:id
    public Book getBookByID(Long id) {
        return bookRepository.findById(id).get();
    }

    // POST api/books/
    public Book addBook(Book book){
        return bookRepository.save(book);
    }

    // UPDATE api/books/:id
    // -> Kasia

    // DELETE api/books/:id
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }
}