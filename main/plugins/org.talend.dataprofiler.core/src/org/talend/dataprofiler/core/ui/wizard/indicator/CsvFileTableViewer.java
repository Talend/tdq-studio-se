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
package org.talend.dataprofiler.core.ui.wizard.indicator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.pattern.PatternToExcelEnum;
import org.talend.dq.helper.FileUtils;
import org.talend.dq.helper.UDIHelper;

import com.talend.csv.CSVReader;

/**
 * CsvFileTableViewer is a table viewer for preview a simple csv file.
 */
public class CsvFileTableViewer extends Composite {

    private Logger log = Logger.getLogger(CsvFileTableViewer.class);

    private static final char CURRENT_SEPARATOR = '\t';

    private boolean useTextQualifier = true;

    private static final Image WARN_IMG = ImageLib.getImage(ImageLib.LEVEL_WARNING);

    private CSVReader reader;

    private TableViewer viewer;

    private boolean quotesError = false;

    private boolean emptyError = true;

    private boolean hasEmptyRow = false;

    private boolean hasPatternHeaders = false;

    /**
     * DOC yyi CsvFileTableViewer class global comment. Detailled comment
     * 
     * FIXME this inner class should be static. Confirm and fix the error.
     */
    class ViewContentProvider implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
            hasEmptyRow = false;
            emptyError = true;
        }

        public void dispose() {
        }

        public Object[] getElements(Object parent) {

            CSVReader csvReader = (CSVReader) parent;

            List<Object> rows = new ArrayList<Object>();

            try {
                while (csvReader.readNext()) {
                    rows.add(csvReader.getValues());
                }
            } catch (IOException e) {
                log.error(e, e);
            }

            List<Object> rows2 = new ArrayList<Object>();
            for (Object obj : rows) {
                if (obj instanceof String[]) {
                    String[] strs = (String[]) obj;
                    for (int i = 0; i < strs.length; ++i) {
                        strs[i] = strs[i].replaceAll(UDIHelper.PARA_SEPARATE_1, UDIHelper.PARA_SEPARATE_1_DISPLAY).replaceAll(
                                UDIHelper.PARA_SEPARATE_2, UDIHelper.PARA_SEPARATE_2_DISPLAY);
                    }
                    rows2.add(strs);
                } else {
                    rows2.add(obj);
                }
            }

            return rows2.toArray(new Object[rows2.size()]);
        }
    }

    /**
     * DOC yyi CsvFileTableViewer class global comment. Detailled comment
     */
    class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {

        public String getColumnText(Object obj, int index) {
            String[] values = (String[]) obj;
            checkEmptyError(values);
            return index < values.length ? values[index] : null;
        }

        /**
         * DOC talend Comment method "checkEmptyError".
         * 
         * @param obj
         * @param index
         */
        private void checkEmptyError(String[] values) {
            if (hasEmptyRow) {
                return;
            }

            try {
                String[] headers = reader.getHeaders();
                if (values.length < headers.length) {
                    String errorRow = "";
                    for (String value : values) {
                        errorRow += value;
                    }
                    log.error("The size of values is less than size of headers.When check the row for: " + errorRow); //$NON-NLS-1$
                    return;
                }
                for (int index = 0; index < headers.length; index++) {
                    String header = headers[index];
                    String value = values[index];
                    if (isNotEmpty(value)) {
                        if (isRegEx(index)) {
                            emptyError = false;
                        } else if (PatternToExcelEnum.Category.getLiteral().equalsIgnoreCase(header)) {
                            emptyError = false;
                        } else if (PatternToExcelEnum.JavaClassName.getLiteral().equalsIgnoreCase(header)) {
                            emptyError = false;
                        } else if (PatternToExcelEnum.JavaJarPath.getLiteral().equalsIgnoreCase(header)) {
                            emptyError = false;
                        }
                    }
                    if (isLastOne(index, headers.length)) {
                        if (emptyError) {
                            hasEmptyRow = true;
                        }
                    }
                }
            } catch (IOException e) {
                log.error(e, e);
            }

        }

        /**
         * judge the method whether is empty
         * 
         * @param input
         * @return true when input is null or length is 0 after remove quote
         */
        private boolean isNotEmpty(String input) {
            String value = trimQuote(input);
            return value != null && !(value.isEmpty());
        }

        /**
         * DOC talend Comment method "isLastOne".
         * 
         * @return
         */
        private boolean isLastOne(int index, int length) {
            return index == (length - 1);

        }

        public Image getColumnImage(Object obj, int index) {
            String[] values = (String[]) obj;
            return index < values.length ? getImage(values[index], index) : null;
        }

        public Image getImage(Object obj, int index) {
            if (!checkQuoteMarks(obj.toString(), index)) {
                quotesError = true;
                return WARN_IMG;
            } else {
                return null;
            }
        }
    }

    public CsvFileTableViewer(final Composite parent, int style) {
        super(parent, style);
        setLayout(new GridLayout());
        createView(this);
        viewer.setInput(reader);
    }

    private void createHeader(String[] headers) {
        int columnCount = headers.length;
        TableColumn[] columns = viewer.getTable().getColumns();
        for (TableColumn column : columns) {
            column.dispose();
        }
        for (int i = 0; i < columnCount; i++) {
            TableColumn tableColumn = new TableColumn(viewer.getTable(), SWT.NONE);
            tableColumn.setText(headers[i]);
            tableColumn.pack();
        }
    }

    private void createView(Composite parent) {
        viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        GridData data = new GridData(GridData.FILL_BOTH);
        data.heightHint = 100;
        viewer.getTable().setLayoutData(data);
        viewer.setContentProvider(new ViewContentProvider());
        viewer.setLabelProvider(new ViewLabelProvider());
        viewer.getTable().setHeaderVisible(true);
        viewer.getTable().setLinesVisible(true);
    }

    private boolean checkQuoteMarks(String text, int index) {
        if (0 == text.length()) {
            return true;
        }
        if (isRegEx(index)) {
            String trimQuote = trimQuote(text);
            if (0 == trimQuote.length()) {
                return true;
            } else if ('\'' == trimQuote.charAt(0) && '\'' == trimQuote.charAt(trimQuote.length() - 1)) {
                return true;
            } else if ('\'' != trimQuote.charAt(0) && '\'' != trimQuote.charAt(trimQuote.length() - 1)) {
                return true;
            } else {
                return false;
            }
        }

        if ('\"' == text.charAt(0) && '\"' == text.charAt(text.length() - 1)) {
            return true;
        }

        if ('\"' != text.charAt(0) && '\"' != text.charAt(text.length() - 1)) {
            return true;
        }

        return false;
    }

    /**
     * the column which index is "index" is a register expression
     * 
     * @param index
     * @return
     */
    private boolean isRegEx(int index) {
        try {
            String[] headers = reader.getHeaders();
            String header = headers[index];
            if (PatternToExcelEnum.DB2Regexp.getLiteral().equalsIgnoreCase(header)) {
                return true;
            } else if (PatternToExcelEnum.MySQLRegexp.getLiteral().equalsIgnoreCase(header)) {
                return true;
            } else if (PatternToExcelEnum.OracleRegexp.getLiteral().equalsIgnoreCase(header)) {
                return true;
            } else if (PatternToExcelEnum.PostgreSQLRegexp.getLiteral().equalsIgnoreCase(header)) {
                return true;
            } else if (PatternToExcelEnum.SQLServerRegexp.getLiteral().equalsIgnoreCase(header)) {
                return true;
            } else if (PatternToExcelEnum.SybaseRegexp.getLiteral().equalsIgnoreCase(header)) {
                return true;
            } else if (PatternToExcelEnum.IngresRegexp.getLiteral().equalsIgnoreCase(header)) {
                return true;
            } else if (PatternToExcelEnum.InformixRegexp.getLiteral().equalsIgnoreCase(header)) {
                return true;
            } else if (PatternToExcelEnum.NETEZZARegexp.getLiteral().equalsIgnoreCase(header)) {
                return true;
            } else if (PatternToExcelEnum.SQLite3Regexp.getLiteral().equalsIgnoreCase(header)) {
                return true;
            } else if (PatternToExcelEnum.Teradata.getLiteral().equalsIgnoreCase(header)) {
                return true;
            } else if (PatternToExcelEnum.JavaRegexp.getLiteral().equalsIgnoreCase(header)) {
                return true;
            } else if (PatternToExcelEnum.Access.getLiteral().equalsIgnoreCase(header)) {
                return true;
            } else if (PatternToExcelEnum.AS400.getLiteral().equalsIgnoreCase(header)) {
                return true;
            } else if (PatternToExcelEnum.Hive.getLiteral().equalsIgnoreCase(header)) {
                return true;
            } else if (PatternToExcelEnum.AllDBRegexp.getLiteral().equalsIgnoreCase(header)) {
                return true;
            }
        } catch (IOException e) {
            log.error(e, e);
        }
        return false;
    }

    private boolean checkFileHeader(String[] headers) {

        List<String> patternEnum = new ArrayList<String>();
        for (PatternToExcelEnum tmpEnum : PatternToExcelEnum.values()) {
            patternEnum.add(tmpEnum.getLiteral());
        }

        for (String header : headers) {
            if (!patternEnum.contains(trimQuote(header))) {
                return false;
            }
        }
        return true;
    }

    private String trimQuote(String text) {
        if (text.length() < 2) {
            return text;
        }

        int beginLen = 0;
        int endLen = text.length();

        if ('\"' == text.charAt(beginLen) && '\"' == text.charAt(endLen - 1)) {
            return text.substring(1, endLen - 1);
        }

        return text;
    }

    /**
     * set a csv file.
     * 
     * @param csvFile
     * @return whether the file be loaded.
     */
    public boolean setCsvFile(File csvFile) {
        quotesError = false;
        hasPatternHeaders = false;
        try {
            if (useTextQualifier) {
                reader = FileUtils.createCSVReader(csvFile, FileUtils.TEXT_QUAL, FileUtils.TEXT_QUAL);
            } else {
                reader = FileUtils.createCSVReader(csvFile, FileUtils.QUOTECHAR_NOTVALID, FileUtils.TEXT_QUAL);
            }
            reader.setSkipEmptyRecords(true);
            reader.readHeaders();
            String[] headers = reader.getHeaders();
            hasPatternHeaders = checkFileHeader(headers);
            createHeader(headers);

        } catch (IOException e1) {
            e1.printStackTrace();
        }
        viewer.setInput(reader);
        return true;
    }

    /**
     * Returns <code>true</code> if the file contains a patterns header.
     * 
     * @return whether the file contains a patterns header.
     */
    public boolean isHeadersInvalid() {
        return !hasPatternHeaders;
    }

    /**
     * Returns <code>true</code> if Use Text Qualifier.
     * 
     * @return whether Use Text Qualifier
     */
    public boolean isUseTextQualifier() {
        return useTextQualifier;
    }

    /**
     * Sets the csv reader use text qualifier if the argument is <code>true</code>.
     * 
     * @param useTextQualifier
     */
    public void setUseTextQualifier(boolean useTextQualifier) {
        this.useTextQualifier = useTextQualifier;
    }

    /**
     * Returns <code>true</code> if the file contains quotes error value.
     * 
     * @return
     */
    public boolean isQuotesError() {
        return quotesError;
    }

    public boolean isEmptyDefinition() {
        return this.hasEmptyRow;
    }

}
