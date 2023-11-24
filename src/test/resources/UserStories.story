Scenario: Add a new user
    Given the system has no existing users
    When the user adds a new user with the username jan_kowalski, email jankowalski@wp.pl and password passwd
    Then the system should return user jan_kowalski, jankowalski@wp.pl, passwd

Scenario: Add a new user with a duplicate username
    Given the system has an existing user with the username jan_kowalski, email jankowalski@wp.pl and password passwd
    When the user adds a new user with the username jan_kowalski, email jankowalski2@wp.pl and password passwd
    Then the system should reject the user addition

Scenario: Remove an existing user
    Given the system has an existing user with the username pawel_nowak, email pawelnowak@wp.pl and password qwerty
    When the user removes the user with the username pawel_nowak
    Then the system should confirm the user is removed successfully

Scenario: Remove a non-existent user
    Given the system has no existing user with the username nonexistent_user
    When the user attempts to remove the user with the username nonexistent_user
    Then the system should provide an error indicating that the user does not exist
