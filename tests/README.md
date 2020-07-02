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

1. Add values to the keys in config.properties file
2. Add test data values to the json files in 'test/java/testdatasource' package

In the 'tests' directory, you can run:

### `mvn clean install`

Installs all the required mvn dependencies.

### `mvn test`

Runs the tests on the specified browser.

## Running the Cucumber Tests with tags

Add values to the keys in config.properties file
Update tags value in 'RunCucumberTest' class

In the 'tests' directory, you can run:

### `mvn test -Dcucumber.options="--tags '@tagname'"`

This runs the tests that are tagged with the specific tag name.
Without the 'tags' option all tests gets executed.

Alternatively, both frontend and backend tests can be run from
RunCucumberTest.java class

## Reports

Html reports will be saved in the test-output folder to view the test results in the browser

### `test/test-output/extent`
