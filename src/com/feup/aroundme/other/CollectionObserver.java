package com.feup.aroundme.other;

import java.util.Collection;

public interface CollectionObserver<E> {

    public void beforeAdd(E o);

    public void afterAdd(E o);
    


}

