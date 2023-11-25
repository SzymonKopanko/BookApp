package to.bookapp.steps;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;
import to.bookapp.models.User;
import to.bookapp.repositories.UserRepository;
import to.bookapp.services.UserService;

public class UserSteps {
    private User result;
    private final ThreadLocal<UserService> userStash = new ThreadLocal<>();
    private UserRepository userRepository;

    private UserService getUserService() {
        return this.userStash.get();
    }

    @Given("the system has no existing users")
    public void givenTheSystemHasNoExistingUsers() {
        userStash.set(new UserService(userRepository));
    }

    @Given("has an existing user with the username $username, email $email and password $password")
    public void givenTheSystemHasUser(String username, String email, String password) {
        User existingUser = new User(username, email, password);
        getUserService().addUser(existingUser);
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

    @Then("the system should return user $expUsername, $expEmail, $expPassword")
    public void thenTheResultShouldBeExpectedUser(String expUsername, String expEmail, String expPassword) {
        User expUser = new User(expUsername, expEmail, expPassword);
        Assert.assertEquals(expUser, result);
    }
}