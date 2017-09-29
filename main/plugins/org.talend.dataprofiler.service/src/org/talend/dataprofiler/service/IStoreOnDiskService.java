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
package org.talend.dataprofiler.service;

import java.io.IOException;
import java.util.Map;

import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.dataquality.indicators.columnset.BlockKeyIndicator;
import org.talend.dataquality.indicators.columnset.RecordMatchingIndicator;
import org.talend.utils.sugars.TypedReturnCode;

/**
 * created by yyin on 2015年8月13日 Detailled comment
 *
 */
public interface IStoreOnDiskService {

    Object createStoreOnDiskHandler(String tempDataPath, int bufferSize, RecordMatchingIndicator recordMatchingIndicator,
            Map<MetadataColumn, String> columnMap) throws IOException;

    void beginQuery(Object storeOnDiskHandler) throws Exception;

    void handleRow(Object[] oneRow, Object storeOnDiskHandler) throws Exception;

    void endQuery(Object storeOnDiskHandler) throws Exception;

    TypedReturnCode<Object> executeWithStoreOnDisk(Map<MetadataColumn, String> columnMap,
            RecordMatchingIndicator recordMatchingIndicator, BlockKeyIndicator blockKeyIndicator, Object storeOnDiskHandler,
            Object matchResultConsumer) throws Exception;
}
