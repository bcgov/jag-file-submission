import React from "react";
import testBasicSnapshot from "../../TestHelper";
import { Sidecard } from "./Sidecard";

describe("Sidecard Component", () => {
  const sideCard = {
    heading: "header",
    content: ["content"],
    type: "grey"
  };

  test("Matches the grey card snapshot", () => {
    testBasicSnapshot(<Sidecard sideCard={sideCard} />);
  });

  test("Matches the blue card snapshot without image", () => {
    testBasicSnapshot(<Sidecard sideCard={{ ...sideCard, type: "blue" }} />);
  });

  test("Matches the blue card snapshot with image", () => {
    testBasicSnapshot(
      <Sidecard
        sideCard={{
          ...sideCard,
          type: "blue",
          image: "image",
          imageLink: "imageLink"
        }}
      />
    );
  });

  test("Matches the bluegrey card snapshot", () => {
    testBasicSnapshot(
      <Sidecard sideCard={{ ...sideCard, type: "bluegrey" }} />
    );
  });

  test("Matches the snapshot when it is wide", () => {
    testBasicSnapshot(<Sidecard sideCard={{ ...sideCard, isWide: true }} />);
  });

  test("Matches the snapshot when heading is long", () => {
    testBasicSnapshot(
      <Sidecard
        sideCard={{
          ...sideCard,
          heading:
            "heading is quite long so we can use custom styling to see it render"
        }}
      />
    );
  });
});
