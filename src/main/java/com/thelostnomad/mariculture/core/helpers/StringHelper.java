package com.thelostnomad.mariculture.core.helpers;


import net.minecraft.util.text.translation.I18n;

import static com.thelostnomad.mariculture.Mariculture.MODID;

public class StringHelper {
    public static String format(String text, Object... varargs) {
        return I18n.translateToLocalFormatted(MODID + "." + text, varargs);
    }

    public static String translate(String text) {
        return I18n.translateToLocal(MODID + "." + text);
    }

    public static String localize(String text) {
        return I18n.translateToLocal(text);
    }
}