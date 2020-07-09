/* eslint-disable react/jsx-one-expression-per-line */
import React from "react";
import PropTypes from "prop-types";

import "./Sidecard.css";

export const Sidecard = ({
  sideCard: { heading, content, type, image, imageLink, isWide, icon }
}) => {
  let sideCardCss = "dashboard-spacing";
  if (isWide) sideCardCss = "wide-dashboard-spacing";

  const sideCardTitleCss =
    heading.length > 30 ? "large-title" : "side-card-title";

  return (
    <div className={sideCardCss} style={{ position: "relative" }}>
      <div className="row">
        <div className="col-12 pt-2" style={{ position: "relative" }}>
          <p />
          {type === "grey" && (
            <section
              id="grey-section"
              className="submit-container"
              style={{
                backgroundColor: "#F2F2F2",
                color: "#000",
                border: "none"
              }}
            >
              <h2 style={{ color: "#000" }}>{heading}</h2>
              <div className="submit-content">
                <span>{content}</span>
              </div>
            </section>
          )}
          {type === "blue" && (
            <section id="blue-section" className="submit-container">
              <h2 className="heading-style">{heading}</h2>
              <div className="submit-content">
                <span>{content}</span>
                {image && imageLink && (
                  <a href={imageLink} target="_blank" rel="noopener noreferrer">
                    <img
                      src={image}
                      alt="imagelink"
                      height="65px"
                      width="310px"
                    />
                  </a>
                )}
              </div>
            </section>
          )}
          {type === "bluegrey" && (
            <section id="bluegrey-section" className="bluegrey-container">
              <div className="container-background bluegrey-heading">
                {!icon && <h2 className="heading-style">{heading}</h2>}
                {icon && (
                  <h2 className="heading-style">
                    <div className="row">
                      <div className="side-card-row">
                        <div className="round-icon-wrapper">{icon}</div>
                        <div className={sideCardTitleCss}>{heading}</div>
                      </div>
                    </div>
                  </h2>
                )}
              </div>
              <div className="bluegrey-content">
                <span className="content-style">{content}</span>
              </div>
            </section>
          )}
        </div>
      </div>
    </div>
  );
};

Sidecard.propTypes = {
  sideCard: PropTypes.shape({
    heading: PropTypes.string.isRequired,
    content: PropTypes.array.isRequired,
    type: PropTypes.string.isRequired,
    image: PropTypes.string,
    imageLink: PropTypes.string,
    isWide: PropTypes.bool,
    icon: PropTypes.element
  }).isRequired
};
