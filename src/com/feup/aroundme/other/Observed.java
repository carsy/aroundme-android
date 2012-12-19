package com.feup.aroundme.other;

import java.util.Collection;
import java.util.Iterator;

public class Observed {

	//this method would go in a utility class
    public static <E> Collection<E> observedCollection(
     final Collection<E> collection, final CollectionObserver<E> observer) {
         return new Collection<E>() {
             public boolean add(final E o) {
    	            observer.beforeAdd(o);
    	            boolean result = collection.add(o);
    	            observer.afterAdd(o);
    	            return result;
             }

			@Override
			public boolean addAll(Collection<? extends E> arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void clear() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean contains(Object arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean containsAll(Collection<?> arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isEmpty() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public Iterator<E> iterator() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean remove(Object arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean removeAll(Collection<?> arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean retainAll(Collection<?> arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public int size() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Object[] toArray() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <T> T[] toArray(T[] arg0) {
				// TODO Auto-generated method stub
				return null;
			}

             // ... generate rest of delegate methods in Eclipse

     };
     }
}
