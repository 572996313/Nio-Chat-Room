package com.company;

import java.io.IOException;

public class BClient {
    public static void main(String[] args) throws IOException {
        new NioClient().start("BClient");
    }
}
