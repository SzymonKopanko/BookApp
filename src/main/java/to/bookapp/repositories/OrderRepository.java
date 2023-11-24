package to.bookapp.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import to.bookapp.models.Order;
import to.bookapp.models.OrderItem;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
