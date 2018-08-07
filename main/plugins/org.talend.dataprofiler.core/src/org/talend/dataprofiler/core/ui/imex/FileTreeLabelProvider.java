// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
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

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.ui.imex.model.ItemRecord;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.resource.EResourceConstant;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class FileTreeLabelProvider extends LabelProvider {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
     */
    @Override
    public Image getImage(Object element) {
        Image image = null;
        if (element instanceof ItemRecord) {
            ItemRecord record = (ItemRecord) element;
            File file = record.getFile();
            String fileName = file.getName();
            if (file.isDirectory()) {
                image = ImageLib.getImage(ImageLib.FOLDERNODE_IMAGE);

                EResourceConstant constant = resolveResourceConstant(fileName);
                if (constant != null) {
                    switch (constant) {
                    case DATA_PROFILING:
                        image = ImageLib.getImage(ImageLib.DATA_PROFILING);
                        break;
                    case METADATA:
                        image = ImageLib.getImage(ImageLib.METADATA);
                        break;
                    case LIBRARIES:
                        image = ImageLib.getImage(ImageLib.LIBRARIES);
                        break;
                    case ANALYSIS:
                        break;
                    case REPORTS:
                        break;
                    case EXCHANGE:
                        image = ImageLib.getImage(ImageLib.EXCHANGE);
                        break;
                    case DB_CONNECTIONS:
                        image = ImageLib.getImage(ImageLib.CONNECTION);
                        break;
                    case FILEDELIMITED:
                        image = ImageLib.getImage(ImageLib.FILE_DELIMITED);
                        break;
                    case HADOOP_CLUSTER:
                        image = ImageLib.getImage(ImageLib.HADOOP_CLUSTER);
                        break;
                    case CONTEXT:
                        image = ImageLib.getImage(ImageLib.CONTEXT);
                        break;
                    default:
                        break;
                    }
                }
            } else {
                if (fileName.endsWith(FactoriesUtil.ANA)) {
                    image = ImageLib.getImage(ImageLib.ANALYSIS_OBJECT);
                } else if (fileName.endsWith(FactoriesUtil.REP)) {
                    image = ImageLib.getImage(ImageLib.REPORT_OBJECT);
                } else if (fileName.endsWith(FactoriesUtil.PATTERN)) {
                    image = ImageLib.getImage(ImageLib.PATTERN_REG);
                } else if (fileName.endsWith(FactoriesUtil.DQRULE)) {
                    if (record.getElement() instanceof MatchRuleDefinition) {
                        image = ImageLib.getImage(ImageLib.MATCH_RULE_ICON);
                    } else {
                        image = ImageLib.getImage(ImageLib.DQ_RULE);
                    }
                } else if (fileName.endsWith(FactoriesUtil.ITEM_EXTENSION)) {
                    if (record.getElement() instanceof DelimitedFileConnection) {
                        image = ImageLib.getImage(ImageLib.FILE_DELIMITED);
                    } else if (record.getElement() instanceof DatabaseConnection) {
                        image = ImageProvider.getImage(ECoreImage.METADATA_CONNECTION_ICON);
                    } else {
                        image = ImageLib.getImage(ImageLib.CONTEXT);
                    }
                } else if (fileName.endsWith(FactoriesUtil.DEFINITION)) {
                    image = ImageLib.getImage(ImageLib.IND_DEFINITION);
                } else if (fileName.endsWith(FactoriesUtil.SQL)) {
                    image = ImageLib.getImage(ImageLib.SOURCE_FILE);
                } else if (fileName.endsWith(FactoriesUtil.JAR)) {
                    image = ImageLib.getImage(ImageLib.JAR_FILE);
                } else if (fileName.endsWith(FactoriesUtil.JRXML)) {
                    image = ImageLib.getImage(ImageLib.JRXML_ICON);
                }
            }
        }

        return image != null ? image : super.getImage(element);
    }

    /**
     * DOC bZhou Comment method "resolveResourceConstant".
     * 
     * @param fileName
     * @return
     */
    private EResourceConstant resolveResourceConstant(String fileName) {
        for (EResourceConstant constant : EResourceConstant.values()) {
            if (StringUtils.equals(constant.getName(), fileName)) {
                return constant;
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
     */
    @Override
    public String getText(Object element) {
        if (element instanceof ItemRecord) {
            ItemRecord record = (ItemRecord) element;
            return record.getName();
        }
        return super.getText(element);
    }

}
