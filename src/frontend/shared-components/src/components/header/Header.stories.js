import React from "react";
import { createMemoryHistory } from "history";
import mdx from "./Header.mdx";

import { Header } from "./Header";

export default {
  title: "Header",
  component: Header,
  parameters: {
    docs: {
      page: mdx
    }
  }
};

const history = createMemoryHistory();
history.entries[0].key = "testkey"; // set mock test key so storyshot is deterministic

const header = {
  name: "Header title",
  history
};

export const Basic = () => <Header header={header} />;

export const Mobile = () => <Header header={header} />;

Mobile.story = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};
