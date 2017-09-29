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
package org.talend.dataprofiler.core.migration.impl;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataprofiler.core.ui.utils.UDIUtils;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.definition.userdefine.UDIndicatorDefinition;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.resource.EResourceConstant;
import org.talend.resource.ResourceService;

/**
 * Update UDI Indicators With the New Model from version 5.3.
 * 
 * @author msjian 2013-2-25 TDQ-6872
 * 
 */
public class UpdateUDIIndicatorsWithNewModelTask extends AbstractWorksapceUpdateTask {

    Logger log = Logger.getLogger(UpdateUDIIndicatorsWithNewModelTask.class);

    private static String old_one = "xmlns:dataquality.indicators.definition=\"http://dataquality.indicators.definition\""; //$NON-NLS-1$

    private static String new_one = "xmlns:dataquality.indicators.definition.userdefine=\"http://dataquality.indicators.definition.userdefine\""; //$NON-NLS-1$

    private static String old_two = "dataquality.indicators.definition:IndicatorDefinition"; //$NON-NLS-1$

    private static String new_two = "dataquality.indicators.definition.userdefine:UDIndicatorDefinition"; //$NON-NLS-1$

    public Date getOrder() {
        return createDate(2013, 2, 25);
    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    @Override
    protected boolean doExecute() throws Exception {
        boolean result = true;
        File udiIndicatorFolder = getWorkspacePath().append(EResourceConstant.USER_DEFINED_INDICATORS.getPath()).toFile();

        String[] indicatorFileExtentionName = { FactoriesUtil.DEFINITION };

        try {
            result &= FilesUtils.migrateFolder(udiIndicatorFolder, indicatorFileExtentionName, initIndicatorReplaceMap(), log);
            if (isWorksapcePath()) {
                for (IRepositoryViewObject viewObject : ProxyRepositoryFactory.getInstance().getAll(
                        ERepositoryObjectType.TDQ_USERDEFINE_INDICATORS)) {
                    ProxyRepositoryFactory.getInstance().reload(viewObject.getProperty());
                }
            }
            ResourceService.refreshStructure();
        } catch (Exception e) {
            result = false;
            log.error(e, e);
        }

        List<IndicatorDefinition> indiDefinitions = DefinitionHandler.getInstance().getUserDefinedIndicatorDefinitions();
        for (IndicatorDefinition indiDefinition : indiDefinitions) {
            if (indiDefinition != null) {
                if (indiDefinition instanceof UDIndicatorDefinition) {
                    UDIndicatorDefinition udi = (UDIndicatorDefinition) indiDefinition;
                    udi = UDIUtils.createDefaultDrillDownList(udi);
                    result &= ElementWriterFactory.getInstance().createIndicatorDefinitionWriter().save(udi).isOk();
                }
            }
        }
        DefinitionHandler.getInstance().reloadIndicatorsDefinitions();
        return result;
    }

    /**
     * DOC msjian Comment method "initIndicatorReplaceMap".
     * 
     * @return
     */
    public static Map<String, String> initIndicatorReplaceMap() {

        Map<String, String> replaceStringMap = new HashMap<String, String>();

        replaceStringMap.put(old_one, new_one);
        replaceStringMap.put(old_two, new_two);

        return replaceStringMap;
    }
}
