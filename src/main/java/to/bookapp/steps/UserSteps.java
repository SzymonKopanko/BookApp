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

    @Given("the system")
    public void givenTheSystem() {
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

    @When("the user removes the user with the username $username")
    public void whenUserRemovesUser(String username) {
        getUserService().removeUser(username);
    }

    @Then("the system should return user $expUsername, $expEmail, $expPassword")
    public void thenTheResultShouldBe(String expUsername, String expEmail, String expPassword) {
        User expUser = new User(expUsername, expEmail, expPassword);
        Assert.assertEquals(expUser, result);
    }
}