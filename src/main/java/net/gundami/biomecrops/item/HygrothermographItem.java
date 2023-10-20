package net.gundami.biomecrops.item;

import net.gundami.biomecrops.utils.EnvironmentUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class HygrothermographItem extends Item {
    public HygrothermographItem(Settings settings) {
        super(settings);
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        EnvironmentUtil environmentUtil = new EnvironmentUtil(world,playerEntity.getBlockPos());
        playerEntity.sendMessage(Text.translatable("tooltip.biomecrops.temperature").append(": ").append(Text.literal(String.format("%s°C ",environmentUtil.getReadableTemperature()))
                .append(Text.translatable("tooltip.biomecrops.humidity")).append(": ").append(Text.literal(String.format("%s",environmentUtil.getReadableHumidity())))));
        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }
}
