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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataprofiler.core.pattern.PatternLanguageType;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.domain.pattern.PatternComponent;
import org.talend.dataquality.domain.pattern.RegularExpression;
import org.talend.dataquality.domain.pattern.util.PatternSwitch;
import org.talend.dataquality.helpers.BooleanExpressionHelper;
import org.talend.dataquality.helpers.DomainHelper;
import org.talend.dq.helper.resourcehelper.PatternResourceFileHelper;
import org.talend.dq.writer.EMFSharedResources;
import org.talend.utils.sugars.ReturnCode;

/**
 * 
 * zshen add a RegularExpression for mysql lanagaue in the International_phone_number pattern if the pattern is exist
 * and it dont't contain a RegularExpression for mysql lanagaue.
 */
public class UpdateSomePatternToMatchMysql extends AbstractWorksapceUpdateTask {

    protected static Logger log = Logger.getLogger(UpdateSomePatternToMatchMysql.class);

    private static final String UPDATE_FILE_NAME = "TDQ_Libraries/Patterns/Regex/phone/International_phone_number.pattern"; //$NON-NLS-1$

    private static final String EXPRESSIONBODY = "'^\\\\+[0-9]{1,3}\\.[0-9]+\\.[0-9]+$'"; //$NON-NLS-1$

    private File updateFile = null;

    public UpdateSomePatternToMatchMysql() {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected boolean doExecute() throws Exception {
        if (updateFile == null) {
            updateFile = getWorkspacePath().append(UPDATE_FILE_NAME).toFile();
        }
        if (!updateFile.exists()) {// if International_phone_number.pattern don't exsit.
            return true;
        }
        URI uri = URI.createFileURI(updateFile.getAbsolutePath());
        Resource patternResource = EMFSharedResources.getInstance().reloadResource(uri);
        if (patternResource != null) {
            Pattern thePattern = retirePattern(patternResource);
            for (PatternComponent currentExpression : thePattern.getComponents()) {
                if (currentExpression instanceof RegularExpression) {
                    if (PatternLanguageType.MYSQL.getLiteral().equals(
                            ((RegularExpression) currentExpression).getExpression().getLanguage())) {
                        return true;// if the case of Mysql has been added.
                    }
                }
            }
            String language = PatternLanguageType.MYSQL.getLiteral();
            RegularExpression newRegularExpress = BooleanExpressionHelper.createRegularExpression(language, EXPRESSIONBODY);
            String expressionType = DomainHelper.getExpressionType(thePattern);
            newRegularExpress.setExpressionType(expressionType);
            List<PatternComponent> componentsList = new ArrayList<PatternComponent>();
            componentsList.add(newRegularExpress);
            componentsList.addAll(thePattern.getComponents());
            thePattern.getComponents().clear();
            thePattern.getComponents().addAll(componentsList);

            ReturnCode rc = PatternResourceFileHelper.getInstance().save(thePattern);

            uri = URI.createFileURI(updateFile.getAbsolutePath());
            EMFSharedResources.getInstance().reloadResource(uri);
            return rc.isOk();
        } else {
            log.error(DefaultMessagesImpl.getString("UpdateSomePatternToMatchMysql_logErr", patternResource)); //$NON-NLS-1$
            return false;
        }

    }

    /**
     * DOC rli Comment method "retireAnalysis".
     * 
     * @param fileResource
     * @return
     */
    private Pattern retirePattern(Resource fileResource) {
        EList<EObject> contents = fileResource.getContents();
        if (contents.isEmpty()) {
            log.error(DefaultMessagesImpl.getString("UpdateSomePatternToMatchMysql_logErr", fileResource)); //$NON-NLS-1$
        }
        if (log.isDebugEnabled()) {
            log.debug("No elements in contents " + contents.size()); //$NON-NLS-1$
        }
        PatternSwitch<Pattern> mySwitch = new PatternSwitch<Pattern>() {

            @Override
            public Pattern casePattern(Pattern object) {
                return object;
            }
        };
        Pattern pattern = null;
        if (contents != null && contents.size() != 0) {
            pattern = mySwitch.doSwitch(contents.get(0));
        }
        return pattern;
    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    public Date getOrder() {
        return createDate(2010, 10, 9);
    }

}
