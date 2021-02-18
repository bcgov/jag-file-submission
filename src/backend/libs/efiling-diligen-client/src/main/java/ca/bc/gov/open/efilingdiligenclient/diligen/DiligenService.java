package ca.bc.gov.open.efilingdiligenclient.diligen;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public interface DiligenService {
    BigDecimal postDocument(String documentType, MultipartFile file);
}
