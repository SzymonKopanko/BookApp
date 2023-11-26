package to.bookapp.steps;
import org.jbehave.core.annotations.*;
import org.jbehave.core.model.ExamplesTable;
import org.junit.Assert;
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
    private List<Order> orders;
    private List<Book> books;

    public OrderSteps() {
    }

    private OrderService getOrderService() {
        return this.orderStash.get();
    }
    private Long userId;
    private List<OrderItem> orderItems;
    private String firstName;
    private String lastName;
    private String phone;
    private String mail;
    private Long orderId;
    private Exception exception;

    @AfterStory
    public void afterEveryStory(){
        if (userId != null){
            Optional<User> user = userRepository.findById(userId);
            if (user.isPresent()){
                List<Order> orders = orderRepository.findAllByUser(user.get());
                if (!orders.isEmpty()){
                    for (Order order : orders){
                        orderItemRepository.deleteAllByOrder(order);
                    }
                    orderRepository.deleteAllByUser(user.get());
                }
                userRepository.deleteById(userId);
            }
        }
        userId = null;
        orderItems = null;
        firstName = null;
        lastName = null;
        phone = null;
        mail = null;
        orderId = null;
        exception = null;
        orders = null;
        books = null;
    }

    @Given("a user with id $userId")
    public void givenAnUserWithId(Long userId) {
        this.userId = userId;
        if (userRepository.findById(userId).isEmpty()) {
            User user = new User("username", "user@mail", "qawsedrf123");
            userRepository.save(user);
        }
    }

    @Given("these books are in the database: $table")
    public void givenTheseBooksAreInTheDatabase(ExamplesTable table) {
        this.books = new ArrayList<>();
        for (Map<String,String> row : table.getRows()){
            Book book = new Book(row.get("Title"), row.get("Author"), Integer.parseInt(row.get("Year")));
            books.add(book);
        }
        bookRepository.saveAll(books);
    }

    @Given("these books are in the order: $orderItems")
    public void givenTheseBooksAreInTheOrder(ExamplesTable table) {
        this.orderItems = new ArrayList<>();
        for (Map<String,String> row : table.getRows()){
            OrderItem item = new OrderItem(bookRepository.getByTitle(row.get("Book Title")).get(), Integer.parseInt(row.get("Quantity")));
            this.orderItems.add(item);
        }

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

    @Given("an order exists placed by user with id $userId")
    public void givenAnOrderPlacedBy(Long userId){
        this.userId = userId;
        User user = new User();
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            User new_user = new User("username", "user@mail", "qawsedrf123");
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
            this.orderId = savedOrder.getId();
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

    @Given("2 orders exist placed by user with id $userId")
    public void givenCoupleOrdersExist(Long userId){
        this.userId = userId;
        User user = new User();
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            User new_user = new User("username", "user@mail", "qawsedrf123");
            user = userRepository.save(new_user);
        }
        Optional<Order> optionalOrder = orderRepository.findByUser(user);
        if (optionalOrder.isEmpty()) {
            List<Book> books = new ArrayList<>();
            books.add(new Book("AddedFirstTestBook", "AuthorTest", 1111));
            books.add(new Book("AddedSecondTestBook", "AuthorTest", 1121));
            for (Book new_book : books) {
                Book book = bookRepository.save(new_book);
                List<OrderItem> orderItems = new ArrayList<>();
                orderItems.add(new OrderItem(book, 2));
                Order new_order = new Order(user, orderItems, "Testowy", "Test", "654323243", "user@mail");
                Order savedOrder = orderRepository.save(new_order);
                orderItems.get(0).setOrder(savedOrder);
                orderItemRepository.save(orderItems.get(0));
            }
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
        User user = userRepository.findById(this.userId).get();
        Order order = new Order(user, orderItems,firstName,lastName,phone,mail);

        try {
            this.orderId = getOrderService().placeOrder(order).getId();
        } catch (IllegalArgumentException e){
            exception = e;
        }
    }
    @When("the user asks for all orders")
    public void whenUserAsksForAll(){
        User user = userRepository.findById(this.userId).get();
        this.orders = getOrderService().getOrdersByUser(user);
    }
    @When("the user deletes order")
    public void whenUserDeletesOrder(){
        orderService.deleteOrder(this.orderId);
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

    @Then("2 records are returned")
    public void thenAllRecordAreReturned(){Assert.assertEquals(2,this.orders.size());}

    @Then("order record is not in database")
    public void thenRecordIsNotPresent(){Assert.assertTrue(orderRepository.findById(this.orderId).isEmpty());}
}