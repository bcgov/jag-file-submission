# jag-file-submission backend

A Collection of server side service to facilitate file upload

## build

### Profile all

this profile will build all projects (apps + libs)

```bash
mvn clean install -P all
```

### Profile libs

this profile will build only library modules

```bash
mvn clean install -P libs
```

## test

```
mvn clean verify -P all
```

to check code coverage open the following [jacoco coverage report page](coverage-report/target/site/jacoco-aggregate/index/html)

