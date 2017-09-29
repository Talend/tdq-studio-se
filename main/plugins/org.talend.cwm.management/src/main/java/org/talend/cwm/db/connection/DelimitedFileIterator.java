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
package org.talend.cwm.db.connection;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.Escape;
import org.talend.core.model.metadata.builder.database.JavaSqlFactory;
import org.talend.cwm.db.connection.file.FileCSVReader;
import org.talend.cwm.db.connection.file.FileDelimitedReader;
import org.talend.cwm.db.connection.file.IFileReader;
import org.talend.dataquality.matchmerge.Record;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * created by yyin on 2014-9-15 Detailled comment
 * 
 */
public class DelimitedFileIterator implements Iterator<Record> {

    private IFileReader fileReader;

    private static Logger log = Logger.getLogger(DelimitedFileIterator.class);

    public DelimitedFileIterator(DelimitedFileConnection delimitedFileconnection, List<ModelElement> analysisElementList) {
        String path = JavaSqlFactory.getURL(delimitedFileconnection);
        IPath iPath = new Path(path);
        File file = iPath.toFile();
        if (!file.exists()) {
            return;
        }
        try {
            if (Escape.CSV.equals(delimitedFileconnection.getEscapeType())) {
                fileReader = new FileCSVReader(file, delimitedFileconnection, analysisElementList);
            } else {
                fileReader = new FileDelimitedReader(file, delimitedFileconnection, analysisElementList);
            }
        } catch (IOException e) {
            log.error(e, e);
        }
    }

    /*
     * check if the file connection has next record, if not, close the file.
     * 
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
        if (this.fileReader == null) {
            return false;
        }
        try {
            if (this.fileReader.hasNext()) {
                return true;
            } else {
                close();
                return false;
            }
        } catch (IOException e) {
            try {
                close();
            } catch (IOException e1) {
                return false;
            }
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Iterator#next()
     */
    public Record next() {
        if (this.fileReader == null) {
            return null;
        }
        try {
            return this.fileReader.next();
        } catch (IOException e) {
            throw new RuntimeException("Could not build next result", e); //$NON-NLS-1$
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Iterator#remove()
     */
    public void remove() {
        throw new UnsupportedOperationException("Read only iterator"); //$NON-NLS-1$
    }

    public void close() throws IOException {
        this.fileReader.close();
    }

}
