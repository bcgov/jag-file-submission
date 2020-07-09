import React from "react";
import mdx from "./Footer.mdx";

import { Footer } from "./Footer";

export default {
  title: "Footer",
  component: Footer,
  parameters: {
    docs: {
      page: mdx
    }
  }
};

export const Basic = () => <Footer />;

export const Mobile = () => <Footer />;

Mobile.story = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};
