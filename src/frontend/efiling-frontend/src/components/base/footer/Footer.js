import React from "react";

export default function Footer() {
  return (
    <footer className="footer">
      <nav className="navbar navbar-expand-sm navbar-dark justify-content-end">
        <button
          className="navbar-toggler"
          type="button"
          data-toggle="collapse"
          data-target="#footerBar"
        >
          <span className="navbar-toggler-icon" />
        </button>
        <div className="collapse navbar-collapse flex-grow-0" id="footerBar">
          <ul className="navbar-nav text-right">
            <li className="nav-item m-1">
              <a
                className="nav-link"
                href="https://www2.gov.bc.ca"
                target="_blank"
                rel="noopener noreferrer"
              >
                BC Government
              </a>
            </li>
            <li className="nav-item m-1">
              <a
                className="nav-link"
                href="https://www2.gov.bc.ca/gov/content/home/disclaimer"
                target="_blank"
                rel="noopener noreferrer"
              >
                Disclaimer
              </a>
            </li>
            <li className="nav-item m-1">
              <a
                className="nav-link"
                href="https://www2.gov.bc.ca/gov/content/home/privacy"
                target="_blank"
                rel="noopener noreferrer"
              >
                Privacy
              </a>
            </li>
            <li className="nav-item m-1">
              <a
                className="nav-link"
                href="https://www2.gov.bc.ca/gov/content/home/accessibility"
                target="_blank"
                rel="noopener noreferrer"
              >
                Accessibility
              </a>
            </li>
            <li className="nav-item m-1">
              <a
                className="nav-link"
                href="https://www2.gov.bc.ca/gov/content/home/copyright"
                target="_blank"
                rel="noopener noreferrer"
              >
                Copyright
              </a>
            </li>
          </ul>
        </div>
      </nav>
    </footer>
  );
}
