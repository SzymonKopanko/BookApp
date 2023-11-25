package to.bookapp.steps;
import jakarta.persistence.Table;
import org.jbehave.core.annotations.*;
import org.jbehave.core.model.ExamplesTable;
import org.junit.Assert;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import to.bookapp.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import to.bookapp.repositories.BookRepository;
import to.bookapp.repositories.OrderItemRepository;
import to.bookapp.repositories.OrderRepository;
import to.bookapp.repositories.UserRepository;
import to.bookapp.services.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class OrderSteps {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    private final ThreadLocal<OrderService> orderStash = new ThreadLocal<>();

    public OrderSteps() {
    }

    private OrderService getOrderService() {
        return this.orderStash.get();
    }
    private String username;
    private List<OrderItem> books;
    private String firstName;
    private String lastName;
    private String phone;
    private String mail;
    private Long orderId;
    private String status;
    private Exception exception;

    @AfterStory
    public void afterEveryStory(){
        username = null;
        books = null;
        firstName = null;
        lastName = null;
        phone = null;
        mail = null;
        orderId = null;
        status = null;
        exception = null;
    }

    @Given("a user with username $username")
    public void givenAUserWithId(String username) {
        this.username = username;
        if (userRepository.findByUsername(username).isEmpty()) {
            User user = new User(username, "user@mail", "qawsedrf123");
            userRepository.save(user);
        }
    }

    @Given("these books are in the database: $table")
    public void givenTheseBooksAreInTheDatabase(ExamplesTable table) {
        //when(bookRepository.saveAll(any(List.class)).thenReturn());
        List<Book> books = new ArrayList<>();
        for (Map<String,String> row : table.getRows()){
            Book book = new Book(row.get("Title"), row.get("Author"), Integer.parseInt(row.get("Year")));
            books.add(book);
        }
        bookRepository.saveAll(books);
    }

    @Given("these books are in the order: $orderItems")
    public void givenTheseBooksAreInTheOrder(List<OrderItem> orderItems) {
        this.books = orderItems;
    }
    @Given("names are $firstName $lastName")
    public void givenNames(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    @Given("phone is $phone")
    public void givenPhoneIs(String phone) {
        this.phone = phone;
    }
    @Given("mail is $mail")
    public void givenMailIs(String mail) {
        this.mail = mail;
    }

    @Given("an order exists placed by $username")
    public void givenAnOrderPlacedBy(String username){
        User user = new User();
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            User new_user = new User(username, "user@mail", "qawsedrf123");
            user = userRepository.save(new_user);
        }
        Optional<Order> optionalOrder = orderRepository.findByUser(user);
        if (optionalOrder.isEmpty()) {
            Book new_book = new Book("AddedTestBook", "Author", 1111);
            Book book = bookRepository.save(new_book);
            List<OrderItem> orderItems = new ArrayList<>();
            orderItems.add(new OrderItem(book,2));
            Order new_order = new Order(user, orderItems, "Testowy", "Test", "654323243", "user@mail");
            Order savedOrder = orderRepository.save(new_order);
            orderItems.get(0).setOrder(savedOrder);
            orderItemRepository.save(orderItems.get(0));
        } else this.orderId = optionalOrder.get().getId();
    }
    @Given("order status is \"$status\"")
    public void givenOrderStatusIs(String status){
        if (!orderRepository.findById(orderId).get().getStatus().equals(status)){
            Order updatedOrder = new Order(status);
            orderService.updateOrder(orderId,updatedOrder);
        }
    }

    @When ("user updates the order's status to \"$status\"")
    public void whenUserUpdatesOrderStatus(String status){
        Order updatedOrder = new Order(status);
        try{
            getOrderService().updateOrder(orderId, updatedOrder);
        } catch (IllegalArgumentException e){
            exception = e;
        }
    }

    @When("the user places order")
    public void whenTheUserPlacesOrder() {
        User user = new User();
        user.setUsername(username);

        Order order = new Order(user, books,firstName,lastName,phone,mail);

        try {
            this.orderId = getOrderService().placeOrder(order).getId();
        } catch (IllegalArgumentException e){
            exception = e;
        }
    }

    @Then("the order should be created")
    public void thenTheOrderShouldBeCreated() {
        Optional<Order> dbOrder = orderRepository.findById(orderId);

        Assert.assertFalse(dbOrder.isEmpty());
    }

    @Then("the order status should be \"$status\"")
    public void thenTheOrderStatusShouldBe(String status) {
        Assert.assertEquals(status,orderRepository.findById(orderId).get().getStatus());
    }
    @Then ("an exception should be thrown")
    public void thenExceptionIsThrown(){
        Assert.assertNotNull(exception);
    }
}
