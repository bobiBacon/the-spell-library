package net.bobbacon.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


class UtilsTest {
    @BeforeAll
    static void beforeAll() {
//        SharedConstants.getGameVersion();
//        Bootstrap.bootStrap();
    }
    @Test
    void weightedRandom() {
        Map<String,Integer> map= new HashMap<>();
        map.put("27%",12);
        map.put("11%",5);
        map.put("36%",16);
        map.put("7%",3);
        map.put("18%",8);
        map.put("0%",0);
    }
}