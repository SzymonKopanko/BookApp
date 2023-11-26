package to.bookapp.steps;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import org.junit.Assert;
import to.bookapp.models.Book;
import to.bookapp.repositories.BookRepository;
import to.bookapp.services.BookService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookSteps {

    private Book addedBook;
    private List<Book> library;
    private BookRepository bookRepository;
    private final ThreadLocal<BookService> bookStash = new ThreadLocal<>();

    private BookService getBookService() {
        return this.bookStash.get();
    }

    @Given("the user has a book with title \"$title\", author \"$author\", and year $year")
    public void givenUserHasBook(String title, String author, int year) {
        bookStash.set(new BookService(bookRepository));
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

        // Assuming Book is a class with an appropriate equals() method
        Book expectedBook = new Book(title, author, year);
        assertTrue(library.contains(expectedBook));
    }

    private Book existingBook;
    private Book newBook;

    @Given("there is an existing book with title \"$title\"")
    public void givenThereIsAnExistingBookWithTitle(String title) {
        // Assume the book already exists in the database
        existingBook = new Book(title, "Sample Author", 2000);
        getBookService().addBook(existingBook);
    }

    @When("I try to add a book with title \"$title\" by author \"$author\" published in $year")
    public void whenITryToAddABookWithTitleAuthorAndYear(String title, String author, int year) {
        // Attempt to add a book with the same title
        newBook = new Book(title, author, year);
        newBook = getBookService().addBook(newBook);
    }

    @Then("the system should not create a new entry for the book")
    public void thenTheSystemShouldNotCreateANewEntryForTheBook() {
        // Ensure that the new book is not added to the database
        Assert.assertTrue("The system should not create a new entry for the book", newBook.getId() == null);
    }

}


