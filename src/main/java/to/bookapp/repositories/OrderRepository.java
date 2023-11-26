package to.bookapp.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import to.bookapp.models.Order;
import to.bookapp.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    //Optional<Order> findByUsername(String username);

    Optional<Order> findByUser(User user);

    void deleteAllByUser(User user);

    List<Order> findAllByUser(User user);
}
