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

### Update Version

Run

```
mvn -f src/backend/pom.xml versions:set -DartifactId=*  -DgroupId=*
```

## caching

Currently lookups are cached using redis. In the event of a code table change redis should be restarted. Check the api for activity beore doing so. 