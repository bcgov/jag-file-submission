/* eslint-disable import/no-named-as-default, import/no-named-as-default-member */
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

export const Default = function () {
  return <SubmissionHistory />;
};
