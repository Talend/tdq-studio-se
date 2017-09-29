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
package org.talend.dataprofiler.core.pattern;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.exception.TalendInternalPersistenceException;
import org.talend.core.model.properties.TDQItem;
import org.talend.cwm.constants.DevelopmentStatus;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.action.actions.ImportObject;
import org.talend.dataprofiler.core.ui.imex.ImportFromExchangeWizard;
import org.talend.dataprofiler.core.ui.utils.UDIUtils;
import org.talend.dataprofiler.core.ui.wizard.parserrule.ParserRuleToExcelEnum;
import org.talend.dataprofiler.ecos.model.IEcosComponent;
import org.talend.dataquality.domain.pattern.ExpressionType;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.domain.pattern.PatternFactory;
import org.talend.dataquality.domain.pattern.RegularExpression;
import org.talend.dataquality.helpers.BooleanExpressionHelper;
import org.talend.dataquality.helpers.MetadataHelper;
import org.talend.dataquality.indicators.definition.DefinitionFactory;
import org.talend.dataquality.indicators.definition.IndicatorCategory;
import org.talend.dataquality.indicators.definition.IndicatorDefinitionParameter;
import org.talend.dataquality.indicators.definition.userdefine.UDIndicatorDefinition;
import org.talend.dataquality.rules.ParserRule;
import org.talend.dq.dqrule.DqRuleBuilder;
import org.talend.dq.factory.ModelElementFileFactory;
import org.talend.dq.helper.FileUtils;
import org.talend.dq.helper.UDIHelper;
import org.talend.dq.helper.resourcehelper.DQRuleResourceFileHelper;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.resource.EResourceConstant;
import org.talend.resource.ResourceManager;
import org.talend.utils.sugars.ReturnCode;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.objectmodel.core.ModelElement;

import com.talend.csv.CSVReader;

/**
 * DOC zqin class global comment. Detailled comment
 */
public final class ImportFactory {

    protected static Logger log = Logger.getLogger(ImportFactory.class);

    private ImportFactory() {

    }

    /**
     * DOC bZhou Comment method "doImport".
     * 
     * @param resourceType
     * @param importFile
     * @return
     * @deprecated
     */
    @Deprecated
    public static List<ReturnCode> doImport(EResourceConstant resourceType, File importFile) {
        return doImport(resourceType, ImportObject.createImportObject(importFile, null), null);
    }

    /**
     * DOC xqliu Comment method "doImport".
     * 
     * @param resourceType
     * @param importFile
     * @param importItemName
     * @return
     * @deprecated use doImport(EResourceConstant, ImportObject, importItemName) instead of
     */
    @Deprecated
    public static List<ReturnCode> doImport(EResourceConstant resourceType, File importFile, String importItemName) {
        return doImport(resourceType, ImportObject.createImportObject(importFile, null), true, true, importItemName);
    }

    /**
     * DOC xqliu Comment method "doImport".
     * 
     * @param resourceType
     * @param importObject
     * @param importItemName
     * @return
     */
    public static List<ReturnCode> doImport(EResourceConstant resourceType, ImportObject importObject, String importItemName) {
        return doImport(resourceType, importObject, true, true, importItemName);
    }

    /**
     * DOC bZhou Comment method "doImport".
     * 
     * @param category
     * @param importFile
     * @return
     * @deprecated
     */
    @Deprecated
    public static List<ReturnCode> doImport(EResourceConstant resourceType, File importFile, boolean skip, boolean rename) {
        return doImport(resourceType, ImportObject.createImportObject(importFile, null), skip, rename, null);
    }

    /**
     * DOC xqliu Comment method "doImport".
     * 
     * @param resourceType
     * @param importFile
     * @param skip
     * @param rename
     * @param importItemName
     * @return
     * @deprecated use doImport(EResourceConstant, ImportObject, boolean, boolean, String) instead of
     */
    @Deprecated
    public static List<ReturnCode> doImport(EResourceConstant resourceType, File importFile, boolean skip, boolean rename,
            String importItemName) {
        assert resourceType != null;

        IFolder restoreFolder = ResourceManager.getOneFolder(resourceType);

        switch (resourceType) {
        case PATTERN_REGEX:
            return importToStucture(importFile, restoreFolder, ExpressionType.REGEXP, skip, rename, importItemName);
        case PATTERN_SQL:
            return importToStucture(importFile, restoreFolder, ExpressionType.SQL_LIKE, skip, rename, importItemName);
        case USER_DEFINED_INDICATORS:
            return importIndicatorToStucture(importFile, restoreFolder, skip, rename, importItemName);
        case RULES_PARSER:
            return importParserRuleToStucture(importFile, restoreFolder, skip, rename, importItemName);
        default:
            return null;
        }
    }

    /**
     * DOC xqliu Comment method "doImport".
     * 
     * @param resourceType
     * @param importObject
     * @param skip
     * @param rename
     * @param importItemName
     * @return
     */
    public static List<ReturnCode> doImport(EResourceConstant resourceType, ImportObject importObject, boolean skip,
            boolean rename, String importItemName) {
        assert resourceType != null;

        IFolder restoreFolder = ResourceManager.getOneFolder(resourceType);

        switch (resourceType) {
        case PATTERN_REGEX:
            return importToStucture(importObject, restoreFolder, ExpressionType.REGEXP, skip, rename, importItemName);
        case PATTERN_SQL:
            return importToStucture(importObject, restoreFolder, ExpressionType.SQL_LIKE, skip, rename, importItemName);
        case USER_DEFINED_INDICATORS:
            return importIndicatorToStucture(importObject, restoreFolder, skip, rename, importItemName);
        case RULES_PARSER:
            return importParserRuleToStucture(importObject, restoreFolder, skip, rename, importItemName);
        default:
            return null;
        }
    }

    /**
     * DOC xqliu Comment method "importToStucture".
     * 
     * @param importFile
     * @param selectionFolder
     * @param type
     * @param skip
     * @param rename
     * @return
     * @deprecated
     */
    @Deprecated
    public static List<ReturnCode> importToStucture(File importFile, IFolder selectionFolder, ExpressionType type, boolean skip,
            boolean rename) {
        return importToStucture(importFile, selectionFolder, type, skip, rename, null);
    }

    /**
     * DOC xqliu Comment method "importToStucture".
     * 
     * @param importFile
     * @param selectionFolder
     * @param type
     * @param skip
     * @param rename
     * @param importItemName
     * @return
     * @deprecated
     */
    @Deprecated
    public static List<ReturnCode> importToStucture(File importFile, IFolder selectionFolder, ExpressionType type, boolean skip,
            boolean rename, String importItemName) {
        return importToStucture(ImportObject.createImportObject(importFile, null), selectionFolder, type, skip, rename,
                importItemName);
    }

    /**
     * DOC xqliu Comment method "importToStucture".
     * 
     * @param importObject
     * @param selectionFolder
     * @param type
     * @param skip
     * @param rename
     * @param importItemName
     * @return
     */
    public static List<ReturnCode> importToStucture(ImportObject importObject, IFolder selectionFolder, ExpressionType type,
            boolean skip, boolean rename, String importItemName) {

        List<ReturnCode> importEvent = new ArrayList<ReturnCode>();

        // MOD qiongli 2012-12-20 TDQ-5899(issue 2),should get all patterns from Pattern folder.
        Set<String> names = PatternUtilities.getNestFolderPatternNames(new HashSet<String>(), ResourceManager.getPatternFolder());

        File importFile = importObject.getObjFile();

        String fileExtName = getFileExtName(importFile);

        if (FileUtils.isCSV(fileExtName)) {
            try {
                CSVReader reader = FileUtils.createCSVReader(importFile, FileUtils.TEXT_QUAL, FileUtils.TEXT_QUAL);
                reader.setSkipEmptyRecords(true);
                reader.readHeaders();
                while (reader.readNext()) {

                    String name = reader.get(PatternToExcelEnum.Label.getLiteral());

                    if (names.contains(name)) {
                        if (skip) {
                            importEvent.add(new ReturnCode(
                                    DefaultMessagesImpl.getString("ImportFactory.patternInported", name), false)); //$NON-NLS-1$
                            continue;
                        }
                        if (rename) {
                            name = name + "(" + new Date() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
                        }
                    }

                    PatternParameters patternParameters = new ImportFactory().new PatternParameters();
                    patternParameters.name = name;
                    patternParameters.auther = reader.get(PatternToExcelEnum.Author.getLiteral());
                    patternParameters.description = reader.get(PatternToExcelEnum.Description.getLiteral());
                    patternParameters.purpose = reader.get(PatternToExcelEnum.Purpose.getLiteral());
                    patternParameters.relativePath = reader.get(PatternToExcelEnum.RelativePath.getLiteral());

                    for (PatternLanguageType languagetype : PatternLanguageType.values()) {
                        String cellStr = reader.get(languagetype.getExcelEnum().getLiteral());
                        if (cellStr != null && !cellStr.equals("")) { //$NON-NLS-1$
                            patternParameters.regex.put(languagetype.getLiteral(), cellStr);
                        }
                    }

                    try {
                        TypedReturnCode<Object> create = createAndStorePattern(patternParameters, selectionFolder, type);
                        if (create.isOk()) {
                            names.add(name);

                            importEvent.add(new ReturnCode(DefaultMessagesImpl.getString("ImportFactory.importPattern", name, //$NON-NLS-1$
                                    selectionFolder.getProjectRelativePath().toString()), true));
                        } else {
                            throw new TalendInternalPersistenceException(create.getMessage());
                        }

                    } catch (Exception e) {
                        importEvent.add(new ReturnCode(DefaultMessagesImpl.getString("ImportFactory.SaveFailed", name), false)); //$NON-NLS-1$
                    }

                }
                reader.close();
            } catch (Exception e) {
                log.error(e, e);
                importEvent.add(new ReturnCode(DefaultMessagesImpl.getString("ImportFactory.importFailed"), false)); //$NON-NLS-1$
            }
        }

        importObject.copyJarFiles();

        // ADD xqliu 2012-04-27 TDQ-5149
        checkImportEvent(importItemName, importEvent);
        // ~ TDQ-5149
        return importEvent;
    }

    /**
     * check the importEvent list, if it is empty add message into it.
     * 
     * @param importItemName
     * @param importEvent
     */
    private static void checkImportEvent(String importItemName, List<ReturnCode> importEvent) {
        if (importEvent.isEmpty()) {
            if (!StringUtils.isBlank(importItemName)) {
                importEvent
                        .add(new ReturnCode(DefaultMessagesImpl.getString("ImportFactory.ImportFailed", importItemName), false)); //$NON-NLS-1$                    
            }
        }
    }

    private static TypedReturnCode<Object> createAndStorePattern(PatternParameters parameters, IFolder selectionFolder,
            ExpressionType type) throws TalendInternalPersistenceException {

        Pattern pattern = createPattern(parameters.name, parameters.auther, parameters.description, parameters.purpose,
                parameters.status);

        for (String key : parameters.regex.keySet()) {
            RegularExpression regularExpr = BooleanExpressionHelper.createRegularExpression(key, parameters.regex.get(key), type);
            pattern.getComponents().add(regularExpr);
        }

        boolean validStatus = PatternUtilities.isPatternValid(pattern);
        TaggedValueHelper.setValidStatus(validStatus, pattern);

        try {
            String relativePath = parameters.relativePath;
            if (EResourceConstant.PATTERN_REGEX.getName().equals(relativePath)
                    || EResourceConstant.PATTERN_SQL.getName().equals(relativePath)) {
                relativePath = PluginConstant.EMPTY_STRING;
            }
            String[] folderNames = relativePath.split("/"); //$NON-NLS-1$

            for (String folderName : folderNames) {

                IFolder folder = selectionFolder.getFolder(folderName);
                if (!folder.exists()) {
                    folder.create(false, true, null);
                }

                selectionFolder = folder;
            }
        } catch (CoreException e) {
            log.error(e, e);
        }

        return ElementWriterFactory.getInstance().createPatternWriter().create(pattern, selectionFolder);
    }

    /**
     * DOC qzhang Comment method "createPattern".
     * 
     * @param name
     * @param author
     * @param description
     * @param purpose
     * @param status
     * @return
     */
    private static Pattern createPattern(String name, String author, String description, String purpose, String status) {
        Pattern pattern = PatternFactory.eINSTANCE.createPattern();
        pattern.setName(name);
        MetadataHelper.setAuthor(pattern, author == null ? "" : author); //$NON-NLS-1$
        MetadataHelper.setDescription(description == null ? "" : description, pattern); //$NON-NLS-1$
        MetadataHelper.setPurpose(purpose == null ? "" : purpose, pattern); //$NON-NLS-1$
        // MOD mzhao feature 7479 2009-10-16
        MetadataHelper.setDevStatus(pattern, status == null ? "" : status); //$NON-NLS-1$
        return pattern;
    }

    private static String getFileExtName(File file) {
        String name = file.getName();
        int index = name.lastIndexOf('.');
        if (index == -1) {
            return null;
        }

        if (index == (name.length() - 1)) {
            return ""; //$NON-NLS-1$
        }

        return name.substring(index + 1);
    }

    /**
     * DOC zqin ImportFactory class global comment. Detailled comment
     */
    private class PatternParameters {

        String name;

        String auther;

        String description;

        String purpose;

        String status;

        String relativePath;

        String javaClassName;

        String javaJarPath;

        Map<String, String> regex;

        public PatternParameters() {
            name = ""; //$NON-NLS-1$
            auther = ""; //$NON-NLS-1$
            description = ""; //$NON-NLS-1$
            purpose = ""; //$NON-NLS-1$
            status = DevelopmentStatus.DRAFT.getLiteral();
            relativePath = ""; //$NON-NLS-1$
            javaClassName = "";//$NON-NLS-1$
            javaJarPath = "";//$NON-NLS-1$
            regex = new HashMap<String, String>();
        }
    }

    /**
     * DOC xqliu ImportFactory class global comment. Detailled comment
     */
    private class UDIParameters extends ImportFactory.PatternParameters {

        String category;

        Map<String, String> paraMap;

        public Map<String, String> getParaMap() {
            return this.paraMap;
        }

        public void setParaMap(Map<String, String> paraMap) {
            this.paraMap = paraMap;
        }

        public UDIParameters() {
            super();
            category = ""; //$NON-NLS-1$
            paraMap = new HashMap<String, String>();
        }
    }

    /**
     * DOC xqliu Comment method "importIndicatorToStucture".
     * 
     * @param importFile
     * @param selectionFolder
     * @param skip
     * @param rename
     * @return
     * @deprecated
     */
    @Deprecated
    public static List<ReturnCode> importIndicatorToStucture(File importFile, IFolder selectionFolder, boolean skip,
            boolean rename) {
        return importIndicatorToStucture(importFile, selectionFolder, skip, rename, null);
    }

    /**
     * DOC xqliu Comment method "importIndicatorToStucture".
     * 
     * @param importFile
     * @param selectionFolder
     * @param skip
     * @param rename
     * @param importItemName
     * @return
     * @deprecated
     */
    @Deprecated
    public static List<ReturnCode> importIndicatorToStucture(File importFile, IFolder selectionFolder, boolean skip,
            boolean rename, String importItemName) {
        return importIndicatorToStucture(ImportObject.createImportObject(importFile, null), selectionFolder, skip, rename,
                importItemName);
    }

    /**
     * DOC xqliu Comment method "importIndicatorToStucture".
     * 
     * @param importObject
     * @param selectionFolder
     * @param skip
     * @param rename
     * @param importItemName
     * @return
     */
    public static List<ReturnCode> importIndicatorToStucture(ImportObject importObject, IFolder selectionFolder, boolean skip,
            boolean rename, String importItemName) {

        List<ReturnCode> information = new ArrayList<ReturnCode>();

        Set<String> names = UDIHelper.getAllIndicatorNames(selectionFolder);

        File importFile = importObject.getObjFile();

        String fileExtName = getFileExtName(importFile);

        if (FileUtils.isCSV(fileExtName)) {
            String name = PluginConstant.EMPTY_STRING;
            try {
                CSVReader reader = FileUtils.createCSVReader(importFile, FileUtils.TEXT_QUAL, FileUtils.TEXT_QUAL);
                reader.setSkipEmptyRecords(true);
                reader.readHeaders();
                java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS"); //$NON-NLS-1$

                while (reader.readNext()) {
                    name = reader.get(PatternToExcelEnum.Label.getLiteral());

                    if (names.contains(name)) {
                        if (skip) {
                            information.add(new ReturnCode(DefaultMessagesImpl.getString("ImportFactory.Imported", name), false)); //$NON-NLS-1$
                            continue;
                        }
                        if (rename) {
                            name = name + "(" + simpleDateFormat.format(new Date()) + Math.random() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
                        }
                    }

                    UDIParameters udiParameters = new ImportFactory().new UDIParameters();
                    udiParameters.name = name;
                    udiParameters.auther = reader.get(PatternToExcelEnum.Author.getLiteral());
                    udiParameters.description = reader.get(PatternToExcelEnum.Description.getLiteral());
                    udiParameters.purpose = reader.get(PatternToExcelEnum.Purpose.getLiteral());
                    udiParameters.relativePath = reader.get(PatternToExcelEnum.RelativePath.getLiteral());
                    udiParameters.category = reader.get(PatternToExcelEnum.Category.getLiteral());
                    udiParameters.javaClassName = reader.get(PatternToExcelEnum.JavaClassName.getLiteral());
                    udiParameters.javaJarPath = reader.get(PatternToExcelEnum.JavaJarPath.getLiteral());
                    String[] headers = reader.getHeaders();
                    String[] columnsValue = reader.getValues();
                    HashMap<String, String> record = new HashMap<String, String>();
                    for (int i = 0; i < headers.length; i++) {
                        // TDQ-6714: fix when there is no regexp for the imported udi (csv) file.
                        if (i >= columnsValue.length) {
                            record.put(headers[i], ""); //$NON-NLS-1$
                        } else {
                            if (columnsValue[i] != null && columnsValue[i].length() > 0) {
                                record.put(headers[i], columnsValue[i]);
                            }
                        }
                    }
                    for (PatternLanguageType languagetype : PatternLanguageType.values()) {
                        String cellStr = record.get(languagetype.getExcelEnum().getLiteral());
                        if (cellStr != null && !cellStr.equals("\"\"")) { //$NON-NLS-1$
                            udiParameters.regex.put(languagetype.getLiteral(), trimQuote(cellStr));
                        }
                    }

                    udiParameters.setParaMap(buildIndDefPara(record));
                    TypedReturnCode<Object> create = createAndStoreUDI(udiParameters, selectionFolder);
                    if (create.isOk()) {
                        names.add(name);

                        // add the suscess message to display.
                        information.add(new ReturnCode(DefaultMessagesImpl.getString("ImportFactory.importedSucess" //$NON-NLS-1$
                                , ((TDQItem) create.getObject()).getProperty().getDisplayName(), selectionFolder
                                        .getProjectRelativePath().toString()), true));
                    } else {
                        throw new TalendInternalPersistenceException(create.getMessage());
                    }
                }

                reader.close();
            } catch (Exception e) {
                log.error(e, e);
                information.add(new ReturnCode(DefaultMessagesImpl.getString("ImportFactory.importedFailed", name), false)); //$NON-NLS-1$
            }
        }

        // MOD qiongli 2011-11-28 TDQ-4038.consider to import the definition file.
        if (FactoriesUtil.DEFINITION.equalsIgnoreCase(fileExtName)) {
            String propFilePath = importFile.getPath().replaceFirst(PluginConstant.DOT_STRING + fileExtName,
                    PluginConstant.DOT_STRING + FactoriesUtil.PROPERTIES_EXTENSION);
            File propFile = new File(propFilePath);
            // just import the definition file which have the realted Property file.
            if (!propFile.exists()) {
                return information;
            }
            String name = importFile.getName();
            try {
                if (names.contains(name)) {
                    if (skip) {
                        information.add(new ReturnCode(DefaultMessagesImpl.getString("ImportFactory.Imported", name), false)); //$NON-NLS-1$
                        return information;
                    }
                    if (rename) {
                        name = name + "(" + new Date() + Math.random() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
                    }
                }

                IFile elementFile = selectionFolder.getFile(name);
                if (!elementFile.exists()) {
                    elementFile.create(new FileInputStream(importFile), false, null);
                    ModelElement modelElement = ModelElementFileFactory.getModelElement(elementFile);
                    if (modelElement != null) {
                        ElementWriterFactory.getInstance().createIndicatorDefinitionWriter()
                                .create(modelElement, selectionFolder);
                        DefinitionHandler.getInstance().reloadIndicatorsDefinitions();
                        names.add(name);
                        information.add(new ReturnCode(DefaultMessagesImpl.getString("ImportFactory.importedSucess" //$NON-NLS-1$
                                , name), true));
                    }
                }
            } catch (Exception e) {
                log.error(e);
                information.add(new ReturnCode(DefaultMessagesImpl.getString("ImportFactory.importedFailed", name), false)); //$NON-NLS-1$
            }
        }

        importObject.copyJarFiles();

        // ADD xqliu 2012-04-27 TDQ-5149
        checkImportEvent(importItemName, information);
        // ~ TDQ-5149
        return information;
    }

    /**
     * get the IndicatorDefinitionParameter information from the record and build the paraMap, set it into
     * UDIParameters.
     * 
     * @param record
     */
    private static Map<String, String> buildIndDefPara(HashMap<String, String> record) {
        Map<String, String> paraMap = new HashMap<String, String>();
        String string = record.get(PatternToExcelEnum.IndicatorDefinitionParameter.getLiteral());
        if (StringUtils.isNotBlank(string)) {
            String[] keyValues = StringUtils.splitByWholeSeparator(string, UDIHelper.PARA_SEPARATE_2);
            for (String keyValue : keyValues) {
                if (StringUtils.isNotBlank(keyValue)) {
                    String[] para = StringUtils.splitByWholeSeparator(keyValue, UDIHelper.PARA_SEPARATE_1);
                    // the key should not be blank, the value can be anything
                    if (para.length == 2 && StringUtils.isNotBlank(para[0]) && para[1] != null) {
                        paraMap.put(para[0], para[1]);
                    }
                }
            }
        }
        return paraMap;
    }

    private static String trimQuote(String text) {
        if (text.length() < 2) {
            return text;
        }

        int beginLen = 0;
        int endLen = text.length();

        if ('\"' == text.charAt(beginLen) && '\"' == text.charAt(endLen - 1)) {
            return text.substring(1, endLen - 1);
        }

        return text;
    }

    /**
     * DOC xqliu Comment method "createAndStoreUDI".
     * 
     * @param parameters
     * @param selectionFolder
     */
    private static TypedReturnCode<Object> createAndStoreUDI(UDIParameters parameters, IFolder selectionFolder) {

        UDIndicatorDefinition indDef = UDIHelper.createUDI(parameters.name, parameters.auther, parameters.description,
                parameters.purpose, parameters.status, parameters.category, parameters.javaClassName, parameters.javaJarPath);

        for (String key : parameters.regex.keySet()) {
            TdExpression expression = BooleanExpressionHelper.createTdExpression(key, parameters.regex.get(key));
            indDef.getSqlGenericExpression().add(expression);
        }

        boolean validStatus = UDIHelper.isUDIValid(indDef);
        TaggedValueHelper.setValidStatus(validStatus, indDef);

        Map<String, String> paraMap = parameters.getParaMap();
        if (!paraMap.isEmpty()) {
            for (String key : paraMap.keySet()) {
                String value = paraMap.get(key);
                IndicatorDefinitionParameter idPara = DefinitionFactory.eINSTANCE.createIndicatorDefinitionParameter();
                idPara.setKey(key);
                idPara.setValue(value);
                indDef.getIndicatorDefinitionParameter().add(idPara);
            }
        }

        try {

            String[] folderNames = parameters.relativePath.split("/"); //$NON-NLS-1$

            for (String folderName : folderNames) {
                IFolder folder = selectionFolder.getFolder(folderName);
                if (!folder.exists()) {
                    folder.create(false, true, null);
                }

                selectionFolder = folder;
            }
        } catch (CoreException e) {
            log.error(e, e);
        }

        indDef = UDIUtils.createDefaultDrillDownList(indDef);
        return ElementWriterFactory.getInstance().createIndicatorDefinitionWriter().create(indDef, selectionFolder);
    }

    /**
     * DOC xqliu Comment method "importFile".
     * 
     * @param importFile
     * @param targetFile
     * @throws IOException
     */
    public static void importFile(File importFile, IFile targetFile) throws IOException {
        if (importFile != null && targetFile != null) {
            File file = new File(targetFile.getRawLocation().toOSString());
            if (file.exists()) {
                java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS"); //$NON-NLS-1$
                File bakFile = new File(file.getAbsolutePath() + "." + simpleDateFormat.format(new Date()) + ".bak"); //$NON-NLS-1$ //$NON-NLS-2$
                FilesUtils.copyFile(file, bakFile);
            }
            FilesUtils.copyFile(importFile, file);
        }
    }

    /**
     * The method is used for importing a parser rule from file.
     * 
     * @param importFile
     * @param selectionFolder
     * @param skip
     * @param rename
     * @return
     * @deprecated
     */
    @Deprecated
    public static List<ReturnCode> importParserRuleToStucture(File importFile, IFolder selectionFolder, boolean skip,
            boolean rename) {
        return importParserRuleToStucture(importFile, selectionFolder, skip, rename, null);
    }

    /**
     * DOC xqliu Comment method "importParserRuleToStucture".
     * 
     * @param importFile
     * @param selectionFolder
     * @param skip
     * @param rename
     * @param importItemName
     * @return
     * @deprecated
     */
    @Deprecated
    public static List<ReturnCode> importParserRuleToStucture(File importFile, IFolder selectionFolder, boolean skip,
            boolean rename, String importItemName) {
        return importParserRuleToStucture(ImportObject.createImportObject(importFile, null), selectionFolder, skip, rename,
                importItemName);
    }

    /**
     * DOC xqliu Comment method "importParserRuleToStucture".
     * 
     * @param importObject
     * @param selectionFolder
     * @param skip
     * @param rename
     * @param importItemName
     * @return
     */
    public static List<ReturnCode> importParserRuleToStucture(ImportObject importObject, IFolder selectionFolder, boolean skip,
            boolean rename, String importItemName) {
        List<ReturnCode> information = new ArrayList<ReturnCode>();

        Set<String> names = DQRuleResourceFileHelper.getInstance().getAllParserRlueNames(selectionFolder);
        ParserRuleParameters prParameters = new ImportFactory().new ParserRuleParameters();
        File importFile = importObject.getObjFile();
        String fileExtName = getFileExtName(importFile);

        if (FileUtils.isCSV(fileExtName)) {
            String name = ""; //$NON-NLS-1$
            boolean isNeedToCreate = true;
            try {
                CSVReader reader = FileUtils.createCSVReader(importFile, FileUtils.TEXT_QUAL, FileUtils.ESCAPE_CHAR);
                reader.setSkipEmptyRecords(true);

                reader.readHeaders();

                java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS"); //$NON-NLS-1$

                while (reader.readNext()) {
                    name = reader.get(ParserRuleToExcelEnum.Label.getLiteral());

                    if (names.contains(name)) {
                        if (skip) {
                            information.add(new ReturnCode(DefaultMessagesImpl
                                    .getString("ImportFactory.ParserRuleImported", name), false)); //$NON-NLS-1$
                            isNeedToCreate = false;
                            break;
                        }
                        if (rename) {
                            name = name + "(" + simpleDateFormat.format(new Date()) + Math.random() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
                        }
                    }
                    prParameters.label = reader.get(ParserRuleToExcelEnum.Label.getLiteral());
                    prParameters.auther = reader.get(ParserRuleToExcelEnum.Author.getLiteral());
                    prParameters.description = reader.get(ParserRuleToExcelEnum.Description.getLiteral());
                    prParameters.purpose = reader.get(ParserRuleToExcelEnum.Purpose.getLiteral());
                    ParserRuleTdExpresstion prExpresstion = new ImportFactory().new ParserRuleTdExpresstion();
                    prExpresstion.name = addQual(reader.get(ParserRuleToExcelEnum.Name.getLiteral()));
                    String type = reader.get(ParserRuleToExcelEnum.Type.getLiteral());
                    prExpresstion.type = type.isEmpty() || type.equals("\"\"") || type == null ? reader//$NON-NLS-1$
                            .get(ParserRuleToExcelEnum.Language.getLiteral()) : type;
                    String value = addQual(reader.get(ParserRuleToExcelEnum.Value.getLiteral()));
                    prExpresstion.value = value.isEmpty() || value.equals("") || value == null ? addQual(reader//$NON-NLS-1$
                            .get(ParserRuleToExcelEnum.Body.getLiteral())) : value;
                    prParameters.prExpresstions.add(prExpresstion);
                }
                if (isNeedToCreate) {
                    //
                    if (name.trim().equals("")) {//$NON-NLS-1$
                        information.add(new ReturnCode(DefaultMessagesImpl.getString("ImportFactory.importedEmptyParserRule" //$NON-NLS-1$
                                ), true));
                    } else {
                        names.add(name);
                        information.add(new ReturnCode(DefaultMessagesImpl.getString("ImportFactory.importedParserRuleSucess" //$NON-NLS-1$
                                , name, selectionFolder.getProjectRelativePath().toString()), true));
                        createAndStoreParserRule(prParameters, selectionFolder, name);
                    }
                }

                reader.close();

            } catch (Exception e) {
                log.error(e, e);
                information.add(new ReturnCode(
                        DefaultMessagesImpl.getString("ImportFactory.importedParserRuleFailed", name), false)); //$NON-NLS-1$
            }
        }

        importObject.copyJarFiles();

        // ADD xqliu 2012-04-27 TDQ-5149
        checkImportEvent(importItemName, information);
        // ~ TDQ-5149
        return information;
    }

    /**
     * DOC klliu Comment method "addQual".
     * 
     * @param string
     * @return
     */
    private static String addQual(String sourceString) {
        String replace = sourceString.replace(sourceString, "\"" + sourceString + "\"");//$NON-NLS-1$ //$NON-NLS-2$
        return replace;
    }

    /**
     * DOC klliu Comment method "createAndStoreParserRule".
     * 
     * @param prParameters
     * @param selectionFolder
     */
    public static void createAndStoreParserRule(ParserRuleParameters prParameters, IFolder selectionFolder, String name) {
        DqRuleBuilder ruleBuilder = new DqRuleBuilder();
        boolean ruleInitialized = ruleBuilder.initializeParserRuleBuilder(prParameters.label);
        if (ruleInitialized) {
            ParserRule parserRule = ruleBuilder.getParserRule();
            parserRule.setName(name);
            TaggedValueHelper.setValidStatus(true, parserRule);
            List<ParserRuleTdExpresstion> prExpresstions = prParameters.getPrExpresstions();
            for (ParserRuleTdExpresstion prtde : prExpresstions) {
                parserRule.addExpression(prtde.name, prtde.type, prtde.value);
            }
            IndicatorCategory ruleIndicatorCategory = DefinitionHandler.getInstance().getDQRuleIndicatorCategory();
            if (ruleIndicatorCategory != null && !parserRule.getCategories().contains(ruleIndicatorCategory)) {
                parserRule.getCategories().add(ruleIndicatorCategory);
            }
            ElementWriterFactory.getInstance().createdRuleWriter().create(parserRule, selectionFolder);
        }
    }

    /**
     * DOC bZhou ImportFactory class global comment. Detailled comment
     */
    private class ParserRuleParameters {

        String label;

        String auther;

        String description;

        String purpose;

        String status;

        List<ParserRuleTdExpresstion> prExpresstions;

        public ParserRuleParameters() {
            label = ""; //$NON-NLS-1$
            auther = ""; //$NON-NLS-1$
            description = ""; //$NON-NLS-1$
            purpose = ""; //$NON-NLS-1$
            status = DevelopmentStatus.DRAFT.getLiteral();
            prExpresstions = new ArrayList<ParserRuleTdExpresstion>();
        }

        public List<ParserRuleTdExpresstion> getPrExpresstions() {
            return this.prExpresstions;
        }
    }

    /**
     * DOC bZhou ImportFactory class global comment. Detailled comment
     */
    private class ParserRuleTdExpresstion {

        String name;

        String type;

        String value;

        public ParserRuleTdExpresstion() {
            name = ""; //$NON-NLS-1$
            type = ""; //$NON-NLS-1$
            value = ""; //$NON-NLS-1$
        }
    }

    /**
     * DOC xqliu Comment method "importFromExchange".
     * 
     * @param componet
     * @return
     */
    public static void importFromExchange(IEcosComponent componet) throws Exception {
        File file = new File(componet.getInstalledLocation());
        File copiedFile = new File(
                System.getProperty("java.io.tmpdir") + File.separator + new Path(componet.getInstalledLocation()).lastSegment()); //$NON-NLS-1$
        org.apache.commons.io.FileUtils.copyFile(file, copiedFile); // copy the downloaded file out of the workspace
        Wizard wizard = new ImportFromExchangeWizard(copiedFile.getAbsolutePath());
        WizardDialog dialog = new WizardDialog(null, wizard);
        if (WizardDialog.OK == dialog.open()) {
            ResourceManager.getLibrariesFolder().refreshLocal(IResource.DEPTH_INFINITE, null);
        }
    }
}
