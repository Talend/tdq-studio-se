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
package org.talend.dataprofiler.core.ui.imex;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.talend.dataprofiler.core.ui.imex.model.EImexType;
import org.talend.dataprofiler.core.ui.imex.model.ExportWriterFactory;
import org.talend.dataprofiler.core.ui.imex.model.IExportWriter;
import org.talend.dataprofiler.core.ui.imex.model.ItemRecord;
import org.talend.dataprofiler.core.ui.progress.ProgressUI;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * created by xqliu on Jan 31, 2013 Detailled comment
 */
public class ExportForExchangeWizard extends ExportWizard {

    private static Logger log = Logger.getLogger(ExportForExchangeWizard.class);

    public ExportForExchangeWizard(String specifiedPath) {
        setWindowTitle("Export For Talend Exchange");//$NON-NLS-1$ 
        this.exportPage = new ExportForExchangeWizardPage(specifiedPath);
    }

    @Override
    public boolean performFinish() {
        // alwayse use ZipFileExportWriter instead of the writer from the page
        final IExportWriter writer = ExportWriterFactory.create(EImexType.ZIP_FILE);
        final IPath writerBashPath = exportPage.getWriter().getBasePath();

        final Map<String, ItemRecord[]> recordsList = buildItemRecordList(exportPage.getElements());

        IRunnableWithProgress op = new IRunnableWithProgress() {

            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                monitor.beginTask("Export Item", recordsList.size());//$NON-NLS-1$ 
                for (String zipFileName : recordsList.keySet()) {
                    writer.setBasePath(writerBashPath.append(zipFileName));
                    writer.write(recordsList.get(zipFileName), monitor);
                }
                monitor.done();
            }
        };

        try {
            ProgressUI.popProgressDialog(op);
        } catch (Exception e) {
            log.error(e, e);
        }

        return true;
    }

    /**
     * build the export record map.
     * 
     * @param records key = export zip file name; value = record list which should be add to the zip file
     * @return
     */
    protected Map<String, ItemRecord[]> buildItemRecordList(ItemRecord[] records) {
        Map<String, ItemRecord[]> map = new HashMap<String, ItemRecord[]>();
        for (ItemRecord record : records) {
            ModelElement element = record.getElement();
            if (element != null) {
                String zipFileName = element.getName() + ".zip"; //$NON-NLS-1$
                List<ItemRecord> list = new ArrayList<ItemRecord>();
                list.add(record);
                map.put(zipFileName, list.toArray(new ItemRecord[list.size()]));
            }
        }
        return map;
    }
}
