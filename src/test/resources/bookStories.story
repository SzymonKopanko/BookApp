Scenario: Add a book to the library
Given the user has a book with title "The Great Gatsby", author "F. Scott Fitzgerald", and year 1925
When the user adds the book to the library
Then the library should contain the book with title "The Great Gatsby", author "F. Scott Fitzgerald", and year 1925

Scenario: Add a new book to the library with incorrect data
When a new book is added with title "Invalid Book", author "Jane Doe", and year "InvalidYear"
Then the system should reject the book with an error message

Scenario: Retrieve a book by its ID
Given there are existing books in the service
When the user requests to get a book by ID
Then the system should return the details of the book

Scenario: Adding a specific book and checking its ID
When I add a book with title "The Great Gatsby" author "F. Scott Fitzgerald" and year 1925
Then the book should be added successfully
And the book should have a valid ID

Scenario: Displaying all books in the library
Given there are books in the system:
  - Title: The Great Gatsby, Author: F. Scott Fitzgerald, Year: 1925
  - Title: To Kill a Mockingbird, Author: Harper Lee, Year: 1960
  - Title: 1984, Author: George Orwell, Year: 1949
When the user requests to display all books
Then the system should display a list of books containing:
  - Title: The Great Gatsby, Author: F. Scott Fitzgerald, Year: 1925
  - Title: To Kill a Mockingbird, Author: Harper Lee, Year: 1960
  - Title: 1984, Author: George Orwell, Year: 1949

Scenario: Displaying an empty library
Given the library is empty
When the user requests to display all books
Then the system should display an empty list of books

Scenario: Displaying books with a specific author
Given there are books in the system:
  - Title: The Great Gatsby, Author: F. Scott Fitzgerald, Year: 1925
  - Title: To Kill a Mockingbird, Author: Harper Lee, Year: 1960
  - Title: The Hobbit, Author: J.R.R. Tolkien, Year: 1937
When the user requests to display books by author "F. Scott Fitzgerald"
Then the system should display a list of books containing:
  - Title: The Great Gatsby, Author: F. Scott Fitzgerald, Year: 1925

Scenario: Updating the details of a book
Given there is a book with title "The Great Gatsby", author "F. Scott Fitzgerald", and year 1925
When the user updates the book with title "The Great Gatsby" with the following details:
  - Title: Great Expectations, Author: Charles Dickens, Year: 1861
Then the library should contain the updated book with title "Great Expectations", author "Charles Dickens", and year 1861

Scenario: Attempting to update a non-existing book
Given the system has no existing book with title "My Book"
When the user attempts to update the book with title "My Book" with the following details:
  - Title: Updated Book, Author: New Author, Year: 2022
Then an error should occur indicating that the book with title "My Book" was not found

Scenario: Updating a book with the same details
Given there is a book with title "The Catcher in the Rye", author "J.D. Salinger", and year 1951
When the user updates the book with title "The Catcher in the Rye" with the same details:
  - Title: The Catcher in the Rye, Author: J.D. Salinger, Year: 1951
Then the library should contain the same book with title "The Catcher in the Rye", author "J.D. Salinger", and year 1951


Scenario: Deleting a book from the library
Given there is a book with title "To Kill a Mockingbird", author "Harper Lee", and year 1960
When the user deletes the book with title "To Kill a Mockingbird"
Then the library should not contain the book with title "To Kill a Mockingbird", author "Harper Lee", and year 1960

Scenario: Delete a non-existing book
Given the system has no existing book with title "My Book"
When the user deletes the book with title "My Book"
Then an error should occur indicating that the book with title "My Book" was not found

Scenario: Deleting the last book in the library
Given there is a single book with title "The Hobbit", author "J.R.R. Tolkien", and year 1937
When the user deletes the book with title "The Hobbit"
Then the library should be empty
