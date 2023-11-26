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
    private List<Book> displayedBooks;
    private Long requestedBookId;
    private Long bookId;
    private Book originalBook;
    private Book deletedBook;
    private Book updatedBook;
    private Long requestedBookId;
    private Book retrievedBook;
    private Book justBook;
    private Long justBookId;
    private Exception exception;
    private Exception thrownException;
    private BookRepository bookRepository;
    private String nonExistingTitle;
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

    //Scenario: Displaying all books in the library

    @Given("there are books in the system:")
    public void givenThereAreBooksInTheSystem(List<Book> books) {
        bookRepository.saveAll(books);
    }

    @When("the user requests to display all books")
    public void whenTheUserRequestsToDisplayAllBooks() {
        displayedBooks = getBookService().getAllBooks();
    }

    @Then("the system should display a list of books containing:")
    public void thenTheSystemShouldDisplayAListOfBooks(List<Book> expectedBooks) {
        assert displayedBooks != null;
        assert displayedBooks.size() == expectedBooks.size();
        assert displayedBooks.containsAll(expectedBooks);
    }

    //Scenario: Displaying an empty library
    @Given("the library is empty")
    public void givenTheLibraryIsEmpty() {
        bookRepository.deleteAll();
    }

    @Then("the system should display an empty list of books")
    public void thenTheSystemShouldDisplayAnEmptyListOfBooks() {
        assert displayedBooks != null;
        assert displayedBooks.isEmpty();
    }

    //Scenario: Updating the details of a book
    @Given("there is a book with title \"$title\", author \"$author\", and year $year")
    public void givenThereIsABookWithTitleAuthorAndYear(String title, String author, int year) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setYear(year);
        bookId = getBookService().addBook(book).getId();
    }

    @When("the user updates the book with title \"$title\" with the following details: - Title: $newTitle, Author: $newAuthor, Year: $newYear")
    public void whenTheUserUpdatesTheBookWithTitleWithTheFollowingDetails(String title, String newTitle, String newAuthor, int newYear) {
        Book updatedBook = new Book();
        updatedBook.setTitle(newTitle);
        updatedBook.setAuthor(newAuthor);
        updatedBook.setYear(newYear);
        this.updatedBook = getBookService().updateBook(bookId, updatedBook);
    }

    @Then("the library should contain the updated book with title \"$title\", author \"$author\", and year $year")
    public void thenTheLibraryShouldContainTheUpdatedBookWithTitleAuthorAndYear(String title, String author, int year) {
        Book actualBook = getBookService().getBookById(bookId);
        assert actualBook != null;
        assert actualBook.getTitle().equals(title);
        assert actualBook.getAuthor().equals(author);
        assert actualBook.getYear() == year;
    }

    //Scenario: Attempting to update a non-existing book
    @Given("the system has no existing book with title \"$title\"")
    public void givenTheSystemHasNoExistingBookWithTitle(String title) {
        nonExistingTitle = title;
        bookRepository.deleteByTitle(title);
    }

    @When("the user attempts to update the book with title \"$title\" with the following details: - Title: $newTitle, Author: $newAuthor, Year: $newYear")
    public void whenTheUserAttemptsToUpdateTheBookWithTitleWithTheFollowingDetails(String title, String newTitle, String newAuthor, int newYear) {
        try {
            Book updatedBook = new Book();
            updatedBook.setTitle(newTitle);
            updatedBook.setAuthor(newAuthor);
            updatedBook.setYear(newYear);
            this.updatedBook = getBookService().updateBook(bookId, updatedBook);
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Then("an error should occur indicating that the book with title \"$title\" was not found")
    public void thenAnErrorShouldOccurIndicatingThatTheBookWithTitleWasNotFound(String title) {
        assert thrownException != null;
        assert thrownException.getMessage().contains(title + " not found");
    }

    //Scenario: Updating a book with the same details
    @When("the user updates the book with title \"$title\" with the same details: - Title: $newTitle, Author: $newAuthor, Year: $newYear")
    public void whenTheUserUpdatesTheBookWithTitleWithTheSameDetails(String title, String newTitle, String newAuthor, int newYear) {
        Book updatedBook = new Book();
        updatedBook.setTitle(newTitle);
        updatedBook.setAuthor(newAuthor);
        updatedBook.setYear(newYear);
        this.updatedBook = getBookService().updateBook(bookId, updatedBook);
    }

    @Then("the library should contain the same book with title \"$title\", author \"$author\", and year $year")
    public void thenTheLibraryShouldContainTheSameBookWithTitleAuthorAndYear(String title, String author, int year) {
        // Assuming you have a method in BookService for getting a book by ID
        Book actualBook = getBookService().getBookById(bookId);
        // Verify that the updated book matches the original book
        assert actualBook != null;
        assert actualBook.getTitle().equals(title);
        assert actualBook.getAuthor().equals(author);
        assert actualBook.getYear() == year;
        assert actualBook.equals(originalBook);
    }


    //Scenario: Deleting a book from the library
    @When("the user deletes the book with title \"$title\"")
    public void whenTheUserDeletesTheBookWithTitle(String title) {
        deletedBook = getBookService().deleteBook(bookId);
    }

    @Then("the library should not contain the book with title \"$title\", author \"$author\", and year $year")
    public void thenTheLibraryShouldNotContainTheBookWithTitleAuthorAndYear(String title, String author, int year) {
        Book actualBook = getBookService().getBookById(bookId);
        assert actualBook == null;
        assert deletedBook != null;
        assert deletedBook.getTitle().equals(title);
        assert deletedBook.getAuthor().equals(author);
        assert deletedBook.getYear() == year;
    }

    //Scenario: Deleting the last book in the library
    @Given("there is a single book with title \"$title\", author \"$author\", and year $year")
    public void givenThereIsASingleBookWithTitleAuthorAndYear(String title, String author, int year) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setYear(year);
        bookId = getBookService().addBook(book).getId();
    }

    @Then("the library should be empty")
    public void thenTheLibraryShouldBeEmpty() {
        Iterable<Book> allBooks = bookRepository.findAll();
        assert !allBooks.iterator().hasNext();
    }

}