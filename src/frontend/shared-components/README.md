## Shared React Components

In order to use these shared react components locally, run:

```bash
yarn prepare
```

```bash
npm install -g linklocal
```

from the `shared-components` directory. Then, to include the component dependency and include it in your react app:

1. Add this to your `package.json` file:

```bash
"shared-components": "file:./../shared-components"
```

2. Then, from the root of your react app, run:

```bash
linklocal
```
