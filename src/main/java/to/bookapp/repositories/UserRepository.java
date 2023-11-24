package to.bookapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import to.bookapp.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    void deleteByUsername(String username);
}
