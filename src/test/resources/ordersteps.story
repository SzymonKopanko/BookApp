Scenario: When user places the order record is created
Given a user with username testUser
And these books are in the database:
  | Book Id | Title   | Author    | Description  | Price | Image              |
  | 1       | Book1   | Author1   | Description1 | 10.0  | /images/book1.jpg  |
  | 2       | Book2   | Author2   | Description2 | 5.0   | /images/book2.jpg  |
And these books are in the order:
  | Book Id | Quantity |
  | 1       | 2        |
  | 2       | 1        |
And names are John Doe
And phone is 12345678
And mail is johndoe@gmail.com
When the user places order
Then the order should be created
And the order status should be "Reserved"
And the total price should be 25.0

Scenario: When worker changes order status the record should be updated
Given an order exists placed by testUser
When user updates the order's status to "Picked up"
Then the order status should be "Picked up"