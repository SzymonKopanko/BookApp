Scenario: When user places the order record is created
Given a user with username testUser
And these books are in the database:
  | Title   | Author    | Year |
  | Book1   | Author1   | 1993 |
  | Book2   | Author2   | 2002 |
And these books are in the order:
  | Book Id | Quantity |
  | 1       | 2        |
  | 2       | 1        |
And names are Adam Kowalski
And phone is 12345678
And mail is kowalski@gmail.com
When the user places order
Then the order should be created
And the order status should be "Reserved"

Scenario: When user places the order without items the order record should not be created
Given a user with username testUser
And these books are in the database:
  | Title   | Author    | Year |
  | Book1   | Author1   | 1993 |
  | Book2   | Author2   | 2002 |
And names are Adam Kowalski
And phone is 12345678
And mail is kowalski@gmail.com
When the user places order
Then an exception should be thrown

Scenario: When worker changes order status to "Picked up" the record should be updated
Given an order exists placed by testUser
When user updates the order's status to "Picked up"
Then the order status should be "Picked up"

Scenario: When worker changes order status to "Returned" the record should be updated
Given an order exists placed by testUser
When user updates the order's status to "Returned"
Then the order status should be "Returned"

Scenario: When worker changes order status to unacceptable state the record shouldn't be updated
Given an order exists placed by testUser
And order status is "Returned"
When user updates the order's status to "Active"
Then an exception should be thrown
And the order status should be "Returned"

