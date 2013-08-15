package org.polyglotted.attributerepo.core;

import java.lang.annotation.*;

/**
 * Signifies that an API class is not instantiable and exists purely as a container
 * for static methods and or constant values. Note that the presence of this annotation 
 * implies nothing about the existence of the class itself. 
 *
 * @author Shankar Vasudevan
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.CONSTRUCTOR)
@Documented
public @interface NotInstantiable {

}
