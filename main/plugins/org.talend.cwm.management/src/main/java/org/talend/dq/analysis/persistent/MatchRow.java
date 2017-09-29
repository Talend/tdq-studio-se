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
package org.talend.dq.analysis.persistent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import routines.system.IPersistableComparableLookupRow;
import routines.system.IPersistableRow;

/**
 * created by zhao on Oct 14, Represent a match row that be able to comparable and being lookup with persistent manager.
 * 
 */
@SuppressWarnings("rawtypes")
public class MatchRow implements IPersistableComparableLookupRow, IPersistableRow {

    private final int DEFAULT_HASHCODE = BlockKey.DEFAULT_HASHCODE;

    private final int PRIME = BlockKey.PRIME;

    private int hashCode = DEFAULT_HASHCODE;

    public boolean hashCodeDirty = true;

    private byte[] byteArrays = new byte[0];

    private static byte[] byteArraysLock = new byte[0];

    private List<String> blockKeyList = new ArrayList<String>();

    private List<String> rowList = new ArrayList<String>();

    private int fieldCount = 0;

    private int blockFieldCount = 0;

    /**
     * 
     * Construct a match row instance with the record feild size and block feild size.
     * 
     * @param recordFieldCount
     * @param blockFieldCount
     */
    public MatchRow(int recordFieldCount, int blockFieldCount) {
        this.fieldCount = recordFieldCount;
        this.blockFieldCount = blockFieldCount;
    }

    /**
     * Getter for row.
     * 
     * @return the row
     */
    public List<String> getRow() {
        return new ArrayList<String>(rowList);
    }

    /**
     * 
     * Get row with blocking key.
     * 
     * @return
     */
    public List<String> getRowWithBlockKey() {
        List<String> rowWithBlockKeyList = new ArrayList<String>(rowList);
        if (blockKeyList.size() != 0) {
            String joinedBlockKey = StringUtils.join(blockKeyList.toArray(new String[blockKeyList.size()]));
            rowWithBlockKeyList.add(joinedBlockKey);
        } else {
            rowWithBlockKeyList.add(StringUtils.EMPTY);
        }
        return rowWithBlockKeyList;

    }

    /**
     * Sets the row.
     * 
     * @param row the row to set
     */
    public void setRow(List<String> rowList) {
        this.rowList = new ArrayList<String>(rowList);
    }

    /**
     * Sets the key.
     * 
     * @param key the key to set
     */
    public void setKey(List<String> key) {
        blockKeyList = new ArrayList<String>(key);
    }

    /**
     * Getter for key.
     * 
     * @return the key
     */
    public List<String> getKey() {
        return new ArrayList<String>(blockKeyList);
    }

    @Override
    public int hashCode() {
        if (this.hashCodeDirty) {
            final int prime = PRIME;
            int result = DEFAULT_HASHCODE;
            for (String bk : blockKeyList) {
                result = prime * result + (bk == null ? 0 : bk.hashCode());
            }
            this.hashCode = result;
            this.hashCodeDirty = false;
        }
        return this.hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MatchRow other = (MatchRow) obj;

        int bkIndex = 0;
        List<String> otherBlockKeys = other.getKey();
        for (String bk : blockKeyList) {
            if (bk == null) {
                if (otherBlockKeys.get(bkIndex) != null) {
                    return false;
                }
            } else if (!bk.equals(otherBlockKeys.get(bkIndex))) {
                return false;
            }
            bkIndex++;
        }
        return true;
    }

    public void writeKeysData(ObjectOutputStream out) {
        for (String bk : blockKeyList) {
            try {
                PersistentRowHelper.writeString(bk, out);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void readKeysData(ObjectInputStream in) {
        synchronized (byteArraysLock) {
            try {
                for (int i = 0; i < blockFieldCount; i++) {
                    if (i == 0) {
                        blockKeyList.clear();
                    }
                    blockKeyList.add(PersistentRowHelper.readString(byteArrays, in));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see routines.system.IPersistableLookupRow#writeValuesData(java.io.DataOutputStream, java.io.ObjectOutputStream)
     */
    public void writeValuesData(DataOutputStream dataOut, ObjectOutputStream objectOut) {
        for (String value : rowList) {
            try {
                PersistentRowHelper.writeString(value, dataOut);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see routines.system.IPersistableLookupRow#readValuesData(java.io.DataInputStream, java.io.ObjectInputStream)
     */
    public void readValuesData(DataInputStream dataIn, ObjectInputStream objectIn) {
        try {
            for (int i = 0; i < fieldCount; i++) {
                if (i == 0) {
                    // Here is magic line that resolve a bug from persistent API.
                    rowList.clear();
                }
                rowList.add(PersistentRowHelper.readString(dataIn));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see routines.system.IPersistableLookupRow#copyKeysDataTo(java.lang.Object)
     */
    public void copyKeysDataTo(Object other) {
        ((MatchRow) other).blockKeyList = new ArrayList<String>(blockKeyList);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object other) {
        int returnValue = -1;
        int idx = 0;
        List<String> otherBlockKeys = ((MatchRow) other).getKey();
        for (String bk : blockKeyList) {
            returnValue = PersistentRowHelper.checkNullsAndCompare(bk, otherBlockKeys.get(idx));
            if (returnValue != 0) {
                return returnValue;
            }
            idx++;
        }

        return returnValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see routines.system.IPersistableLookupRow#copyDataTo(java.lang.Object)
     */
    public void copyDataTo(Object other) {
        ((MatchRow) other).rowList = new ArrayList<String>(rowList);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @SuppressWarnings("nls")
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("row [");
        for (String value : rowList) {
            sb.append(";" + value);
        }
        sb.append("]");
        return sb.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see routines.system.IPersistableRow#writeData(java.io.ObjectOutputStream)
     */
    public void writeData(ObjectOutputStream out) {
        writeValuesData(new DataOutputStream(out), out);

    }

    /*
     * (non-Javadoc)
     * 
     * @see routines.system.IPersistableRow#readData(java.io.ObjectInputStream)
     */
    public void readData(ObjectInputStream in) {
        readValuesData(new DataInputStream(in), in);

    }
}
