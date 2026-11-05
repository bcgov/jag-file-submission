import Dinero from "dinero.js";

/**
 * Formats the given current amount to be of the form $1,000.00
 * @param amount
 */
export const formatCurrency = (amount) =>
  Dinero({ amount: parseInt((amount * 100).toFixed(0), 10) }).toFormat(
    "$0,0.00"
  );
