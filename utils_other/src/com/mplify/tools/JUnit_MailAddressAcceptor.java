package com.mplify.tools;

import org.junit.Test;
import static org.junit.Assert.*;

/* 34567890123456789012345678901234567890123456789012345678901234567890123456789
 * *****************************************************************************
 * Copyright (c) 2010, M-PLIFY S.A.
 *                     21, rue Glesener
 *                     L-1631 LUXEMBOURG
 *
 * All rights reserved.
 *******************************************************************************
 *******************************************************************************
 * Testing "MailAddressAcceptor", which accepts strings supposed to be e-mail 
 * addresses
 ******************************************************************************/

public class JUnit_MailAddressAcceptor {

    @Test
    public void testVariousAddresses() {
        assertFalse("Test 1", MailAddressAcceptor.acceptAddress(null));
        assertFalse("Test 2", MailAddressAcceptor.acceptAddress(""));
        assertFalse("Test 3", MailAddressAcceptor.acceptAddress("."));
        assertTrue("Test 4", MailAddressAcceptor.acceptAddress("a@b"));
        assertFalse("Test 5", MailAddressAcceptor.acceptAddress("x"));
        assertFalse("Test 6", MailAddressAcceptor.acceptAddress("@"));
        assertFalse("Test 7", MailAddressAcceptor.acceptAddress("@@"));
        assertFalse("Test 8", MailAddressAcceptor.acceptAddress("@c"));
        assertFalse("Test 9", MailAddressAcceptor.acceptAddress("d@"));
        assertFalse("Test 10", MailAddressAcceptor.acceptAddress("a@b@c"));
        assertTrue("Test 11", MailAddressAcceptor.acceptAddress("a@b.c.d"));
        assertTrue("Test 12", MailAddressAcceptor.acceptAddress("a@b.c.d."));
        assertFalse("Test 13", MailAddressAcceptor.acceptAddress("a.b.c.@b.c.d"));
        assertFalse("Test 14", MailAddressAcceptor.acceptAddress(".a.b.c@b.c.d"));
        assertTrue("Test 15", MailAddressAcceptor.acceptAddress("a.b.c@b.c.d"));
        assertTrue("Test 16", MailAddressAcceptor.acceptAddress("a*.b$.c%ALPHA{YES}@b.c.d"));
        assertTrue("Test 17", MailAddressAcceptor.acceptAddress("a0123456789.b.c@b.c.d"));
        assertFalse("Test 18", MailAddressAcceptor.acceptAddress("a.b.é@b.c.d"));
        assertFalse("Test 19", MailAddressAcceptor.acceptAddress("a.b.ké@b.c.d"));
        assertFalse("Test 20", MailAddressAcceptor.acceptAddress("a.b.kék@b.c.d"));
        assertTrue("Test 21", MailAddressAcceptor.acceptAddress("someone@at.somewhere.org"));
        assertTrue("Test 22", MailAddressAcceptor.acceptAddress("someone@at.somewhere.org."));
        assertTrue("Test 23", MailAddressAcceptor.acceptAddress("someone@1at.somewhere.org"));
        assertTrue("Test 24", MailAddressAcceptor.acceptAddress("someone@at.1somewhere.org"));
        assertTrue("Test 25", MailAddressAcceptor.acceptAddress("someone@3446.somewhere1.org"));
        assertTrue("Test 26", MailAddressAcceptor.acceptAddress("someone@3446.989777661.org"));
        assertFalse("Test 27", MailAddressAcceptor.acceptAddress("someone@3446.989777661.7org"));
        assertTrue("Test 28", MailAddressAcceptor.acceptAddress("someone@at.somewhere1234.org"));
        assertTrue("Test 29", MailAddressAcceptor.acceptAddress("someone@at.someWHERE1234.ORG"));
        assertTrue("Test 30", MailAddressAcceptor.acceptAddress("someone@at.so-meW--HERE1-234.ORG"));
        assertFalse("Test 31", MailAddressAcceptor.acceptAddress("someone@at.somew%here1.org"));
        assertFalse("Test 32", MailAddressAcceptor.acceptAddress("someone@at.somewhere1..org"));
        assertFalse("Test 33", MailAddressAcceptor.acceptAddress("someone@at.?.org"));
        assertTrue("Test 34", MailAddressAcceptor.acceptAddress("someone@at.12where.org"));
        assertFalse("Test 35", MailAddressAcceptor.acceptAddress("someone@at.wh---ere-.org"));
        assertTrue("Test 36", MailAddressAcceptor.acceptAddress("someone@at.wh---ere.org"));
        assertFalse("Test 37", MailAddressAcceptor.acceptAddress("email@bureau.?"));
    }

    @Test
    public void testVariousAddressesWithUnderscores() {
        assertTrue("Test U1", MailAddressAcceptor.acceptAddress("someone@at.somewhere.org", true));
        assertTrue("Test U2", MailAddressAcceptor.acceptAddress("someone@at.somewhere.org", false));
        assertTrue("Test U3", MailAddressAcceptor.acceptAddress("nagios@sup_centreon_01.m-plify.net", true));
        assertFalse("Test U4", MailAddressAcceptor.acceptAddress("nagios@sup_centreon_01.m-plify.net", false));
    }
    
}
