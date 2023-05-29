package net.gundami.biomecrop.utils;

import net.gundami.biomecrop.BiomeCrop;
import net.minecraft.util.Identifier;

public class ModIdentifier extends Identifier {
    public ModIdentifier(String path) {
        super(BiomeCrop.MOD_ID, path);
    }
}
