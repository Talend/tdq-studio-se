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
package org.talend.dataprofiler.core.license;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.PlatformUI;

/**
 * Manage the License validateion.
 */
public final class LicenseManagement {

    // LICENSE_VALIDATION_DONE = 1 : registration OK
    private static final double LICENSE_VALIDATION_DONE = 2;

    private LicenseManagement() {
    }

    public static void acceptLicense() {
        PlatformUI.getPreferenceStore().setValue("LICENSE_VALIDATION_DONE", 1); //$NON-NLS-1$
    }

    public static boolean isLicenseValidated() {
        initPreferenceStore();
        IPreferenceStore prefStore = PlatformUI.getPreferenceStore();
        if (prefStore.getInt("LICENSE_VALIDATION_DONE") != 1) { //$NON-NLS-1$
            return false;
        }
        return true;
    }

    private static void initPreferenceStore() {
        IPreferenceStore prefStore = PlatformUI.getPreferenceStore();
        if (prefStore.getDefaultInt("LICENSE_VALIDATION_DONE") == 0) { //$NON-NLS-1$
            prefStore.setDefault("LICENSE_VALIDATION_DONE", LICENSE_VALIDATION_DONE); //$NON-NLS-1$
        }
    }
}
