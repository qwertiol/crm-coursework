package mephi.olkulagina.crm.search;

import mephi.olkulagina.crm.client.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchHandler {
    Page<Client> handle(SearchRequest request, Pageable pageable);
    void setNext(SearchHandler next);
}