/* eslint-disable react/function-component-definition */
import React from "react";
import PropTypes from "prop-types";
import Modal from "react-bootstrap/Modal";

import "./RushConfirmation.scss";

export default function RushConfirmation({ show, setShow, setShowRush }) {
  const handleOnHide = () => {
    setShow(false);
    setShowRush(true);
  };
  return (
    <Modal
      show={show}
      onHide={handleOnHide}
      className="safety-check-modal rush-modal"
      size="lg"
    >
      <Modal.Header closeButton className="border-0 modal-header">
        <Modal.Title>
          <h2>Rush Documents</h2>
        </Modal.Title>
      </Modal.Header>
      <Modal.Body className="modal-body">
        <p>
          An application made under Rule 8-5(1) of the Supreme Court Rules for
          short notice will be processed on an urgent(rush) basis. Prior to
          e-filing an application for short notice, review Practice Direction
          20, Short Notice Applications - Civil.{" "}
        </p>
        <br />

        <p>
          When directed by the court, an order will be processed on an
          urgent(rush) basis. The registry will consider the following reasons
          for processing an order on an urgent(rush) basis:{" "}
        </p>
        <ul>
          <li>the order must be served immediately, </li>
          <li>the order is necessary for immediate enforcement purposes, </li>
          <li>the order is a protection order or restraining order, or </li>
          <li>for any reason considered by the registry to be sufficient. </li>
        </ul>
        <br />

        <p>
          <b>
            Only request processing on an urgent(rush) basis in exceptional
            circumstances.{" "}
          </b>
        </p>
        <br />
        <p>
          For your information, the following types of orders are generally
          entered on an urgent(rush) basis:{" "}
        </p>
        <ul>
          <li>Restraining Orders</li>
          <li>Injunctions</li>
          <li>Transfer of Lands when completion date is imminent</li>
          <li>Custodial</li>
        </ul>
        <br />

        <p>
          The following types of orders are not generally entered on an
          urgent(rush) basis:{" "}
        </p>
        <ul>
          <li>Trial Orders</li>
          <li>Reserved Judgements</li>
          <li>Desk Orders that require the Courts&apos; Approval </li>
        </ul>
        <br />

        <p>
          The registry will determine whether to process a submission on an
          urgent(rush) basis after reviewing the reasons provided by you. The
          registry may contact you for further information.{" "}
        </p>
      </Modal.Body>
    </Modal>
  );
}

RushConfirmation.defaultProps = { setShowRush: () => {} };

RushConfirmation.propTypes = {
  show: PropTypes.bool.isRequired,
  setShow: PropTypes.func.isRequired,
  setShowRush: PropTypes.func,
};
