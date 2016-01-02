package com.vedikunnel.tapestry.services;

import org.apache.tapestry5.services.URLEncoder;

import java.util.BitSet;

public class CustomURLEncoder implements URLEncoder {
    static final String ENCODED_NULL = "$N";
    static final String ENCODED_BLANK = "$B";

    /**
     * Bit set indicating which character are safe to pass through (when encoding or decoding) as-is. All other
     * characters are encoded as a kind of unicode escape.
     */
    private final BitSet safe = new BitSet(128);

    {
        // tapestry native
        markSafe("abcdefghijklmnopqrstuvwxyz");
        markSafe("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        markSafe("01234567890-_.:");

        markSafe("?!*$@#&\"„“”‚‘’»«›‹— –…¿¡"); // misc

//        markSafe("ÀÂÆÇÉÈÊËÎÏÔŒÙÛÜŸàâæçéèêëîïôœùûüÿ"); // french
//        markSafe("ÓÒóò"); // italian (upon french)
//        markSafe("ÑñÁáÍí"); // spanish (upon french and italian)
//        markSafe("ÄÖÜẞäöüß"); // german
//        markSafe("ĄĆĘŁŃÓŚŹŻąćęłńóśźż"); // polandais
//        markSafe("АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЬЮЯѢѪабвгдежзийклмнопрстуфхцчшщъьюяѣѫ"); // bulgarien
    }


    private void markSafe(String s) {
        for (char ch : s.toCharArray()) {
            safe.set((int) ch);
        }
    }


    public CustomURLEncoder() {
    }

    public String encode(String input) {
        if (input == null)
            return ENCODED_NULL;

        if (input.equals(""))
            return ENCODED_BLANK;

        boolean dirty = false;

        int length = input.length();

        StringBuilder output = new StringBuilder(length * 2);

        for (int i = 0; i < length; i++) {
            char ch = input.charAt(i);

            if (ch == '$') {
                output.append("$$");
                dirty = true;
                continue;
            }

            int chAsInt = (int) ch;

            if (safe.get(chAsInt)) {
                output.append(ch);
                continue;
            }

            output.append(String.format("$%04x", chAsInt));
            dirty = true;
        }

        return dirty ? output.toString() : input;
    }

    public String decode(String input) {
        assert input != null;

        if (input.equals(ENCODED_NULL))
            return null;

        if (input.equals(ENCODED_BLANK))
            return "";

        boolean dirty = false;

        int length = input.length();

        StringBuilder output = new StringBuilder(length * 2);

        for (int i = 0; i < length; i++) {
            char ch = input.charAt(i);

            if (ch == '$') {
                dirty = true;

                if (i + 1 < length && input.charAt(i + 1) == '$') {
                    output.append('$');
                    i++;

                    dirty = true;
                    continue;
                }

                if (i + 4 < length) {
                    String hex = input.substring(i + 1, i + 5);

                    try {
                        int unicode = Integer.parseInt(hex, 16);

                        output.append((char) unicode);
                        i += 4;
                        dirty = true;
                        continue;
                    } catch (NumberFormatException ex) {
                        // Ignore.
                    }
                }

                throw new IllegalArgumentException(
                        String.format(
                                "Input string '%s' is not valid; the '$' character at position %d should be followed by another '$' or a four digit hex number (a unicode value).",
                                input, i + 1));
            }

            if (!safe.get((int) ch)) {
                throw new IllegalArgumentException(
                        String.format("Input string '%s' is not valid; the character '%s' at position %d is not valid.",
                                input, ch, i + 1));
            }

            output.append(ch);
        }

        return dirty ? output.toString() : input;
    }
}
