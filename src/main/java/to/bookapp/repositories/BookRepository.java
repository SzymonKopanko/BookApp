package to.bookapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import to.bookapp.models.Book;

import javax.swing.text.html.Option;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    void deleteByTitle(String title);
    Optional<Book> getByTitle(String title);
}