Feature: The feature enables user to remove products from the basket.

  Scenario: Product is removed from my basket.
    Given I'm logged in as "john" using password "pass" in brand 2.
    And My basket has products:
      | 1 |
      | 2 |
    And My basket has "IN_PROCESS" state.
    When I ask to remove products from basket:
      | 1 |
    Then The basket value should be 14.99 and should contain products:
      | 2 |

  Scenario: The duplicated product is removed from my basket.
    Given I'm logged in as "john" using password "pass" in brand 2.
    And My basket has products:
      | 1 |
      | 1 |
      | 2 |
    And My basket has "IN_PROCESS" state.
    When I ask to remove products from basket:
      | 1 |
    Then The basket value should be 24.98 and should contain products:
      | 1 |
      | 2 |

  Scenario: All products are removed from my basket.
    Given I'm logged in as "john" using password "pass" in brand 2.
    And My basket has products:
      | 1 |
      | 2 |
    And My basket has "IN_PROCESS" state.
    When I ask to remove products from basket:
      | 1 |
      | 2 |
    Then The basket value should be 0.00 and should be empty.

  Scenario: Both duplicated products are removed from my basket.
    Given I'm logged in as "john" using password "pass" in brand 2.
    And My basket has products:
      | 1 |
      | 1 |
      | 2 |
    And My basket has "IN_PROCESS" state.
    When I ask to remove products from basket:
      | 1 |
      | 1 |
    Then The basket value should be 14.99 and should contain products:
      | 2 |

  Scenario: Product is not removed from my basket, because it is not in basket.
    Given I'm logged in as "john" using password "pass" in brand 2.
    And My basket has products:
      | 1 |
    And My basket has "IN_PROCESS" state.
    When I ask to remove products from basket:
      | 2 |
    Then The basket value should be 9.99 and should contain products:
      | 1 |

  Scenario: Product is not removed from my basket, because it does not exist.
    Given I'm logged in as "john" using password "pass" in brand 2.
    And My basket has products:
      | 1 |
    And My basket has "IN_PROCESS" state.
    When I ask to remove products from basket:
      | 0 |
    Then The basket value should be 9.99 and should contain products:
      | 1 |

  Scenario: Product is not removed from my basket, because my basket is in checkout.
    Given I'm logged in as "john" using password "pass" in brand 2.
    And My basket has products:
      | 1 |
      | 2 |
    And My basket has "IN_CHECKOUT" state.
    When I ask to remove products from basket:
      | 1 |
    Then The basket value should be 24.98 and should contain products:
      | 1 |
      | 2 |

  Scenario: Product is not removed from my basket, because my basket is completed.
    Given I'm logged in as "john" using password "pass" in brand 2.
    And My basket has products:
      | 1 |
      | 2 |
    And My basket has "COMPLETED" state.
    When I ask to remove products from basket:
      | 1 |
    Then The basket value should be 24.98 and should contain products:
      | 1 |
      | 2 |