package ca.bc.gov.open.jag.efilingapi.document;

public interface DocumentStore {

    byte[] put(String compositeId, byte[] content);

    byte[] get(String compositeId);

}