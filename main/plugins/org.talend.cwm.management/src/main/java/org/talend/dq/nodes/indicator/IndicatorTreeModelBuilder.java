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
package org.talend.dq.nodes.indicator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.talend.core.model.properties.Property;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.helper.UDIHelper;
import org.talend.dq.helper.resourcehelper.IndicatorResourceFileHelper;
import org.talend.dq.helper.resourcehelper.PatternResourceFileHelper;
import org.talend.dq.helper.resourcehelper.ResourceFileMap;
import org.talend.dq.nodes.indicator.impl.IndicatorCategoryNode;
import org.talend.dq.nodes.indicator.impl.PatternNode;
import org.talend.dq.nodes.indicator.impl.UDINode;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import org.talend.resource.ResourceManager;
import org.talend.utils.io.FilesUtils;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * This class for the indicator tree building.
 * 
 */
public final class IndicatorTreeModelBuilder {

    private static final String ADVANCED_LABEL = Messages.getString("IndicatorTreeModelBuilder.AdvancedStatistics"); //$NON-NLS-1$

    private static final String FRAUD_LABEL = Messages.getString("IndicatorTreeModelBuilder.FraudStatistics"); //$NON-NLS-1$

    private static final String PHONE_NUMBER_LABEL = Messages.getString("IndicatorTreeModelBuilder.PhonenumberStatistics"); //$NON-NLS-1$

    private static final String SOUNDEX_LABEL = Messages.getString("IndicatorTreeModelBuilder.SoundexStatistics"); //$NON-NLS-1$

    private static final String PATTERN_LABEL = Messages.getString("IndicatorTreeModelBuilder.PatternStatistics"); //$NON-NLS-1$

    private static final String SUMMARY_LABEL = Messages.getString("IndicatorTreeModelBuilder.SummaryStatistics"); //$NON-NLS-1$

    private static final String TEXT_LABEL = Messages.getString("IndicatorTreeModelBuilder.TextStatistics"); //$NON-NLS-1$

    private static final String SIMPLE_LABEL = Messages.getString("IndicatorTreeModelBuilder.SimpleStatistics"); //$NON-NLS-1$

    private static final String UDI_LABEL = Messages.getString("IndicatorTreeModelBuilder.UserDefIndicators"); //$NON-NLS-1$

    private static final String PATTERN = Messages.getString("IndicatorTreeModelBuilder.Patterns"); //$NON-NLS-1$

    private static final String REGEX_PATTERN = Messages.getString("IndicatorTreeModelBuilder.Regex_Patterns"); //$NON-NLS-1$

    private static final String SQL_PATTERN = Messages.getString("IndicatorTreeModelBuilder.Sql_Patterns"); //$NON-NLS-1$

    private IndicatorTreeModelBuilder() {
    }

    private static IndicatorCategoryNode[] indicatorCategoryNodes;

    /**
     * 
     * Get the root node
     * 
     * @return
     */
    public static IIndicatorNode getRootNode() {
        IndicatorCategoryNode rootNode = new IndicatorCategoryNode("root"); //$NON-NLS-1$
        rootNode.setChildren(buildIndicatorCategory());

        return rootNode;
    }

    /**
     * build Indicator Category. we DO NOT use the CACHE here, because sometimes, when the indicator definitions become
     * to Proxy, we can not get its property to get Label display on the select indicator dialog(can prefer to TDQ-8857)
     * 
     * @return
     */
    public static IIndicatorNode[] buildIndicatorCategory() {
        // if (indicatorCategoryNodes != null) {
        // return indicatorCategoryNodes;
        // }
        // build Basic Statistic categoryNode
        IndicatorCategoryNode simpleCategoryNode = new IndicatorCategoryNode(SIMPLE_LABEL, IndicatorEnum.CountsIndicatorEnum);

        // build Text statistics categoryNode
        IndicatorCategoryNode textCategoryNode = new IndicatorCategoryNode(TEXT_LABEL, IndicatorEnum.TextIndicatorEnum);

        // build Summary Statistic categoryNode
        IndicatorCategoryNode boxCategoryNode = new IndicatorCategoryNode(SUMMARY_LABEL, IndicatorEnum.BoxIIndicatorEnum);

        // build Pattern Frequency Statistics categoryNode
        IndicatorEnum[] patternFinderEnums = new IndicatorEnum[] { IndicatorEnum.PatternFreqIndicatorEnum,
                IndicatorEnum.PatternLowFreqIndicatorEnum, IndicatorEnum.EastAsiaPatternFreqIndicatorEnum,
                        IndicatorEnum.EastAsiaPatternLowFreqIndicatorEnum, IndicatorEnum.DatePatternFreqIndicatorEnum,
                        IndicatorEnum.CSWordPatternFreqIndicatorEnum, IndicatorEnum.CSWordPatternLowFreqIndicatorEnum,
                        IndicatorEnum.CIWordPatternFreqIndicatorEnum, IndicatorEnum.CIWordPatternLowFreqIndicatorEnum };
        IndicatorCategoryNode patternFinderCategoryNode = new IndicatorCategoryNode(PATTERN_LABEL, patternFinderEnums);

        // MOD mzhao 2009-03-05 build Soundex Statistic categoryNode
        IndicatorEnum[] soundexIndicatorEnums = new IndicatorEnum[] { IndicatorEnum.SoundexIndicatorEnum,
                IndicatorEnum.SoundexLowIndicatorEnum };
        IndicatorCategoryNode soundexCategoryNode = new IndicatorCategoryNode(SOUNDEX_LABEL, soundexIndicatorEnums);
        IndicatorCategoryNode phoneCategoryNode = new IndicatorCategoryNode(PHONE_NUMBER_LABEL,
                IndicatorEnum.PhoneNumbStatisticsIndicatorEnum);

        // build Nominal Statistic categoryNode
        IndicatorEnum[] advanceIndicatorEnums = new IndicatorEnum[] { IndicatorEnum.ModeIndicatorEnum,
                IndicatorEnum.FrequencyIndicatorEnum, IndicatorEnum.DateFrequencyIndicatorEnum,
                IndicatorEnum.WeekFrequencyIndicatorEnum, IndicatorEnum.MonthFrequencyIndicatorEnum,
                IndicatorEnum.QuarterFrequencyIndicatorEnum, IndicatorEnum.YearFrequencyIndicatorEnum,
                IndicatorEnum.BinFrequencyIndicatorEnum, IndicatorEnum.LowFrequencyIndicatorEnum,
                IndicatorEnum.DateLowFrequencyIndicatorEnum, IndicatorEnum.WeekLowFrequencyIndicatorEnum,
                IndicatorEnum.MonthLowFrequencyIndicatorEnum, IndicatorEnum.QuarterLowFrequencyIndicatorEnum,
                IndicatorEnum.YearLowFrequencyIndicatorEnum, IndicatorEnum.BinLowFrequencyIndicatorEnum };
        IndicatorCategoryNode advanceCategoryNode = new IndicatorCategoryNode(ADVANCED_LABEL, advanceIndicatorEnums);

        // Added yyin 20120827, TDQ-5076, build Fraud Detection category and SIndicator
        IndicatorEnum[] fraudIndicatorEnums = new IndicatorEnum[] { IndicatorEnum.BenfordLawFrequencyIndicatorEnum };
        IndicatorCategoryNode fraudCategoryNode = new IndicatorCategoryNode(FRAUD_LABEL, fraudIndicatorEnums);

        // Add UDIEnums
        IndicatorCategoryNode UDICategoryNode = new IndicatorCategoryNode(UDI_LABEL);
        UDICategoryNode.setChildren(createUDIChildrens());
        // Add PatternEnums
        IndicatorCategoryNode regexPatternCategoryNode = new IndicatorCategoryNode(REGEX_PATTERN);
        regexPatternCategoryNode.setChildren(createRegexPatternChildrens());
        IndicatorCategoryNode sqlPatternCategoryNode = new IndicatorCategoryNode(SQL_PATTERN);// new
        sqlPatternCategoryNode.setChildren(createSqlPatternChildrens()); // IndicatorEnum[]
        // {IndicatorEnum.SqlPatternMatchingIndicatorEnum
        // });

        IndicatorCategoryNode patternCategoryNode = new IndicatorCategoryNode(PATTERN,
                new IndicatorEnum[] { IndicatorEnum.PatternIndicatorEnum });

        patternCategoryNode.setChildren(new IIndicatorNode[] { sqlPatternCategoryNode, regexPatternCategoryNode });
        indicatorCategoryNodes = new IndicatorCategoryNode[] { simpleCategoryNode, textCategoryNode, boxCategoryNode,
                advanceCategoryNode, patternFinderCategoryNode, soundexCategoryNode, phoneCategoryNode, fraudCategoryNode,
                UDICategoryNode, patternCategoryNode };
        return indicatorCategoryNodes;
    }

    /**
     * DOC talend Comment method "createSqlPatternChildrens".
     * 
     * @return
     */
    private static IIndicatorNode[] createSqlPatternChildrens() {
        return getNestFolderNodes(ResourceManager.getPatternSQLFolder(), IndicatorEnum.SqlPatternMatchingIndicatorEnum,
                PatternResourceFileHelper.getInstance());
    }

    /**
     * 
     * Create IIndicatorNode for every folder or file which is valid
     * 
     * @param folder
     * @param indiEnum
     * @param resourceMap
     * @return nodes if folder is not empty else return empty array
     */
    public static IIndicatorNode[] getNestFolderNodes(IFolder folder, IndicatorEnum indiEnum, ResourceFileMap resourceMap) {
        List<IIndicatorNode> chilren = new ArrayList<IIndicatorNode>();
        try {
            if (UDIHelper.isUDILibFolder(folder)) {
                return new IIndicatorNode[0];
            }
            for (IResource resource : folder.members()) {
                if (resource instanceof IFile) {
                    IFile iFile = (IFile) resource;
                    // TDQ-11217: ignore ".gitkeep" file under the git remote project's folders
                    if (iFile.getName().equalsIgnoreCase(FilesUtils.GITKEEP)) {
                        continue;
                    }
                    // TDQ-11217~

                    ModelElement modelElement = resourceMap.getModelElement(iFile);
                    if (modelElement == null) {
                        continue;
                    }
                    Property property = PropertyHelper.getProperty(modelElement);
                    if (property == null) {
                        continue;
                    }
                    IIndicatorNode indicatorNode = createLeafNode(property.getDisplayName(), indiEnum, modelElement);
                    if (indicatorNode == null) {
                        continue;
                    }
                    chilren.add(indicatorNode);
                } else {
                    ICategoryNode indicatorCateNode = new IndicatorCategoryNode(resource.getName());
                    IIndicatorNode[] nestFolderNodes = getNestFolderNodes((IFolder) resource, indiEnum, resourceMap);
                    if (nestFolderNodes.length > 0) {
                        indicatorCateNode.setChildren(nestFolderNodes);
                        chilren.add(indicatorCateNode);
                    }
                }
            }
        } catch (CoreException e) {
        }
        return chilren.toArray(new IIndicatorNode[chilren.size()]);
    }

    /**
     * Create leafNode by the type of indiEnum
     * 
     * @param label
     * @param indiEnum
     * @return
     */
    private static IIndicatorNode createLeafNode(String label, IndicatorEnum indiEnum, ModelElement modelElement) {
        IIndicatorNode indicatorNode = null;
        if (IndicatorEnum.RegexpMatchingIndicatorEnum == indiEnum || IndicatorEnum.SqlPatternMatchingIndicatorEnum == indiEnum) {
            indicatorNode = new PatternNode(label, (Pattern) modelElement);
        } else if (IndicatorEnum.UserDefinedIndicatorEnum == indiEnum) {
            indicatorNode = new UDINode(label);
        } else {
            return indicatorNode;
        }
        indicatorNode.setIndicatorEnum(indiEnum);
        return indicatorNode;
    }

    /**
     * DOC talend Comment method "createRegexPatternChildrens".
     * 
     * @return
     */
    private static IIndicatorNode[] createRegexPatternChildrens() {
        return getNestFolderNodes(ResourceManager.getPatternRegexFolder(), IndicatorEnum.RegexpMatchingIndicatorEnum,
                PatternResourceFileHelper.getInstance());
    }

    /**
     * Init and return all of user define indicators
     * 
     * @return
     */
    private static IIndicatorNode[] createUDIChildrens() {
        return getNestFolderNodes(ResourceManager.getUDIFolder(), IndicatorEnum.UserDefinedIndicatorEnum,
                IndicatorResourceFileHelper.getInstance());
    }
}
