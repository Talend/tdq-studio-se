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

import java.util.Properties;

/**
 * DOC zqin class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z zqin $
 * 
 */
public class DBConnectionParameter extends ConnectionParameter {

    private String driverPath;

    private String sqlTypeName;

    private String filePath;

    private String jdbcUrl;

    private String aDDParameter;

    private String driverClassName;

    private Properties parameters;

    private String host;

    private String port;

    private String dbName;

    private String dbmsId;

    // ADD klliu 2011-05-19 21704: Refactoring this "otherParameter" !
    private String filterSchema;

    private String filterCatalog;

    public String getFilterSchema() {
        return this.filterSchema;
    }

    public void setFilterSchema(String filterSchema) {
        this.filterSchema = filterSchema;
    }

    public String getFilterCatalog() {
        return this.filterCatalog;
    }

    public void setFilterCatalog(String filterCatalog) {
        this.filterCatalog = filterCatalog;
    }

    // ~
    // ADD klliu 2010-10-09 feature 15821
    private String otherParameter;

    public String getOtherParameter() {
        return this.otherParameter;
    }

    public void setOtherParameter(String otherParameter) {
        this.otherParameter = otherParameter;
    }

    // ADD xqliu 2010-03-03 feature 11412
    private boolean retrieveAllMetadata;

    /**
     * Getter for retrieveAllMetadata.
     * 
     * @return the retrieveAllMetadata
     */
    public boolean isRetrieveAllMetadata() {
        return retrieveAllMetadata;
    }

    /**
     * Sets the retrieveAllMetadata.
     * 
     * @param retrieveAllMetadata the retrieveAllMetadata to set
     */
    public void setRetrieveAllMetadata(boolean retrieveAllMetadata) {
        this.retrieveAllMetadata = retrieveAllMetadata;
    }

    // ~11412

    public DBConnectionParameter() {
        super(EParameterType.CONNECTION);
        retrieveAllMetadata = true;
    }

    public void setSqlTypeName(String sqlTypeName) {
        this.sqlTypeName = sqlTypeName;
    }

    public String getSqlTypeName() {
        return sqlTypeName;
    }

    public String getDriverPath() {
        return driverPath;
    }

    public void setDriverPath(String driverPath) {
        this.driverPath = driverPath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Getter for jdbcUrl.
     * 
     * @return the jdbcUrl
     */
    public String getJdbcUrl() {
        return this.jdbcUrl;
    }

    /**
     * Sets the jdbcUrl.
     * 
     * @param jdbcUrl the jdbcUrl to set
     */
    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    /**
     * Getter for driverClassName.
     * 
     * @return the driverClassName
     */
    public String getDriverClassName() {
        return this.driverClassName;
    }

    /**
     * Sets the driverClassName.
     * 
     * @param driverClassName the driverClassName to set
     */
    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    /**
     * Getter for parameters.
     * 
     * @return the parameters
     */
    public Properties getParameters() {
        return this.parameters;
    }

    /**
     * Sets the parameters.
     * 
     * @param parameters the parameters to set
     */
    public void setParameters(Properties parameters) {
        this.parameters = parameters;
    }

    /**
     * Getter for host.
     * 
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets the host.
     * 
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Getter for port.
     * 
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * Sets the port.
     * 
     * @param port the port to set
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * Getter for dbName.
     * 
     * @return the dbName
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * Sets the dbName.
     * 
     * @param dbName the dbName to set
     */
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    /**
     * 
     * DOC zshen Comment method "getaDDParameter".
     * 
     * @return
     */
    public String getADDParameter() {
        return aDDParameter;
    }

    /**
     * 
     * DOC zshen Comment method "setADDParameter".
     * 
     * @param aDDParameter
     */
    public void setADDParameter(String aDDParameter) {
        this.aDDParameter = aDDParameter;
    }

    public String getDbmsId() {
        return dbmsId;
    }

    public void setDbmsId(String dbmsId) {
        this.dbmsId = dbmsId;
    }

}
