import React from "react";
import { FooterComponent, HeaderComponent } from "shared-components";
import { Button } from "../../base/button/Button";

export const generateUrl = () => {};

export default function App() {
  return (
    <div>
      <HeaderComponent header={{ name: "Sauce" }} />
      <p>eFiling Demo Client</p>
      <Button onClick={generateUrl} label="With CSO Account" />
      <br />
      <Button onClick={generateUrl} label="Without CSO Account" />
      <FooterComponent />
    </div>
  );
}
