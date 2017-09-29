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
package org.talend.dq.analysis.parameters;

import java.util.List;

import org.talend.commons.utils.VersionUtils;
import org.talend.cwm.constants.DevelopmentStatus;
import org.talend.cwm.management.api.FolderProvider;
import org.talend.repository.model.IRepositoryNode;

/**
 * DOC zqin class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z zqin $
 * 
 */
public class ConnectionParameter {

    private String name;

    private String purpose;

    private String description;

    private String author;

    private String status = DevelopmentStatus.DRAFT.getLiteral();

    private String version;

    private FolderProvider folderProvider;

    private EParameterType paramType;

    private IRepositoryNode connectionRepNode;

    public IRepositoryNode getConnectionRepNode() {
        return this.connectionRepNode;
    }

    public void setConnectionRepNode(IRepositoryNode connectionRepNode) {
        this.connectionRepNode = connectionRepNode;
    }

    public ConnectionParameter(EParameterType paramType) {
        this.paramType = paramType;
    }

    private List<IRepositoryNode> packages;

    public List<IRepositoryNode> getPackages() {
        return packages;
    }

    public void setPackages(List<IRepositoryNode> nodes) {
        this.packages = nodes;
    }

    public EParameterType getParamType() {
        return paramType;
    }

    public String getName() {
        return name;

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVersion() {
        return version == null ? VersionUtils.DEFAULT_VERSION : version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Getter for folderProvider.
     * 
     * @return the folderProvider
     */
    public FolderProvider getFolderProvider() {
        return this.folderProvider;
    }

    /**
     * Sets the folderProvider.
     * 
     * @param folderProvider the folderProvider to set
     */
    public void setFolderProvider(FolderProvider folderProvider) {
        this.folderProvider = folderProvider;
    }

    /**
     * Enumeration of types.
     */
    public enum EParameterType {
        ANALYSIS,
        REPORT,
        PATTERN,
        CONNECTION,
        DQRULE,
        UDINDICATOR;
    }

}
