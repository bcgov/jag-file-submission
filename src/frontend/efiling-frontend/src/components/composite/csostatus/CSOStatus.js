import React from "react";

export default function CSOStatus({ accountExists }) {
  console.log(accountExists);
  return (
    <div>
      {accountExists && <p>Account exists! Proceed</p>}
      {!accountExists && <p>Account does not exist, form will be here</p>}
    </div>
  );
}
