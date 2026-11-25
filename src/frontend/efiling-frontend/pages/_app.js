import "@bcgov/bootstrap-theme/dist/css/bootstrap-theme.min.css";
import "../src/index.css";
// Next only allows global CSS to be imported from _app, so every component
// stylesheet is listed here, in the order the old CRA bundle loaded them
// (cascade order matters for equal-specificity rules).
import "../src/domain/package/package-confirmation/RushConfirmation.scss";
import "../src/components/page/rush/Rush.scss";
import "../src/components/page/rush/rush-document-list/RushDocumentList.scss";
import "../src/components/input/Input.scss";
import "../src/domain/package/package-confirmation/PackageConfirmation.scss";
import "../src/components/register-payment-card/RegisterPaymentCard.scss";
import "../src/domain/payment/Payment.scss";
import "../src/components/page/upload/Upload.scss";
import "../src/domain/package/package-confirmation/FileList.scss";
import "../src/components/radio/Radio.scss";
import "../src/components/page/cso-account/CSOAccount.scss";
import "../src/components/page/page.css";
import "../src/components/safety-check/SafetyCheckHelp.scss";
import "../src/components/safety-check/SafetyCheck.scss";
import "../src/domain/package/package-review/PackageReview.scss";
import "../src/domain/package/package-review/DocumentList.scss";
import "../src/domain/package/package-review/tooltip/Tooltip.scss";
import "../src/domain/package/package-review/PartyList.scss";
import "../src/domain/package/package-review/PaymentList.scss";
import "../src/domain/submission/submission-history/SubmissionList.scss";
import "../src/domain/submission/submission-history/SubmissionHistory.scss";

export default function App({ Component, pageProps }) {
  return <Component {...pageProps} />;
}
