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
package org.talend.cwm.db.connection.file;

import java.io.IOException;

import org.talend.dataquality.matchmerge.Record;

/**
 * created by yyin on 2014-9-22 Detailled comment
 * 
 */
public interface IFileReader {

    boolean hasNext() throws IOException;

    Record next() throws IOException;

    void close() throws IOException;
}
