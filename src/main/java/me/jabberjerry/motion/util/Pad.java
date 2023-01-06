package me.jabberjerry.motion.util;

import java.util.ArrayList;

public class Pad {
    public static String right(String str, char padding, int length) {
        StringBuilder padded = new StringBuilder();

        int index = 0;
        while (index < length) {
            if (index <= str.length() - 1)
                padded.append(str.charAt(index));
            else
                padded.append(padding);

            index++;
        }

        return padded.toString();
    }
}
