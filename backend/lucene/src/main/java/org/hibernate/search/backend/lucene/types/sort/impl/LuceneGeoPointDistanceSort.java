/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.backend.lucene.types.sort.impl;

import java.lang.invoke.MethodHandles;

import org.hibernate.search.backend.lucene.logging.impl.Log;
import org.hibernate.search.backend.lucene.search.impl.AbstractLuceneSearchValueFieldQueryElementFactory;
import org.hibernate.search.backend.lucene.search.impl.LuceneSearchContext;
import org.hibernate.search.backend.lucene.search.impl.LuceneSearchValueFieldContext;
import org.hibernate.search.backend.lucene.types.sort.comparatorsource.impl.LuceneFieldComparatorSource;
import org.hibernate.search.backend.lucene.types.sort.comparatorsource.impl.LuceneGeoPointDistanceComparatorSource;
import org.hibernate.search.engine.search.common.SortMode;
import org.hibernate.search.engine.search.sort.SearchSort;
import org.hibernate.search.engine.search.sort.spi.DistanceSortBuilder;
import org.hibernate.search.engine.spatial.GeoPoint;
import org.hibernate.search.util.common.logging.impl.LoggerFactory;

public class LuceneGeoPointDistanceSort extends AbstractLuceneDocumentValueSort {

	private static final Log log = LoggerFactory.make( Log.class, MethodHandles.lookup() );

	private LuceneGeoPointDistanceSort(Builder builder) {
		super( builder );
	}

	public static class Factory
			extends AbstractLuceneSearchValueFieldQueryElementFactory<DistanceSortBuilder, GeoPoint> {
		@Override
		public DistanceSortBuilder create(LuceneSearchContext searchContext,
				LuceneSearchValueFieldContext<GeoPoint> field) {
			return new Builder( searchContext, field );
		}
	}

	private static class Builder extends AbstractBuilder implements DistanceSortBuilder {
		private GeoPoint center;

		private Builder(LuceneSearchContext searchContext, LuceneSearchValueFieldContext<GeoPoint> field) {
			super( searchContext, field );
		}

		@Override
		public void center(GeoPoint center) {
			this.center = center;
		}

		@Override
		public void mode(SortMode mode) {
			switch ( mode ) {
				case MIN:
				case MAX:
				case AVG:
				case MEDIAN:
					super.mode( mode );
					break;
				case SUM:
				default:
					throw log.invalidSortModeForDistanceSort( mode, getEventContext() );
			}
		}

		@Override
		public SearchSort build() {
			return new LuceneGeoPointDistanceSort( this );
		}

		@Override
		protected LuceneFieldComparatorSource toFieldComparatorSource() {
			return new LuceneGeoPointDistanceComparatorSource( nestedDocumentPath, center, getMultiValueMode(),
					getNestedFilter() );
		}
	}
}
