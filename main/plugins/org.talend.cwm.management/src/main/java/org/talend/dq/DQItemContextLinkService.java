// ============================================================================
//
// Copyright (C) 2006-2020 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dq;

import java.io.InputStream;
import java.util.List;

import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.context.link.AbstractItemContextLinkService;
import org.talend.core.model.context.link.ContextLinkService;
import org.talend.core.model.context.link.ItemContextLink;
import org.talend.core.model.properties.Item;
import org.talend.dataquality.properties.TDQAnalysisItem;
import org.talend.dataquality.properties.TDQReportItem;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.dq.helper.ContextHelper;

/**
 * created by msjian on 2020年5月6日
 * this class is used for save DQ item's context link
 *
 */
public class DQItemContextLinkService extends AbstractItemContextLinkService {

    @Override
    public boolean mergeItemLink(Item item, ItemContextLink backupContextLink, InputStream remoteLinkFileInput)
            throws PersistenceException {
        ItemContextLink remoteContextLink =
                ContextLinkService.getInstance().doLoadContextLinkFromFile(remoteLinkFileInput);
        List<ContextType> contextTypeList = ContextHelper.getAllContextType(item);
        return super.saveContextLink(contextTypeList, item, backupContextLink, remoteContextLink);
    }
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.context.link.IItemContextLinkService#accept(org.talend.core.model.properties.Item)
     */
    @Override
    public boolean accept(Item item) {
        if (item instanceof TDQAnalysisItem || item instanceof TDQReportItem) {
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.context.link.IItemContextLinkService#saveItemLink(org.talend.core.model.properties.Item)
     */
    @Override
    public boolean saveItemLink(Item item) throws PersistenceException {
        // here item can only be TDQAnalysisItem or TDQReportItem
        // for ConnectionItem, will do save at ConnectionItemContextLinkService
        List<ContextType> contextTypeList = ContextHelper.getAllContextType(item);
        return super.saveContextLink(contextTypeList, item);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.context.link.IItemContextLinkService#loadItemLink(org.talend.core.model.properties.Item)
     */
    @Override
    public ItemContextLink loadItemLink(Item item) throws PersistenceException {
        return ContextLinkService.getInstance().doLoadContextLinkFromJson(item);
    }

}
