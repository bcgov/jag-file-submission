import React from "react";
import { Alert } from "shared-components";
import { MdError } from "react-icons/md";

export const rejectedDocumentsAlert = (
  <Alert
    icon={<MdError size={32} />}
    type="success"
    styling="bcgov-success-background bcgov-no-padding-bottom rejectedMsg"
    element={
      <p>
        Your rejected document(s) will be <b>resubmitted</b>, and will be
        reviewed by the registry.
      </p>
    }
  />
);
