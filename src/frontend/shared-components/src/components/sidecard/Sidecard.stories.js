import React from "react";
import { FaIdCard } from "react-icons/fa";
import mdx from "./Sidecard.mdx";

import { Sidecard } from "./Sidecard";

export default {
  title: "Sidecard",
  component: Sidecard,
  parameters: {
    docs: {
      page: mdx
    }
  }
};

const sideCard = {
  heading: "Heading",
  content: ["This is where I put my content for the sidecard"],
  type: "blue"
};

export const Blue = () => <Sidecard sideCard={sideCard} />;

export const Grey = () => <Sidecard sideCard={{ ...sideCard, type: "grey" }} />;

export const BlueGrey = () => (
  <Sidecard sideCard={{ ...sideCard, type: "bluegrey" }} />
);

export const WithIcon = () => (
  <Sidecard
    sideCard={{
      ...sideCard,
      type: "bluegrey",
      icon: <FaIdCard className="side-card-icon" />
    }}
  />
);

export const Mobile = () => (
  <Sidecard
    sideCard={{
      ...sideCard,
      type: "bluegrey",
      icon: <FaIdCard className="side-card-icon" />
    }}
  />
);

const mobileViewport = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};

Mobile.story = mobileViewport;
