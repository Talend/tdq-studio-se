// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataquality.indicators.mapdb;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Random;
import java.util.SortedSet;

import org.mapdb.BTreeKeySerializer;
import org.mapdb.BTreeKeySerializer.BasicKeySerializer;

/**
 * created by talend on Jul 24, 2014 Detailled comment
 * 
 */
public class DBSet<E> extends AbstractDB<E> implements NavigableSet<E> {

    private NavigableSet<E> dbSet = null;

    protected String setName = "testDBSet" + new Random().nextLong(); //$NON-NLS-1$;

    public DBSet(DBMapParameter parameter) {
        if (parameter == null) {
            initDefaultDB();
        } else {
            initDBByDBMapParameter(parameter);
        }
        initSet();
    }

    public DBSet() {
        initDefaultDB();
        initSet();
    }

    public DBSet(String parentFullPathStr, String fileName) {
        if (!checkParameter(parentFullPathStr, fileName)) {
            return;
        }
        initDefaultDB(parentFullPathStr, fileName);
        initSet();
    }

    public DBSet(String parentFullPathStr, String fileName, String setName) {
        this(parentFullPathStr, fileName, setName, 0l);
    }

    public DBSet(String parentFullPathStr, String fileName, String setName, Long limSize) {
        if (!checkParameter(parentFullPathStr, fileName)) {
            return;
        }
        initDefaultDB(parentFullPathStr, fileName);
        this.limitSize = limSize;
        initSet(setName);
    }

    /**
     * init the set
     */
    private void initSet() {
        TalendSerializerBase talendSerializerBase = new TalendSerializerBase();
        BasicKeySerializer talendBasicKeySerializer = new BTreeKeySerializer.BasicKeySerializer(talendSerializerBase);
        dbSet = getDB().createTreeSet(setName).serializer(talendBasicKeySerializer).comparator(new DBMapCompartor()).make();

    }

    /**
     * init the set
     */
    private void initSet(String theSetName) {
        setName = theSetName;
        if (getDB().exists(setName)) {
            dbSet = getDB().getTreeSet(setName);
        } else {
            TalendSerializerBase talendSerializerBase = new TalendSerializerBase();
            BasicKeySerializer talendBasicKeySerializer = new BTreeKeySerializer.BasicKeySerializer(talendSerializerBase);
            dbSet = getDB().createTreeSet(setName).serializer(talendBasicKeySerializer).comparator(new DBMapCompartor()).make();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.AbstractCollection#iterator()
     */
    @Override
    public Iterator<E> iterator() {
        return dbSet.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.AbstractCollection#size()
     */
    @Override
    public int size() {
        return dbSet.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Set#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return dbSet.isEmpty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Set#contains(java.lang.Object)
     */
    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return dbSet.contains(EMPTY);
        }
        return dbSet.contains(o);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Set#toArray()
     */
    @Override
    public Object[] toArray() {
        return dbSet.toArray();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Set#toArray(T[])
     */
    @Override
    public <T> T[] toArray(T[] a) {
        return dbSet.toArray(a);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Set#add(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean add(E e) {
        if (e == null) {
            return dbSet.add((E) EMPTY);
        }

        return dbSet.add(e);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Set#remove(java.lang.Object)
     */
    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return dbSet.remove(EMPTY);
        }
        return dbSet.remove(o);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Set#containsAll(java.util.Collection)
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        return dbSet.containsAll(c);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Set#addAll(java.util.Collection)
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        return dbSet.addAll(c);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Set#retainAll(java.util.Collection)
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        return dbSet.retainAll(c);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Set#removeAll(java.util.Collection)
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        return dbSet.removeAll(c);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Set#clear()
     */
    @Override
    public void clear() {
        if (!getDB().isClosed()) {
            dbSet.clear();
            this.getDB().delete(setName);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Set#hashCode()
     */
    @Override
    public int hashCode() {
        return dbSet.hashCode();

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.SortedSet#comparator()
     */
    @Override
    public Comparator<? super E> comparator() {
        return dbSet.comparator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.SortedSet#first()
     */
    @Override
    public E first() {
        return dbSet.first();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.SortedSet#last()
     */
    @Override
    public E last() {
        return dbSet.last();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.NavigableSet#lower(java.lang.Object)
     */
    @Override
    public E lower(E e) {
        return dbSet.lower(e);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.NavigableSet#floor(java.lang.Object)
     */
    @Override
    public E floor(E e) {
        return dbSet.floor(e);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.NavigableSet#ceiling(java.lang.Object)
     */
    @Override
    public E ceiling(E e) {
        return dbSet.ceiling(e);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.NavigableSet#higher(java.lang.Object)
     */
    @Override
    public E higher(E e) {
        return dbSet.higher(e);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.NavigableSet#pollFirst()
     */
    @Override
    public E pollFirst() {
        return dbSet.pollFirst();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.NavigableSet#pollLast()
     */
    @Override
    public E pollLast() {
        return dbSet.pollLast();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.NavigableSet#descendingSet()
     */
    @Override
    public NavigableSet<E> descendingSet() {
        return dbSet.descendingSet();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.NavigableSet#descendingIterator()
     */
    @Override
    public Iterator<E> descendingIterator() {
        return dbSet.descendingIterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.NavigableSet#subSet(java.lang.Object, boolean, java.lang.Object, boolean)
     */
    @Override
    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
        return dbSet.subSet(fromElement, fromInclusive, toElement, toInclusive);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.NavigableSet#headSet(java.lang.Object, boolean)
     */
    @Override
    public NavigableSet<E> headSet(E toElement, boolean inclusive) {
        return dbSet.headSet(toElement, inclusive);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.NavigableSet#tailSet(java.lang.Object, boolean)
     */
    @Override
    public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
        return dbSet.tailSet(fromElement, inclusive);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.NavigableSet#subSet(java.lang.Object, java.lang.Object)
     */
    @Override
    public NavigableSet<E> subSet(E fromElement, E toElement) {
        return dbSet.subSet(fromElement, true, toElement, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.NavigableSet#headSet(java.lang.Object)
     */
    @Override
    public SortedSet<E> headSet(E toElement) {
        return dbSet.headSet(toElement);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.NavigableSet#tailSet(java.lang.Object)
     */
    @Override
    public SortedSet<E> tailSet(E fromElement) {
        return dbSet.tailSet(fromElement);
    }

}
