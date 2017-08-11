/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.dataquality.rules.util;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.rules.*;
import org.talend.dataquality.rules.AlgorithmDefinition;
import org.talend.dataquality.rules.AppliedBlockKey;
import org.talend.dataquality.rules.BlockKeyDefinition;
import org.talend.dataquality.rules.DQRule;
import org.talend.dataquality.rules.DefaultSurvivorshipDefinition;
import org.talend.dataquality.rules.InferredDQRule;
import org.talend.dataquality.rules.JoinElement;
import org.talend.dataquality.rules.KeyDefinition;
import org.talend.dataquality.rules.MatchKeyDefinition;
import org.talend.dataquality.rules.MatchRule;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dataquality.rules.ParserRule;
import org.talend.dataquality.rules.RulesPackage;
import org.talend.dataquality.rules.SpecifiedDQRule;
import org.talend.dataquality.rules.SurvivorshipKeyDefinition;
import org.talend.dataquality.rules.WhereRule;
import orgomg.cwm.objectmodel.core.Element;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.talend.dataquality.rules.RulesPackage
 * @generated
 */
public class RulesSwitch<T> {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static RulesPackage modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RulesSwitch() {
        if (modelPackage == null) {
            modelPackage = RulesPackage.eINSTANCE;
        }
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    public T doSwitch(EObject theEObject) {
        return doSwitch(theEObject.eClass(), theEObject);
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    protected T doSwitch(EClass theEClass, EObject theEObject) {
        if (theEClass.eContainer() == modelPackage) {
            return doSwitch(theEClass.getClassifierID(), theEObject);
        }
        else {
            List<EClass> eSuperTypes = theEClass.getESuperTypes();
            return
                eSuperTypes.isEmpty() ?
                    defaultCase(theEObject) :
                    doSwitch(eSuperTypes.get(0), theEObject);
        }
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    protected T doSwitch(int classifierID, EObject theEObject) {
        switch (classifierID) {
            case RulesPackage.DQ_RULE: {
                DQRule dqRule = (DQRule)theEObject;
                T result = caseDQRule(dqRule);
                if (result == null) result = caseIndicatorDefinition(dqRule);
                if (result == null) result = caseModelElement(dqRule);
                if (result == null) result = caseElement(dqRule);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case RulesPackage.SPECIFIED_DQ_RULE: {
                SpecifiedDQRule specifiedDQRule = (SpecifiedDQRule)theEObject;
                T result = caseSpecifiedDQRule(specifiedDQRule);
                if (result == null) result = caseDQRule(specifiedDQRule);
                if (result == null) result = caseIndicatorDefinition(specifiedDQRule);
                if (result == null) result = caseModelElement(specifiedDQRule);
                if (result == null) result = caseElement(specifiedDQRule);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case RulesPackage.INFERRED_DQ_RULE: {
                InferredDQRule inferredDQRule = (InferredDQRule)theEObject;
                T result = caseInferredDQRule(inferredDQRule);
                if (result == null) result = caseDQRule(inferredDQRule);
                if (result == null) result = caseIndicatorDefinition(inferredDQRule);
                if (result == null) result = caseModelElement(inferredDQRule);
                if (result == null) result = caseElement(inferredDQRule);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case RulesPackage.WHERE_RULE: {
                WhereRule whereRule = (WhereRule)theEObject;
                T result = caseWhereRule(whereRule);
                if (result == null) result = caseSpecifiedDQRule(whereRule);
                if (result == null) result = caseDQRule(whereRule);
                if (result == null) result = caseIndicatorDefinition(whereRule);
                if (result == null) result = caseModelElement(whereRule);
                if (result == null) result = caseElement(whereRule);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case RulesPackage.JOIN_ELEMENT: {
                JoinElement joinElement = (JoinElement)theEObject;
                T result = caseJoinElement(joinElement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case RulesPackage.PARSER_RULE: {
                ParserRule parserRule = (ParserRule)theEObject;
                T result = caseParserRule(parserRule);
                if (result == null) result = caseDQRule(parserRule);
                if (result == null) result = caseIndicatorDefinition(parserRule);
                if (result == null) result = caseModelElement(parserRule);
                if (result == null) result = caseElement(parserRule);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case RulesPackage.MATCH_RULE_DEFINITION: {
                MatchRuleDefinition matchRuleDefinition = (MatchRuleDefinition)theEObject;
                T result = caseMatchRuleDefinition(matchRuleDefinition);
                if (result == null) result = caseIndicatorDefinition(matchRuleDefinition);
                if (result == null) result = caseModelElement(matchRuleDefinition);
                if (result == null) result = caseElement(matchRuleDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case RulesPackage.BLOCK_KEY_DEFINITION: {
                BlockKeyDefinition blockKeyDefinition = (BlockKeyDefinition)theEObject;
                T result = caseBlockKeyDefinition(blockKeyDefinition);
                if (result == null) result = caseKeyDefinition(blockKeyDefinition);
                if (result == null) result = caseModelElement(blockKeyDefinition);
                if (result == null) result = caseElement(blockKeyDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case RulesPackage.KEY_DEFINITION: {
                KeyDefinition keyDefinition = (KeyDefinition)theEObject;
                T result = caseKeyDefinition(keyDefinition);
                if (result == null) result = caseModelElement(keyDefinition);
                if (result == null) result = caseElement(keyDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case RulesPackage.MATCH_KEY_DEFINITION: {
                MatchKeyDefinition matchKeyDefinition = (MatchKeyDefinition)theEObject;
                T result = caseMatchKeyDefinition(matchKeyDefinition);
                if (result == null) result = caseKeyDefinition(matchKeyDefinition);
                if (result == null) result = caseModelElement(matchKeyDefinition);
                if (result == null) result = caseElement(matchKeyDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case RulesPackage.ALGORITHM_DEFINITION: {
                AlgorithmDefinition algorithmDefinition = (AlgorithmDefinition)theEObject;
                T result = caseAlgorithmDefinition(algorithmDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case RulesPackage.MATCH_RULE: {
                MatchRule matchRule = (MatchRule)theEObject;
                T result = caseMatchRule(matchRule);
                if (result == null) result = caseModelElement(matchRule);
                if (result == null) result = caseElement(matchRule);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case RulesPackage.APPLIED_BLOCK_KEY: {
                AppliedBlockKey appliedBlockKey = (AppliedBlockKey)theEObject;
                T result = caseAppliedBlockKey(appliedBlockKey);
                if (result == null) result = caseKeyDefinition(appliedBlockKey);
                if (result == null) result = caseModelElement(appliedBlockKey);
                if (result == null) result = caseElement(appliedBlockKey);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case RulesPackage.SURVIVORSHIP_KEY_DEFINITION: {
                SurvivorshipKeyDefinition survivorshipKeyDefinition = (SurvivorshipKeyDefinition)theEObject;
                T result = caseSurvivorshipKeyDefinition(survivorshipKeyDefinition);
                if (result == null) result = caseKeyDefinition(survivorshipKeyDefinition);
                if (result == null) result = caseModelElement(survivorshipKeyDefinition);
                if (result == null) result = caseElement(survivorshipKeyDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case RulesPackage.DEFAULT_SURVIVORSHIP_DEFINITION: {
                DefaultSurvivorshipDefinition defaultSurvivorshipDefinition = (DefaultSurvivorshipDefinition)theEObject;
                T result = caseDefaultSurvivorshipDefinition(defaultSurvivorshipDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case RulesPackage.PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS: {
                ParticularDefaultSurvivorshipDefinitions particularDefaultSurvivorshipDefinitions = (ParticularDefaultSurvivorshipDefinitions)theEObject;
                T result = caseParticularDefaultSurvivorshipDefinitions(particularDefaultSurvivorshipDefinitions);
                if (result == null) result = caseKeyDefinition(particularDefaultSurvivorshipDefinitions);
                if (result == null) result = caseDefaultSurvivorshipDefinition(particularDefaultSurvivorshipDefinitions);
                if (result == null) result = caseModelElement(particularDefaultSurvivorshipDefinitions);
                if (result == null) result = caseElement(particularDefaultSurvivorshipDefinitions);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>DQ Rule</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>DQ Rule</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDQRule(DQRule object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Specified DQ Rule</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Specified DQ Rule</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSpecifiedDQRule(SpecifiedDQRule object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Inferred DQ Rule</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Inferred DQ Rule</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseInferredDQRule(InferredDQRule object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Where Rule</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Where Rule</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseWhereRule(WhereRule object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Join Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Join Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseJoinElement(JoinElement object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Parser Rule</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Parser Rule</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseParserRule(ParserRule object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Match Rule Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Match Rule Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMatchRuleDefinition(MatchRuleDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Block Key Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Block Key Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBlockKeyDefinition(BlockKeyDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Key Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Key Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseKeyDefinition(KeyDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Match Key Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Match Key Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMatchKeyDefinition(MatchKeyDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Algorithm Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Algorithm Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAlgorithmDefinition(AlgorithmDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Match Rule</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Match Rule</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMatchRule(MatchRule object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Applied Block Key</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Applied Block Key</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAppliedBlockKey(AppliedBlockKey object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Survivorship Key Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Survivorship Key Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSurvivorshipKeyDefinition(SurvivorshipKeyDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Default Survivorship Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Default Survivorship Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDefaultSurvivorshipDefinition(DefaultSurvivorshipDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Particular Default Survivorship Definitions</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Particular Default Survivorship Definitions</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseParticularDefaultSurvivorshipDefinitions(ParticularDefaultSurvivorshipDefinitions object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseElement(Element object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Model Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Model Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseModelElement(ModelElement object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Indicator Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Indicator Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIndicatorDefinition(IndicatorDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch, but this is the last case anyway.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject)
     * @generated
     */
    public T defaultCase(EObject object) {
        return null;
    }

} //RulesSwitch
