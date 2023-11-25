package to.bookapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import to.bookapp.models.Book;
import to.bookapp.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // POST api/books/
    public Book addBook(Book book){
        return bookRepository.save(book);
    }

    // DELETE api/books/:id
    public Book deleteBook(Long id) {
        Book deletedBook = bookRepository.findById(id).get();
        bookRepository.deleteById(id);
        return deletedBook;
    }


    public Book updateBook(Long id, Book updatedBook) {
        Optional<Book> existingBookOptional = bookRepository.findById(id);

        if (((Optional<?>) existingBookOptional).isPresent()) {
            Book existingBook = existingBookOptional.get();

            existingBook.setTitle(updatedBook.getTitle());
            existingBook.setAuthor(updatedBook.getAuthor());
            existingBook.setYear(updatedBook.getYear());

            return bookRepository.save(existingBook);
        } else {
            throw new RuntimeException("Book with ID " + id + " not found");
        }
    }

    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId).get();
    }
}
