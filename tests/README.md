# Frontend Tests

## Folder Structure

The folder structure for the frontend automation will be as follows:

```
tests
├── src
│    └── main
|    |   └──config
|    |   └──pages
|    |   └──util
|    └── test
|    |   └──tests
|    |   └──resources
├── .gitignore
├── pom.xml
└── README.md
```

## Running the maven Tests without cucumber tags

[config.properties](src/main/java/ca/bc/gov/open/jagefilingapi/qa/config/config.properties) are pointing to docker, change it for other source.

[account-data.json](src/test/java/testdatasource/account-data.json) uses demo accounts, value can be changed.

```bash
cd tests
```

Run the tests

```bash
mvn clean verify
```

Running the tests create an html report [here](test-output/extent/HtmlReport/ExtentHtml.html)

## Cucumber Tags

We support running the tests with the current tags:

### @backend

This would only run backend tests.

```bash
cd tests
```

```bash
mvn verify -Dcucumber.options="--tags '@backend'"
```
