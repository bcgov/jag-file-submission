package ca.bc.gov.open.jag.efilingreviewerapi.document.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WebHookServiceImpl implements WebHookService{

    @Override
    public void sendDocumentReady(BigDecimal documentId, String documentType) {

    }

}
