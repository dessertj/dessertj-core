package org.dessertj.samples.nesting;

/*-
 * #%L
 * DessertJ Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2024 Hans Jörg Heßmann
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Host {
    
    public static class PublicStatic {
        static class NestedDefaultStatic {
            private static class NestedPrivateStatic {
            }
        }
    }

    static class DefaultStatic {

    }

    protected static class ProtectedStatic {

    }

    protected static class PrivateStatic {

    }

    public interface PublicInterface {
        
    }

    public interface DefaultInterface {

    }

    public interface ProtectedInterface {

    }

    public interface PrivateInterface {

    }

    public class PublicClass {

    }

    class DefaultClass {

       public void methodUsingAnonymousInnerClass(List<Integer> list) {
            Collections.sort(list, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o2 - o1;
                }
            });
        }

    }

    protected class ProtectedClass {

    }

    private class PrivateClass {

    }

    public void methodUsingInnerClass(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {

        }
    }
}
