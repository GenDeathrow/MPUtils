package com.gendeathrow.mputils.utils;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;

/**
 * Common class for all runtime hacks (stuff requiring reflection). It is not
 * unexpected to have it break with new minecraft versions and may need regular
 * adjustment - as such, those have been collected here.
 *
 * @author Stan Hebben
 */
public class ReflectionHelper {

    private static final Field NBTTAGLIST_TAGLIST;
    public static final String[] NBTTAGLIST_TAGLIST_STRING = {"tagList", "field_74747_a"};

    static {
        NBTTAGLIST_TAGLIST = getField(NBTTagList.class, NBTTAGLIST_TAGLIST_STRING);
    }

    private ReflectionHelper() {
    }


    public static List<NBTBase> getTagList(NBTTagList list) {
        if(NBTTAGLIST_TAGLIST == null) {
            return null;
        }
        try {
            return (List<NBTBase>) NBTTAGLIST_TAGLIST.get(list);
        } catch(IllegalArgumentException ex) {
            return null;
        } catch(IllegalAccessException ex) {
            return null;
        }
    }

    public static <T> T getPrivateStaticObject(Class<?> cls, String... names) {
        for(String name : names) {
            try {
                Field field = cls.getDeclaredField(name);
                field.setAccessible(true);
                return (T) field.get(null);
            } catch(NoSuchFieldException | SecurityException | IllegalAccessException ignored) {

            }
        }

        return null;
    }

    public static <T> T getPrivateObject(Object object, String... names) {
        Class<?> cls = object.getClass();
        for(String name : names) {
            try {
                Field field = cls.getDeclaredField(name);
                field.setAccessible(true);
                return (T) field.get(object);
            } catch(NoSuchFieldException | SecurityException | IllegalAccessException ignored) {

            }
        }

        return null;
    }

    // #######################
    // ### Private Methods ###
    // #######################

    private static Field getField(Class cls, String... names) {
        for(String name : names) {
            try {
                Field field = cls.getDeclaredField(name);
                field.setAccessible(true);
                return field;
            } catch(NoSuchFieldException | SecurityException ignored) {
            }
        }

        return null;
    }
}
