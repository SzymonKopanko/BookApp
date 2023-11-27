package to.bookapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import to.bookapp.models.User;
import to.bookapp.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

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

    public Optional<User> deleteUser(Long userId) {
        Optional<User> deletedUser = userRepository.findById(userId);
        userRepository.deleteById(userId);
        if(deletedUser.isPresent()) {
            return deletedUser;
        } else {
            throw new RuntimeException("User with ID " + userId + " not found");
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long userId) {return userRepository.findById(userId);}

    public User updateUser(Long userId, User updatedUser) {
        Optional<User> existingUserOptional = userRepository.findById(userId);

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setAdmin(updatedUser.isAdmin());
            return userRepository.save(existingUser);
        } else {
            throw new RuntimeException("User with ID " + userId + " not found");
        }
    }

}
