package org.talend.dataprofiler.core.migration.impl;

import java.util.Date;

import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataprofiler.core.migration.helper.IndicatorDefinitionFileHelper;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.indicators.definitions.DefinitionHandler;

import orgomg.cwm.objectmodel.core.TaggedValue;


public class UpdateDescriptionOfAllTextIndicatorsTask extends AbstractWorksapceUpdateTask {

    // indicator uuids
    private final String AVERAGE_LENGTH_WITH_BLANK_AND_NULL_UUID = "__TbUIJSOEd-TE5ti6XNR2Q"; //$NON-NLS-1$

    private final String AVERAGE_LENGTH_WITH_BLANK_UUID = "__gPoIJSOEd-TE5ti6XNR2Q"; //$NON-NLS-1$

    private final String AVERAGE_LENGTH_WITH_NULL_UUID = "__vI_wJSOEd-TE5ti6XNR2Q"; //$NON-NLS-1$

    private final String AVERAGE_LENGTH_UUID = "_ccIR4BF2Ed2PKb6nEJEvhw"; //$NON-NLS-1$

    private final String MAXIMAL_LENGTH_WITH_BLANK_AND_NULL_UUID = "_-hzp8JSOEd-TE5ti6XNR2Q"; //$NON-NLS-1$

    private final String MAXIMAL_LENGTH_WITH_BLANK_UUID = "_-xmZcJSOEd-TE5ti6XNR2Q"; //$NON-NLS-1$

    private final String MAXIMAL_LENGTH_WITH_NULL_UUID = "_-_UFUJSOEd-TE5ti6XNR2Q"; //$NON-NLS-1$

    private final String MAXIMAL_LENGTH_UUID = "_ccHq1RF2Ed2PKb6nEJEvhw"; //$NON-NLS-1$

    private final String MINIMAL_LENGTH_WITH_BLANK_AND_NULL_UUID = "_9HDjMJSOEd-TE5ti6XNR2Q"; //$NON-NLS-1$

    private final String MINIMAL_LENGTH_WITH_BLANK_UUID = "_G4EzQZU9Ed-Y15ulK_jijQ"; //$NON-NLS-1$

    private final String MINIMAL_LENGTH_WITH_NULL_UUID = "_a4KsoI1qEd-xwI2imLgHRA"; //$NON-NLS-1$

    private final String MINIMAL_LENGTH_UUID = "_ccHq1BF2Ed2PKb6nEJEvhw"; //$NON-NLS-1$

    @Override
    public Date getOrder() {
        return createDate(2019, 7, 3);
    }

    @Override
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    @Override
    protected boolean doExecute() throws Exception {
        boolean result = true;
        DefinitionHandler definitionHandler = DefinitionHandler.getInstance();

        // AVERAGE_LENGTH_WITH_BLANK_AND_NULL
        IndicatorDefinition definition = definitionHandler.getDefinitionById(AVERAGE_LENGTH_WITH_BLANK_AND_NULL_UUID);
        if (definition != null) {
            updateIndicator(definition, "evaluates the average length of the textual record",
                    "computes the average length of the field. ");
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // AVERAGE_LENGTH_WITH_BLANK
        definition = definitionHandler.getDefinitionById(AVERAGE_LENGTH_WITH_BLANK_UUID);
        if (definition != null) {
            updateIndicator(definition, "evaluates the average length of the textual record of non-null values",
                    "computes the average length of the field. Does not take into account the null values when computing the average length.");
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // AVERAGE_LENGTH_WITH_NULL
        definition = definitionHandler.getDefinitionById(AVERAGE_LENGTH_WITH_NULL_UUID);
        if (definition != null) {
            updateIndicator(definition, "evaluates the average length of the textual record of non-blank values",
                    "computes the average length of the field. Does not take into account the blank values when computing the average length.");
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // AVERAGE_LENGTH
        definition = definitionHandler.getDefinitionById(AVERAGE_LENGTH_UUID);
        if (definition != null) {
            updateIndicator(definition, "evaluates the average length of the textual record of non-null and non-blank values",
                    "computes the average length of the field. Does not take into account the null and blank values when computing the average length.");
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // MAXIMAL_LENGTH_WITH_BLANK_AND_NULL
        definition = definitionHandler.getDefinitionById(MAXIMAL_LENGTH_WITH_BLANK_AND_NULL_UUID);
        if (definition != null) {
            updateIndicator(definition, "evaluates the length of the longest textual record",
                    "computes the maximal length of a text field.");
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // MAXIMAL_LENGTH_WITH_BLANK
        definition = definitionHandler.getDefinitionById(MAXIMAL_LENGTH_WITH_BLANK_UUID);
        if (definition != null) {
            updateIndicator(definition, "evaluates the length of the longest textual record of non-null values",
                    "computes the maximal length of a text field. Does not take into account the null values when computing the maximal length.");
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // MAXIMAL_LENGTH_WITH_NULL
        definition = definitionHandler.getDefinitionById(MAXIMAL_LENGTH_WITH_NULL_UUID);
        if (definition != null) {
            updateIndicator(definition, "evaluates the length of the longest textual record of non-blank values",
                    "computes the maximal length of a text field. Does not take into account the blank values when computing the maximal length.");
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // MAXIMAL_LENGTH
        definition = definitionHandler.getDefinitionById(MAXIMAL_LENGTH_UUID);
        if (definition != null) {
            updateIndicator(definition, "evaluates the length of the longest textual record of non-null and non-blank values",
                    "computes the maximal length of a text field. Does not take into account the null and blank values when computing the maximal length.");
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // MINIMAL_LENGTH_WITH_BLANK_AND_NULL
        definition = definitionHandler.getDefinitionById(MINIMAL_LENGTH_WITH_BLANK_AND_NULL_UUID);
        if (definition != null) {
            updateIndicator(definition, "evaluates the length of the smallest textual record",
                    "computes the minimal length of a text field.");
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // MINIMAL_LENGTH_WITH_BLANK
        definition = definitionHandler.getDefinitionById(MINIMAL_LENGTH_WITH_BLANK_UUID);
        if (definition != null) {
            updateIndicator(definition, "evaluates the length of the smallest textual record of non-null values",
                    "computes the minimal length of a text field. Does not take into account the null values when computing the minimal length.");
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // MINIMAL_LENGTH_WITH_NULL
        definition = definitionHandler.getDefinitionById(MINIMAL_LENGTH_WITH_NULL_UUID);
        if (definition != null) {
            updateIndicator(definition, "evaluates the length of the smallest textual record of non-blank values",
                    "computes the minimal length of a text field. Does not take into account the blank values when computing the minimal length.");
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // MINIMAL_LENGTH
        definition = definitionHandler.getDefinitionById(MINIMAL_LENGTH_UUID);
        if (definition != null) {
            updateIndicator(definition, "evaluates the length of the smallest textual record of non-null and non-blank values",
                    "computes the minimal length of a text field. Does not take into account the null and blank values when computing the minimal length.");
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        DefinitionHandler.getInstance().reloadIndicatorsDefinitions();

        return result;
    }

    private void updateIndicator(IndicatorDefinition definition, String purpose, String description) {
        TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.DESCRIPTION, definition.getTaggedValue());
        if (taggedValue != null) {
            taggedValue.setValue(description);
        }
        taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.PURPOSE, definition.getTaggedValue());
        if (taggedValue != null) {
            taggedValue.setValue(purpose);
        }

    }
}
