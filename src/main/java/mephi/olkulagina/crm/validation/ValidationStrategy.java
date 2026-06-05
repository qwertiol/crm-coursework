package mephi.olkulagina.crm.validation;

public interface ValidationStrategy {
    boolean isValid(String value);
    String getErrorMessage();
}