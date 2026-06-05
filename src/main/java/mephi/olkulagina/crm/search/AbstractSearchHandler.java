package mephi.olkulagina.crm.search;

import mephi.olkulagina.crm.client.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

public abstract class AbstractSearchHandler implements SearchHandler {
    protected SearchHandler nextHandler;

    @Override
    public void setNext(SearchHandler next) {
        this.nextHandler = next;
    }

    @Override
    public Page<Client> handle(SearchRequest request, Pageable pageable) {
        if (canHandle(request)) {
            return doSearch(request, pageable);
        } else if (nextHandler != null) {
            return nextHandler.handle(request, pageable);
        } else {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
    }

    protected abstract boolean canHandle(SearchRequest request);
    protected abstract Page<Client> doSearch(SearchRequest request, Pageable pageable);
}