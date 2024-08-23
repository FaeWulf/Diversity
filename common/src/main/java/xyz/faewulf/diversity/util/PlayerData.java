package xyz.faewulf.diversity.util;

import xyz.faewulf.diversity.inter.IPlayerDataSaver;
import net.minecraft.nbt.CompoundTag;

public class PlayerData {
    public static void setData(IPlayerDataSaver player, String type, Boolean data) {
        CompoundTag nbt = player.getPersistentData();
        nbt.putBoolean(type, data);
    }

    public static void setData(IPlayerDataSaver player, String type, String data) {
        CompoundTag nbt = player.getPersistentData();
        nbt.putString(type, data);
    }


    public static void setData(IPlayerDataSaver player, String type, int data) {
        CompoundTag nbt = player.getPersistentData();
        nbt.putInt(type, data);
    }

    public static void resetData(IPlayerDataSaver player, String type) {
        CompoundTag nbt = player.getPersistentData();
        nbt.remove(type);
    }

    public static String getStringData(IPlayerDataSaver player, String type) {
        return player.getPersistentData().getString(type);
    }

    public static Boolean getBooleanData(IPlayerDataSaver player, String type) {
        return player.getPersistentData().getBoolean(type);
    }

    public static int getIntData(IPlayerDataSaver player, String type) {
        return player.getPersistentData().getInt(type);
    }
}
