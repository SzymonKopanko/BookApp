package to.bookapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import to.bookapp.models.User;
import to.bookapp.repositories.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public void removeUser(String username) {
        userRepository.deleteByUsername(username);
    }

    @RequestMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
