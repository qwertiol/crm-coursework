package mephi.olkulagina.crm.company.lookup;

import mephi.olkulagina.crm.company.Company;

import java.util.List;

public interface CompanyLookupService {
    List<Company> findAll();
    List<Company> findMatching(String query);
}