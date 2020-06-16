## Frontend Tests 

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

## Running the Tests
Add values to the keys in config.properties file

In the 'tests' directory, you can run:

### `mvn clean install`

Installs all the required mvn dependencies.

### `mvn test`

Runs the tests on the specified browser.
