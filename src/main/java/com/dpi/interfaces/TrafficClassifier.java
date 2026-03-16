package com.dpi.interfaces;

import com.dpi.model.Flow;
import com.dpi.model.TrafficCategory;

/**
 * Classifies network traffic flows into categories (e.g., streaming, gaming, malware).
 */
public interface TrafficClassifier {

    /**
     * Classify a network flow into a traffic category.
     *
     * @param flow the traffic flow to classify
     * @return category such as STREAMING, BROWSING, GAMING, MALWARE, etc.
     */
    TrafficCategory classify(Flow flow);

    /**
     * Return the classifier name/version for logging.
     *
     * @return classifier identifier
     */
    String getClassifierName();

    /**
     * Whether this classifier can operate on partial (incomplete) flows.
     *
     * @return true if partial flow classification is supported
     */
    boolean supportsPartialFlow();
}
