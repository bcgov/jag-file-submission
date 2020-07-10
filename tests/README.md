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

[config.properties](tests\src\main\java\ca\bc\gov\open\jagefilingapi\qa\config\config.properties) are pointing to docker, change it for other source.

[account-data.json](tests\src\test\java\testdatasource\account-data.json) uses demo accounts, value can be changed.

```bash
cd tests
```

Run the tests

```bash
mvn clean verify
```

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
