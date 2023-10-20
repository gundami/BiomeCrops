package net.gundami.biomecrops.utils;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.gundami.biomecrops.item.HygrothermographItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ItemRegistry {
    public static final Item HYGROTHERMOGRAPH = new HygrothermographItem(new FabricItemSettings());
    public static void register(){
        Registry.register(Registries.ITEM, new Identifier("biomecrops", "hygrothermograph"), HYGROTHERMOGRAPH);
    }
}
