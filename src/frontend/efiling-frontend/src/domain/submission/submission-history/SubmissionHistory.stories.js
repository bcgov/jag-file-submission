import React from "react";
import axios from "axios";
import MockAdapter from "axios-mock-adapter";
import SubmissionHistory from "./SubmissionHistory";

export default {
  title: "SubmissionHistory",
  component: SubmissionHistory,
};

const mock = new MockAdapter(axios);

const submissions = [
  {
    packageNumber: 1234567,
    submittedDate: "04/30/1996",
    status: {
      description: "Approved",
    },
    court: {
      location: "Kelowna",
    },
  },
  {
    packageNumber: 7654321,
    submittedDate: "04/30/1996",
    status: {
      description: "Rejected",
    },
    court: {
      location: "Victoria",
    },
  },
];

mock.onGet("/filingpackages").reply(200, submissions);

export const Default = () => <SubmissionHistory />;
