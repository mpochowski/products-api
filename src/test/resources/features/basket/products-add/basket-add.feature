Feature: The feature enables user to add products to the basket.

  Scenario: Product is added to the empty basket.
    Given I'm logged in as "john" using password "pass" in brand 2.
    And My basket is empty.
    And My basket has "IN_PROCESS" state.
    When I ask to add products to basket:
      | 1 |
      | 2 |
    Then The basket value should be 24.98 and should contain products:
      | 1 |
      | 2 |

  Scenario: Product is added to the basket.
    Given I'm logged in as "john" using password "pass" in brand 2.
    And My basket has products:
      | 1 |
    And My basket has "IN_PROCESS" state.
    When I ask to add products to basket:
      | 2 |
    Then The basket value should be 24.98 and should contain products:
      | 1 |
      | 2 |

  Scenario: Product is added twice to the basket.
    Given I'm logged in as "john" using password "pass" in brand 2.
    And My basket has products:
      | 1 |
    And My basket has "IN_PROCESS" state.
    When I ask to add products to basket:
      | 1 |
      | 2 |
    Then The basket value should be 34.97 and should contain products:
      | 1 |
      | 1 |
      | 2 |

  Scenario: Product is not added to the basket, because it does not exist.
    Given I'm logged in as "john" using password "pass" in brand 2.
    And My basket has products:
      | 1 |
    And My basket has "IN_PROCESS" state.
    When I ask to add products to basket:
      | 0 |
    Then The basket value should be 9.99 and should contain products:
      | 1 |

  Scenario: Product is not added to the basket, because it is offered by the different brand.
    Given I'm logged in as "john" using password "pass" in brand 2.
    And My basket has products:
      | 1 |
    And My basket has "IN_PROCESS" state.
    When I ask to add products to basket:
      | 3 |
    Then The basket value should be 9.99 and should contain products:
      | 1 |

  Scenario: Product is not added to the basket, because basket is in checkout state.
    Given I'm logged in as "john" using password "pass" in brand 2.
    And My basket has products:
      | 1 |
    And My basket has "IN_CHECKOUT" state.
    When I ask to add products to basket:
      | 2 |
    Then The basket value should be 9.99 and should contain products:
      | 1 |

  Scenario: Product is not added to the basket, because basket is in completed state.
    Given I'm logged in as "john" using password "pass" in brand 2.
    And My basket has products:
      | 1 |
    And My basket has "COMPLETED" state.
    When I ask to add products to basket:
      | 2 |
    Then The basket value should be 9.99 and should contain products:
      | 1 |