package com.example.security;

import java.util.Properties;

public class OSTest {
    public static void main(String[] args) {
        System.out.println(System.getProperty("os.name"));
        Properties p = System.getProperties();
        System.out.println(p);
    }
}
