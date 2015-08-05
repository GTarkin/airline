/**
 * Copyright (C) 2010-15 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.rvesse.airline.restrictions.factories;

import java.lang.annotation.Annotation;
import java.util.Comparator;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.github.rvesse.airline.annotations.restrictions.ranges.ByteRange;
import com.github.rvesse.airline.annotations.restrictions.ranges.DoubleRange;
import com.github.rvesse.airline.annotations.restrictions.ranges.FloatRange;
import com.github.rvesse.airline.annotations.restrictions.ranges.IntegerRange;
import com.github.rvesse.airline.annotations.restrictions.ranges.LexicalRange;
import com.github.rvesse.airline.annotations.restrictions.ranges.LongRange;
import com.github.rvesse.airline.annotations.restrictions.ranges.ShortRange;
import com.github.rvesse.airline.restrictions.ArgumentsRestriction;
import com.github.rvesse.airline.restrictions.OptionRestriction;
import com.github.rvesse.airline.restrictions.common.RangeRestriction;
import com.github.rvesse.airline.utils.comparators.ByteComparator;
import com.github.rvesse.airline.utils.comparators.DoubleComparator;
import com.github.rvesse.airline.utils.comparators.FloatComparator;
import com.github.rvesse.airline.utils.comparators.IntegerComparator;
import com.github.rvesse.airline.utils.comparators.LexicalComparator;
import com.github.rvesse.airline.utils.comparators.LongComparator;
import com.github.rvesse.airline.utils.comparators.ShortComparator;

/**
 * Factory which generates range restrictions from a variety of different range
 * annotations
 *
 */
public class RangeRestrictionFactory implements OptionRestrictionFactory, ArgumentsRestrictionFactory {

    private static final Comparator<Object> LONG_COMPARATOR = new LongComparator();
    private static final Comparator<Object> INTEGER_COMPARATOR = new IntegerComparator();
    private static final Comparator<Object> SHORT_COMPARATOR = new ShortComparator();
    private static final Comparator<Object> BYTE_COMPARATOR = new ByteComparator();
    private static final Comparator<Object> DOUBLE_COMPARATOR = new DoubleComparator();
    private static final Comparator<Object> FLOAT_COMPARATOR = new FloatComparator();

    @Override
    public final OptionRestriction createOptionRestriction(Annotation annotation) {
        return createCommon(annotation);
    }

    @Override
    public final ArgumentsRestriction createArgumentsRestriction(Annotation annotation) {
        return createCommon(annotation);
    }

    /**
     * Handles converting all the basic range annotations into appropriate range
     * restrictions
     * 
     * @param annotation
     *            Annotation to create range from
     * @return Range restriction
     */
    protected final RangeRestriction createCommon(Annotation annotation) {
        if (annotation instanceof LongRange) {
            return createLongRange(annotation);
        } else if (annotation instanceof IntegerRange) {
            return createIntegerRange(annotation);
        } else if (annotation instanceof ShortRange) {
            return createShortRange(annotation);
        } else if (annotation instanceof ByteRange) {
            return createByteRange(annotation);
        } else if (annotation instanceof DoubleRange) {
            return createDoubleRange(annotation);
        } else if (annotation instanceof FloatRange) {
            return createFloatRange(annotation);
        } else if (annotation instanceof LexicalRange) {
            return createLexicalRange(annotation);
        }
        return createUnknownRange(annotation);
    }

    /**
     * Handles turning unknown annotations into range restrictions, derived
     * factories can extend this to add support for additional range
     * restrictions
     * 
     * @param annotation
     *            Annotation to create range from
     * 
     * @return Range restriction or null if not a supported annotation
     */
    protected RangeRestriction createUnknownRange(Annotation annotation) {
        return null;
    }

    protected RangeRestriction createLexicalRange(Annotation annotation) {
        LexicalRange lRange = (LexicalRange) annotation;
        return new RangeRestriction(StringUtils.isEmpty(lRange.min()) ? null : lRange.min(), lRange.minInclusive(),
                StringUtils.isEmpty(lRange.max()) ? null : lRange.max(), lRange.maxInclusive(), new LexicalComparator(
                        Locale.forLanguageTag(lRange.locale())));
    }

    protected RangeRestriction createFloatRange(Annotation annotation) {
        FloatRange sRange = (FloatRange) annotation;
        return new RangeRestriction(Float.valueOf(sRange.min()), sRange.minInclusive(), Float.valueOf(sRange.max()),
                sRange.maxInclusive(), FLOAT_COMPARATOR);
    }

    protected RangeRestriction createDoubleRange(Annotation annotation) {
        DoubleRange sRange = (DoubleRange) annotation;
        return new RangeRestriction(Double.valueOf(sRange.min()), sRange.minInclusive(), Double.valueOf(sRange.max()),
                sRange.maxInclusive(), DOUBLE_COMPARATOR);
    }

    protected RangeRestriction createByteRange(Annotation annotation) {
        ByteRange sRange = (ByteRange) annotation;
        return new RangeRestriction(Byte.valueOf(sRange.min()), sRange.minInclusive(), Byte.valueOf(sRange.max()),
                sRange.maxInclusive(), BYTE_COMPARATOR);
    }

    protected RangeRestriction createShortRange(Annotation annotation) {
        ShortRange sRange = (ShortRange) annotation;
        return new RangeRestriction(Short.valueOf(sRange.min()), sRange.minInclusive(), Short.valueOf(sRange.max()),
                sRange.maxInclusive(), SHORT_COMPARATOR);
    }

    protected RangeRestriction createIntegerRange(Annotation annotation) {
        IntegerRange iRange = (IntegerRange) annotation;
        return new RangeRestriction(Integer.valueOf(iRange.min()), iRange.minInclusive(),
                Integer.valueOf(iRange.max()), iRange.maxInclusive(), INTEGER_COMPARATOR);
    }

    protected RangeRestriction createLongRange(Annotation annotation) {
        LongRange iRange = (LongRange) annotation;
        return new RangeRestriction(Long.valueOf(iRange.min()), iRange.minInclusive(), Long.valueOf(iRange.max()),
                iRange.maxInclusive(), LONG_COMPARATOR);
    }

}