package to.bookapp.steps;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import to.bookapp.models.Book;
import to.bookapp.repositories.BookRepository;
import to.bookapp.services.BookService;

import java.util.List;

@Component
public class DisplayBooksSteps {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    private List<Book> displayedBooks;

    @Given("books exist in the system")
    public void givenBooksExistInTheSystem(List<Book> books) {
        bookRepository.saveAll(books);
    }

    @When("the user requests to display all books")
    public void whenTheUserRequestsToDisplayAllBooks() {
        displayedBooks = bookService.getAllBooks();
    }

    @Then("the system displays a list of books")
    public void thenTheSystemDisplaysAListOfBooks() {
        // Assert 
        List<Book> allBooks = bookRepository.findAll();
        assert displayedBooks.size() == allBooks.size();
        assert displayedBooks.containsAll(allBooks);
    }
}
