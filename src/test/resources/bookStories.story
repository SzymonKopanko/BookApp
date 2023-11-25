Scenario: Add a book to the library
Given the user has a book with title "The Great Gatsby", author "F. Scott Fitzgerald", and year 1925
When the user adds the book to the library
Then the library should contain the book with title "The Great Gatsby", author "F. Scott Fitzgerald", and year 1925

Scenario: Add an existing book should not create a duplicate entry
Given there is an existing book with title "The Catcher in the Rye"
When I try to add a book with title "The Catcher in the Rye" by author "J.D. Salinger" published in 1951
Then the system should not create a new entry for the book


