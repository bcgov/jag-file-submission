## eFiling Demo

Welcome to the eFiling demo!

## Running the Frontend

### Running against local dockerized backend

In the project directory `efiling-demo`:

Create a `.env.development` file and populate the environment variables as
shown in the `.env.example` file.

To bring up all the backend docker containers locally, in the root directory
`jag-file-submission`, run:

```bash
docker-compose up efiling-api efiling-frontend keycloak keycloak-config redis
```

Continue below with `yarn install`

### Running the Frontend against the dockerized containers on DEV

It's possible to run the demo application locally, but use keycloak, the api,
and the other backend containers running on DEV (dev.justice.gov.bc.ca).

In the project directory `efiling-demo`:

Create a `.env.development` file and populate the environment variables as
shown in the `.env.example` with values from OpenShift.

In the project directory `efiling-demo` open a new terminal and run:

```bash
yarn install
```

Installs all the required dependencies to get the application up and running.

```bash
yarn start
```

Runs the app in the development mode.<br />
Open [http://localhost:3001](http://localhost:3001) to view it in the browser.

The page will reload if you make edits.<br />
You will also see any lint errors in the console.

```bash
yarn run build
```

Builds the app for production to the `build` folder.<br />
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.<br />
Your app is ready to be deployed!

```bash
yarn run eject
```

**Note: this is a one-way operation. Once you `eject`, you can’t go back!**

If you aren’t satisfied with the build tool and configuration choices, you can `eject` at any time. This command will remove the single build dependency from your project.

Instead, it will copy all the configuration files and the transitive dependencies (Webpack, Babel, ESLint, etc) right into your project so you have full control over them. All of the commands except `eject` will still work, but they will point to the copied scripts so you can tweak them. At this point you’re on your own.

You don’t have to ever use `eject`. The curated feature set is suitable for small and middle deployments, and you shouldn’t feel obligated to use this feature. However we understand that this tool wouldn’t be useful if you couldn’t customize it when you are ready for it.

## ESLint and Prettier

This project uses ESLint and Prettier to ensure that the code being written follows standard guidelines and standards, and that the code styling is kept consistent throughout the application.

```bash
npx eslint .
```

Runs the linter on the entire frontend codebase and reports any errors or warnings that may be present.

```bash
prettier [opts] [filename ...]
```

Runs prettier and formats your file. This has been setup by Husky to run on every commit, so prettier will check all the files within the directory and format them on every commit.

## Storybook

This project uses component-driven development and storybook in order to create stories for frontend components. In order to run the storybook locally, you can run:

```bash
yarn run storybook
```

Storybook should start, on a random open port in dev-mode. Now you can develop your components and write stories and see the changes in Storybook immediately since it uses Webpack’s hot module reloading.<br />

Open [http://localhost:9009](http://localhost:9009) to view it in the browser.

## Jest

This project uses Jest for snapshot component testing. Snapshot tests are a very useful tool whenever you want to make sure your UI does not change unexpectedly. A typical snapshot test case renders a UI component, takes a snapshot, then compares it to a reference snapshot file stored alongside the test. The test will fail if the two snapshots do not match: either the change is unexpected, or the reference snapshot needs to be updated to the new version of the UI component.

```bash
yarn run test
```

Launches the test runner in the interactive watch mode.
