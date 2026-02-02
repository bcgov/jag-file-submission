import axios from "axios";


/**
 * Request data required to create/update a payment profile
 */
export interface SetupCardRequest {
    /** Name on card */
    name: string;
    /** Tokenized credit card as returned by Worldline */
    tokenCode: string;
}

/**
 * Creates a Worldline (formerly Bambora) payment profile for the given clientId.
 */
export const createPaymentProfile = (clientId: string, data: SetupCardRequest) =>
    axios.post(`/payment/${clientId}/profile`, data);

/**
 * Updates a Worldline (formerly Bambora) payment profile for the given clientId and paymentProfileId.
 */
export const updatePaymentProfile = (clientId: string, paymentProfileId: string, data: SetupCardRequest) =>
    axios.put(`/payment/${clientId}/profile/${paymentProfileId}`, data);
