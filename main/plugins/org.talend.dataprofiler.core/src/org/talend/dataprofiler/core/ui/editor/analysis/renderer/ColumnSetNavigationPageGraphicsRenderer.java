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
package org.talend.dataprofiler.core.ui.editor.analysis.renderer;

import java.util.Locale;

import org.eclipse.nebula.widgets.pagination.AbstractPageControllerComposite;
import org.eclipse.nebula.widgets.pagination.PageableController;
import org.eclipse.nebula.widgets.pagination.PaginationHelper;
import org.eclipse.nebula.widgets.pagination.Resources;
import org.eclipse.nebula.widgets.pagination.renderers.navigation.graphics.BlueNavigationPageGraphicsConfigurator;
import org.eclipse.nebula.widgets.pagination.renderers.navigation.graphics.INavigationPageGraphicsConfigurator;
import org.eclipse.nebula.widgets.pagination.renderers.navigation.graphics.NavigationPageGraphics;
import org.eclipse.nebula.widgets.pagination.renderers.navigation.graphics.NavigationPageGraphicsItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class ColumnSetNavigationPageGraphicsRenderer extends AbstractPageControllerComposite {

    /** the result label **/
    private Label resultLabel;

    /** the navigation page graphics **/
    private NavigationPageGraphics navigationPage;

    private final INavigationPageGraphicsConfigurator configurator;

    private boolean keepLeft = false;

    public ColumnSetNavigationPageGraphicsRenderer(Composite parent, int style, PageableController controller) {
        this(parent, style, controller, BlueNavigationPageGraphicsConfigurator.getInstance());
    }

    public ColumnSetNavigationPageGraphicsRenderer(Composite parent, int style, PageableController controller,
            INavigationPageGraphicsConfigurator configurator) {
        super(parent, style, controller, PageableController.DEFAULT_PAGE_SIZE, null, false);
        this.configurator = configurator;
        createUI(this);
        refreshEnabled(controller);
    }

    /**
     * Create the UI like this :
     * 
     * <p>
     * 
     * <pre>
     * Results 1-5 of 10          Previous 1 2 ...10 Next
     * </pre>
     * 
     * </p>
     */
    @Override
    protected void createUI(Composite parent) {
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        this.setLayout(layout);

        if (keepLeft) {
            createLeftContainer(parent);
        }
        createRightContainer(parent);
    }

    /**
     * Create result label "Results 1-5 of 10"
     * 
     * @param parent
     */
    private void createEmptyLeftContainer(Composite parent) {
        Composite left = createComposite(parent, SWT.NONE);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = 100;
        left.setLayoutData(data);
    }

    /**
     * Create result label "Results 1-5 of 10"
     * 
     * @param parent
     */
    private void createLeftContainer(Composite parent) {
        Composite left = createComposite(parent, SWT.NONE);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        left.setLayoutData(data);

        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        left.setLayout(layout);

        resultLabel = new Label(left, SWT.NONE);
        resultLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }

    /**
     * Create page links "Previous 1 2 ...10 Next" with {@link GC}.
     * 
     * @param parent
     */
    private void createRightContainer(Composite parent) {
        Composite right = createComposite(parent, SWT.NONE);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        if (!keepLeft) {
            gridData = new GridData(SWT.END, SWT.CENTER, true, false, 2, 1);
            gridData.widthHint = 760;
        }
        right.setLayoutData(gridData);

        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        right.setLayout(layout);

        navigationPage = new ColumnSetNavigationPageGraphics(right, SWT.NONE, configurator) {

            @Override
            protected void handleSelection(NavigationPageGraphicsItem pageItem) {
                // Page item was clicked, update the page controller according
                // to the
                // selected
                // page item.
                Integer newCurrentPage = null;
                if (!pageItem.isEnabled()) {
                    return;
                }
                if (pageItem.isNext()) {
                    newCurrentPage = getController().getCurrentPage() + 1;
                } else if (pageItem.isPrevious()) {
                    newCurrentPage = getController().getCurrentPage() - 1;
                } else {
                    newCurrentPage = pageItem.getIndex();
                }
                if (newCurrentPage != null) {
                    getController().setCurrentPage(newCurrentPage);
                }
            }
        };
        navigationPage.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }

    public void pageIndexChanged(int oldPageNumber, int newPageNumber, PageableController controller) {
        // 1) Compute page indexes
        int[] indexes = PaginationHelper.getPageIndexes(controller.getCurrentPage(), controller.getTotalPages(), 10);
        // Update the GC navigation page with page indexes and selected page.
        navigationPage.update(indexes, newPageNumber, getLocale());
        refreshEnabled(controller);
    }

    public void pageSizeChanged(int oldPageSize, int newPageSize, PageableController paginationController) {
        // Do nothing
    }

    public void totalElementsChanged(long oldTotalElements, long newTotalElements, PageableController controller) {
        // 1) Compute page indexes
        int[] indexes = PaginationHelper.getPageIndexes(controller.getCurrentPage(), controller.getTotalPages(), 10);
        // Update the GC navigation page with page indexes and selected page.
        navigationPage.update(indexes, 0, getLocale());
        refreshEnabled(controller);
    }

    public void sortChanged(String oldPopertyName, String propertyName, int oldSortDirection, int sortDirection,
            PageableController paginationController) {
        // Do nothing
    }

    private void refreshEnabled(PageableController controller) {
        if (keepLeft) {
            resultLabel.setText(PaginationHelper.getResultsText(controller, getLocale()));
        }
        navigationPage.setEnabled(controller.hasPreviousPage(), controller.hasNextPage());
    }

    protected void displayResults(PageableController controller) {

    }

    protected Composite createComposite(Composite parent, int style) {
        return new Composite(parent, style);
    }

    @Override
    public void setLocale(Locale locale) {
        super.setLocale(locale);
        // Local has changed, update the Previous/Next link text.
        navigationPage.setText(Resources.getText(Resources.PaginationRenderer_previous, getLocale()),
                Resources.getText(Resources.PaginationRenderer_next, getLocale()));
        if (keepLeft) {
            resultLabel.setText(PaginationHelper.getResultsText(getController(), getLocale()));
        }
    }

    /**
     * Returns the {@link GC} navigation page.
     * 
     * @return
     */
    public NavigationPageGraphics getNavigationPage() {
        return navigationPage;
    }

    /**
     * Configure navigation page.
     * 
     * @param configurator
     */
    public void setConfigurator(INavigationPageGraphicsConfigurator configurator) {
        getNavigationPage().setConfigurator(configurator);
    }

    /**
     * Getter for keepLeft.
     * 
     * @return the keepLeft
     */
    public boolean isKeepLeft() {
        return this.keepLeft;
    }

    /**
     * Sets the keepLeft.
     * 
     * @param keepLeft the keepLeft to set
     */
    public void setKeepLeft(boolean keepLeft) {
        this.keepLeft = keepLeft;
    }

}