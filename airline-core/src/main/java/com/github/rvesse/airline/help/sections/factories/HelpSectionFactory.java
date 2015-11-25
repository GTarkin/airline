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
package com.github.rvesse.airline.help.sections.factories;

import java.lang.annotation.Annotation;
import java.util.List;

import com.github.rvesse.airline.help.sections.HelpSection;

/**
 * Interface for factories that convert annotations into {@link HelpSection}
 * instances
 *
 */
public interface HelpSectionFactory {

    /**
     * Tries to create a section from the given annotation
     * 
     * @param annotation
     *            Annotation
     * @return Help section or {@code null} if the annotation is not supported
     */
    public abstract HelpSection createSection(Annotation annotation);

    /**
     * Gets a list of the supported annotations
     * 
     * @return Supported annotations
     */
    public abstract List<Class<? extends Annotation>> supportedAnnotations();
}
