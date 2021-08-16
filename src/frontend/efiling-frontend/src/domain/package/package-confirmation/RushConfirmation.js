import React from "react";
import {ConfirmationPopup} from "shared-components";

export default function RushConfirmation({setShow}) {
  const modal = {};
  const mainButton = <></>;
  const confirmButton = <></>;
  const cancelButton = <></>;

  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);
  const handleConfirm = () => {};

  const confirmationPopup = {
    modal,
    mainButton: { ...mainButton, onClick: handleShow },
    confirmButton: { ...confirmButton, onClick: handleConfirm },
    cancelButton: { ...cancelButton, onClick: handleClose }
  }

  return (
    <ConfirmationPopup modal={modal} mainButton={mainButton} confirmButton={confirmButton} cancelButton={cancelButton} />
  );

}