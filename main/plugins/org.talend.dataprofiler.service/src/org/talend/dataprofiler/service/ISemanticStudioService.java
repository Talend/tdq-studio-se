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

import org.eclipse.jface.wizard.Wizard;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.dataquality.analysis.Analysis;

public interface ISemanticStudioService {

    void enrichOntRepoWithAnalysisResult(Analysis analysis);

    int openSemanticDiscoveryWizard(MetadataTable metadataTable);

    Wizard getSemanticDiscoveryWizard(MetadataTable metadataTable, Object parameter);

}
