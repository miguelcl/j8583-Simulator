package org.j8583.simulator.generator.types;

import static com.google.common.base.Preconditions.checkArgument;
import com.solab.iso8583.IsoType;
import org.apache.commons.lang3.RandomStringUtils;
import org.j8583.simulator.generator.DataGenerator;

/**
 *
 * @author Mauri
 */
public class AlphaGenerator extends DataGenerator {

    private final boolean onlyAlpha;
    private final int caseOpt;

    /**
     * 
     * @param length length of the String generated
     * @param onlyAlpha only use alpha charcters
     * @param caseOpt 0- Lower & Upper case  1 - LowerCase , 2 - UpperCase
     * @param type 
     */
    public AlphaGenerator(int length, boolean onlyAlpha, int caseOpt, IsoType type) {
        super(length, type);
        this.onlyAlpha = onlyAlpha;
        checkArgument(0 <= caseOpt && caseOpt <= 2, "%s must be in range [%s, %s]", caseOpt, 0, 2);
        this.caseOpt = caseOpt;
    }

    @Override
    public String getNextRandomValue(long randomTrace) {
        String ret;
        if (onlyAlpha) {
            ret = RandomStringUtils.randomAlphabetic(length);
        } else {
            ret = RandomStringUtils.randomAlphanumeric(length);
        }

        if (caseOpt == 1) {
            ret = ret.toLowerCase();
        } else if (caseOpt == 2) {
            ret = ret.toUpperCase();
        }
        return ret;
    }
}
