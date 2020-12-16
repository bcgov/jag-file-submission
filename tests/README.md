# Test Automation

## Folder Structure

The folder structure for the automation will be as follows:

```
tests
├── src
│    └── main
|    |   └──pages
|    |   └──util
|    └── test
|    |   └──tests
|    |   └──resources
├── .gitignore
├── pom.xml
└── README.md
```

## To run locally

Set the following property values in the [local.properties file](src/test/resources/local.properties)

| Name                                     | Example Value              |
| ---------------------------------------- | -------------------------- |
| BCEID_USERNAME                           | Bceid Username             |
| BCEID_PASSWORD                           | Bceid Password             |
| KEYCLOAK_URL                             | Dev Keycloak Url           |
| EFILING_DEMO_KEYCLOAK_CREDENTIALS_SECRET | Efiling demo client secret |
| BASE_URI                                 | http://localhost:8080      |
| BROWSER                                  | chrome                     |

## Running the maven Tests without cucumber tags

[config.properties](src/test/resources/config.properties) are for CI/CD env.
[account-data.json](src/test/java/testdatasource/account-data.json) uses demo accounts, value can be changed.

```bash
cd tests
```

Run the tests with the local profile

```bash
mvn verify -Plocal
```

Running the tests create an html report [here](test-output/extent/HtmlReport/ExtentHtml.html)

## Cucumber Tags

We support running the tests with the current tags:

### Tags

Using tags will only run the specified tests.

```bash
cd tests
```

```bash
mvn verify -Dcucumber.options="--tags '@backend'"
```

```bash
mvn verify -Dcucumber.options="--tags '@frontend'"
```
