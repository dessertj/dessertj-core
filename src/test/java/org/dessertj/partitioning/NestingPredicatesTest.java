package org.dessertj.partitioning;

/*-
 * #%L
 * DessertJ Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2023 Hans Jörg Heßmann
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

import org.dessertj.samples.TestGeneratorTool;
import org.dessertj.slicing.Classpath;
import org.dessertj.slicing.Clazz;
import org.dessertj.slicing.PartitionSlice;
import org.dessertj.slicing.Slice;
import org.junit.Test;

import java.util.SortedMap;

import static org.fest.assertions.Assertions.assertThat;

public class NestingPredicatesTest {
    private static final Classpath cp = new Classpath();
    private final Slice samples = cp.packageTreeOf(TestGeneratorTool.class);

    @Test
    public void testSamplesNests() {
        SortedMap<String, PartitionSlice> nests = samples.partitionBy(SlicePartitioners.HOST);
        dump(nests);
        for (PartitionSlice nest : nests.values()) {
            for (Clazz clazz : nest.getClazzes()) {
                assertThat(clazz.getNest().getClazzes()).as(clazz.getName()).isEqualTo(nest.getClazzes());
            }
        }
    }

    private void dump(SortedMap<String, PartitionSlice> nests) {
        for (PartitionSlice nest : nests.values()) {
            if (nest.getClazzes().size() > 1) {
                System.out.println(nest.getPartKey());
                for (Clazz clazz : nest.getClazzes()) {
                    System.out.println("\t" + clazz.getName());
                }
            }
        }
    }
}
