package to.bookapp.steps;
import org.jbehave.core.annotations.*;
import org.junit.Assert;
import to.bookapp.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import to.bookapp.repositories.OrderRepository;
import to.bookapp.services.OrderService;

import java.util.List;
import java.util.Optional;

@Component
public class OrderSteps {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OrderRepository orderRepository;

    private final ThreadLocal<OrderService> orderStash = new ThreadLocal<>();
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
    }

    @Given("these books are in the database: $books")
    public void givenTheseBooksAreInTheDatabase(List<Book> books) {
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
        //todo sprawdz czy jest user
        //todo sprawdz czy jest order

        User user = new User();
        user.setUsername(username);
        //TODO add some false order
        Order order = new Order();
        this.orderId = getOrderService().placeOrder(order).getId();
    }
    @Given("order status is \"$status\"")
    public void givenOrderStatusIs(String status){
        if (!orderRepository.findById(orderId).get().getStatus().equals(status)){
            Order updatedOrder = new Order(status);
            getOrderService().updateOrder(orderId,updatedOrder);
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
