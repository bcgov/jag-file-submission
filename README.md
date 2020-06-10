# jag-file-submission
Generic File Submission API (to be used by the Family Law Act Application at first)

## Project Structure

    .
    ├── .github                             # Contains GitHub Related sources
    ├── openshift                           # openshift templates and pipeline
    ├── src/                                # application source files
    │   └── backend                         # backend applications
    │       └── jag-efiling-api             # efiling api 
    ├── COMPLIANCE.yaml                     # 
    ├── CONTRIBUTING.md                     # 
    ├── LICENSE                             # Apache License
    └── README.md                           # This file.


## Apps

| Name | description | doc |
| --- | --- | --- |
| jag-efiling-api | the main api for interating with the service | [README](src/backend/jag-efiling-api/README.md) |


## Frontend Folder Structure

The folder structure for the frontend react application will be as follows:

```
my-app
├── build
├── public
│   ├── favicon.ico
│   ├── index.html
│   └── manifest.json
├── src
├── .gitignore
├── package.json
└── README.md
```

- `build` is the location of the final, production-ready build.
- `public` is where the static files will reside.
- `src` is where the dynamic files will reside.

`src` will look something like this:

```
src
├── components
│   └── app
│   │   ├── app.css
│   │   ├── app.js
│   │   └── app.test.js
│   └── index.js
├── images
│   └── logo.svg
├── index.css
├── index.js
└── service-worker.js
```

All the react components will be found in the `components` directory. The `components/index.js` file will serve as a barrel through which all sibling components are exported.

Since we are using storybook and CDD, each component will be its own directory with the component code, styling, tests, as well as `.stories.js` file.

This is pretty much what `create react app` provides out of the box, except slightly modified and adjusted to better suit CDD and focusing on component-first design and development.


## Backend Folder Structure

The backend API will follow the standard Java Spring Boot MVC model for folder structure breakdown where there are `models` and `controllers`.
