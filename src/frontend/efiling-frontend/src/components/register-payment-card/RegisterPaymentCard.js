import React, { useEffect, useRef, useState } from "react";
import PropTypes from "prop-types";
import "./RegisterPaymentCard.scss";
import Modal from "react-bootstrap/Modal";
import { BsExclamationTriangle } from "react-icons/bs";

/** Component Modal for registering a payment card */
export default function RegisterPaymentCard({ showModal, onHide }) {
    const cardNumberId = "rpb-card-number";
    const cardCvvId = "rpb-card-cvv";
    const cardExpiryId = "rpb-card-expiry";
    const customCheckoutRef = useRef(null);
    const isCardNumberComplete = useRef(false);
    const isCVVComplete = useRef(false);
    const isExpiryComplete = useRef(false);
    const mountedRef = useRef(false);

    const [payEnabled, setPayEnabled] = useState(false);
    const [worldlineFieldsReady, setWorldlineFieldsReady] = useState(false);
    const [cardName, setCardName] = useState("");
    const [cardNameError, setCardNameError] = useState("");
    const [feedbackMessage, setFeedbackMessage] = useState("");
    const [feedbackIsError, setFeedbackIsError] = useState(false);

    useEffect(() => {
        if (!showModal) return;
        mountedRef.current = true;

        if (!window.customcheckout) {
            console.error("Bambora customcheckout script not loaded");
            return;
        }

        const cc = window.customcheckout();
        customCheckoutRef.current = cc;

        // create & mount inputs
        try {
            const fieldStyle = {
                base: {
                    fontFamily: "\"BCSans\", Arial, sans-serif",
                    fontSize: "14px",
                    color: "#313132",
                },
            };

            cc.create("card-number", { placeholder: "Card number", style: fieldStyle }).mount(`#${cardNumberId}`);
            cc.create("cvv", { placeholder: "CVV", style: fieldStyle }).mount(`#${cardCvvId}`);
            cc.create("expiry", { placeholder: "MM / YY", style: fieldStyle }).mount(`#${cardExpiryId}`);

            cc.on("brand", (event) => {
                const el = document.getElementById(cardNumberId);
                if (el) {
                    let cardLogo = "none";
                    if (event.brand && event.brand !== "unknown") {
                        const filePath = `https://cdn.na.bambora.com/downloads/images/cards/${event.brand}.svg`;
                        cardLogo = `url(${filePath})`;
                    }
                    el.style.backgroundImage = cardLogo;
                }
            });

            cc.on("empty", (event) => {
                if (event.empty) {
                    if (event.field === "card-number") isCardNumberComplete.current = false;
                    if (event.field === "cvv") isCVVComplete.current = false;
                    if (event.field === "expiry") isExpiryComplete.current = false;
                    setWorldlineFieldsReady(false);
                }
            });

            cc.on("complete", (event) => {
                if (event.field === "card-number") {
                    isCardNumberComplete.current = true;
                    setErrorForId(cardNumberId, "");
                }
                if (event.field === "cvv") {
                    isCVVComplete.current = true;
                    setErrorForId(cardCvvId, "");
                }
                if (event.field === "expiry") {
                    isExpiryComplete.current = true;
                    setErrorForId(cardExpiryId, "");
                }

                setWorldlineFieldsReady(isCardNumberComplete.current && isCVVComplete.current && isExpiryComplete.current);
            });

            cc.on("error", (event) => {
                if (event.field === "card-number") {
                    isCardNumberComplete.current = false;
                    setErrorForId(cardNumberId, event.message);
                }
                if (event.field === "cvv") {
                    isCVVComplete.current = false;
                    setErrorForId(cardCvvId, event.message);
                }
                if (event.field === "expiry") {
                    isExpiryComplete.current = false;
                    setErrorForId(cardExpiryId, event.message);
                }
                setWorldlineFieldsReady(false);
            });
        } catch (e) {
            console.error("Failed to initialize Bambora inputs", e);
        }

        return () => {
            mountedRef.current = false;
            // cleanup DOM mounts
            const els = [cardNumberId, cardCvvId, cardExpiryId].map((id) => document.getElementById(id));
            els.forEach((el) => { if (el) el.innerHTML = ""; });
            customCheckoutRef.current = null;
            isCardNumberComplete.current = false;
            isCVVComplete.current = false;
            isExpiryComplete.current = false;
            setWorldlineFieldsReady(false);
            setCardName("");
            setCardNameError("");
            setFeedbackMessage("");
            setFeedbackIsError(false);
        };
    }, [showModal]);

    useEffect(() => {
        const isNameValid = cardName.trim().length > 0 && !cardNameError;
        setPayEnabled(worldlineFieldsReady && isNameValid);
    }, [worldlineFieldsReady, cardName, cardNameError]);

    function setErrorForId(id, message) {
        const label = document.getElementById(`${id}-error`);
        if (label) label.innerText = message || "";
    }

    const onSubmit = (e) => {
        e.preventDefault();
        if (!customCheckoutRef.current) return;
        if (!cardName.trim()) {
            setCardNameError("Name on Card is required.");
            return;
        }
        setPayEnabled(false);

        customCheckoutRef.current.createToken((result) => {            
            if (result.error) {
                const err = result.error;
                const msg = typeof err === "string" ? err : JSON.stringify(err);
                console.error("Bambora tokenization error:", msg);
                setFeedbackMessage(`Error connecting to payment gateway`);
                setFeedbackIsError(true);
                setPayEnabled(true);
            } else {
                // token created - in real flow send token to backend to register card
                console.log("Bambora token:", result.token);
                setFeedbackMessage("");
                setFeedbackIsError(false);
                onHide();
            }
        });
    };

    return (
        <Modal show={showModal} onHide={onHide} className="register-payment-card-modal">
            <Modal.Header closeButton>
                <div role="heading" aria-level="3" className="h3 text-primary" data-testid="help-title">
                    Register a Credit Card
                </div>
            </Modal.Header>
            <Modal.Body>
                <div className="container">
                    <form id="checkout-form" onSubmit={onSubmit}>
                        <div className="row">
                            <div className="form-group col-xs-6 has-feedback">
                                <input
                                    type="text"
                                    className="form-control"
                                    id="card-name"
                                    name="cardName"
                                    placeholder="Name on Card"
                                    autoComplete="cc-name"
                                    value={cardName}
                                    onChange={(e) => {
                                        const nextValue = e.target.value;
                                        setCardName(nextValue);
                                        if (nextValue.trim()) {
                                            setCardNameError("");
                                        }
                                    }}
                                    onBlur={() => {
                                        if (!cardName.trim()) {
                                            setCardNameError("Name on Card is required.");
                                        }
                                    }}
                                />
                                <label className="help-block pl-1" htmlFor="card-name">
                                    {cardNameError}
                                </label>
                            </div>
                        </div>
                        <div className="row">
                            <div className="form-group col-xs-6 has-feedback" id="card-number-bootstrap">
                                <div id={cardNumberId} className="form-control" />
                                <label className="help-block pl-1" htmlFor="card-number" id={`${cardNumberId}-error`} />
                            </div>
                        </div>
                        <div className="row">
                            <div className="form-group col-xs-2 has-feedback" id="card-cvv-bootstrap">
                                <div id={cardCvvId} className="form-control" />
                                <label className="help-block pl-1" htmlFor="card-cvv" id={`${cardCvvId}-error`} />
                            </div>
                        </div>
                        <div className="row">
                            <div className="form-group col-xs-2 has-feedback" id="card-expiry-bootstrap">
                                <div id={cardExpiryId} className="form-control" />
                                <label className="help-block pl-1" htmlFor="card-expiry" id={`${cardExpiryId}-error`} />
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-xs-2 text-center">
                                <button id="pay-button" type="submit" className="btn btn-primary" disabled={!payEnabled}>
                                    Register Card
                                </button>
                            </div>
                        </div>
                        {feedbackMessage && (
                            <div className="row">
                                <div className="col-xs-2">
                                    <div className={`rpb-feedback ${feedbackIsError ? "is-error" : ""}`}>
                                        {feedbackIsError && (
                                            <BsExclamationTriangle className="rpb-feedback__icon" aria-hidden />
                                        )}
                                        <span className="rpb-feedback__text">{feedbackMessage}</span>
                                    </div>
                                </div>
                            </div>
                        )}
                    </form>
                </div>
            </Modal.Body>
        </Modal>
    );
}

RegisterPaymentCard.propTypes = {
    showModal: PropTypes.bool.isRequired,
    onHide: PropTypes.func.isRequired,
};
