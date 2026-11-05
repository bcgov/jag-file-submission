import axios from "axios";
import FileSaver from "file-saver";

export const downloadPaymentReceipt = async (packageId) => {
  const response = await axios.get(
    `/filingpackages/${packageId}/paymentReceipt`,
    {
      responseType: "blob",
    }
  );
  const fileData = new Blob([response.data], { type: "application/pdf" });
  const fileUrl = URL.createObjectURL(fileData);
  FileSaver.saveAs(fileUrl, "PaymentReceipt.pdf");
};
