package com.iscas.pm.auth.service.impl;

import junit.framework.TestCase;
import org.bouncycastle.crypto.generators.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class UserServiceImplTest extends TestCase {
    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }

}