package mephi.olkulagina.crm.search;

import java.util.ArrayList;
import java.util.List;

public class SearchRequest {

    private String nameQuery;
    private String companyQuery;
    private List<Long> statusIds;
    private String searchType;

    private SearchRequest() {
        this.statusIds = new ArrayList<>();
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getNameQuery() {
        return nameQuery;
    }

    public String getCompanyQuery() {
        return companyQuery;
    }

    public List<Long> getStatusIds() {
        return statusIds != null ? List.copyOf(statusIds) : List.of();
    }

    public String getSearchType() {
        return searchType;
    }

    public static class Builder {
        private final SearchRequest request;

        private Builder() {
            this.request = new SearchRequest();
        }

        public Builder nameQuery(String nameQuery) {
            request.nameQuery = nameQuery;
            return this;
        }

        public Builder companyQuery(String companyQuery) {
            request.companyQuery = companyQuery;
            return this;
        }

        public Builder statusIds(List<Long> statusIds) {
            request.statusIds = statusIds != null ? new ArrayList<>(statusIds) : new ArrayList<>();
            return this;
        }

        public Builder searchType(String searchType) {
            request.searchType = searchType;
            return this;
        }

        public SearchRequest build() {
            return request;
        }
    }
}