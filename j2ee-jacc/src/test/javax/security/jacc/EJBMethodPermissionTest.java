/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 *    "Apache Geronimo" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    "Apache Geronimo", nor may "Apache" appear in their name, without
 *    prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 * ====================================================================
 */
package javax.security.jacc;

import junit.framework.TestCase;

/**
 *
 * @version $Revision: 1.1 $ $Date: 2003/08/30 01:55:13 $
 */
public class EJBMethodPermissionTest extends TestCase {

    /*
     * Testing EJBMethodPermission(java.lang.String, java.lang.String)
     */
    public void testConstructorStringString() {

        // methodSpec ::= null
        EJBMethodPermission permission = new EJBMethodPermission("foo", null);

        // methodSpec ::= methodNameSpec
        permission = new EJBMethodPermission("foo", "");
        permission = new EJBMethodPermission("foo", "cat");

        // methodSpec ::= methodNameSpec comma methodInterface
        permission = new EJBMethodPermission("foo", ",ServiceEndpoint");
        permission = new EJBMethodPermission("foo", "cat,ServiceEndpoint");

        // methodSpec ::= methodNameSpec comma methodInterfaceSpec comma methodParamsSpec
        permission = new EJBMethodPermission("foo", ",,");
        permission = new EJBMethodPermission("foo", "cat,,");
        permission = new EJBMethodPermission("foo", ",Home,");
        permission = new EJBMethodPermission("foo", "cat,Home,");
        permission = new EJBMethodPermission("foo", ",,a,b,c,d");
        permission = new EJBMethodPermission("foo", "cat,,a,b,c,d");
        permission = new EJBMethodPermission("foo", ",Home,a,b,c,d");
        permission = new EJBMethodPermission("foo", "cat,Home,a,b,c,d");


        // methodInterface ::= "Home" | "LocalHome" | "Remote" | "Local" | "ServiceEndpoint"
        permission = new EJBMethodPermission("foo", "cat,Home,a,b,c,d");
        permission = new EJBMethodPermission("foo", "cat,LocalHome,a,b,c,d");
        permission = new EJBMethodPermission("foo", "cat,Remote,a,b,c,d");
        permission = new EJBMethodPermission("foo", "cat,Local,a,b,c,d");
        permission = new EJBMethodPermission("foo", "cat,ServiceEndpoint,a,b,c,d");

        assertEquals(permission.getName(), "foo");
        assertEquals(permission.getActions(), "cat,ServiceEndpoint,a,b,c,d");


        // bad methodInterface
        try {
            permission = new EJBMethodPermission("foo", "cat,Interface,a,b,c,d");
            fail("Bad method interface");
        } catch(IllegalArgumentException iae) {
        }

        // no production produces "emptyString,emptyString"
        try {
            permission = new EJBMethodPermission("foo", ",");
            fail("Empty method interface");
        } catch(IllegalArgumentException iae) {
        }

        // no production produces "methodName ,emptyString"
        try {
            permission = new EJBMethodPermission("foo", "cat,");
            fail("Empty method interface");
        } catch(IllegalArgumentException iae) {
        }

        // no production produces an empty method parameter
        try {
            permission = new EJBMethodPermission("foo", ",,,");
            fail("Empty method parameter");
        } catch(IllegalArgumentException iae) {
        }

        // no production produces an empty method parameter
        try {
            permission = new EJBMethodPermission("foo", ",,,,,,");
            fail("Empty method parameter");
        } catch(IllegalArgumentException iae) {
        }
    }

    public void testImpliesStringString() {
        EJBMethodPermission permissionFooEEE = new EJBMethodPermission("foo", ",,");
        EJBMethodPermission permissionFooMIP = new EJBMethodPermission("foo", "cat,LocalHome,a,b,c,d");
        EJBMethodPermission permissionBarEEE = new EJBMethodPermission("bar", ",,");
        EJBMethodPermission permissionFooEIP = new EJBMethodPermission("foo", ",LocalHome,a,b,c,d");
        EJBMethodPermission permissionFooEIE = new EJBMethodPermission("foo", ",LocalHome,");
        EJBMethodPermission permissionFooEI  = new EJBMethodPermission("foo", ",LocalHome");

        assertTrue(permissionFooEEE.implies(permissionFooEEE));
        assertTrue(permissionFooEEE.implies(permissionFooMIP));
        assertTrue(permissionFooEEE.implies(permissionFooEIP));
        assertTrue(permissionFooEEE.implies(permissionFooEIE));
        assertTrue(permissionFooEEE.implies(permissionFooEI));
        assertFalse(permissionFooMIP.implies(permissionFooEEE));

        assertTrue(permissionFooEEE.equals(permissionFooEEE));
        assertFalse(permissionFooEEE.equals(permissionFooMIP));
        assertFalse(permissionFooMIP.equals(permissionFooEEE));

        assertFalse(permissionFooEEE.implies(permissionBarEEE));
        assertFalse(permissionBarEEE.implies(permissionFooEEE));

        assertFalse(permissionFooEEE.equals(permissionBarEEE));
        assertFalse(permissionBarEEE.equals(permissionFooEEE));

        assertTrue(permissionFooEIP.implies(permissionFooMIP));
        assertTrue(permissionFooEIE.implies(permissionFooMIP));
        assertTrue(permissionFooEI.implies(permissionFooMIP));
        assertTrue(permissionFooEI.implies(permissionFooEIP));
        assertTrue(permissionFooEI.implies(permissionFooEIE));

        assertFalse(permissionFooEEE.hashCode() == permissionBarEEE.hashCode());
    }

    /*
     * Testing EJBMethodPermission(String, String, String, String[])
     */
    public void testConstructorStringStringStringStringArray() {
    }

    public void testImpliesStringStringStringStringArray() {
    }

    /*
     * Testing EJBMethodPermission(String, String, Method)
     */
    public void testConstructorStringStringMethod() {
    }

    public void testImpliesStringStringMethod() {
    }
    public static void main(String[] args) {
        EJBMethodPermissionTest test = new EJBMethodPermissionTest();
        test.testConstructorStringString();
        test.testImpliesStringString();
    }
}

