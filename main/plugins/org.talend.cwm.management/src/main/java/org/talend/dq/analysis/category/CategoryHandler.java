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
package org.talend.dq.analysis.category;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.talend.commons.emf.EMFUtil;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.analysis.category.AnalysisCategories;
import org.talend.dataquality.analysis.category.util.CategorySwitch;
import org.talend.dataquality.helpers.IndicatorCategoryHelper;
import org.talend.dataquality.indicators.definition.DefinitionFactory;
import org.talend.dataquality.indicators.definition.IndicatorCategory;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.indicators.definitions.DefinitionHandler;

/**
 * @author scorreia
 * 
 * This class is a handler for the categories of analysis. A tree structure of categories is loaded from a configuration
 * file.
 */
public final class CategoryHandler {

    private static Logger log = Logger.getLogger(CategoryHandler.class);

    private static AnalysisCategories analysisCategories;

    private static List<String> shouldNotIncludeIndicatorDefinitionUuidList;

    private static final String CATEGORY_OTHER = "Others";//$NON-NLS-1$

    private static IndicatorCategory indicatorCategory;

    private static Map<IndicatorCategory, List<IndicatorDefinition>> categoriesIDMaps;

    private CategoryHandler() {
    }

    /**
     * DOC bZhou Comment method "loadFromFile".
     * 
     * @return
     */
    private static AnalysisCategories loadFromFile() {
        EMFUtil util = new EMFUtil();
        String pathName = "/org.talend.cwm.management/My.category"; //$NON-NLS-1$
        URI uri = URI.createPlatformPluginURI(pathName, false);
        Resource catFile = null;
        try {
            catFile = util.getResourceSet().getResource(uri, true);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
        }
        if (catFile == null) {
            // try to load from a local file
            catFile = util.getResourceSet().getResource(URI.createFileURI(".." + File.separator + pathName), true); //$NON-NLS-1$
        }
        if (catFile == null) {
            log.error(Messages.getString("CategoryHandler.NORESOUTCEFOUND", pathName, uri));//$NON-NLS-1$
            return null;
        }
        EList<EObject> contents = catFile.getContents();
        if (contents == null) {
            log.error(Messages.getString("CategoryHandler.NOCATALOGFOUND", uri));//$NON-NLS-1$
            return null;
        }
        CategorySwitch<AnalysisCategories> catSwitch = new CategorySwitch<AnalysisCategories>() {

            @Override
            public AnalysisCategories caseAnalysisCategories(AnalysisCategories object) {
                return object;
            }
        };

        return catSwitch.doSwitch(contents.get(0));
    }

    /**
     * Method "getAnalysisCategories".
     * 
     * @return the singleton analysis categories (or null if a problem occured)
     */
    public static AnalysisCategories getAnalysisCategories() {
        if (analysisCategories == null) {
            analysisCategories = loadFromFile();
        }

        return analysisCategories;
    }

    /**
     * DOC bZhou Comment method "getIndicatorCategory".
     * 
     * Get the current category.
     * 
     * @return if null, return an empty category.
     */
    private static IndicatorCategory getIndicatorCategory() {
        if (indicatorCategory == null) {
            indicatorCategory = DefinitionFactory.eINSTANCE.createIndicatorCategory();
            indicatorCategory.setLabel(CATEGORY_OTHER);
            indicatorCategory.setName(CATEGORY_OTHER);
        }
        return indicatorCategory;
    }

    /**
     * DOC bZhou Comment method "getCategoriesIDMaps".
     * 
     * impact definition to category.
     * 
     * @return a map in structure.
     */
    public static Map<IndicatorCategory, List<IndicatorDefinition>> getCategoriesIDMaps() {
        if (categoriesIDMaps == null) {
            categoriesIDMaps = buildCategories();
        }
        return categoriesIDMaps;
    }

    /**
     * DOC bZhou Comment method "getIndicatorDefinitionList".
     * 
     * @param category
     * @return
     */
    public static List<IndicatorDefinition> getIndicatorDefinitionList(IndicatorCategory category) {
        if (category != null && getCategoriesIDMaps() != null) {
            return getCategoriesIDMaps().get(category);
        }
        return null;
    }

    /**
     * DOC bZhou Comment method "getShouldNotIncludeIndicatorDefinitionUuidList".
     * 
     * Get no need to display indicator definitions.
     * 
     * FIXME lazy initialization of a static field.
     * 
     * @return
     */
    public static List<String> getShouldNotIncludeIndicatorDefinitionUuidList() {
        if (shouldNotIncludeIndicatorDefinitionUuidList == null) {
            shouldNotIncludeIndicatorDefinitionUuidList = new ArrayList<String>();
            // Overview \/
            shouldNotIncludeIndicatorDefinitionUuidList.add("_nZEo8MYSEd27NP4lvE0A4w"); // Connection Overview //$NON-NLS-1$
            shouldNotIncludeIndicatorDefinitionUuidList.add("_QwDiwMYUEd27NP4lvE0A4w"); // Catalog Overview //$NON-NLS-1$
            shouldNotIncludeIndicatorDefinitionUuidList.add("_V4SA0MYUEd27NP4lvE0A4w"); // Schema Overview //$NON-NLS-1$
            shouldNotIncludeIndicatorDefinitionUuidList.add("_hgO7YMYUEd27NP4lvE0A4w"); // Table Overview //$NON-NLS-1$
            shouldNotIncludeIndicatorDefinitionUuidList.add("_lNIE0MbNEd2d_JPxxDRSfQ"); // View Overview //$NON-NLS-1$
            // composite \/
            //            shouldNotIncludeIndicatorDefinitionUuidList.add("_ccHq1BF2Ed2PKb6nEJEvhw"); // Minimal Length //$NON-NLS-1$
            //            shouldNotIncludeIndicatorDefinitionUuidList.add("_ccHq1RF2Ed2PKb6nEJEvhw"); // Maximal Length //$NON-NLS-1$
            //            shouldNotIncludeIndicatorDefinitionUuidList.add("_ccIR4BF2Ed2PKb6nEJEvhw"); // Average Length //$NON-NLS-1$

            shouldNotIncludeIndicatorDefinitionUuidList.add("_ccJgAhF2Ed2PKb6nEJEvhw"); // SUM //$NON-NLS-1$
            shouldNotIncludeIndicatorDefinitionUuidList.add("_vf0k4PkbEd2z55b7dTkWFw"); // Multiple Column Correlation //$NON-NLS-1$
            shouldNotIncludeIndicatorDefinitionUuidList.add("_UUIyoCOMEd6YB57jaCfKaA"); // DQ Rule //$NON-NLS-1$
            shouldNotIncludeIndicatorDefinitionUuidList.add("_10gx4JxcEd2YicTszQEJLA"); // Multiple Column Frequency //$NON-NLS-1$
            // Table
            // Summary Statistics \/
            // shouldNotIncludeIndicatorDefinitionUuidList.add("_ccI48RF2Ed2PKb6nEJEvhw"); // Mean
            // shouldNotIncludeIndicatorDefinitionUuidList.add("_ccI48hF2Ed2PKb6nEJEvhw"); // Median
            // shouldNotIncludeIndicatorDefinitionUuidList.add("_ccI48xF2Ed2PKb6nEJEvhw"); // Inter Quartile Range
            // shouldNotIncludeIndicatorDefinitionUuidList.add("_ccI49BF2Ed2PKb6nEJEvhw"); // Lower Quartile
            // shouldNotIncludeIndicatorDefinitionUuidList.add("_ccI49RF2Ed2PKb6nEJEvhw"); // Upper Quartile
            // shouldNotIncludeIndicatorDefinitionUuidList.add("_ccI49hF2Ed2PKb6nEJEvhw"); // Range
            // shouldNotIncludeIndicatorDefinitionUuidList.add("_ccJgABF2Ed2PKb6nEJEvhw"); // Minimum
            // shouldNotIncludeIndicatorDefinitionUuidList.add("_ccJgARF2Ed2PKb6nEJEvhw"); // Maximum
        }
        return shouldNotIncludeIndicatorDefinitionUuidList;
    }

    /**
     * DOC bZhou Comment method "getShowIndicatorsDefinitions".
     * 
     * @return
     */
    public static List<IndicatorDefinition> getShowIndicatorsDefinitions() {
        // if (showIndicatorDefinitions == null) {
        ArrayList<IndicatorDefinition> showIndicatorDefinitions = new ArrayList<IndicatorDefinition>();

        List<IndicatorDefinition> indicatorDefinitions = DefinitionHandler.getInstance().getIndicatorsDefinitions();

        for (IndicatorDefinition indicatorDefinition : indicatorDefinitions) {
            if (!indicatorDefinition.getSqlGenericExpression().isEmpty()
                    && !getShouldNotIncludeIndicatorDefinitionUuidList().contains(ResourceHelper.getUUID(indicatorDefinition))) {
                showIndicatorDefinitions.add(indicatorDefinition);
            }
        }

        // }
        return showIndicatorDefinitions;
    }

    /**
     * DOC xqliu Comment method "buildCategories".
     * 
     * @return
     */
    private static Map<IndicatorCategory, List<IndicatorDefinition>> buildCategories() {
        Map<IndicatorCategory, List<IndicatorDefinition>> categories = new HashMap<IndicatorCategory, List<IndicatorDefinition>>();

        for (IndicatorDefinition indicatorDefinition : getShowIndicatorsDefinitions()) {
            IndicatorCategory category = IndicatorCategoryHelper.getCategory(indicatorDefinition);
            if (category == null) {
                category = getIndicatorCategory();
            }
            if (category != null) {
                List<IndicatorDefinition> list = categories.get(category);
                if (list == null) {
                    List<IndicatorDefinition> tempList = new ArrayList<IndicatorDefinition>();
                    tempList.add(indicatorDefinition);
                    categories.put(category, tempList);
                } else {
                    list.add(indicatorDefinition);
                }
            }
        }

        return categories;
    }

    /**
     * Get i18n label in resource bundle. 2010-03-16 yyi 11739
     * 
     * @param key
     * @return
     */
    public static String getLabel(String categoryLabel) {

        String messageKey = Messages.getString("AnalysisType." + categoryLabel.replaceAll("\\s*", PluginConstant.EMPTY_STRING));//$NON-NLS-1$//$NON-NLS-2$
        if (messageKey.startsWith(Messages.KEY_NOT_FOUND_PREFIX) && messageKey.endsWith(Messages.KEY_NOT_FOUND_SUFFIX)) {
            return categoryLabel;
        }
        return messageKey;
    }
}
