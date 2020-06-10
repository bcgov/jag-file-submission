## eFiling Frontend

Welcome to the eFiling frontend!

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

## Running the Frontend

In the project directory (`efiling-frontend`), you can run:

### `npm install`

Installs all the required dependencies to get the application up and running.

### `npm start`

Runs the app in the development mode.<br />
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

The page will reload if you make edits.<br />
You will also see any lint errors in the console.

### `npm run build`

Builds the app for production to the `build` folder.<br />
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.<br />
Your app is ready to be deployed!

### `npm run eject`

**Note: this is a one-way operation. Once you `eject`, you can’t go back!**

If you aren’t satisfied with the build tool and configuration choices, you can `eject` at any time. This command will remove the single build dependency from your project.

Instead, it will copy all the configuration files and the transitive dependencies (Webpack, Babel, ESLint, etc) right into your project so you have full control over them. All of the commands except `eject` will still work, but they will point to the copied scripts so you can tweak them. At this point you’re on your own.

You don’t have to ever use `eject`. The curated feature set is suitable for small and middle deployments, and you shouldn’t feel obligated to use this feature. However we understand that this tool wouldn’t be useful if you couldn’t customize it when you are ready for it.

## ESLint and Prettier

This project uses ESLint and Prettier to ensure that the code being written follows standard guidelines and standards, and that the code styling is kept consistent throughout the application.

### `npx eslint .`

Runs the linter on the entire frontend codebase and reports any errors or warnings that may be present.

### `prettier [opts] [filename ...]`

Runs prettier and formats your file. This has been setup by Husky to run on every commit, so prettier will check all the files within the directory and format them on every commit.

## Storybook

This project uses component-driven development and storybook in order to create stories for frontend components. In order to run the storybook locally, you can run:

### `npm run storybook`

Storybook should start, on a random open port in dev-mode. Now you can develop your components and write stories and see the changes in Storybook immediately since it uses Webpack’s hot module reloading.<br />

Open [http://localhost:9009](http://localhost:9009) to view it in the browser.

## Jest

This project uses Jest for snapshot component testing. Snapshot tests are a very useful tool whenever you want to make sure your UI does not change unexpectedly. A typical snapshot test case renders a UI component, takes a snapshot, then compares it to a reference snapshot file stored alongside the test. The test will fail if the two snapshots do not match: either the change is unexpected, or the reference snapshot needs to be updated to the new version of the UI component.

### `npm run test`

Launches the test runner in the interactive watch mode.
