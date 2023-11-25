package to.bookapp.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import to.bookapp.models.Book;
import to.bookapp.models.Order;
import to.bookapp.models.OrderItem;
import to.bookapp.models.User;
import to.bookapp.repositories.BookRepository;
import to.bookapp.repositories.OrderItemRepository;
import to.bookapp.repositories.OrderRepository;
import to.bookapp.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        return optionalOrder.orElse(null);
    }

    public Order placeOrder(Order order) {
        if (order.getUser() == null || order.getBooks() == null || order.getBooks().isEmpty()) {
            throw new IllegalArgumentException("Invalid order. User and at least one book are required.");
        }

        if (order.getFirstName() == null || order.getFirstName().isEmpty() || order.getLastName() == null || order.getLastName().isEmpty()) {
            throw new IllegalArgumentException("Invalid order. First and last name of person is required.");
        }

        Optional<User> optionalUser = userRepository.findById(order.getUser().getId());
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User not found. Cannot place the order.");
        }

        for (OrderItem book : order.getBooks()) {
            Optional<Book> optionalBook = bookRepository.findById(book.getBook().getId());
            if (optionalBook.isEmpty()) {
                throw new IllegalArgumentException("Book not found: " + book.getId());
            }
        }
        order.setStatus("Reserved");

        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = order.getBooks();
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(savedOrder);
            orderItemRepository.save(orderItem);
        }
        return savedOrder;
    }

    public Order updateOrder(Long orderId, Order updatedOrder) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order existingOrder = optionalOrder.get();

            if (updatedOrder.getFirstName() != null) {
                existingOrder.setFirstName(updatedOrder.getFirstName());
            }
            if (updatedOrder.getLastName() != null) {
                existingOrder.setLastName(updatedOrder.getLastName());
            }
            if (updatedOrder.getEmail() != null) {
                existingOrder.setEmail(updatedOrder.getEmail());
            }
            if (updatedOrder.getPhone() != null) {
                existingOrder.setPhone(updatedOrder.getPhone());
            }
            if (updatedOrder.getComments() != null){
                existingOrder.setComments(updatedOrder.getComments());
            }
            if (updatedOrder.getStatus() != null){
                String status = updatedOrder.getStatus();
                if (status.equals("Reserved") || status.equals("Picked up") || status.equals("Returned"))
                    existingOrder.setStatus(updatedOrder.getStatus());
                else throw new IllegalArgumentException("Unacceptable order's status: '"+status+"'.Acceptable statuses are 'Reserved', 'Picked up' and 'Returned' ");
            }
            return orderRepository.save(existingOrder);
        } else
            return null;
    }

    public Order deleteOrder(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order existingOrder = optionalOrder.get();
            orderItemRepository.deleteAllByOrder(existingOrder);
            orderRepository.deleteById(orderId);
            return existingOrder;
        } else
            return null;
    }


    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findAllByUser(user);
    }
}

