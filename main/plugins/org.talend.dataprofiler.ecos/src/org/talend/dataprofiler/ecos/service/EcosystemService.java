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
package org.talend.dataprofiler.ecos.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.JavaTypeMapper;
import org.osgi.framework.Version;
import org.talend.dataprofiler.ecos.model.IEcosCategory;
import org.talend.dataprofiler.ecos.model.RevisionInfo;
import org.talend.dataprofiler.ecos.model.VersionInfo;
import org.talend.dataprofiler.ecos.model.impl.EcosCategory;
import org.talend.dataprofiler.ecos.model.impl.Revision;
import org.talend.dataprofiler.ecos.proxy.EcosystemProxyAdapter;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public abstract class EcosystemService {

    protected static Logger log = Logger.getLogger(EcosystemService.class);

    public final static String VERSION_LIST_URL = "http://talendforge.org/exchange/top/api/get_version_list.php"; //$NON-NLS-1$

    public final static String REVISION_LIST_URL = "http://talendforge.org/exchange/top/api/get_revision_list.php"; //$NON-NLS-1$

    private final static String CATEGORY_LIST_URL = "http://talendforge.org/exchange/top/api/get_category_list.php";//$NON-NLS-1$

    private static MultiValueMap versionMap = new MultiValueMap();

    private final static int TIMEOUT = 1000000;

    public static String[] getBranch(String version) throws Exception {
        String mainVersion = getMainVersion(version);
        if (versionMap.isEmpty()) {
            getVersionList();
        }
        Collection<String> branch = versionMap.getCollection(mainVersion);
        return branch == null ? null : branch.toArray(new String[branch.size()]);
    }

    public static String getMainVersion(String version) {
        if (version == null) {
            return version;
        }
        Pattern pattern = Pattern.compile("(\\d+\\.\\d+).*"); //$NON-NLS-1$
        Matcher matcher = pattern.matcher(version);
        String resultVersion = version;
        if (matcher.matches()) {
            resultVersion = matcher.group(1);
        }
        return resultVersion;
    }

    @SuppressWarnings("unchecked")
    public static String[] getVersionList() throws Exception {
        versionMap.clear();
        try {
            String jsonContent = sendGetRequest(VERSION_LIST_URL);
            List<VersionInfo> list = parseJsonObject(jsonContent, VersionInfo.class);

            Pattern pattern = Pattern.compile("(\\d+\\.\\d+).*"); //$NON-NLS-1$
            for (VersionInfo info : list) {
                String name = info.getName();
                Matcher matcher = pattern.matcher(name);
                if (matcher.matches()) {
                    versionMap.put(matcher.group(1), String.valueOf(info.getId()));
                }
            }
            // sort version
            List<String> versions = new ArrayList<String>(versionMap.keySet());
            Collections.sort(versions, new Comparator<String>() {

                @Override
                public int compare(String o1, String o2) {
                    Version ver1 = new Version(o1);
                    Version ver2 = new Version(o2);
                    return ver2.compareTo(ver1);
                }

            });
            return versions.toArray(new String[versions.size()]);
        } catch (Exception e) {
            throw e;
        }
    }

    public static <T> List<T> parseJsonObject(String jsonContent, Class<T> clazz) throws Exception {
        // need factory for creating parser to use
        JsonFactory jf = new JsonFactory();
        if (jsonContent == null || clazz == null) {
            return new ArrayList<T>();
        }
        List<?> result = (List<?>) new JavaTypeMapper().read(jf.createJsonParser(new StringReader(jsonContent)));
        List<T> objList = new ArrayList<T>(result.size());
        for (int i = 0; i < result.size(); i++) {
            T obj = clazz.newInstance();
            Object source = result.get(i);
            BeanUtils.copyProperties(obj, source);
            objList.add(obj);
        }
        return objList;
    }

    public static String sendGetRequest(String urlAddress) throws Exception {
        HttpClient httpclient = new HttpClient();

        // adapt proxy to http client.
        EcosystemProxyAdapter.adapt(httpclient, urlAddress);

        GetMethod getMethod = new GetMethod(urlAddress);
        getMethod.getParams().setSoTimeout(TIMEOUT);
        httpclient.executeMethod(getMethod);
        String response = getMethod.getResponseBodyAsString();
        getMethod.releaseConnection();
        return response;
    }

    public static String sendPostRequest(String urlAddress, Map<String, String> parameters) throws Exception {
        HttpClient httpclient = new HttpClient();

        // adapt proxy to http client.
        EcosystemProxyAdapter.adapt(httpclient, urlAddress);

        PostMethod postMethod = new PostMethod(urlAddress);
        postMethod.getParams().setSoTimeout(TIMEOUT);
        if (parameters != null) {
            NameValuePair[] postData = new NameValuePair[parameters.size()];
            int i = 0;
            for (String key : parameters.keySet()) {
                String value = parameters.get(key);
                postData[i++] = new NameValuePair(key, value);
            }
            postMethod.addParameters(postData);
        }

        httpclient.executeMethod(postMethod);
        String response = postMethod.getResponseBodyAsString();
        postMethod.releaseConnection();
        return response;
    }

    /**
     * Return true if revision1 is newer than revision2.
     * 
     * @param revision1
     * @param revision2
     * @return
     */
    public static boolean isRevisionNewerThan(Revision revision1, Revision revision2) {
        String[] rev1 = revision1.getName().split("\\."); //$NON-NLS-1$
        String[] rev2 = revision2.getName().split("\\."); //$NON-NLS-1$
        for (int i = 0; i < rev1.length && i < rev2.length; i++) {
            int a = Integer.parseInt(rev1[i]);
            int b = Integer.parseInt(rev2[i]);
            if (a == b) {
                continue;
            } else {
                return a > b;
            }
        }
        // the two revision has different length, the longer one is newer
        return rev1.length > rev2.length;
    }

    public static List<IEcosCategory> getCategoryList(String version) throws Exception {

        String jsonContent = sendGetRequest(CATEGORY_LIST_URL);
        List<EcosCategory> categorys = parseJsonObject(jsonContent, EcosCategory.class);
        if (categorys != null) {
            for (EcosCategory category : categorys) {
                category.setVersion(version);
            }
        } else {
            categorys = Collections.emptyList();
        }
        // MOD yyi 2010-11-29 15271: svn project can't load exchange nodes
        List<IEcosCategory> iCategorys = new ArrayList<IEcosCategory>();
        for (EcosCategory ecosCategory : categorys) {
            iCategorys.add(ecosCategory);
        }
        // ~15271
        return iCategorys;
    }

    public static List<RevisionInfo> getRevisionList(String category, String version) throws Exception {
        StringBuffer url = new StringBuffer();
        url.append(REVISION_LIST_URL).append("?categories=").append(category).append("&version="); //$NON-NLS-1$ //$NON-NLS-2$
        String[] branch = getBranch(version);
        if (branch != null) {
            url.append(StringUtils.join(branch, ",")); //$NON-NLS-1$
            String jsonContent = sendGetRequest(url.toString());
            if (StringUtils.isNotEmpty(jsonContent)) {
                return parseJsonObject(jsonContent, RevisionInfo.class);
            }
        }

        return new ArrayList<RevisionInfo>();
    }

}
