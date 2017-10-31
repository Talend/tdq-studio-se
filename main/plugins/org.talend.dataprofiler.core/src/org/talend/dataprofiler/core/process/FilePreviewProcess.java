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
package org.talend.dataprofiler.core.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.talend.commons.utils.StringUtils;
import org.talend.core.language.LanguageManager;
import org.talend.core.repository.model.preview.IPreview;
import org.talend.core.repository.model.preview.IProcessDescription;
import org.talend.core.utils.CsvArray;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataprofiler.core.ui.utils.MessageUI;
import org.talend.dq.helper.FileUtils;
import org.talend.dq.helper.ParameterUtil;

import com.talend.csv.CSVReader;

/**
 * DOC bZhou class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class FilePreviewProcess implements IPreview {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.preview.IPreview#preview(org.talend.repository.preview.IProcessDescription,
     * java.lang.String)
     */
    public CsvArray preview(IProcessDescription description, String type) throws CoreException {
        return preview(description, type, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.preview.IPreview#preview(org.talend.repository.preview.IProcessDescription,
     * java.lang.String, boolean)
     */
    public CsvArray preview(IProcessDescription description, String type, boolean errorOutputAsException) throws CoreException {
        CsvArray csvArray = new CsvArray();
        CSVReader csvReader = null;
        try {

            if (description.getLoopLimit() == null) {
                description.setLoopLimit(-1);
            }

            // MOD qiongli 2011-6-20 bug 21850,use CsvReader to parser
            final String pathStr = TalendQuoteUtils.removeQuotes(description.getFilepath());
            String fileSeparator = TalendQuoteUtils.removeQuotes(description.getFieldSeparator());
            String encoding = TalendQuoteUtils.removeQuotes(description.getEncoding());
            int headValue = description.getHeaderRow();

            if (fileSeparator.contains("t")) { //$NON-NLS-1$
                fileSeparator = String.valueOf('\t');
            }

            IPath filePath = new Path(pathStr);
            File file = filePath.toFile();

            if (!file.exists()) {
                MessageUI.openWarning(Messages.getString("System can not find the file specified")); //$NON-NLS-1$
                return csvArray;
            }
            csvReader = new CSVReader(new BufferedReader(new InputStreamReader(new java.io.FileInputStream(file),
                    encoding == null ? "UTF-8" : encoding)), ParameterUtil //$NON-NLS-1$
                    .trimParameter(fileSeparator).charAt(0));

            initCsvReader(csvReader, description);
            for (int i = 0; i < headValue && csvReader.readNext(); i++) {
                // do nothing, just ignore the header part
            }
            int limitRows = description.getLimitRows();
            int currentRecord = 0;
            // just preview the top of 50
            while (csvReader.readNext() && currentRecord < 50) {
                currentRecord++;
                if (limitRows != -1 && currentRecord > limitRows) {
                    break;
                }
                csvArray.add(csvReader.getValues());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (csvReader != null) {
                try {
                    csvReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return csvArray;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.preview.IPreview#stopLoading()
     */
    public void stopLoading() {
        // TODO Auto-generated method stub

    }

    public boolean isTopPreview() {
        return true;
    }

    /**
     * 
     * DOC qiongli Comment method "initCsvReader".
     * 
     * @param csvReader
     * @param description
     */
    private void initCsvReader(CSVReader csvReader, IProcessDescription description) {
        String rowSep = description.getRowSeparator();
        if (rowSep != null && rowSep.length() > 0) {
            String languageName = LanguageManager.getCurrentLanguage().getName();
            char rowSeparator[] = ParameterUtil.trimParameter(StringUtils.loadConvert(rowSep, languageName)).toCharArray();
            if (rowSeparator[0] != FileUtils.ROW_SEPARATOR_N && rowSeparator[0] != FileUtils.ROW_SEPARATOR_R) {
                csvReader.setLineEnd(ParameterUtil.trimParameter(StringUtils.loadConvert(rowSep, languageName)));
            }
        }

        csvReader.setSkipEmptyRecords(true);
        String textEnclosure = description.getTextEnclosure();
        if (!textEnclosure.equals("\"\"") && textEnclosure.length() > 0) { //$NON-NLS-1$
            csvReader.setQuoteChar(ParameterUtil.trimParameter(textEnclosure).charAt(0));
        }
        String escapeChar = description.getEscapeCharacter();
        if (escapeChar == null || escapeChar.equals(FileUtils.ESCAPECHAR1) || escapeChar.equals(FileUtils.ESCAPECHAR2)) {
            csvReader.setEscapeChar('\\');
        } else {
            csvReader.setEscapeChar(FileUtils.TEXT_QUAL);
        }
    }

}
