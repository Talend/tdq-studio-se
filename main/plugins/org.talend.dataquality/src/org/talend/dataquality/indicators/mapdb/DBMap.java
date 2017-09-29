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

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentNavigableMap;

import org.mapdb.BTreeKeySerializer;
import org.mapdb.BTreeKeySerializer.BasicKeySerializer;
import org.mapdb.BTreeMap;
import org.mapdb.DB.BTreeMapMaker;
import org.mapdb.Fun;
import org.mapdb.Pump;
import org.mapdb.Serializer;
import org.talend.cwm.indicator.DataValidation;

/**
 * created by talend on Aug 5, 2014 Detailled comment
 * 
 */
public class DBMap<K, V> extends AbstractDB<K> implements ConcurrentNavigableMap<K, V> {

    protected ConcurrentNavigableMap<K, V> dbMap = null;

    protected String mapName = "testDBMap" + new Random().nextLong(); //$NON-NLS-1$

    protected TalendSerializerBase talendSerializerBase = new TalendSerializerBase();

    protected BasicKeySerializer talendBasicKeySerializer = new BTreeKeySerializer.BasicKeySerializer(talendSerializerBase);

    public DBMap() {
        initDefaultDB();
        initMap();
    }

    public DBMap(String parentFullPathStr, String fileName) {
        if (!checkParameter(parentFullPathStr, fileName)) {
            return;
        }
        initDefaultDB(parentFullPathStr, fileName);
        initMap();
    }

    public DBMap(String parentFullPathStr, String fileName, String mapName) {
        this(parentFullPathStr, fileName, mapName, 0l);
    }

    public DBMap(String parentFullPathStr, String fileName, String mapName, Long limSize) {
        if (!checkParameter(parentFullPathStr, fileName)) {
            return;
        }
        initDefaultDB(parentFullPathStr, fileName);
        this.limitSize = limSize;
        initMap(mapName);
    }

    protected void initMap() {
        dbMap = getDB().createTreeMap(mapName).keySerializer(talendBasicKeySerializer).comparator(new DBMapCompartor())
                .valueSerializer(talendSerializerBase).make();
    }

    protected void initMap(String theMapName) {
        mapName = theMapName;
        if (getDB().exists(mapName)) {
            dbMap = getDB().get(mapName);
        } else {
            BTreeMapMaker treeMapMaker = getDB().createTreeMap(mapName);
            if (MapDBContent.isValuesOutsideNodesEnable()) {
                treeMapMaker = treeMapMaker.valuesOutsideNodesEnable();
            }
            dbMap = treeMapMaker.keySerializer(talendBasicKeySerializer).comparator(new DBMapCompartor())
                    .valueSerializer(talendSerializerBase).make();
        }
    }

    public <VV> BTreeMap<K, VV> pumpSource(Fun.Function1<VV, K> valueExtractor) {
        DBMapCompartor dbMapCompartor = new DBMapCompartor();
        Iterator<K> sortIterator = Pump.sort(dbMap.keySet().iterator(), false, 100000, Collections.reverseOrder(dbMapCompartor), // reverse
                // order
                // comparator
                Serializer.BASIC);

        String randomString = randomString(10);
        BTreeMap<K, VV> make = getDB().createTreeMap("map" + randomString).pumpSource(sortIterator, valueExtractor) //$NON-NLS-1$
                .keySerializer(talendBasicKeySerializer).comparator(dbMapCompartor).valueSerializer(talendSerializerBase)
                .makeOrGet();
        return make;
    }

    /**
     * 
     * Generate a random string
     * 
     * @param size the length of the string
     * @return
     */
    public String randomString(int size) {
        String chars = "0123456789abcdefghijklmnopqrstuvwxyz !@#$%^&*()_+=-{}[]:\",./<>?|\\"; //$NON-NLS-1$
        StringBuilder b = new StringBuilder(size);
        Random r = new Random();
        for (int i = 0; i < size; i++) {
            b.append(chars.charAt(r.nextInt(chars.length())));
        }
        return b.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Map#size()
     */
    @Override
    public int size() {
        return dbMap.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Map#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return dbMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) {
            return dbMap.containsKey(EMPTY);
        }
        return dbMap.containsKey(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    @Override
    public boolean containsValue(Object value) {
        return dbMap.containsValue(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Map#get(java.lang.Object)
     */
    @Override
    public V get(Object key) {
        if (key == null) {
            return dbMap.get(EMPTY);
        }
        return dbMap.get(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked")
    public V put(K key, V value) {
        if (key == null) {
            return dbMap.put((K) EMPTY, value);
        }
        // TDQ-10833 format Date.like as :Date and Timestamp is "yyyy-MM-dd HH:MM:ss";Time is "HH:MM:ss".
        if (key instanceof Date) {
            if (key instanceof Time) {
                return dbMap.put((K) new TalendFormatTime(((Time) key)), value);
            }
            return dbMap.put((K) new TalendFormatDate(((Date) key)), value);
        }
        return dbMap.put(key, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Map#remove(java.lang.Object)
     */
    @Override
    public V remove(Object key) {
        if (key == null) {
            return dbMap.remove(EMPTY);
        }
        return dbMap.remove(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Map#putAll(java.util.Map)
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        dbMap.putAll(m);

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Map#clear()
     */
    @Override
    public void clear() {
        if (!getDB().isClosed()) {
            dbMap.clear();
            this.getDB().delete(mapName);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Map#keySet()
     */
    @Override
    public NavigableSet<K> keySet() {
        return dbMap.keySet();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Map#values()
     */
    @Override
    public Collection<V> values() {
        return dbMap.values();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Map#entrySet()
     */
    @Override
    public Set<java.util.Map.Entry<K, V>> entrySet() {
        return dbMap.entrySet();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.AbstractMap#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        return dbMap.equals(o);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.AbstractMap#hashCode()
     */
    @Override
    public int hashCode() {
        return dbMap.hashCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.AbstractMap#toString()
     */
    @Override
    public String toString() {
        return dbMap.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.concurrent.ConcurrentMap#putIfAbsent(java.lang.Object, java.lang.Object)
     */
    @Override
    public V putIfAbsent(K key, V value) {
        return dbMap.putIfAbsent(key, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.concurrent.ConcurrentMap#remove(java.lang.Object, java.lang.Object)
     */
    @Override
    public boolean remove(Object key, Object value) {
        return dbMap.remove(key, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.concurrent.ConcurrentMap#replace(java.lang.Object, java.lang.Object, java.lang.Object)
     */
    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        return dbMap.replace(key, oldValue, newValue);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.concurrent.ConcurrentMap#replace(java.lang.Object, java.lang.Object)
     */
    @Override
    public V replace(K key, V value) {
        return dbMap.replace(key, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.NavigableMap#lowerEntry(java.lang.Object)
     */
    @Override
    public java.util.Map.Entry<K, V> lowerEntry(K key) {
        return dbMap.lowerEntry(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.NavigableMap#lowerKey(java.lang.Object)
     */
    @Override
    public K lowerKey(K key) {
        return dbMap.lowerKey(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.NavigableMap#floorEntry(java.lang.Object)
     */
    @Override
    public java.util.Map.Entry<K, V> floorEntry(K key) {
        return dbMap.floorEntry(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.NavigableMap#floorKey(java.lang.Object)
     */
    @Override
    public K floorKey(K key) {
        return dbMap.floorKey(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.NavigableMap#ceilingEntry(java.lang.Object)
     */
    @Override
    public java.util.Map.Entry<K, V> ceilingEntry(K key) {
        return dbMap.ceilingEntry(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.NavigableMap#ceilingKey(java.lang.Object)
     */
    @Override
    public K ceilingKey(K key) {
        return dbMap.ceilingKey(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.NavigableMap#higherEntry(java.lang.Object)
     */
    @Override
    public java.util.Map.Entry<K, V> higherEntry(K key) {
        return dbMap.higherEntry(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.NavigableMap#higherKey(java.lang.Object)
     */
    @Override
    public K higherKey(K key) {
        return dbMap.higherKey(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.NavigableMap#firstEntry()
     */
    @Override
    public java.util.Map.Entry<K, V> firstEntry() {
        return dbMap.firstEntry();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.NavigableMap#lastEntry()
     */
    @Override
    public java.util.Map.Entry<K, V> lastEntry() {
        return dbMap.lastEntry();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.NavigableMap#pollFirstEntry()
     */
    @Override
    public java.util.Map.Entry<K, V> pollFirstEntry() {
        return dbMap.pollFirstEntry();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.NavigableMap#pollLastEntry()
     */
    @Override
    public java.util.Map.Entry<K, V> pollLastEntry() {
        return dbMap.pollLastEntry();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.SortedMap#comparator()
     */
    @Override
    public Comparator<? super K> comparator() {
        return dbMap.comparator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.SortedMap#firstKey()
     */
    @Override
    public K firstKey() {
        return dbMap.firstKey();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.SortedMap#lastKey()
     */
    @Override
    public K lastKey() {
        return dbMap.lastKey();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.concurrent.ConcurrentNavigableMap#subMap(java.lang.Object, boolean, java.lang.Object, boolean)
     */
    @Override
    public ConcurrentNavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        return dbMap.subMap(fromKey, fromInclusive, toKey, toInclusive);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.concurrent.ConcurrentNavigableMap#headMap(java.lang.Object, boolean)
     */
    @Override
    public ConcurrentNavigableMap<K, V> headMap(K toKey, boolean inclusive) {
        return dbMap.headMap(toKey, inclusive);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.concurrent.ConcurrentNavigableMap#tailMap(java.lang.Object, boolean)
     */
    @Override
    public ConcurrentNavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
        return dbMap.tailMap(fromKey, inclusive);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.concurrent.ConcurrentNavigableMap#subMap(java.lang.Object, java.lang.Object)
     */
    @Override
    public ConcurrentNavigableMap<K, V> subMap(K fromKey, K toKey) {
        return dbMap.subMap(fromKey, toKey);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.concurrent.ConcurrentNavigableMap#headMap(java.lang.Object)
     */
    @Override
    public ConcurrentNavigableMap<K, V> headMap(K toKey) {
        return dbMap.headMap(toKey);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.concurrent.ConcurrentNavigableMap#tailMap(java.lang.Object)
     */
    @Override
    public ConcurrentNavigableMap<K, V> tailMap(K fromKey) {
        return dbMap.tailMap(fromKey);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.concurrent.ConcurrentNavigableMap#descendingMap()
     */
    @Override
    public ConcurrentNavigableMap<K, V> descendingMap() {
        return dbMap.descendingMap();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.concurrent.ConcurrentNavigableMap#navigableKeySet()
     */
    @Override
    public NavigableSet<K> navigableKeySet() {
        return dbMap.navigableKeySet();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.concurrent.ConcurrentNavigableMap#descendingKeySet()
     */
    @Override
    public NavigableSet<K> descendingKeySet() {
        return dbMap.descendingKeySet();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.MapDB.utils.AbstractDB#tailSet(java.lang.Object, boolean)
     */
    @Override
    public NavigableSet<K> tailSet(K fromElement, boolean inclusive) {
        return dbMap.keySet().tailSet(fromElement, inclusive);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.MapDB.utils.AbstractDB#headSet(java.lang.Object, boolean)
     */
    @Override
    public NavigableSet<K> headSet(K toElement, boolean inclusive) {
        return dbMap.keySet().headSet(toElement, inclusive);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.MapDB.utils.AbstractDB#iterator()
     */
    @Override
    public Iterator<K> iterator() {
        return dbMap.keySet().iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.MapDB.utils.AbstractDB#subSet(java.lang.Object, java.lang.Object)
     */
    @Override
    public NavigableSet<K> subSet(K fromElement, K toElement) {
        return dbMap.keySet().subSet(fromElement, true, toElement, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.MapDB.utils.AbstractDB#subList(long, long, java.util.Map)
     */
    @Override
    public List<Object[]> subList(long fromIndex, long toIndex, Map<Long, K> indexMap) {
        return subList(fromIndex, toIndex, indexMap, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.mapdb.AbstractDB#subList(long, long, java.util.Map,
     * org.talend.cwm.indicator.DataValidation)
     */
    @Override
    public List<Object[]> subList(long fromIndex, long toIndex, Map<Long, K> indexMap, DataValidation dataValiator) {
        boolean stratToRecord = false;
        List<Object[]> returnList = new ArrayList<Object[]>();
        if (!checkIndex(fromIndex, toIndex)) {
            return returnList;
        }
        K fromKey = null;
        K toKey = null;
        if (indexMap != null) {
            fromKey = indexMap.get(fromIndex);
            toKey = indexMap.get(toIndex);
        }
        Iterator<K> iterator = null;
        long index = 0l;
        if (fromKey == null) {
            iterator = this.iterator();
        } else if (toKey == null) {
            NavigableSet<K> tailSet = tailSet(fromKey, true);
            index = fromIndex;
            iterator = tailSet.iterator();
        } else {
            NavigableSet<K> tailSet = subSet(fromKey, toKey);
            index = fromIndex;
            iterator = tailSet.iterator();
        }

        while (iterator.hasNext()) {
            K next = iterator.next();
            if (dataValiator != null) {
                V v = this.get(next);
                if (!dataValiator.isValid(v)) {
                    continue;
                }
            }
            if (index == 0 && fromKey == null && indexMap != null) {
                indexMap.put(0l, next);
            }
            if (index == fromIndex) {
                stratToRecord = true;
            }
            if (index == toIndex) {
                if (toKey == null && indexMap != null) {
                    indexMap.put(toIndex, next);
                }
                break;
            }
            if (stratToRecord == true) {
                V v = this.get(next);
                if (v.getClass().isArray()) {
                    returnList.add((Object[]) v);
                } else if (List.class.isInstance(v)) {
                    returnList.add(((List<?>) v).toArray());
                } else {
                    returnList.add(new Object[] { v });
                }
            }
            index++;

        }

        return returnList;
    }

}
