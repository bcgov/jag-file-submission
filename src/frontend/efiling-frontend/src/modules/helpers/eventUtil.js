/** Returns true if the param is a click event. */
export const isClick = (e) => e && e.type === "click";

/** Returns true if the param is a keydown event and Enter(13) has been pressed. */
export const isEnter = (e) => e && e.type === "keydown" && e.keyCode === 13;
