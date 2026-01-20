Scenario : Accept Order if inventory is sufficient.
Given inventory has 10 quantity of product with id "product-1"
When user tries to place order for product with id "product-1" with quantity 5
Then Accept the order

Scenario : Reject Order if inventory is sufficient.
Given inventory has 10 quantity of product with id "product-1"
When user tries to place order for product with id "product-1" with quantity 12
Then Reject the order