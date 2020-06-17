import React from "react";
import axios from "axios";
import { Button } from "../../base/button/Button";

const generateUrlBody = {
  documentProperties: {
    type: "string",
    subType: "string",
    submissionAccess: {
      url: "string",
      verb: "GET",
      headers: {
        additionalProp1: "string",
        additionalProp2: "string",
        additionalProp3: "string"
      }
    }
  },
  navigation: {
    success: {
      url: "string"
    },
    error: {
      url: "string"
    },
    cancel: {
      url: "string"
    }
  }
};

export const generateUrl = () => {
  axios
    .post(`/submission/generateUrl`, generateUrlBody)
    .then(response => {
      console.log(response);
    })
    .catch(() => {
      throw new Error(
        "An error occurred with generating the url. Please try again."
      );
    });
};

export default function App() {
  return (
    <div>
      <p>eFiling Demo Client</p>
      <Button onClick={generateUrl} label="With CSO Account" />
      <br />
      <Button onClick={generateUrl} label="Without CSO Account" />
    </div>
  );
}
