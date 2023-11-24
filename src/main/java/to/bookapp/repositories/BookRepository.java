package to.bookapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import to.bookapp.models.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    void deleteByTitle(String title);
}