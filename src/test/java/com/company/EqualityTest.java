package com.company;

import com.company.Equality;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests of the Calculator class
 */
public class EqualityTest {

    @Test
    public void calculate_test() {
        String str = "445+989**(768-3)";
        Equality exp = new Equality(str);
        assertFalse(exp.calculate());
    }

    @Test
    public void calculate_test2() {
        String str = "445+989)*(768-3";
        Equality exp = new Equality(str);
        assertFalse(exp.calculate());
    }

    @Test
    public void calculate_test3() {
        String str = "445*r+989*(768-3)";
        Equality exp = new Equality(str);
        assertFalse(exp.calculate());
    }
}
