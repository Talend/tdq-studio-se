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
package org.talend.dq.helper;

import java.io.IOException;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.dataprofiler.service.IStoreOnDiskService;
import org.talend.dataquality.indicators.columnset.BlockKeyIndicator;
import org.talend.dataquality.indicators.columnset.RecordMatchingIndicator;
import org.talend.utils.sugars.TypedReturnCode;

/**
 * created by yyin on 2015年8月13日 Detailled comment
 *
 */
public class StoreOnDiskUtils extends AbstractOSGIServiceUtils {

    private IStoreOnDiskService sdService;

    private static StoreOnDiskUtils sdUtils;

    public static StoreOnDiskUtils getDefault() {
        if (sdUtils == null) {
            sdUtils = new StoreOnDiskUtils();
        }
        return sdUtils;
    }

    @Override
    public String getServiceName() {
        return IStoreOnDiskService.class.getName();
    }

    @Override
    public boolean isServiceInstalled() {
        return getStoreOnDiskService() != null;
    }

    IStoreOnDiskService getStoreOnDiskService() {
        if (this.sdService == null) {
            initService(false);
        }
        return this.sdService;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void setService(BundleContext context, ServiceReference serviceReference) {
        Object obj = context.getService(serviceReference);
        if (obj != null) {
            this.sdService = (IStoreOnDiskService) obj;
        }
    }

    public Object createStoreOnDiskHandler(String tempDataPath, int bufferSize, RecordMatchingIndicator recordMatchingIndicator,
            Map<MetadataColumn, String> columnMap) throws IOException {
        if (getStoreOnDiskService() != null) {
            return getStoreOnDiskService().createStoreOnDiskHandler(tempDataPath, bufferSize, recordMatchingIndicator, columnMap);
        }
        return null;
    }

    public void beginQuery(Object storeOnDiskHandler) throws Exception {
        if (getStoreOnDiskService() != null) {
            getStoreOnDiskService().beginQuery(storeOnDiskHandler);
        }
    }

    public void handleRow(Object[] oneRow, Object storeOnDiskHandler) throws Exception {
        if (getStoreOnDiskService() != null) {
            getStoreOnDiskService().handleRow(oneRow, storeOnDiskHandler);
        }
    }

    public void endQuery(Object storeOnDiskHandler) throws Exception {
        if (getStoreOnDiskService() != null) {
            getStoreOnDiskService().endQuery(storeOnDiskHandler);
        }
    }

    public TypedReturnCode<Object> executeWithStoreOnDisk(Map<MetadataColumn, String> columnMap,
            RecordMatchingIndicator recordMatchingIndicator, BlockKeyIndicator blockKeyIndicator, Object storeOnDiskHandler,
            Object matchResultConsumer) throws Exception {
        if (getStoreOnDiskService() != null) {
            return getStoreOnDiskService().executeWithStoreOnDisk(columnMap, recordMatchingIndicator, blockKeyIndicator,
                    storeOnDiskHandler, matchResultConsumer);
        } else {
            TypedReturnCode<Object> returnCode = new TypedReturnCode<Object>(false);
            returnCode.setMessage("The Store on Disk Service is null.");
            returnCode.setOk(false);
            return returnCode;
        }
    }

    @Override
    public String getPluginName() {
        return null;
    }

    @Override
    protected String getMissingMessageName() {
        return null;
    }

    @Override
    protected String getRestartMessageName() {
        return null;
    }
}
