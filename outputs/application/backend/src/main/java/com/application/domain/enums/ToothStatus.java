package com.application.domain.enums;

/**
 * Enumeration defining possible conditions of a tooth.
 * Used in the Odontogram and ToothCondition domain objects.
 */
public enum ToothStatus {
    /**
     * Tooth is in a healthy, natural state with no issues.
     */
    HEALTHY,

    /**
     * Tooth has decay (cavities).
     */
    CARIES,

    /**
     * Tooth has been restored with a filling.
     */
    FILLED,

    /**
     * Tooth has undergone root canal treatment.
     */
    ROOT_CANAL,

    /**
     * Tooth is no longer present in the mouth.
     */
    MISSING,

    /**
     * Tooth has been replaced with a dental implant.
     */
    IMPLANT
}