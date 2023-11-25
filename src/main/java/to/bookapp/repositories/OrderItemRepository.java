package to.bookapp.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import to.bookapp.models.Order;
import to.bookapp.models.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    void deleteAllByOrder(Order order);
}
