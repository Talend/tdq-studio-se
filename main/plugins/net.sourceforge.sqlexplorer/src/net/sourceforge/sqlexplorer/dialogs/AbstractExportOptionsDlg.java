package net.sourceforge.sqlexplorer.dialogs;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.SortedMap;

import net.sourceforge.sqlexplorer.IConstants;
import net.sourceforge.sqlexplorer.Messages;
import net.sourceforge.sqlexplorer.plugin.SQLExplorerPlugin;
import net.sourceforge.sqlexplorer.util.ImageUtil;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Abstract base dialog for obtaining data export options.
 * 
 * @author Rocco Rutte <a href="mailto:pdmef@gmx.net">&lt;pdmef@gmx.net&gt;</a>.
 * 
 */
public abstract class AbstractExportOptionsDlg extends TitleAreaDialog {

    private static final ImageDescriptor _image = ImageUtil.getDescriptor("Images.ExportIconLarge");

    private Combo uiCharset;

    private Combo uiDelim;

    private Text uiNullValue;

    private Button uiIncHeaders;

    private Button uiQuoteText;

    private Button uiRtrim;

    private Text uiFile;

    private String charset;

    private String delim;

    private String nullValue;

    private boolean incHeaders;

    private boolean quoteText;

    private boolean rtrim;

    private String file;

    private final Shell shell;

    /**
     * Flag indicating consumer wants to obtain character set.
     */
    public static final int FMT_CHARSET = 1 << 0;

    /**
     * Flag indicating consumer wants to obtain field delimiter.
     */
    public static final int FMT_DELIM = 1 << 1;

    /**
     * Flag indicating consumer wants to obtain null value string.
     */
    public static final int FMT_NULL = 1 << 2;

    /**
     * Flag indicating consumer wants to know whether to export column headers.
     */
    public static final int OPT_HDR = 1 << 3;

    /**
     * Flag indicating consumer wants to know whether to quote string values.
     */
    public static final int OPT_QUOTE = 1 << 4;

    /**
     * Flag indicating consumer wants to know whether to right-trim values.
     */
    public static final int OPT_RTRIM = 1 << 5;

    private static final String[] DELIMS = { ";", "|", "\\t [TAB]", "," };

    /**
     * Create new base dialog.
     * 
     * @param parentShell Parent's shell.
     */
    public AbstractExportOptionsDlg(Shell parentShell) {
        super(parentShell);
        this.shell = parentShell;
    }

    /**
     * Get dialog's title. It must be translated already.
     * 
     * @return Translated title.
     */
    public abstract String getTitle();

    /**
     * Get dialog's message. It must be translated already.
     * 
     * @return Translated message.
     */
    @Override
    public abstract String getMessage();

    /**
     * Get dialog's file filter when choosing input file.
     * 
     * @return List of file patterns (already containing '*').
     */
    public abstract String[] getFileFilter();

    /**
     * Get dialog's flags. Read: what to ask for. Choosing a filename cannot be turned off.
     * 
     * @return Flags.
     */
    public abstract int getFlags();

    @Override
    protected Control createContents(Composite parent) {
        Control contents = super.createContents(parent);
        setTitle(getTitle());
        setMessage(getMessage());
        setTitleImage(_image.createImage());
        return contents;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite comp = new Composite(parent, SWT.NONE);
        comp.setLayout(new FillLayout(SWT.VERTICAL));
        comp.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));

        String columnSeparator = SQLExplorerPlugin.getDefault().getPreferenceStore().getString(IConstants.CLIP_EXPORT_SEPARATOR);
        boolean hdr = SQLExplorerPlugin.getDefault().getPreferenceStore().getBoolean(IConstants.CLIP_EXPORT_COLUMNS);

        Label l = null;
        int flags = getFlags();
        addContentChooseRadio(comp);
        if ((flags & FMT_CHARSET) != 0 || (flags & FMT_DELIM) != 0 || (flags & FMT_NULL) != 0) {
            Group fmtGroup = new Group(comp, SWT.SHADOW_ETCHED_IN);
            fmtGroup.setText(Messages.getString("ExportDialog.group.format"));
            fmtGroup.setLayout(new GridLayout(2, false));
            int i = 0, def = 0;

            if ((flags & FMT_CHARSET) != 0) {
                l = new Label(fmtGroup, SWT.NONE);
                l.setText(Messages.getString("ExportDialog.format.cs"));
                uiCharset = new Combo(fmtGroup, SWT.READ_ONLY);
                SortedMap m = Charset.availableCharsets();
                for (Iterator it = m.keySet().iterator(); it.hasNext(); i++) {
                    Charset cs = (Charset) m.get(it.next());
                    uiCharset.add(cs.displayName());
                    if (cs.displayName().equalsIgnoreCase("utf-8")) {
                        def = i;
                    }
                }
                uiCharset.select(def);
            }

            if ((flags & FMT_DELIM) != 0) {
                l = new Label(fmtGroup, SWT.NONE);
                l.setText(Messages.getString("ExportDialog.format.delim"));
                uiDelim = new Combo(fmtGroup, SWT.NONE);
                for (i = 0, def = 0; i < DELIMS.length; i++) {
                    uiDelim.add(DELIMS[i]);
                    if (DELIMS[i].equalsIgnoreCase(columnSeparator)) {
                        def = i;
                    }
                }
                uiDelim.select(def);
            }

            if ((flags & FMT_NULL) != 0) {
                l = new Label(fmtGroup, SWT.NONE);
                l.setText(Messages.getString("ExportDialog.format.null"));
                uiNullValue = new Text(fmtGroup, SWT.SINGLE | SWT.BORDER | SWT.FILL);
                uiNullValue.setText("<null>");
                uiNullValue.setLayoutData(new GridData(50, SWT.DEFAULT));
            }
        }

        if ((flags & OPT_HDR) != 0 || (flags & OPT_QUOTE) != 0 || (flags & OPT_RTRIM) != 0) {
            Group optionsGroup = new Group(comp, SWT.SHADOW_ETCHED_IN);
            optionsGroup.setText(Messages.getString("ExportDialog.group.options"));
            optionsGroup.setLayout(new GridLayout(1, true));

            if ((flags & OPT_HDR) != 0) {
                uiIncHeaders = new Button(optionsGroup, SWT.CHECK);
                uiIncHeaders.setText(Messages.getString("ExportDialog.options.hdr"));
                uiIncHeaders.setSelection(hdr);
            }
            if ((flags & OPT_QUOTE) != 0) {
                uiQuoteText = new Button(optionsGroup, SWT.CHECK);
                uiQuoteText.setText(Messages.getString("ExportDialog.options.quote"));
            }
            if ((flags & OPT_RTRIM) != 0) {
                uiRtrim = new Button(optionsGroup, SWT.CHECK);
                uiRtrim.setText(Messages.getString("ExportDialog.options.rtrim"));
            }
        }

        Group fileGroup = new Group(comp, SWT.SHADOW_ETCHED_IN);
        fileGroup.setText(Messages.getString("ExportDialog.group.file"));
        fileGroup.setLayout(new GridLayout(2, false));

        uiFile = new Text(fileGroup, SWT.BORDER | SWT.FILL | SWT.SINGLE);
        uiFile.setLayoutData(new GridData(300, SWT.DEFAULT));
        uiFile.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
                sync();
            }

            public void keyReleased(KeyEvent e) {
                sync();
            }
        });
        Button choose = new Button(fileGroup, SWT.NONE);
        choose.setText(Messages.getString("ExportDialog.file.choose"));
        choose.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                chooseFilename();
            }
        });

        comp.pack();
        sync();
        return comp;
    }

    /**
     * DOC talend Comment method "addContentChooseRadio".
     * 
     * @param comp
     */
    protected abstract void addContentChooseRadio(Composite comp);

    @Override
    protected Control createButtonBar(Composite parent) {
        Control c = super.createButtonBar(parent);
        /*
         * interupt dialog setup to force Ok button to disabled since filename field is empty.
         */
        sync();
        return c;
    }

    /**
     * Toggle accessibility of Ok button depending on whether all input is given. This currently only depends on the
     * filename being present.
     */
    private void sync() {
        String filename = uiFile.getText();
        if (filename == null || filename.trim().length() == 0) {
            setErrorMessage(Messages.getString("ExportDialog.error.file"));
        } else {
            setErrorMessage(null);
        }
        Button ok = getButton(IDialogConstants.OK_ID);
        if (ok != null) {
            ok.setEnabled(filename != null && filename.trim().length() != 0);
        }
    }

    private void chooseFilename() {
        FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
        fileDialog.setFilterExtensions(getFileFilter());

        final String fileName = fileDialog.open();
        if (fileName != null && fileName.trim().length() != 0) {
            uiFile.setText(fileName);
        }
        sync();
    }

    @Override
    protected void okPressed() {
        charset = uiCharset != null ? uiCharset.getText() : null;
        delim = uiDelim != null ? uiDelim.getText() : null;
        incHeaders = uiIncHeaders != null && uiIncHeaders.getSelection();
        quoteText = uiQuoteText != null && uiQuoteText.getSelection();
        rtrim = uiRtrim != null && uiRtrim.getSelection();
        file = uiFile != null ? uiFile.getText() : null;
        nullValue = uiNullValue != null ? uiNullValue.getText() : null;
        super.okPressed();
    }

    /**
     * Return chosen character set. The underlying list is obtained from Character set, i.e. it's valid.
     * 
     * @return Character set or <tt>null</tt> if not requested.
     */
    public String getCharacterSet() {
        return charset;
    }

    /**
     * Return chosen delimiter.
     * 
     * @return Delimiter or <tt>null</tt> if not requested.
     */
    public String getDelimiter() {
        if (delim == null) {
            return null;
        }
        if (delim.toLowerCase().startsWith("\\t")) {
            return "\t";
        }
        return delim;
    }

    /**
     * Return whether to include column headers.
     * 
     * @return Whether to include column headers or <tt>false</tt> if not requested.
     */
    public boolean includeHeaders() {
        return incHeaders;
    }

    /**
     * Return whether to quote text values.
     * 
     * @return Whether to quote text values or <tt>false</tt> if not requested.
     */
    public boolean quoteText() {
        return quoteText;
    }

    /**
     * Return whether to right-trim spaces.
     * 
     * @return Whether to right-trim spaces or <tt>false</tt> if not requested.
     */
    public boolean trimSpaces() {
        return rtrim;
    }

    /**
     * Return chosen filename.
     * 
     * @return Filename
     */
    public String getFilename() {
        return file;
    }

    /**
     * Return chosen null value replacement string.
     * 
     * @return String or <tt>null</tt> if not requested.
     */
    public String getNullValue() {
        return nullValue;
    }
}