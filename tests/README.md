# Test Automation

## Folder Structure

The folder structure for test automation will be as follows:

```
tests
├── src
│    └── main
|    └── test
|    |   └──resources
├── .gitignore
├── pom.xml
└── README.md
```

## To run locally

Docker-compose up and then run mvn command from the root

```bash
mvn verify -f tests/pom.xml
```

## To run with different auth providers

Tests can be run with BCEID and BC Services card on dev

Assign value to the "AUTH_PROVIDER" env variable as below

```bash
AUTH_PROVIDER:bceid
```
or 

```bash
AUTH_PROVIDER:bcsc
```

Running the tests create an html report [here](test-output/extent/HtmlReport/ExtentHtml.html)

## Cucumber Tags

We support running the tests with the current tags:

```bash
@frontend
```

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
### Plugins

Useful cucumber plugins to write feature files

* Cucumber for java from Jetbrains
* Gherkin from Jetbrains
