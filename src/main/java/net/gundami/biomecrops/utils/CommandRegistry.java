package net.gundami.biomecrops.utils;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static dev.architectury.hooks.level.biome.BiomeHooks.getBiomeProperties;
import static io.github.lucaargolo.seasons.FabricSeasons.getCurrentSeason;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandRegistry {
    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher,
                                        CommandRegistryAccess commandRegistryAccess,
                                        CommandManager.RegistrationEnvironment registrationEnvironment){
        dispatcher.register(literal("biomecrops")
                .then(literal("env")
                        .executes(context -> {
                            ServerPlayerEntity player = context.getSource().getPlayer();
                            EnvironmentUtil environmentUtil = new EnvironmentUtil(player.getWorld(),player.getBlockPos());
                            player.sendMessage(Text.literal("Biome: "+player.getWorld().getBiome(player.getBlockPos()).getType().name()+" temp: "+ environmentUtil.getReadableTemperature()+ " humi: "+environmentUtil.getReadableHumidity()));
                            return 1;
                        })
                )
        );
    }
}
