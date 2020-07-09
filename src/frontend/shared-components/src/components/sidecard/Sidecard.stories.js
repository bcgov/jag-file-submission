import React from "react";
import { FaIdCard } from "react-icons/fa";
import { Sidecard } from "./Sidecard";

export default {
  title: "Sidecard",
  component: Sidecard,
  includeStories: []
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

export const WithIconMobile = () => (
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

WithIconMobile.story = mobileViewport;
