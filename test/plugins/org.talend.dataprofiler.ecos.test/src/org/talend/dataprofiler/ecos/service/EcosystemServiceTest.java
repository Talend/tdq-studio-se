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

import static org.junit.Assert.fail;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.talend.dataprofiler.ecos.model.impl.Revision;

/**
 * 
 * DOC mzhao class global comment. Detailled comment
 */
public class EcosystemServiceTest {

    /**
     * Test method for {@link org.talend.dataprofiler.ecos.service.EcosystemService#getBranch(java.lang.String)}.
     */
    @Test
    public void testGetBranch() {
        try {
            String[] branch = EcosystemService.getBranch("6.1.7"); //$NON-NLS-1$
            Assert.assertTrue("The version start with 6.1 should more than 0", branch.length > 0); //$NON-NLS-1$
            Assert.assertEquals("158", branch[0]); //$NON-NLS-1$
            //            Assert.assertEquals("161", branch[1]); //$NON-NLS-1$
            //            Assert.assertEquals("162", branch[2]); //$NON-NLS-1$
            //            Assert.assertEquals("163", branch[3]); //$NON-NLS-1$
            branch = EcosystemService.getBranch("6.1"); //$NON-NLS-1$
            Assert.assertTrue("The version start with 6.1 should more than 0", branch.length > 0); //$NON-NLS-1$
            Assert.assertEquals("158", branch[0]); //$NON-NLS-1$
            //            Assert.assertEquals("161", branch[1]); //$NON-NLS-1$
            //            Assert.assertEquals("162", branch[2]); //$NON-NLS-1$
            //            Assert.assertEquals("163", branch[3]); //$NON-NLS-1$
            branch = EcosystemService.getBranch("6.1.1sdfeofjsijfer93rhfhush99f9832hrfsdohg98d"); //$NON-NLS-1$
            Assert.assertTrue("The version start with 6.1 should more than 0", branch.length > 0); //$NON-NLS-1$
            Assert.assertEquals("158", branch[0]); //$NON-NLS-1$
            //            Assert.assertEquals("161", branch[1]); //$NON-NLS-1$
            //            Assert.assertEquals("162", branch[2]); //$NON-NLS-1$
            //            Assert.assertEquals("163", branch[3]); //$NON-NLS-1$
            branch = EcosystemService.getBranch("6.11sdfeofjsijfer93rhfhush99f9832hrfsdohg98d"); //$NON-NLS-1$
            Assert.assertNull(branch);
            branch = EcosystemService.getBranch("100000.1.1"); //$NON-NLS-1$
            Assert.assertNull(branch);
            branch = EcosystemService.getBranch("6"); //$NON-NLS-1$
            Assert.assertNull(branch);
            branch = EcosystemService.getBranch("6aaaaaa"); //$NON-NLS-1$
            Assert.assertNull(branch);
            branch = EcosystemService.getBranch("6.aaa"); //$NON-NLS-1$
            Assert.assertNull(branch);
            branch = EcosystemService.getBranch("0.0"); //$NON-NLS-1$
            Assert.assertNull(branch);
            branch = EcosystemService.getBranch("0"); //$NON-NLS-1$
            Assert.assertNull(branch);
            branch = EcosystemService.getBranch(null);
            Assert.assertNull(branch);
            branch = EcosystemService.getBranch(StringUtils.EMPTY);
            Assert.assertNull(branch);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test method for {@link org.talend.dataprofiler.ecos.service.EcosystemService#getMainVersion(java.lang.String)}.
     */
    @Test
    public void testGetMainVersion() {
        String mainVersion = EcosystemService.getMainVersion("6.1.7"); //$NON-NLS-1$
        Assert.assertEquals(mainVersion, "6.1"); //$NON-NLS-1$
        mainVersion = EcosystemService.getMainVersion("6.1"); //$NON-NLS-1$
        Assert.assertEquals(mainVersion, "6.1"); //$NON-NLS-1$
        mainVersion = EcosystemService.getMainVersion("6.11sdfeofjsijfer93rhfhush99f9832hrfsdohg98d"); //$NON-NLS-1$
        Assert.assertEquals(mainVersion, "6.11"); //$NON-NLS-1$
        mainVersion = EcosystemService.getMainVersion("6.1.1sdfeofjsijfer93rhfhush99f9832hrfsdohg98d"); //$NON-NLS-1$
        Assert.assertEquals(mainVersion, "6.1"); //$NON-NLS-1$
        mainVersion = EcosystemService.getMainVersion("6.1.1sdfeofjsijfer93rhfhush99f9832hrfsdohg98d"); //$NON-NLS-1$
        Assert.assertEquals(mainVersion, "6.1"); //$NON-NLS-1$
        mainVersion = EcosystemService.getMainVersion("100000.1.1"); //$NON-NLS-1$
        Assert.assertEquals(mainVersion, "100000.1"); //$NON-NLS-1$
        mainVersion = EcosystemService.getMainVersion("100000"); //$NON-NLS-1$
        Assert.assertEquals(mainVersion, "100000"); //$NON-NLS-1$
        mainVersion = EcosystemService.getMainVersion("6"); //$NON-NLS-1$
        Assert.assertEquals(mainVersion, "6"); //$NON-NLS-1$
        mainVersion = EcosystemService.getMainVersion("6aaaaaa"); //$NON-NLS-1$
        Assert.assertEquals(mainVersion, "6aaaaaa"); //$NON-NLS-1$
        mainVersion = EcosystemService.getMainVersion("6.aaa"); //$NON-NLS-1$
        Assert.assertEquals(mainVersion, "6.aaa"); //$NON-NLS-1$
        mainVersion = EcosystemService.getMainVersion("0.0"); //$NON-NLS-1$
        Assert.assertEquals(mainVersion, "0.0"); //$NON-NLS-1$
        mainVersion = EcosystemService.getMainVersion("0"); //$NON-NLS-1$
        Assert.assertEquals(mainVersion, "0"); //$NON-NLS-1$
        mainVersion = EcosystemService.getMainVersion(null);
        Assert.assertEquals(mainVersion, null);
        mainVersion = EcosystemService.getMainVersion(StringUtils.EMPTY);
        Assert.assertEquals(mainVersion, StringUtils.EMPTY);
    }

    /**
     * Test method for {@link org.talend.dataprofiler.ecos.service.EcosystemService#getVersionList()}.
     */
    @Ignore
    @Test
    public void testGetVersionList() {
        // try {
        //            List<RevisionInfo> revisionList = EcosystemService.getRevisionList("37", "5.6.0"); //$NON-NLS-1$ //$NON-NLS-2$
        // Assert.assertTrue(revisionList.size() >= 14);
        //            revisionList = EcosystemService.getRevisionList("37", null); //$NON-NLS-1$
        // Assert.assertTrue(revisionList.size() == 0);
        //            revisionList = EcosystemService.getRevisionList(null, "5.6.0"); //$NON-NLS-1$
        // Assert.assertTrue(revisionList.size() == 0);
        // } catch (Exception e) {
        // fail(e.getMessage());
        // }
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.ecos.service.EcosystemService#parseJsonObject(java.lang.String, java.lang.Class)}.
     */
    @Ignore
    @Test
    public void testParseJsonObject() {
        // try {
        // String jsonContent = EcosystemService
        //                    .sendGetRequest("http://talendforge.org/exchange/top/api/get_revision_list.php?categories=37&version=128"); //$NON-NLS-1$
        // List<RevisionInfo> parseJsonObject = EcosystemService.parseJsonObject(jsonContent, RevisionInfo.class);
        // Assert.assertTrue(parseJsonObject.size() >= 15);
        // parseJsonObject = EcosystemService.parseJsonObject(null, RevisionInfo.class);
        // Assert.assertTrue(parseJsonObject.size() == 0);
        // parseJsonObject = EcosystemService.parseJsonObject(jsonContent, null);
        // Assert.assertTrue(parseJsonObject.size() == 0);
        // } catch (Exception e) {
        // fail(e.getMessage());
        // }
    }

    /**
     * Test method for {@link org.talend.dataprofiler.ecos.service.EcosystemService#sendGetRequest(java.lang.String)}.
     */
    @Ignore
    @Test
    public void testSendGetRequest() {
        // try {
        // String jsonContent = EcosystemService
        //                    .sendGetRequest("http://talendforge.org/exchange/top/api/get_revision_list.php?categories=37&version=128"); //$NON-NLS-1$
        // Assert.assertNotNull(jsonContent);
        //            Assert.assertNotEquals("[]", jsonContent); //$NON-NLS-1$
        //            jsonContent = EcosystemService.sendGetRequest("http://talendforge.org/exchange/top/api/get_revision_list.php"); //$NON-NLS-1$
        //            Assert.assertEquals("[]", jsonContent); //$NON-NLS-1$
        // } catch (Exception e) {
        // fail(e.getMessage());
        // }
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.ecos.service.EcosystemService#sendPostRequest(java.lang.String, java.util.Map)}.
     */
    @Ignore
    @Test
    public void testSendPostRequest() {
        // Map<String, String> parameter = new HashMap<String, String>();
        //        parameter.put("categories", "37"); //$NON-NLS-1$ //$NON-NLS-2$
        //        parameter.put("version", "128"); //$NON-NLS-1$ //$NON-NLS-2$
        // String jsonContent;
        // try {
        //            jsonContent = EcosystemService.sendPostRequest("http://talendforge.org/exchange/top/api/get_revision_list.php", //$NON-NLS-1$
        // parameter);
        // Assert.assertNotNull(jsonContent);
        //            Assert.assertNotEquals("[]", jsonContent); //$NON-NLS-1$
        // } catch (Exception e) {
        // fail(e.getMessage());
        // }
        // parameter = new HashMap<String, String>();
        //        parameter.put("categories", null); //$NON-NLS-1$ 
        //        parameter.put("version", null); //$NON-NLS-1$ 
        // try {
        //            jsonContent = EcosystemService.sendPostRequest("http://talendforge.org/exchange/top/api/get_revision_list.php", //$NON-NLS-1$
        // parameter);
        //            Assert.assertEquals("[]", jsonContent); //$NON-NLS-1$
        // } catch (Exception e) {
        // fail(e.getMessage());
        // }
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.ecos.service.EcosystemService#isRevisionNewerThan(org.talend.dataprofiler.ecos.model.impl.Revision, org.talend.dataprofiler.ecos.model.impl.Revision)}
     * .
     */
    @Test
    public void testIsRevisionNewerThan() {
        Revision revisionBigOne = new Revision();
        revisionBigOne.setName("6.0.1"); //$NON-NLS-1$
        Revision revisionSmallOne = new Revision();
        revisionSmallOne.setName("3.2.1"); //$NON-NLS-1$
        Assert.assertTrue(
                revisionBigOne + " should more than " + revisionSmallOne, EcosystemService.isRevisionNewerThan(revisionBigOne, revisionSmallOne)); //$NON-NLS-1$
        revisionBigOne.setName("3.3.1"); //$NON-NLS-1$
        Assert.assertTrue(
                revisionBigOne + " should more than " + revisionSmallOne, EcosystemService.isRevisionNewerThan(revisionBigOne, revisionSmallOne)); //$NON-NLS-1$
        revisionBigOne.setName("3.2.1.modify"); //$NON-NLS-1$
        Assert.assertTrue(
                revisionBigOne + " should more than " + revisionSmallOne, EcosystemService.isRevisionNewerThan(revisionBigOne, revisionSmallOne)); //$NON-NLS-1$
        revisionBigOne.setName("123456"); //$NON-NLS-1$
        revisionSmallOne.setName("12345"); //$NON-NLS-1$
        Assert.assertTrue(
                revisionBigOne + " should more than " + revisionSmallOne, EcosystemService.isRevisionNewerThan(revisionBigOne, revisionSmallOne)); //$NON-NLS-1$
        try {
            revisionBigOne.setName("a.b.c"); //$NON-NLS-1$
            revisionSmallOne.setName("12345"); //$NON-NLS-1$
            Assert.assertTrue(
                    revisionBigOne + " should more than " + revisionSmallOne, EcosystemService.isRevisionNewerThan(revisionBigOne, revisionSmallOne)); //$NON-NLS-1$
            fail("there should be a RuntimeException"); //$NON-NLS-1$
        } catch (NumberFormatException e) {
            // nothing need to do
        }
        try {
            revisionBigOne.setName("12345"); //$NON-NLS-1$
            revisionSmallOne.setName("a.b.c"); //$NON-NLS-1$
            Assert.assertTrue(
                    revisionBigOne + " should more than " + revisionSmallOne, EcosystemService.isRevisionNewerThan(revisionBigOne, revisionSmallOne)); //$NON-NLS-1$
            fail("there should be a RuntimeException"); //$NON-NLS-1$
        } catch (NumberFormatException e) {
            // nothing need to do
        }

    }

    /**
     * Test method for {@link org.talend.dataprofiler.ecos.service.EcosystemService#getCategoryList(java.lang.String)}.
     */
    @Ignore
    @Test
    public void testGetCategoryList() {
        // List<IEcosCategory> list;
        // try {
        //            list = EcosystemService.getCategoryList("3.1.2");//$NON-NLS-1$
        // for (IEcosCategory iEcosCategory : list) {
        // EcosCategory object = (EcosCategory) iEcosCategory;
        //                Assert.assertEquals("3.1.2", object.getVersion()); //$NON-NLS-1$
        //
        // }
        // assertNotNull(list);
        // } catch (Exception e) {
        // fail(e.getMessage());
        // }

    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.ecos.service.EcosystemService#getRevisionList(java.lang.String, java.lang.String)} .
     */
    @Ignore
    @Test
    public void testGetRevisionList() {
        // try {
        //            List<RevisionInfo> revisionList = EcosystemService.getRevisionList("37", "5.6.0"); //$NON-NLS-1$ //$NON-NLS-2$
        //            Assert.assertTrue("revisionList should be more than 14", revisionList.size() >= 14); //$NON-NLS-1$
        // } catch (Exception e) {
        // fail(e.getMessage());
        // }
    }

}
