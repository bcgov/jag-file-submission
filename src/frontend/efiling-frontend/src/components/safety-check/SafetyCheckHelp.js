/* eslint-disable react/function-component-definition */
import React from "react";
import PropTypes from "prop-types";
import Modal from "react-bootstrap/Modal";
import { Button } from "shared-components";

import "./SafetyCheckHelp.scss";

/** Opens up a modal popup with help content */
export default function SafetyCheckHelp({ showModal, onHide }) {
  return (
    <Modal show={showModal} onHide={onHide} className="safety-check-modal">
      <Modal.Header closeButton>
        <Modal.Title>
          <h3 className="text-primary" data-testid="help-title">
            Get Help Opening and Saving PDF forms
          </h3>
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <h3>Your application</h3>
        <p>
          You can review, print, or save your application by clicking on the
          document name. This will open or download a PDF version of the
          application.
        </p>
        <p>
          &quot;PDF&quot; stand for Portable Document Format, which is a file
          format created by Adobe and is a standard format for printed
          documents. A PDF retains the design and layout of the document so it
          appears the same wherever it is viewed or printed.
        </p>
        <h3>Open the form in your browser</h3>
        <p>
          Your browser (e.g. Internet Explorer, Edge, Google Chrome, Firefox, or
          Safari) may have a built-in PDF viewer. If so, you can open the forms
          directly on the web.
        </p>
        <ol>
          <li>
            Click on the &quot;Review and Print&quot; button to open the PDF
            form in your browser window.
          </li>
          <li>
            Depending on your browser settings, your PDF might save the form to
            your computer (see below) or it will open in a new tab or window.
          </li>
          <li>
            To print, click on the printer icon at the top of the document (it
            might be on the left or the right side).
          </li>
          <li>
            To save the file to your computer, click on the save icon (disk) or
            download icon (downward arrow) at the top of the document.
          </li>
        </ol>
        <p>
          If you are having trouble viewing PDFs, you may need to adjust your
          browser settings. See{" "}
          <a
            href="https://helpx.adobe.com/acrobat/using/display-pdf-in-browser.html"
            target="_blank"
            rel="noopener noreferrer"
          >
            Adobe&apos;s instructions for displaying PDFs
          </a>{" "}
          in various browsers for detailed instructions.
        </p>
        <h3>Save the forms to your computer</h3>
        <p>
          It&apos;s a great idea to save the PDF&apos;s of your forms to your
          computer so you have a backup, and can print it later. You must have
          Adobe Reader installed (or Preview on Mac), otherwise, you can{" "}
          <a
            href="https://get.adobe.com/reader/"
            target="_blank"
            rel="noopener noreferrer"
          >
            download it here
          </a>
          .
        </p>
        <ol>
          <li>
            Right-click on the &quot;Review and Print&quot; button next to the
            name of the document.
          </li>
          <li>
            Select &quot;Save Target As&quot; or &quot;Save Link As&quot;.
          </li>
          <li>
            Choose a folder to keep your forms in (for example
            &quot;Documents&quot;). You may want to make a note of where you
            saved it.
          </li>
          <li>
            Open Adobe Reader: When Adobe Reader is open, go to File, then to
            Open, then to where you saved the document. Double-click on the
            document to open it.
            <br />
            OR
            <br />
            Find the file: On your computer, find where you saved the document
            and double-click it.
          </li>
          <li>View and/or print your form.</li>
        </ol>
        <h3>Download the form through your browser</h3>
        <p>
          Depending on your browser settings, your PDF forms may download
          automatically.
        </p>
        <ol>
          <li>
            If asked, choose a folder to keep your forms in (for example
            &quot;Documents&quot;). You may want to make a note of where you
            saved it.
          </li>
          <li>
            Open Adobe Reader (or Preview on Mac). If you need to, you can{" "}
            <a
              href="https://get.adobe.com/reader/"
              target="_blank"
              rel="noopener noreferrer"
            >
              download it here
            </a>
            .
          </li>
          <li>
            When Adobe Reader is open, go to File, then to Open, then go to your
            &quot;Downloads&quot; folder (or to where you saved the document).
          </li>
          <li>View and/or print your form.</li>
        </ol>
        <h3>Remove Downloaded Documents from your computer</h3>
        <ol>
          <li>Navigate to the search bar next to the Windows Start Menu.</li>
          <li>Enter &quot;File Explorer&quot; and select File Explorer.</li>
          <li>
            Select the <span className="font-weight-bold">Downloads</span>{" "}
            folder on the left side of the window.
          </li>
          <li>
            Select the document{" "}
            <span className="font-weight-bold">in the Downloads</span> folder.
          </li>
          <li>
            Right-click the selected file and select{" "}
            <span className="font-weight-bold">Delete</span>.
          </li>
          <li>
            <span className="font-weight-bold">
              Navigate to your Recycle Bin
            </span>{" "}
            and empty the recycle bin.
          </li>
        </ol>
        <h3>Delete saved pdf document from your computer</h3>
        <ol>
          <li>
            Navigate to the <span className="font-weight-bold">file</span> you
            want to <span className="font-weight-bold">delete</span> from the
            computer.
          </li>
          <li>
            Right-click on the <span className="font-weight-bold">file</span> to
            bring up the contextual menu and select{" "}
            <span className="font-weight-bold">&quot;Delete&quot;</span>.
          </li>
          <li>
            Go to the desktop and double-click on the Recycle Bin to open it.
          </li>
          <li>
            Click on the <span className="font-weight-bold">file</span>, press{" "}
            <span className="font-weight-bold">&quot;Delete&quot;</span> and
            click &quot;Yes&quot; to permanently{" "}
            <span className="font-weight-bold">delete</span> that one{" "}
            <span className="font-weight-bold">file</span>.
          </li>
        </ol>
        <h3>Clear Browser History from your computer</h3>
        <p>
          All web browsers remember a list of the web pages you&apos;ve visited.
          You can delete this list at any time, clearing your browsing history
          and erasing the tracks stored on your computer, smartphone, or tablet.
          Each browser has its own separate history, so you&apos;ll need to
          clear the history in multiple places if you&apos;ve used more than one
          browser.
        </p>
        <Button
          onClick={onHide}
          label="Close"
          styling="btn btn-primary pull-right"
          testId="close-btn"
        />
      </Modal.Body>
    </Modal>
  );
}

SafetyCheckHelp.propTypes = {
  showModal: PropTypes.bool.isRequired,
  onHide: PropTypes.func.isRequired,
};
