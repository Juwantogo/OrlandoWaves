package com.juwan.orlandowaves.toAccess;

import java.util.UUID;

/**
 * Created by Juwan on 12/2/2017.
 */

public class RandomStringUUID {
    public static String main(String[] args) {
        // Creating a random UUID (Universally unique identifier).
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();

        return randomUUIDString;

        //System.out.println("Random UUID String = " + randomUUIDString);
        //System.out.println("UUID version       = " + uuid.version());
        //System.out.println("UUID variant       = " + uuid.variant());
    }
}
