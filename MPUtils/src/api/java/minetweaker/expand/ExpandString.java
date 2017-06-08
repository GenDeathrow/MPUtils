package minetweaker.expand;

import minetweaker.*;
import minetweaker.api.chat.IChatMessage;
import minetweaker.api.data.*;
import minetweaker.api.formatting.IFormattedText;
import stanhebben.zenscript.annotations.*;
import stanhebben.zenscript.annotations.Optional;

import java.util.*;

/**
 * @author Stanneke
 */
@ZenExpansion("string")
public class ExpandString {

    @ZenOperator(OperatorType.ADD)
    public static IFormattedText add(String str, IFormattedText formattedText) {
        return MineTweakerAPI.format.string(str).add(formattedText);
    }

    @ZenOperator(OperatorType.CAT)
    public static IFormattedText cat(String str, IFormattedText formattedText) {
        return add(str, formattedText);
    }

    @ZenCaster
    public static IData asData(String value) {
        return new DataString(value);
    }

    @ZenCaster
    public static IChatMessage asChatMessage(String value) {
        return MineTweakerImplementationAPI.platform.getMessage(value);
    }

    @ZenCaster
    public static IFormattedText asFormattedText(String value) {
        return MineTweakerAPI.format.string(value);
    }

    @ZenMethod
    public static List<String> split(String value, String separator, @Optional int maximum) {
        List<String> result = new ArrayList<>();
        int minIndex = 0;
        int numSplits = 0;

        while(minIndex + separator.length() <= value.length()) {
            int index = value.indexOf(separator, minIndex);
            if(index < 0)
                break;
            result.add(value.substring(minIndex, index));
            minIndex = index + separator.length();

            numSplits++;
            if(maximum > 0 && numSplits >= maximum)
                break;
        }

        result.add(value.substring(minIndex));
        return result;
    }

    @ZenMethod
    public static int indexOf(String value, String needle) {
        return value.indexOf(needle);
    }

    @ZenMethod
    public static int indexOf(String value, String needle, int fromIndex) {
        return value.indexOf(needle, fromIndex);
    }

    @ZenMethod
    public static int lastIndexOf(String value, String needle) {
        return value.lastIndexOf(needle);
    }

    @ZenMethod
    public static int lastIndexOf(String value, String needle, int fromIndex) {
        return value.lastIndexOf(value, fromIndex);
    }
}
