/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.engine.search.dsl.aggregation;

import java.util.Map;

import org.hibernate.search.util.common.data.Range;

/**
 * The step in a "range" aggregation definition where optional parameters can be set,
 * (see the superinterface {@link RangeAggregationOptionsStep}),
 * or more ranges can be added.
 *
 * @param <F> The type of the targeted field.
 */
public interface RangeAggregationRangeMoreStep<F>
		extends RangeAggregationOptionsStep<F, Map<Range<F>, Long>>,
				RangeAggregationRangeStep<F> {

}