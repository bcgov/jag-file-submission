Feature: User can navigate to the efiling landing page

  @frontend
  Scenario: Verify valid account guid redirects efiling form page
    Given user is on the landing page
    When  user enters invalid account guid "5EDDA22161C043AB9D508158BF4A7828"
    Then efiling page is displayed and cancel button exists
  @frontend
  Scenario: Verify non existing account guid redirects to the efiling form page
    Given user is on the landing page
    When  user enters invalid account guid "123"
    Then efiling page is displayed and cancel button exists
  @frontend
  Scenario: Verify existing account guid with conflicts returns error message
    Given user is on the landing page
    When  user enters invalid account guid "01E242F88A66475586C2F2B13C5445FA"
    Then error message is displayed
