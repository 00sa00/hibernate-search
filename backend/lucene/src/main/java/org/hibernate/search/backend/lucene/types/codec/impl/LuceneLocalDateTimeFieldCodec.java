/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.backend.lucene.types.codec.impl;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.util.Locale;

import org.hibernate.search.backend.lucene.document.impl.LuceneDocumentBuilder;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.IndexableField;

public final class LuceneLocalDateTimeFieldCodec implements LuceneNumericFieldCodec<LocalDateTime, Long> {

	static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
			.append( LuceneLocalDateFieldCodec.FORMATTER )
			.appendLiteral( 'T' )
			.append( LuceneLocalTimeFieldCodec.FORMATTER )
			.toFormatter( Locale.ROOT )
			.withResolverStyle( ResolverStyle.STRICT );

	private final boolean projectable;

	private final boolean sortable;

	public LuceneLocalDateTimeFieldCodec(boolean projectable, boolean sortable) {
		this.projectable = projectable;
		this.sortable = sortable;
	}

	@Override
	public void encode(LuceneDocumentBuilder documentBuilder, String absoluteFieldPath, LocalDateTime value) {
		if ( value == null ) {
			return;
		}

		if ( projectable ) {
			documentBuilder.addField( new StoredField( absoluteFieldPath, FORMATTER.format( value ) ) );
		}

		long numericValue = encode( value );

		if ( sortable ) {
			documentBuilder.addField( new NumericDocValuesField( absoluteFieldPath, numericValue ) );
		}

		documentBuilder.addField( new LongPoint( absoluteFieldPath, numericValue ) );
	}

	@Override
	public LocalDateTime decode(Document document, String absoluteFieldPath) {
		IndexableField field = document.getField( absoluteFieldPath );

		if ( field == null ) {
			return null;
		}

		String value = field.stringValue();

		if ( value == null ) {
			return null;
		}

		return LocalDateTime.parse( value, FORMATTER );
	}

	@Override
	public boolean isCompatibleWith(LuceneFieldCodec<?> obj) {
		if ( this == obj ) {
			return true;
		}
		if ( LuceneLocalDateTimeFieldCodec.class != obj.getClass() ) {
			return false;
		}

		LuceneLocalDateTimeFieldCodec other = (LuceneLocalDateTimeFieldCodec) obj;

		return ( projectable == other.projectable ) && ( sortable == other.sortable );
	}

	@Override
	public Long encode(LocalDateTime value) {
		return value == null ? null : value.toInstant( ZoneOffset.UTC ).toEpochMilli();
	}

	@Override
	public LuceneNumericDomain<Long> getDomain() {
		return LuceneNumericDomain.LONG;
	}
}