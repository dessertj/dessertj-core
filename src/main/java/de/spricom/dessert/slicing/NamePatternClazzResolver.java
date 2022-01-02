package de.spricom.dessert.slicing;

/*-
 * #%L
 * Dessert Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2022 Hans Jörg Heßmann
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import de.spricom.dessert.matching.NamePattern;
import de.spricom.dessert.resolve.ClassEntry;
import de.spricom.dessert.resolve.ClassVisitor;
import de.spricom.dessert.resolve.TraversalRoot;

import java.util.Set;

final class NamePatternClazzResolver extends AbstractClazzResolver implements ClassVisitor {
    private final NamePattern pattern;
    private final TraversalRoot root;

    NamePatternClazzResolver(Classpath cp, NamePattern pattern, TraversalRoot root) {
        super(cp);
        this.pattern = pattern;
        this.root = root;
    }

    @Override
    protected void resolve() {
        root.traverse(pattern, this);
    }

    @Override
    public void visit(ClassEntry ce) {
        add(ce);
    }

    @Override
    public Set<Clazz> getClazzes() {
        Set<Clazz> clazzes = super.getClazzes();
        if (clazzes.isEmpty() && pattern.isAny()) {
            throw new ResolveException("No classes found in " + root);
        }
        return clazzes;
    }

    public NamePatternClazzResolver filtered(NamePattern additionalPattern) {
        if (additionalPattern.isMoreConcreteThan(pattern)) {
            return new NamePatternClazzResolver(getClasspath(), additionalPattern, root);
        }
        return this;
    }
}
