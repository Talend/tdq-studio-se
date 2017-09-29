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
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.talend.commons.utils.platform.PluginChecker;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.dataprofiler.core.migration.helper.IndicatorDefinitionFileHelper;
import org.talend.dataprofiler.core.ui.imex.model.ItemRecord;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.definition.userdefine.UDIndicatorDefinition;
import org.talend.resource.EResourceConstant;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class FileTreeContentProvider implements ITreeContentProvider {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
     */
    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof ItemRecord) {
            ItemRecord record = (ItemRecord) parentElement;
            return filterRecords(record.getChildern());
        }
        return new Object[0];
    }

    /**
     * filter the ItemRecords.
     * 
     * @param childern
     * @return
     */
    private Object[] filterRecords(ItemRecord[] childern) {
        ItemRecord[] result = childern;
        result = hideSomeItems(result);
        return result;
    }

    /**
     * hide Some Items.
     * 
     * @param itemRecords
     * @return
     */
    private ItemRecord[] hideSomeItems(ItemRecord[] itemRecords) {
        List<ItemRecord> result = new ArrayList<ItemRecord>();
        for (ItemRecord itemRecord : itemRecords) {
            if (itemRecord != null) {
                // hide the Technial indicators
                if (itemRecord.getElement() != null && itemRecord.getElement() instanceof IndicatorDefinition
                        && !(itemRecord.getElement() instanceof UDIndicatorDefinition)) {
                    IndicatorDefinition indDef = (IndicatorDefinition) itemRecord.getElement();
                    String uuid = ResourceHelper.getUUID(indDef);
                    if (IndicatorDefinitionFileHelper.isTechnialIndicator(uuid)) {
                        continue;
                    }
                }
                // TDQ-10933: Hide the Exchange node TDQ-11036: always hidden
                if (EResourceConstant.EXCHANGE.getName().equals(itemRecord.getName())) {
                    continue;
                }
                // TDQ-10933~

                // TDQ-9495: Hide the Reports node when top
                if (PluginChecker.isOnlyTopLoaded()) {
                    if (EResourceConstant.REPORTS.getName().equals(itemRecord.getName())) {
                        continue;
                    }
                }

            }
            result.add(itemRecord);
        }
        return result.toArray(new ItemRecord[result.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
     */
    public Object getParent(Object element) {
        if (element instanceof ItemRecord) {
            return ((ItemRecord) element).getParent();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
     */
    public boolean hasChildren(Object element) {
        if (element instanceof ItemRecord) {
            File[] listFiles = ((ItemRecord) element).getFile().listFiles();
            return listFiles != null && listFiles.length > 0;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements(Object inputElement) {
        return getChildren(inputElement);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     */
    public void dispose() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object,
     * java.lang.Object)
     */
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        ItemRecord.clear();
    }

}
