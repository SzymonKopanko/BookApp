package to.bookapp.steps;
import org.junit.Assert;
import to.bookapp.models.*;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
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

    private String username;
    private List<OrderItem> books;
    private String firstName;
    private String lastName;
    private String phone;
    private String mail;
    private Optional<Order> dbOrder;
    private Long orderId;


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
        User user = new User();
        user.setUsername(username);
        //TODO add some false order
        Order order = new Order();
        this.orderId = orderService.placeOrder(order).getId();
    }

    @When ("user updates the order's status to \"$status\"")
    public void whenUserUpdatesOrderStatus(String status){
        Order updatedOrder = new Order(status);
        orderService.updateOrder(orderId, updatedOrder);
    }

    @When("the user places order")
    public void whenTheUserPlacesOrder() {
        User user = new User();
        user.setUsername(username);

        Order order = new Order(user, books,firstName,lastName,phone,mail);

        this.orderId = orderService.placeOrder(order).getId();
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

    @Then("the total price should be $total")
    public void thenTheTotalPriceShouldBe(Double total) {
        Assert.assertEquals(total, orderRepository.findById(orderId).get().getTotalPrice(), 0.01);
    }
}
