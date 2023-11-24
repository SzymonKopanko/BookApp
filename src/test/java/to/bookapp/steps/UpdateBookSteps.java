package to.bookapp.steps;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import to.bookapp.models.Book;
import to.bookapp.repositories.BookRepository;
import to.bookapp.services.BookService;

import java.util.Optional;

@Component
public class UpdateBookSteps {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    private Long bookId;
    private Book updatedBook;
    private RuntimeException exception;

    @Given("a book with ID $id exists")
    public void givenABookWithIdExists(Long id) {
        this.bookId = id;
    }

    @When("the user updates the book with ID $id")
    public void whenTheUserUpdatesTheBookWithId(Long id, Book updatedBook) {
        this.updatedBook = updatedBook;
        try {
            bookService.updateBook(id, updatedBook);
        } catch (RuntimeException e) {
            exception = e;
        }
    }

    @Then("the book with ID $id updates successfully")
    public void thenTheBookWithIdUpdatedSuccess(Long id) {
        Optional<Book> dbBook = bookRepository.findById(id);
        Book actualBook = dbBook.orElse(null);

        // Assert
        assert actualBook != null;
        assert updatedBook.getTitle().equals(actualBook.getTitle());
        assert updatedBook.getAuthor().equals(actualBook.getAuthor());
        assert updatedBook.getYear() == actualBook.getYear();
    }

    @Then("the book with ID $id was not found")
    public void thenTheBookWithIdWasNotFound(Long id) {
        // Assert that the expected exception is thrown
        assert exception != null;
        assert exception.getMessage().equals("Book with ID " + id + " not found");
    }
}
