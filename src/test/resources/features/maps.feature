Feature: Maps endpoint

  Background:
    Given there is a valid user
      | X-User-Email | testey@realxdata.com |
      | X-User-Token | gy3dPS5WaJ4N3CyPWUHG |

  Scenario: City View Options for Berlin
    When city view options is called for Berlin [city_id:11000000]
    Then the status code is 200

  Scenario: Assets for Berlin
    When assets is called for Berlin [city_id:11000000]
    Then the status code is 200
    And 293 assets are returned

  Scenario: Level 4 main maps for Berlin
    When the maps endpoint is called with params
      | city_id  | 11000000 |
      | level    | 4        |
      | map_type | MAIN     |
    Then the status code is 200
    And the response has 12 areas
    And the areas returned are
      | Mitte                      | 1100000001 |
      | Friedrichshain-Kreuzberg   | 1100000002 |
      | Pankow                     | 1100000003 |
      | Charlottenburg-Wilmersdorf | 1100000004 |
      | Spandau                    | 1100000005 |
      | Steglitz-Zehlendorf        | 1100000006 |
      | Tempelhof-Schöneberg       | 1100000007 |
      | Neukölln                   | 1100000008 |
      | Treptow-Köpenick           | 1100000009 |
      | Marzahn-Hellersdorf        | 1100000010 |
      | Lichtenberg                | 1100000011 |
      | Reinickendorf              | 1100000012 |


