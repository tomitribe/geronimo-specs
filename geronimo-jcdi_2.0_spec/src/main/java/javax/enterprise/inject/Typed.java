/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package javax.enterprise.inject;

import javax.enterprise.util.AnnotationLiteral;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used for definining explicit bean api types.
 * 
 * @version $Rev$ $Date$
 * 
 */
@Target(value={ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Typed
{

    Class<?>[] value() default {};

    final class Literal extends AnnotationLiteral<Typed> implements Typed
    {
        public static final Literal INSTANCE = of(new Class[0]);
        private static final long serialVersionUID = 1L;

        private final Class<?>[] value;


        public static Literal of(Class<?>[] value)
        {
            return new Literal(value);
        }

        private Literal(Class<?>[] value)
        {
            this.value = value;
        }

        @Override
        public Class<?>[] value()
        {
            return value;
        }
    }

}
