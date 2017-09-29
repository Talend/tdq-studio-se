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

import routines.system.IPersistableComparableLookupRow;

/**
 * created by zhao on Oct 16, 2013 Detailled comment
 * 
 */
public class BlockKey implements IPersistableComparableLookupRow<BlockKey> {

    public static final int DEFAULT_HASHCODE = 1;

    public static final int PRIME = 31;

    private final byte[] byteArrayLock = new byte[0];

    private byte[] byteArray = new byte[0];

    private int hashCode = DEFAULT_HASHCODE;

    private boolean hashCodeDirty = true;

    private List<String> blockKey = null;

    private int blockFieldCount = 0;

    public BlockKey(int blockFieldCount) {
        this.blockFieldCount = blockFieldCount;
    }

    public List<String> getBlockKey() {
        return new ArrayList<String>(blockKey);
    }

    public void setBlockKey(List<String> blockKey) {
        this.blockKey = new ArrayList<String>(blockKey);
    }

    @Override
    public int hashCode() {
        if (this.hashCodeDirty) {
            final int prime = PRIME;
            int result = DEFAULT_HASHCODE;
            if (blockKey != null) {
                for (String bk : blockKey) {
                    result = prime * result + (bk == null ? 0 : bk.hashCode());
                }
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
        final BlockKey other = (BlockKey) obj;

        int idx = 0;
        List<String> otherBlockKeys = other.getBlockKey();
        for (String bk : blockKey) {
            if (bk == null) {
                if (otherBlockKeys.get(idx) != null) {
                    return false;
                }
            } else if (!bk.equals(otherBlockKeys.get(idx))) {
                return false;
            }
            idx++;
        }

        return true;
    }

    @SuppressWarnings("nls")
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("block key [");
        for (String value : blockKey) {
            sb.append(";" + value);
        }
        sb.append("]");
        return sb.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see routines.system.IPersistableLookupRow#writeKeysData(java.io.ObjectOutputStream)
     */
    public void writeKeysData(ObjectOutputStream out) {
        for (String bk : blockKey) {
            try {
                PersistentRowHelper.writeString(bk, out);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see routines.system.IPersistableLookupRow#readKeysData(java.io.ObjectInputStream)
     */
    public void readKeysData(ObjectInputStream in) {
        synchronized (byteArrayLock) {
            try {
                for (int i = 0; i < blockFieldCount; i++) {
                    if (i == 0) {
                        blockKey.clear();
                    }
                    blockKey.add(PersistentRowHelper.readString(byteArray, in));
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
        // No implementation.
    }

    /*
     * (non-Javadoc)
     * 
     * @see routines.system.IPersistableLookupRow#readValuesData(java.io.DataInputStream, java.io.ObjectInputStream)
     */
    public void readValuesData(DataInputStream dataIn, ObjectInputStream objectIn) {
        // No implementation.
    }

    /*
     * (non-Javadoc)
     * 
     * @see routines.system.IPersistableLookupRow#copyDataTo(java.lang.Object)
     */
    public void copyDataTo(BlockKey other) {
        other.blockKey = new ArrayList<String>(this.blockKey);

    }

    /*
     * (non-Javadoc)
     * 
     * @see routines.system.IPersistableLookupRow#copyKeysDataTo(java.lang.Object)
     */
    public void copyKeysDataTo(BlockKey other) {
        other.blockKey = new ArrayList<String>(this.blockKey);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(BlockKey other) {
        int returnValue = -1;
        int idx = 0;
        List<String> otherBlockKey = other.getBlockKey();
        for (String bk : blockKey) {
            returnValue = PersistentRowHelper.checkNullsAndCompare(bk, otherBlockKey.get(idx));
            if (returnValue != 0) {
                return returnValue;
            }
            idx++;
        }
        return returnValue;
    }

}
