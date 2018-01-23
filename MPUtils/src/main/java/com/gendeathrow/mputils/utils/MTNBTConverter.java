package com.gendeathrow.mputils.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import crafttweaker.api.data.DataByte;
import crafttweaker.api.data.DataByteArray;
import crafttweaker.api.data.DataDouble;
import crafttweaker.api.data.DataFloat;
import crafttweaker.api.data.DataInt;
import crafttweaker.api.data.DataIntArray;
import crafttweaker.api.data.DataList;
import crafttweaker.api.data.DataLong;
import crafttweaker.api.data.DataMap;
import crafttweaker.api.data.DataShort;
import crafttweaker.api.data.DataString;
import crafttweaker.api.data.IData;
import crafttweaker.api.data.IDataConverter;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;

/**
 * @author Stan
 */
public class MTNBTConverter implements IDataConverter<NBTBase> {

    private static final MTNBTConverter INSTANCE = new MTNBTConverter();

    public MTNBTConverter() {
    }

    public static NBTBase from(IData data) {
        return data.convert(INSTANCE);
    }

    public static IData from(NBTBase nbt, boolean immutable) {
        if(nbt == null)
            return null;

        switch(nbt.getId()) {
            case 1: // byte
                return new DataByte(((NBTPrimitive) nbt).getByte());
            case 2: // short
                return new DataShort(((NBTPrimitive) nbt).getShort());
            case 3: // int
                return new DataInt(((NBTPrimitive) nbt).getInt());
            case 4: // long
                return new DataLong(((NBTPrimitive) nbt).getLong());
            case 5: // float
                return new DataFloat(((NBTPrimitive) nbt).getFloat());
            case 6: // double
                return new DataDouble(((NBTPrimitive) nbt).getDouble());
            case 7: // byte[]
                return new DataByteArray(((NBTTagByteArray) nbt).getByteArray(), immutable);
            case 8: // string
                return new DataString(((NBTTagString) nbt).getString());
            case 9: { // list
                List<IData> values = new ArrayList<>();
                List<NBTBase> original = ReflectionHelper.getTagList((NBTTagList) nbt);
                values.addAll(original.stream().map(value -> from(value, immutable)).collect(Collectors.toList()));
                return new DataList(values, immutable);
            }
            case 10: { // compound
                Map<String, IData> values = new HashMap<>();
                NBTTagCompound original = (NBTTagCompound) nbt;
                for(String key : original.getKeySet()) {
                    values.put(key, from(original.getTag(key), immutable));
                }
                return new DataMap(values, immutable);
            }
            case 11: // int[]
                return new DataIntArray(((NBTTagIntArray) nbt).getIntArray(), immutable);
            default:
                throw new RuntimeException("Unknown tag type: " + nbt.getId());
        }
    }


    @Override
    public NBTBase fromBool(boolean value) {
        return new NBTTagInt(value ? 1 : 0);
    }

    @Override
    public NBTBase fromByte(byte value) {
        return new NBTTagByte(value);
    }

    @Override
    public NBTBase fromShort(short value) {
        return new NBTTagShort(value);
    }

    @Override
    public NBTBase fromInt(int value) {
        return new NBTTagInt(value);
    }

    @Override
    public NBTBase fromLong(long value) {
        return new NBTTagLong(value);
    }

    @Override
    public NBTBase fromFloat(float value) {
        return new NBTTagFloat(value);
    }

    @Override
    public NBTBase fromDouble(double value) {
        return new NBTTagDouble(value);
    }

    @Override
    public NBTBase fromString(String value) {
        return new NBTTagString(value);
    }

    @Override
    public NBTBase fromList(List<IData> values) {
        NBTTagList result = new NBTTagList();
        for(IData value : values) {
            result.appendTag(from(value));
        }
        return result;
    }

    @Override
    public NBTBase fromMap(Map<String, IData> values) {
        NBTTagCompound result = new NBTTagCompound();
        for(Map.Entry<String, IData> entry : values.entrySet()) {
            result.setTag(entry.getKey(), from(entry.getValue()));
        }
        return result;
    }

    @Override
    public NBTBase fromByteArray(byte[] value) {
        return new NBTTagByteArray(value);
    }

    @Override
    public NBTBase fromIntArray(int[] value) {
        return new NBTTagIntArray(value);
    }
}
