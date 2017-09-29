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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dataprofiler.core.ui.imex.model.ItemRecord;
import org.talend.dataprofiler.core.ui.utils.UDIUtils;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.TaggedValue;

/**
 * created by xqliu on Jan 31, 2013 Detailled comment
 */
public class ExportUdiForExchangeWizard extends ExportForExchangeWizard {

    public ExportUdiForExchangeWizard(String specifiedPath) {
        super(specifiedPath);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.imex.ExportForExchangeWizard#buildItemRecordList(org.talend.dataprofiler.core
     * .ui.imex.model.ItemRecord[])
     */
    @Override
    protected Map<String, ItemRecord[]> buildItemRecordList(ItemRecord[] records) {
        Map<String, ItemRecord[]> map = new HashMap<String, ItemRecord[]>();
        for (ItemRecord record : records) {
            ModelElement element = record.getElement();
            if (element != null) {
                String zipFileName = element.getName() + ".zip"; //$NON-NLS-1$
                List<ItemRecord> list = new ArrayList<ItemRecord>();
                list.add(record);
                list.addAll(getDependencyRecords(record));
                map.put(zipFileName, list.toArray(new ItemRecord[list.size()]));
            }
        }
        return map;
    }

    /**
     * get the dependency ItemRecord for UDI.
     * 
     * @param record UDI ItemRecord
     * @param records jar files ItemRecord which dependencied by UDI
     * @return
     */
    private List<ItemRecord> getDependencyRecords(ItemRecord record) {
        List<ItemRecord> list = new ArrayList<ItemRecord>();

        List<File> udiJarfiles = new ArrayList<File>();
        TaggedValue tv = TaggedValueHelper.getTaggedValue(TaggedValueHelper.JAR_FILE_PATH, record.getElement().getTaggedValue());
        if (tv != null) {
            for (IFile udiJarFile : UDIUtils.getLibJarFileList()) {
                for (String jarName : tv.getValue().split("\\|\\|")) { //$NON-NLS-1$
                    if (udiJarFile.getName().equals(jarName)) {
                        udiJarfiles.add(udiJarFile.getLocation().toFile());
                        break;
                    }
                }
            }
        }

        for (File jarFile : udiJarfiles) {
            list.add(new ItemRecord(jarFile));
        }

        return list;
    }
}
