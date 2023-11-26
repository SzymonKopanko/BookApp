package to.bookapp.steps;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import org.junit.Assert;
import org.springframework.stereotype.Component;
import to.bookapp.models.Book;
import to.bookapp.repositories.BookRepository;
import to.bookapp.services.BookService;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Component
public class BookSteps {

    private Book addedBook;
    private List<Book> library;
    private List<Book> existingBooks;
    private Long requestedBookId;
    private Book retrievedBook;
    private Book justBook;
    private Long justBookId;
    private Exception exception;
    private BookRepository bookRepository;
    private final ThreadLocal<BookService> bookStash = new ThreadLocal<>();

    private BookService getBookService() {
        return this.bookStash.get();
    }

    // Scenario: Add a book to the library

    @Given("the user has a book with title \"$title\", author \"$author\", and year $year")
    public void givenUserHasBook(String title, String author, int year) {
        //bookStash.set(new BookService(bookRepository));
        addedBook = new Book();
        addedBook.setTitle(title);
        addedBook.setAuthor(author);
        addedBook.setYear(year);
    }

    @When("the user adds the book to the library")
    public void whenUserAddsBookToLibrary() {
        addedBook = getBookService().addBook(addedBook);
    }

    @Then("the library should contain the book with title \"$title\", author \"$author\", and year $year")
    public void thenLibraryShouldContainBook(String title, String author, int year) {
        library = getBookService().getAllBooks();

        Book expectedBook = new Book(title, author, year);
        assertTrue(library.contains(expectedBook));
    }

    // Scenario: Add a new book to the library with incorrect data

    @When("a new book is added with title \"$title\", author \"$author\", and year \"$year\"")
    public void whenANewBookIsAdded(String title, String author, String year) {
        try {
            Book newBook = new Book(title, author, Integer.parseInt(year));
            addedBook = getBookService().addBook(newBook);
        } catch (NumberFormatException e) {
            exception = e;
        }
    }

    @Then("the system should reject the book with an error message")
    public void thenTheSystemShouldRejectTheBookWithAnErrorMessage() {
        assertNotNull(exception);
        assertTrue(exception instanceof NumberFormatException, "The exception should be a NumberFormatException");
    }

    // Scenario: Retrieve a book by its ID

    @Given("there are existing books in the service")
    public void givenThereAreExistingBooks() {
        getBookService().addBook(new Book("Sample Title 1", "Sample Author 1", 2022));
        getBookService().addBook(new Book("Sample Title 2", "Sample Author 2", 2023));
        getBookService().addBook(new Book("Sample Title 3", "Sample Author 3", 2021));

        existingBooks = getBookService().getAllBooks();
    }

    @When("the user requests to get a book by ID")
    public void whenUserRequestsToGetBookById() {
        if (!existingBooks.isEmpty()) {
            requestedBookId = existingBooks.get(0).getId();
            retrievedBook = getBookService().getBookById(requestedBookId);
        }
    }

    @Then("the system should return the details of the book")
    public void thenSystemShouldReturnBookDetails() {
        assertEquals(requestedBookId, retrievedBook.getId());
        assertNotNull("Title should not be null", retrievedBook.getTitle());
        assertNotNull("Author should not be null", retrievedBook.getAuthor());
    }

    // Scenario: Adding a specific book and checking its ID

    @When("I add a book with title \"$title\" author \"$author\" and year $year")
    public void whenIAddBook(String title, String author, int year) {
        Book book = new Book(title, author, year);
        justBook = getBookService().addBook(book);
        justBookId = justBook.getId();
    }

    @Then("the book should be added successfully")
    public void thenBookAddedSuccessfully() {
        assertNotNull(justBook);
    }

    @Then("the book should have a valid ID")
    public void thenBookShouldHaveValidId() {
        assertNotNull(justBookId);
    }
}