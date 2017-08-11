/**
 * <copyright> </copyright>
 *
 * $Id$
 */
package org.talend.dataquality.rules.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.talend.dataquality.indicators.definition.impl.IndicatorDefinitionImpl;
import org.talend.dataquality.rules.AppliedBlockKey;
import org.talend.dataquality.rules.BlockKeyDefinition;
import org.talend.dataquality.rules.DefaultSurvivorshipDefinition;
import org.talend.dataquality.rules.MatchRule;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dataquality.rules.ParticularDefaultSurvivorshipDefinitions;
import org.talend.dataquality.rules.RulesPackage;
import org.talend.dataquality.rules.SurvivorshipKeyDefinition;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Match Rule Definition</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.dataquality.rules.impl.MatchRuleDefinitionImpl#getBlockKeys <em>Block Keys</em>}</li>
 *   <li>{@link org.talend.dataquality.rules.impl.MatchRuleDefinitionImpl#getMatchRules <em>Match Rules</em>}</li>
 *   <li>{@link org.talend.dataquality.rules.impl.MatchRuleDefinitionImpl#getXmiId <em>Xmi Id</em>}</li>
 *   <li>{@link org.talend.dataquality.rules.impl.MatchRuleDefinitionImpl#getRecordLinkageAlgorithm <em>Record Linkage Algorithm</em>}</li>
 *   <li>{@link org.talend.dataquality.rules.impl.MatchRuleDefinitionImpl#getAppliedBlockKeys <em>Applied Block Keys</em>}</li>
 *   <li>{@link org.talend.dataquality.rules.impl.MatchRuleDefinitionImpl#getSurvivorshipKeys <em>Survivorship Keys</em>}</li>
 *   <li>{@link org.talend.dataquality.rules.impl.MatchRuleDefinitionImpl#getDefaultSurvivorshipDefinitions <em>Default Survivorship Definitions</em>}</li>
 *   <li>{@link org.talend.dataquality.rules.impl.MatchRuleDefinitionImpl#getMatchGroupQualityThreshold <em>Match Group Quality Threshold</em>}</li>
 *   <li>{@link org.talend.dataquality.rules.impl.MatchRuleDefinitionImpl#getParticularDefaultSurvivorshipDefinitions <em>Particular Default Survivorship Definitions</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MatchRuleDefinitionImpl extends IndicatorDefinitionImpl implements MatchRuleDefinition {

    /**
     * The cached value of the '{@link #getBlockKeys() <em>Block Keys</em>}' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getBlockKeys()
     * @generated
     * @ordered
     */
    protected EList<BlockKeyDefinition> blockKeys;

    /**
     * The cached value of the '{@link #getMatchRules() <em>Match Rules</em>}' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getMatchRules()
     * @generated
     * @ordered
     */
    protected EList<MatchRule> matchRules;

    /**
     * The default value of the '{@link #getXmiId() <em>Xmi Id</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getXmiId()
     * @generated
     * @ordered
     */
    protected static final String XMI_ID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getXmiId() <em>Xmi Id</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getXmiId()
     * @generated
     * @ordered
     */
    protected String xmiId = XMI_ID_EDEFAULT;

    /**
     * The default value of the '{@link #getRecordLinkageAlgorithm() <em>Record Linkage Algorithm</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getRecordLinkageAlgorithm()
     * @generated NOT
     * @ordered
     */
    protected static final String RECORD_LINKAGE_ALGORITHM_EDEFAULT = "simpleVSRMatcher";//$NON-NLS-1$

    /**
     * The cached value of the '{@link #getRecordLinkageAlgorithm() <em>Record Linkage Algorithm</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getRecordLinkageAlgorithm()
     * @generated
     * @ordered
     */
    protected String recordLinkageAlgorithm = RECORD_LINKAGE_ALGORITHM_EDEFAULT;

    /**
     * The cached value of the '{@link #getAppliedBlockKeys() <em>Applied Block Keys</em>}' containment reference list.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #getAppliedBlockKeys()
     * @generated
     * @ordered
     */
    protected EList<AppliedBlockKey> appliedBlockKeys;

    /**
     * The cached value of the '{@link #getSurvivorshipKeys() <em>Survivorship Keys</em>}' containment reference list.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #getSurvivorshipKeys()
     * @generated
     * @ordered
     */
    protected EList<SurvivorshipKeyDefinition> survivorshipKeys;

    /**
     * The cached value of the '{@link #getDefaultSurvivorshipDefinitions() <em>Default Survivorship Definitions</em>}' containment reference list.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #getDefaultSurvivorshipDefinitions()
     * @generated
     * @ordered
     */
    protected EList<DefaultSurvivorshipDefinition> defaultSurvivorshipDefinitions;

    /**
     * The default value of the '{@link #getMatchGroupQualityThreshold() <em>Match Group Quality Threshold</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getMatchGroupQualityThreshold()
     * @generated NOT
     * @ordered
     */
    protected static final double MATCH_GROUP_QUALITY_THRESHOLD_EDEFAULT = 0.9;

    /**
     * The cached value of the '{@link #getMatchGroupQualityThreshold() <em>Match Group Quality Threshold</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #getMatchGroupQualityThreshold()
     * @generated
     * @ordered
     */
    protected double matchGroupQualityThreshold = MATCH_GROUP_QUALITY_THRESHOLD_EDEFAULT;

    /**
     * The cached value of the '{@link #getParticularDefaultSurvivorshipDefinitions() <em>Particular Default Survivorship Definitions</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getParticularDefaultSurvivorshipDefinitions()
     * @generated
     * @ordered
     */
    protected EList<ParticularDefaultSurvivorshipDefinitions> particularDefaultSurvivorshipDefinitions;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected MatchRuleDefinitionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return RulesPackage.Literals.MATCH_RULE_DEFINITION;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<BlockKeyDefinition> getBlockKeys() {
        if (blockKeys == null) {
            blockKeys = new EObjectContainmentEList<BlockKeyDefinition>(BlockKeyDefinition.class, this, RulesPackage.MATCH_RULE_DEFINITION__BLOCK_KEYS);
        }
        return blockKeys;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<MatchRule> getMatchRules() {
        if (matchRules == null) {
            matchRules = new EObjectContainmentEList<MatchRule>(MatchRule.class, this, RulesPackage.MATCH_RULE_DEFINITION__MATCH_RULES);
        }
        return matchRules;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getXmiId() {
        return xmiId;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setXmiId(String newXmiId) {
        String oldXmiId = xmiId;
        xmiId = newXmiId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RulesPackage.MATCH_RULE_DEFINITION__XMI_ID, oldXmiId, xmiId));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getRecordLinkageAlgorithm() {
        return recordLinkageAlgorithm;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setRecordLinkageAlgorithm(String newRecordLinkageAlgorithm) {
        String oldRecordLinkageAlgorithm = recordLinkageAlgorithm;
        recordLinkageAlgorithm = newRecordLinkageAlgorithm;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RulesPackage.MATCH_RULE_DEFINITION__RECORD_LINKAGE_ALGORITHM, oldRecordLinkageAlgorithm, recordLinkageAlgorithm));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<AppliedBlockKey> getAppliedBlockKeys() {
        if (appliedBlockKeys == null) {
            appliedBlockKeys = new EObjectContainmentEList<AppliedBlockKey>(AppliedBlockKey.class, this, RulesPackage.MATCH_RULE_DEFINITION__APPLIED_BLOCK_KEYS);
        }
        return appliedBlockKeys;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<SurvivorshipKeyDefinition> getSurvivorshipKeys() {
        if (survivorshipKeys == null) {
            survivorshipKeys = new EObjectContainmentEList<SurvivorshipKeyDefinition>(SurvivorshipKeyDefinition.class, this, RulesPackage.MATCH_RULE_DEFINITION__SURVIVORSHIP_KEYS);
        }
        return survivorshipKeys;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<DefaultSurvivorshipDefinition> getDefaultSurvivorshipDefinitions() {
        if (defaultSurvivorshipDefinitions == null) {
            defaultSurvivorshipDefinitions = new EObjectContainmentEList<DefaultSurvivorshipDefinition>(DefaultSurvivorshipDefinition.class, this, RulesPackage.MATCH_RULE_DEFINITION__DEFAULT_SURVIVORSHIP_DEFINITIONS);
        }
        return defaultSurvivorshipDefinitions;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public double getMatchGroupQualityThreshold() {
        return matchGroupQualityThreshold;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setMatchGroupQualityThreshold(double newMatchGroupQualityThreshold) {
        double oldMatchGroupQualityThreshold = matchGroupQualityThreshold;
        matchGroupQualityThreshold = newMatchGroupQualityThreshold;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RulesPackage.MATCH_RULE_DEFINITION__MATCH_GROUP_QUALITY_THRESHOLD, oldMatchGroupQualityThreshold, matchGroupQualityThreshold));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<ParticularDefaultSurvivorshipDefinitions> getParticularDefaultSurvivorshipDefinitions() {
        if (particularDefaultSurvivorshipDefinitions == null) {
            particularDefaultSurvivorshipDefinitions = new EObjectContainmentEList<ParticularDefaultSurvivorshipDefinitions>(ParticularDefaultSurvivorshipDefinitions.class, this, RulesPackage.MATCH_RULE_DEFINITION__PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS);
        }
        return particularDefaultSurvivorshipDefinitions;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case RulesPackage.MATCH_RULE_DEFINITION__BLOCK_KEYS:
                return ((InternalEList<?>)getBlockKeys()).basicRemove(otherEnd, msgs);
            case RulesPackage.MATCH_RULE_DEFINITION__MATCH_RULES:
                return ((InternalEList<?>)getMatchRules()).basicRemove(otherEnd, msgs);
            case RulesPackage.MATCH_RULE_DEFINITION__APPLIED_BLOCK_KEYS:
                return ((InternalEList<?>)getAppliedBlockKeys()).basicRemove(otherEnd, msgs);
            case RulesPackage.MATCH_RULE_DEFINITION__SURVIVORSHIP_KEYS:
                return ((InternalEList<?>)getSurvivorshipKeys()).basicRemove(otherEnd, msgs);
            case RulesPackage.MATCH_RULE_DEFINITION__DEFAULT_SURVIVORSHIP_DEFINITIONS:
                return ((InternalEList<?>)getDefaultSurvivorshipDefinitions()).basicRemove(otherEnd, msgs);
            case RulesPackage.MATCH_RULE_DEFINITION__PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS:
                return ((InternalEList<?>)getParticularDefaultSurvivorshipDefinitions()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case RulesPackage.MATCH_RULE_DEFINITION__BLOCK_KEYS:
                return getBlockKeys();
            case RulesPackage.MATCH_RULE_DEFINITION__MATCH_RULES:
                return getMatchRules();
            case RulesPackage.MATCH_RULE_DEFINITION__XMI_ID:
                return getXmiId();
            case RulesPackage.MATCH_RULE_DEFINITION__RECORD_LINKAGE_ALGORITHM:
                return getRecordLinkageAlgorithm();
            case RulesPackage.MATCH_RULE_DEFINITION__APPLIED_BLOCK_KEYS:
                return getAppliedBlockKeys();
            case RulesPackage.MATCH_RULE_DEFINITION__SURVIVORSHIP_KEYS:
                return getSurvivorshipKeys();
            case RulesPackage.MATCH_RULE_DEFINITION__DEFAULT_SURVIVORSHIP_DEFINITIONS:
                return getDefaultSurvivorshipDefinitions();
            case RulesPackage.MATCH_RULE_DEFINITION__MATCH_GROUP_QUALITY_THRESHOLD:
                return getMatchGroupQualityThreshold();
            case RulesPackage.MATCH_RULE_DEFINITION__PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS:
                return getParticularDefaultSurvivorshipDefinitions();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case RulesPackage.MATCH_RULE_DEFINITION__BLOCK_KEYS:
                getBlockKeys().clear();
                getBlockKeys().addAll((Collection<? extends BlockKeyDefinition>)newValue);
                return;
            case RulesPackage.MATCH_RULE_DEFINITION__MATCH_RULES:
                getMatchRules().clear();
                getMatchRules().addAll((Collection<? extends MatchRule>)newValue);
                return;
            case RulesPackage.MATCH_RULE_DEFINITION__XMI_ID:
                setXmiId((String)newValue);
                return;
            case RulesPackage.MATCH_RULE_DEFINITION__RECORD_LINKAGE_ALGORITHM:
                setRecordLinkageAlgorithm((String)newValue);
                return;
            case RulesPackage.MATCH_RULE_DEFINITION__APPLIED_BLOCK_KEYS:
                getAppliedBlockKeys().clear();
                getAppliedBlockKeys().addAll((Collection<? extends AppliedBlockKey>)newValue);
                return;
            case RulesPackage.MATCH_RULE_DEFINITION__SURVIVORSHIP_KEYS:
                getSurvivorshipKeys().clear();
                getSurvivorshipKeys().addAll((Collection<? extends SurvivorshipKeyDefinition>)newValue);
                return;
            case RulesPackage.MATCH_RULE_DEFINITION__DEFAULT_SURVIVORSHIP_DEFINITIONS:
                getDefaultSurvivorshipDefinitions().clear();
                getDefaultSurvivorshipDefinitions().addAll((Collection<? extends DefaultSurvivorshipDefinition>)newValue);
                return;
            case RulesPackage.MATCH_RULE_DEFINITION__MATCH_GROUP_QUALITY_THRESHOLD:
                setMatchGroupQualityThreshold((Double)newValue);
                return;
            case RulesPackage.MATCH_RULE_DEFINITION__PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS:
                getParticularDefaultSurvivorshipDefinitions().clear();
                getParticularDefaultSurvivorshipDefinitions().addAll((Collection<? extends ParticularDefaultSurvivorshipDefinitions>)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case RulesPackage.MATCH_RULE_DEFINITION__BLOCK_KEYS:
                getBlockKeys().clear();
                return;
            case RulesPackage.MATCH_RULE_DEFINITION__MATCH_RULES:
                getMatchRules().clear();
                return;
            case RulesPackage.MATCH_RULE_DEFINITION__XMI_ID:
                setXmiId(XMI_ID_EDEFAULT);
                return;
            case RulesPackage.MATCH_RULE_DEFINITION__RECORD_LINKAGE_ALGORITHM:
                setRecordLinkageAlgorithm(RECORD_LINKAGE_ALGORITHM_EDEFAULT);
                return;
            case RulesPackage.MATCH_RULE_DEFINITION__APPLIED_BLOCK_KEYS:
                getAppliedBlockKeys().clear();
                return;
            case RulesPackage.MATCH_RULE_DEFINITION__SURVIVORSHIP_KEYS:
                getSurvivorshipKeys().clear();
                return;
            case RulesPackage.MATCH_RULE_DEFINITION__DEFAULT_SURVIVORSHIP_DEFINITIONS:
                getDefaultSurvivorshipDefinitions().clear();
                return;
            case RulesPackage.MATCH_RULE_DEFINITION__MATCH_GROUP_QUALITY_THRESHOLD:
                setMatchGroupQualityThreshold(MATCH_GROUP_QUALITY_THRESHOLD_EDEFAULT);
                return;
            case RulesPackage.MATCH_RULE_DEFINITION__PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS:
                getParticularDefaultSurvivorshipDefinitions().clear();
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case RulesPackage.MATCH_RULE_DEFINITION__BLOCK_KEYS:
                return blockKeys != null && !blockKeys.isEmpty();
            case RulesPackage.MATCH_RULE_DEFINITION__MATCH_RULES:
                return matchRules != null && !matchRules.isEmpty();
            case RulesPackage.MATCH_RULE_DEFINITION__XMI_ID:
                return XMI_ID_EDEFAULT == null ? xmiId != null : !XMI_ID_EDEFAULT.equals(xmiId);
            case RulesPackage.MATCH_RULE_DEFINITION__RECORD_LINKAGE_ALGORITHM:
                return RECORD_LINKAGE_ALGORITHM_EDEFAULT == null ? recordLinkageAlgorithm != null : !RECORD_LINKAGE_ALGORITHM_EDEFAULT.equals(recordLinkageAlgorithm);
            case RulesPackage.MATCH_RULE_DEFINITION__APPLIED_BLOCK_KEYS:
                return appliedBlockKeys != null && !appliedBlockKeys.isEmpty();
            case RulesPackage.MATCH_RULE_DEFINITION__SURVIVORSHIP_KEYS:
                return survivorshipKeys != null && !survivorshipKeys.isEmpty();
            case RulesPackage.MATCH_RULE_DEFINITION__DEFAULT_SURVIVORSHIP_DEFINITIONS:
                return defaultSurvivorshipDefinitions != null && !defaultSurvivorshipDefinitions.isEmpty();
            case RulesPackage.MATCH_RULE_DEFINITION__MATCH_GROUP_QUALITY_THRESHOLD:
                return matchGroupQualityThreshold != MATCH_GROUP_QUALITY_THRESHOLD_EDEFAULT;
            case RulesPackage.MATCH_RULE_DEFINITION__PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS:
                return particularDefaultSurvivorshipDefinitions != null && !particularDefaultSurvivorshipDefinitions.isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (xmiId: ");
        result.append(xmiId);
        result.append(", recordLinkageAlgorithm: ");
        result.append(recordLinkageAlgorithm);
        result.append(", matchGroupQualityThreshold: ");
        result.append(matchGroupQualityThreshold);
        result.append(')');
        return result.toString();
    }

} // MatchRuleDefinitionImpl
