Feature: The feature enables user to process the basket state.

  Scenario: Basket is created with in process state.
    Given I'm logged in as "john" using password "pass" in brand 2.
    When I ask to create new empty basket.
    Then The basket value should be 0.00
    And The basket state should be "IN_PROCESS"

  Scenario: Basket can be moved to checkout.
    Given I'm logged in as "john" using password "pass" in brand 2.
    And My basket is "IN_PROCESS" with products:
      | 1 |
    When I ask to update basket state to "IN_CHECKOUT"
    Then The basket value should be 9.99
    And The basket state should be "IN_CHECKOUT"

  Scenario: Basket can be moved back to in process.
    Given I'm logged in as "john" using password "pass" in brand 2.
    And My basket is "IN_CHECKOUT" with products:
      | 1 |
    When I ask to update basket state to "IN_PROCESS"
    Then The basket value should be 9.99
    And The basket state should be "IN_PROCESS"

  Scenario: User cannot pay for basket items, if basket is not in checkout.
    Given I'm logged in as "john" using password "pass" in brand 2.
    And My basket is "IN_PROCESS" with products:
      | 1 |
    When I ask to update basket state to "COMPLETED"
    Then The basket value should be 9.99
    And The basket state should be "IN_PROCESS"

  Scenario: User can pay for basket items.
    Given I'm logged in as "george" using password "pass" in brand 2.
    And My basket is "IN_CHECKOUT" with products:
      | 1 |
      | 2 |
    When I ask to update basket state to "COMPLETED"
    Then I should own 2 active subscriptions for products:
      | 1 |
      | 2 |
    And The basket state should be "COMPLETED"

  Scenario: Completed basket state cannot be changed to in checkout.
    Given I'm logged in as "john" using password "pass" in brand 2.
    And My basket is "COMPLETED" with products:
      | 1 |
    When I ask to update basket state to "IN_CHECKOUT"
    Then The basket value should be 9.99
    And The basket state should be "COMPLETED"

  Scenario: Completed basket state cannot be changed to in process.
    Given I'm logged in as "john" using password "pass" in brand 2.
    And My basket is "COMPLETED" with products:
      | 1 |
    When I ask to update basket state to "IN_PROCESS"
    Then The basket value should be 9.99
    And The basket state should be "COMPLETED"