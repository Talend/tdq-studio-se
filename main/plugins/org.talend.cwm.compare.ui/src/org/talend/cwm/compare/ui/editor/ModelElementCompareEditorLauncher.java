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
package org.talend.cwm.compare.ui.editor;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.ICompareInputLabelProvider;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorLauncher;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.cwm.compare.i18n.Messages;
import org.talend.dq.helper.resourcehelper.PrvResourceFileHelper;
import org.talend.dq.nodes.foldernode.IFolderNode;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.resource.relational.Catalog;

/**
 * DOC mzhao class global comment. Detailled comment
 */
public class ModelElementCompareEditorLauncher implements IEditorLauncher {

    private String connectionName = ""; //$NON-NLS-1$

    private Object selectedObject = null;

    private boolean compareEachOther;

    public ModelElementCompareEditorLauncher(String connName, Object selObj, boolean ce) {
        connectionName = connName;
        selectedObject = selObj;
        compareEachOther = ce;
    }

    @Override
    public void open(IPath file) {
        try {
            // final EObject snapshot = ModelUtils.load(file.toFile(), new ResourceSetImpl());
            // if (snapshot instanceof ComparisonResourceSnapshot) {
            CompareConfiguration comapreConfiguration = new CompareConfiguration();
            comapreConfiguration.setDefaultLabelProvider(new ICompareInputLabelProvider() {

                @Override
                public Image getAncestorImage(Object input) {
                    return null;
                }

                @Override
                public String getAncestorLabel(Object input) {
                    return ""; //$NON-NLS-1$
                }

                @Override
                public Image getLeftImage(Object input) {
                    return null;
                }

                @Override
                public String getLeftLabel(Object input) {
                    String showLabel = Messages.getString("ModelElementCompareEditorLauncher.LocalStructure", connectionName); //$NON-NLS-1$
                    if (compareEachOther) {
                        showLabel = Messages.getString("ModelElementCompareEditorLauncher.FirstElement"); //$NON-NLS-1$
                    }
                    return showLabel;
                }

                @Override
                public Image getRightImage(Object input) {
                    return null;
                }

                @Override
                public String getRightLabel(Object input) {
                    String showLabel = Messages.getString("ModelElementCompareEditorLauncher.DistantStructure"); //$NON-NLS-1$
                    if (compareEachOther) {
                        showLabel = Messages.getString("ModelElementCompareEditorLauncher.SecondElement"); //$NON-NLS-1$
                    }
                    return showLabel;
                }

                @Override
                public Image getImage(Object element) {
                    return null;
                }

                @Override
                public String getText(Object element) {
                    return Messages.getString("ModelElementCompareEditorLauncher.TextLabel"); //$NON-NLS-1$
                }

                @Override
                public void addListener(ILabelProviderListener listener) {
                    // needn't to do anything ???
                }

                @Override
                public void dispose() {
                    // needn't to do anything ???
                }

                @Override
                public boolean isLabelProperty(Object element, String property) {
                    return false;
                }

                @Override
                public void removeListener(ILabelProviderListener listener) {
                    // needn't to do anything ???
                }

            });
            // ModelElementCompareEditorInput compEditorInput = new ModelElementCompareEditorInput(
            // (ComparisonResourceSnapshot) snapshot, comapreConfiguration, selectedObject);
            // MOD mzhao bug 8581 Add the specific title for comparison
            // editor.
            String editorTitle = Messages.getString("ModelElementCompareEditorLauncher.Compare"); //$NON-NLS-1$
            if (selectedObject instanceof IFile) {
                editorTitle = PrvResourceFileHelper.getInstance().findProvider((IFile) selectedObject).getName();
            } else if (selectedObject instanceof IFolderNode) {
                editorTitle = ((ModelElement) ((IFolderNode) selectedObject).getParent()).getName();
            } else if (selectedObject instanceof Catalog) {
                editorTitle = ((Catalog) selectedObject).getName();
            } else if (selectedObject instanceof IRepositoryViewObject) {
                editorTitle = ((ConnectionItem) ((IRepositoryViewObject) selectedObject).getProperty().getItem()).getConnection()
                        .getName();
            } else if (selectedObject instanceof Connection) {
                editorTitle = ((Connection) selectedObject).getName();
            }
            // compEditorInput.setTitle(editorTitle);
            // CompareUI.openCompareEditor(compEditorInput);
            // compEditorInput.hookLeftPanelContextMenu(compareEachOther);
            // compEditorInput.hookToolBar(compareEachOther);

            // }
            // } catch (IOException e) {
        } catch (Exception e) {
            // Fichier non lisible
            System.out.println(e);
            assert false;
        }
    }

}
