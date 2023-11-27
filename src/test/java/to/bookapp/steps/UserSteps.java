package to.bookapp.steps;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;
import org.springframework.stereotype.Component;
import to.bookapp.models.User;
import to.bookapp.repositories.UserRepository;
import to.bookapp.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserSteps {
    private User result;
    private List<User> listResult;
    private boolean userRemovalConfirmation;
    private final ThreadLocal<UserService> userStash = new ThreadLocal<>();
    private UserRepository userRepository;

    private UserService getUserService() {
        return this.userStash.get();
    }

    public void setUserRemovalConfirmation(boolean confirmationStatus) {
        this.userRemovalConfirmation = confirmationStatus;
    }

    @Given("the system has no existing users")
    public void givenTheSystemHasNoExistingUsers() {
        userStash.set(new UserService(userRepository));
    }

    @Given("the system has an existing user with the username $username, email $email and password $password")
    public void givenTheSystemHasUser(String username, String email, String password) {
        userStash.set(new UserService(userRepository));
        User existingUser = new User(username, email, password);
        getUserService().addUser(existingUser);
    }

    @Given("the system has 2 existing users: $username1, $email1, $password1 and $username2, $email2, $password2")
    public void givenTheSystemHas2Users(String username1, String email1, String password1, String username2, String email2, String password2) {
        userStash.set(new UserService(userRepository));
        User existingUser1 = new User(username1, email1, password1);
        getUserService().addUser(existingUser1);
        User existingUser2 = new User(username2, email2, password2);
        getUserService().addUser(existingUser2);
    }

    @Given("the system has no existing user with id $id")
    public void givenTheSystemHasNoUser(Long id) {
        userStash.set(new UserService(userRepository));
        getUserService().deleteUser(id);
        Optional<User> optionalUser = getUserService().getUserById(id);
        boolean isUserRemoved = optionalUser.isEmpty();
        setUserRemovalConfirmation(isUserRemoved);
    }

    @When("the user gets the user with the id $id")
    public void whenUserGetsUser(Long id) {
        result = getUserService().getUserById(id).get();
    }

    @When("the user gets all users")
    public void whenUserGetsAllUsers() {
        listResult = getUserService().getAllUsers();
    }

    @When("the user adds a new user with the username $username, email $email and password $password")
    public void whenUserAddsUser(String username, String email, String password) {
        User user = new User(username, email, password);
        result = getUserService().addUser(user);
    }

    @When("the user updates the user with the id $id, username $username setting the email to $email and password to $password")
    public void whenUserUpdatesUser(Long id, String username, String email, String password) {
        User updatedUser = new User(username, email, password);
        result = getUserService().updateUser(id, updatedUser);
    }

    @When("the user deletes the user with the id $id")
    public void whenUserRemovesUser(Long id) {
        getUserService().deleteUser(id);
    }

    @Then("the system should return a list [$username1, $email1, $password1], [$username2, $email2, $password2]")
    public void thenTheResultShouldBeExpectedList(String username1, String email1, String password1, String username2, String email2, String password2) {
        User expUser1 = new User(username1, email1, password1);
        User expUser2 = new User(username2, email2, password2);
        List<User> expList = new ArrayList<>();
        expList.add(expUser1);
        expList.add(expUser2);

        Assert.assertEquals(expList, listResult);
    }

    @Then("the system should return user $expUsername, $expEmail, $expPassword")
    public void thenTheResultShouldBeExpectedUser(String expUsername, String expEmail, String expPassword) {
        User expUser = new User(expUsername, expEmail, expPassword);
        Assert.assertEquals(expUser, result);
    }

    @Then("the system should confirm the user is removed successfully")
    public void thenTheSystemShouldConfirmTheUserIsRemovedSuccessfully() {
        Assert.assertTrue(userRemovalConfirmation);
    }

    @Then("the system should provide an error indicating that the user with id $id does not exist")
    public void thenTheSystemShouldProvideError(Long id) {
        try {
            getUserService().deleteUser(id);
            Assert.fail("Expected RuntimeException but no exception was thrown");
        } catch (RuntimeException e) {
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
}