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
export const createPaymentProfile = (clientId: string, data: SetupCardRequest) => {
    const encodedClientId = encodeURIComponent(clientId ?? "");
    return axios.post(`/payment/${encodedClientId}/profile`, data);
};

/**
 * Updates a Worldline (formerly Bambora) payment profile for the given clientId and paymentProfileId.
 */
export const updatePaymentProfile = (clientId: string, paymentProfileId: string, data: SetupCardRequest) => {
    const encodedClientId = encodeURIComponent(clientId ?? "");
    const encodedPaymentProfileId = encodeURIComponent(paymentProfileId ?? "");
    return axios.put(`/payment/${encodedClientId}/profile/${encodedPaymentProfileId}`, data);
};
