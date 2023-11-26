Scenario: Add a new user
Given the system has no existing users
When the user adds a new user with the username jan_kowalski, email jankowalski@wp.pl and password passwd
Then the system should return user jan_kowalski, jankowalski@wp.pl, passwd

Scenario: Remove an existing user
Given the system has an existing user with the username pawel_nowak, email pawelnowak@wp.pl and password qwerty
When the user deletes the user with the id 1
Then the system should confirm the user is removed successfully

Scenario: Remove a non-existent user
Given the system has no existing user with id 5
When the user deletes the user with the id 5
Then the system should provide an error indicating that the user does not exist

Scenario: Update an existing user
Given the system has an existing user with the username jan_kowalski, email jankowalski@wp.pl, and password passwd
When the user updates the user with the id 1, username jan_kowalski setting the email to newemail@wp.pl and password to newpasswd
Then the system should return the updated user jan_kowalski, newemail@wp.pl, newpasswd


