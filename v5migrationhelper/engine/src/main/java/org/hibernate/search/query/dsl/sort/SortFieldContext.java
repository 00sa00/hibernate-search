/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.query.dsl.sort;

/**
 * @author Emmanuel Bernard emmanuel@hibernate.org
 * @author Yoann Rodiere
 * @deprecated See the deprecation note on {@link SortContext}.
 */
@Deprecated
public interface SortFieldContext extends SortAdditionalSortFieldContext, SortOrder<SortFieldContext>, SortTermination {

	/**
	 * Describe how to treat missing values when doing the sorting.
	 * @return a context to specify the behavior for missing values
	 */
	SortMissingValueContext<SortFieldContext> onMissingValue();

}
