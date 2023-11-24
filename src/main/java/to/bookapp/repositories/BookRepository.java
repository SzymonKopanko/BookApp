package to.bookapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import to.bookapp.models.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

}