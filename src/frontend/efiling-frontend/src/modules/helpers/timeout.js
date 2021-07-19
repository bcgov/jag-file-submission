/**
 * A function to allow setTimeout to be used in a promise chain by resolving with a callback function (v)
 * @param t
 * @param v
 */
export const timeout = (t, v) =>
  new Promise((resolve) => {
    setTimeout(() => resolve(v), t);
  });
