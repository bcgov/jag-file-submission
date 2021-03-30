import React from "react";
import axios from "axios";
import MockAdapter from "axios-mock-adapter";
import SubmissionHistory from "./SubmissionHistory";
import { submissions } from "../../../modules/test-data/submissionHistoryTestData";

export default {
  title: "SubmissionHistory",
  component: SubmissionHistory,
};

const mock = new MockAdapter(axios);

mock.onGet("/filingpackages").reply(200, submissions);

export const Default = () => <SubmissionHistory />;
