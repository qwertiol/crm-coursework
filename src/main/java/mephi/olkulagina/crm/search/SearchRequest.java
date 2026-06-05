package mephi.olkulagina.crm.search;

import java.util.List;

public class SearchRequest {
    private String nameQuery;
    private String companyQuery;
    private List<Long> statusIds;
    private String searchType;

    public SearchRequest() {
    }

    public SearchRequest(String nameQuery, String companyQuery, List<Long> statusIds, String searchType) {
        this.nameQuery = nameQuery;
        this.companyQuery = companyQuery;
        this.statusIds = statusIds;
        this.searchType = searchType;
    }

    public String getNameQuery() {
        return nameQuery;
    }

    public void setNameQuery(String nameQuery) {
        this.nameQuery = nameQuery;
    }

    public String getCompanyQuery() {
        return companyQuery;
    }

    public void setCompanyQuery(String companyQuery) {
        this.companyQuery = companyQuery;
    }

    public List<Long> getStatusIds() {
        return statusIds;
    }

    public void setStatusIds(List<Long> statusIds) {
        this.statusIds = statusIds;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }
}