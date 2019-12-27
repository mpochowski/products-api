Feature: The feature enables user to register, login and get their user details.

  Scenario: User can be registered using unique username within brand scope.
    Given I'm using brand with id 2.
    When I ask to register user with username "mark" and password "pass".
    Then The user should be successfully registered with username "mark".

  Scenario: User cannot be registered using already taken username within brand scope.
    Given I'm using brand with id 2.
    When I ask to register user with username "john" and password "pass".
    Then The user should not be registered.

  Scenario: User can login and see their details.
    Given I'm using brand with id 2.
    And I'm logged in as "john" using password "pass".
    When I ask to get my user details.
    Then The user should be returned with username "john".

  Scenario: Not authenticated user cannot see their details.
    Given I'm using brand with id 2.
    And I'm not logged in.
    When I ask to get my user details.
    Then There should be no user details returned.