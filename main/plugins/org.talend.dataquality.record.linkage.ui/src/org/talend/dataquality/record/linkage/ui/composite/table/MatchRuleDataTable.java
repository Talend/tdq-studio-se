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
package org.talend.dataquality.record.linkage.ui.composite.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.talend.core.service.IMatchRuleUIService;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.record.linkage.ui.MatchRuleUIService;
import org.talend.dataquality.record.linkage.ui.composite.utils.ImageLib;
import org.talend.dataquality.record.linkage.ui.i18n.internal.DefaultMessagesImpl;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class MatchRuleDataTable extends Composite {

    private static final Log log = LogFactory.getLog(MatchRuleDataTable.class);

    private List<String[]> viewData = new ArrayList<String[]>();

    private List<String[]> displayViewData;

    // TDQ-8774 change the type of GID from int to string
    private List<String> disGIDList = new ArrayList<String>();

    private final Map<String, Integer> itemCount = new HashMap<String, Integer>();

    protected TableViewer dataViewer;

    public static final Color[] COLOR_LIST = MatchRuleColorRegistry.getColorsForSwt();

    private int gidColumn, grpSizeColumn, masterColumn;

    private int page = 0;

    private int rowCount = 10;

    private int totalPage = 0;

    private String[] viewColumn;

    private Button firstBtn, previousBtn, nextBtn, lastBtn;

    private Label index;

    // TDQ-9297: Set the default value of "hide groups less than" to 2 instead of 1
    private int disGroupSize = PluginConstant.HIDDEN_GROUP_LESS_THAN_DEFAULT;

    private List<Integer> groupSizeList = new ArrayList<Integer>();

    private final ControlAdapter matchRuleTableResizeListener = new MatchRuleTableResizeListener();

    private IMatchRuleUIService uiService = null;

    /**
     * Getter for uiService.
     * 
     * @return the uiService
     */
    public IMatchRuleUIService getUiService() {
        return this.uiService;
    }

    /**
     * Sets the uiService.
     * 
     * @param uiService the uiService to set
     */
    public void setUiService(IMatchRuleUIService uiService) {
        this.uiService = uiService;
    }

    /**
     * DOC yyi DataTable constructor comment.
     */
    public MatchRuleDataTable(Composite parent, String[] viewColumn) {
        super(parent, SWT.NONE);

        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        this.setLayout(layout);
        this.viewColumn = viewColumn;
        initTableData();

    }

    private void intiTableData(List<String[]> viewData) {

        for (String[] value : viewData) {
            if (Boolean.valueOf(value[masterColumn])) {
                itemCount.put(value[gidColumn], Integer.parseInt(value[grpSizeColumn]));
            }
        }

    }

    public boolean initTableData() {
        // MOD yyi 2012-01-11 TDQ-4362:fix the bug when checking the distance detail option
        List<String> header = java.util.Arrays.asList(viewColumn);
        this.gidColumn = header.indexOf("GID"); //$NON-NLS-1$
        this.grpSizeColumn = header.indexOf("GRP_SIZE"); //$NON-NLS-1$
        this.masterColumn = header.indexOf("MASTER"); //$NON-NLS-1$
        createTable();
        createPagination();
        if (this.getUiService() != null) {
            MatchRuleUIService service = (MatchRuleUIService) this.getUiService();
            refresh(viewData, service.computeGroupSize2FreqMap(viewData, header));
        }
        return true;
    }

    /**
     * DOC yyi Comment method "createTable".
     */
    private void createTable() {

        GridLayout layout = new GridLayout();
        Composite composite = new Composite(this, SWT.BORDER);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, Boolean.TRUE, Boolean.TRUE));

        dataViewer = new TableViewer(composite, SWT.FULL_SELECTION | SWT.NO_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.BORDER);
        dataViewer.getTable().setHeaderVisible(true);
        dataViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));

        for (String title : viewColumn) {
            TableViewerColumn column = new TableViewerColumn(dataViewer, SWT.NONE);
            column.getColumn().setText(title);

            column.getColumn().setWidth(150);
            column.getColumn().setMoveable(true);

        }

        dataViewer.setContentProvider(new ArrayContentProvider());
        dataViewer.setLabelProvider(new DataLabelProvider());
        dataViewer.getTable().addControlListener(matchRuleTableResizeListener);

    }

    private void reComputePageSize() {
        // resize rowcount
        int tableHeight = dataViewer.getTable().getClientArea().height;
        int itemHeight = dataViewer.getTable().getItemHeight();
        rowCount = (int) Math.floor(tableHeight / itemHeight - 1);
        rowCount = rowCount < 1 ? 1 : rowCount;
        totalPage = (int) Math.ceil(1.0 * getDisplayViewData().size() / rowCount);
        // load page after resize
        page = 0;
    }

    /**
     * Getter for displayViewData.
     * 
     * @return the displayViewData
     */
    private List<String[]> getDisplayViewData() {
        if (this.displayViewData == null) {
            this.displayViewData = new ArrayList<String[]>();
        }
        return this.displayViewData;
    }

    private List<String[]> filterDisViewData() {
        displayViewData = new ArrayList<String[]>();
        for (String[] dataArray : viewData) {
            if (canDisplay(dataArray)) {
                displayViewData.add(dataArray);
            }
        }
        return displayViewData;
    }

    private boolean canDisplay(String[] dataArray) {
        try {
            if (getDisGIDList().contains(String.valueOf(dataArray[gidColumn]))) {
                return true;
            }
            if (getDisGroupSize() <= Integer.parseInt(dataArray[grpSizeColumn])) {
                getDisGIDList().add(String.valueOf(dataArray[gidColumn]));
                return true;
            }
        } catch (NumberFormatException e) {
            log.error(e, e);
        }
        return false;
    }

    /**
     * Getter for disGIDList.
     * 
     * @return the disGIDList
     */
    public List<String> getDisGIDList() {
        return this.disGIDList;
    }

    /**
     * Getter for disGroupSize.
     * 
     * @return the disGroupSize
     */
    private int getDisGroupSize() {
        return this.disGroupSize;
    }

    /**
     * Sets the disGroupSize.
     * 
     * @param disGroupSize the disGroupSize to set
     */
    public void setDisGroupSize(int disGroupSize) {
        this.disGroupSize = disGroupSize;
    }

    /**
     * DOC yyi Comment method "createPagination".
     */
    private void createPagination() {

        GridLayout layout = new GridLayout(5, false);

        Composite pagination = new Composite(this, SWT.NONE);
        pagination.setLayout(layout);
        GridData data = new GridData(SWT.END, SWT.CENTER, true, false);
        pagination.setLayoutData(data);

        data = new GridData();
        data.widthHint = 100;

        firstBtn = new Button(pagination, SWT.NONE);
        firstBtn.setText(DefaultMessagesImpl.getString("DataTable.first")); //$NON-NLS-1$
        firstBtn.setLayoutData(data);
        firstBtn.addSelectionListener(new PageButtonAdapter(0));

        previousBtn = new Button(pagination, SWT.NONE);
        previousBtn.setText(DefaultMessagesImpl.getString("DataTable.previous")); //$NON-NLS-1$
        previousBtn.setLayoutData(data);
        previousBtn.addSelectionListener(new PageButtonAdapter(1));

        nextBtn = new Button(pagination, SWT.NONE);
        nextBtn.setText(DefaultMessagesImpl.getString("DataTable.next")); //$NON-NLS-1$
        nextBtn.setLayoutData(data);
        nextBtn.addSelectionListener(new PageButtonAdapter(2));

        lastBtn = new Button(pagination, SWT.NONE);
        lastBtn.setText(DefaultMessagesImpl.getString("DataTable.last")); //$NON-NLS-1$
        lastBtn.setLayoutData(data);
        lastBtn.addSelectionListener(new PageButtonAdapter(3));

        index = new Label(pagination, SWT.NONE);

    }

    protected void loadPage(int page) {
        List<String[]> itemPage = new ArrayList<String[]>();
        for (int i = page * rowCount; i < Math.min((page + 1) * rowCount, getDisplayViewData().size()); i++) {
            itemPage.add(getDisplayViewData().get(i));
        }
        if (itemPage.size() >= 0) {
            // remove matchRuleTableResizeListener to avoid unHandle loop when H_SCORLL will be appear or disappear
            dataViewer.getTable().removeControlListener(matchRuleTableResizeListener);
            this.dataViewer.setInput(itemPage);
            dataViewer.getTable().addControlListener(matchRuleTableResizeListener);
            // ~
        }
        updateButons();
    }

    public void refresh(List<String[]> newViewData, TreeMap<Object, Long> groupSize2groupFrequency) {
        this.viewData = newViewData;
        initGroupSizeArray(groupSize2groupFrequency, getDisGroupSize());
        clearDisGIDList();
        filterDisViewData();
        intiTableData(getDisplayViewData());
        reComputePageSize();
        loadPage(page);
        this.dataViewer.refresh();
    }

    /**
     * DOC xqliu Comment method "buildGroupSizeArray".
     * 
     * @param groupSize2groupFrequency
     * @param minGroupSize
     */
    private void initGroupSizeArray(TreeMap<Object, Long> groupSize2groupFrequency, int minGroupSize) {
        groupSizeList.clear();
        for (Object obj : groupSize2groupFrequency.keySet()) {
            int grpSize = Integer.valueOf(obj.toString());
            if (grpSize >= minGroupSize) {
                groupSizeList.add(grpSize);
            }
        }
        Collections.sort(groupSizeList, new Comparator<Integer>() {

            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }

        });
    }

    /**
     * DOC zshen Comment method "clearDisGIDList".
     */
    private void clearDisGIDList() {
        getDisGIDList().clear();
    }

    /**
     * DOC yyi DataTable class global comment. Detailled comment
     */
    class DataLabelProvider extends LabelProvider implements ITableLabelProvider, ITableColorProvider {

        private static final String GRP_SIZE = "GRP_SIZE"; //$NON-NLS-1$

        private Image masterImage = ImageLib.getImage(ImageLib.MASTER_IMAGE);

        @Override
        public Image getColumnImage(Object element, int columnIndex) {
            String isMaster = ((String[]) element)[masterColumn];
            if (columnIndex == 0) {
                if (Boolean.parseBoolean(isMaster)) {
                    return masterImage;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }

        @Override
        public String getColumnText(Object element, int columnIndex) {
            if (GRP_SIZE.equals(viewColumn[columnIndex]) && "0".equals(((String[]) element)[columnIndex])) { //$NON-NLS-1$
                return ""; //$NON-NLS-1$
            } else {
                return ((String[]) element)[columnIndex];
            }
        }

        Color textColor = GUIHelper.COLOR_BLACK;

        @Override
        public Color getForeground(Object element, int columnIndex) {
            String isMaster = ((String[]) element)[masterColumn];
            if (Boolean.parseBoolean(isMaster)) {
                return GUIHelper.COLOR_BLACK;
            } else {
                return ImageLib.COLOR_GREY;
            }
        }

        @Override
        public org.eclipse.swt.graphics.Color getBackground(Object element, int columnIndex) {
            int grpSizeValue = getGrpSize(((String[]) element)[gidColumn]);
            return COLOR_LIST[Math.abs((grpSizeValue - 1) % COLOR_LIST.length)];
        }

        private int getGrpSize(String grpId) {
            if (itemCount == null) {
                return 0;
            }
            return itemCount.get(grpId);
        }

    }

    protected void updateButons() {
        firstBtn.setEnabled(true);
        previousBtn.setEnabled(true);
        lastBtn.setEnabled(true);
        nextBtn.setEnabled(true);
        // When the first time the wizard is opened, the table is empty, the next and last button should be in disable
        // status.
        if (totalPage == 0 || 0 == page) {
            firstBtn.setEnabled(false);
            previousBtn.setEnabled(false);
        }
        if (totalPage == 0 || totalPage == page + 1) {
            lastBtn.setEnabled(false);
            nextBtn.setEnabled(false);
        }

        index.setText(DefaultMessagesImpl.getString("DataTable.index", (page + 1), (totalPage))); //$NON-NLS-1$

        index.getParent().layout();
    }

    /**
     * DOC yyi DataTable class global comment. Detailled comment
     */
    class PageButtonAdapter extends SelectionAdapter {

        private final int type;

        public PageButtonAdapter(int type) {
            this.type = type;

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
         */
        @Override
        public void widgetSelected(SelectionEvent e) {
            switch (type) {
            case 0:
                page = 0;
                break;
            case 1:
                if (page > 0) {
                    page--;
                }
                break;
            case 2:
                if (page < totalPage) {
                    page++;
                }
                break;
            case 3:
                // MOD yyi 2011-10-31 last page
                page = totalPage - 1;
                break;
            }
            loadPage(page);
        }
    }

    private class MatchRuleTableResizeListener extends ControlAdapter {

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.swt.events.ControlAdapter#controlResized(org.eclipse.swt.events.ControlEvent)
         */
        @Override
        public void controlResized(ControlEvent e) {
            reComputePageSize();

            loadPage(page);

            dataViewer.getTable().layout();
        }
    }
}
