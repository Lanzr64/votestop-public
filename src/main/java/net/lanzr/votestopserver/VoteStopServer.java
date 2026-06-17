package net.lanzr.votestopserver;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;

@Mod(VoteStopServer.MODID)
public class VoteStopServer {
    public static final String MODID = "votestopserver";

    public VoteStopServer(IEventBus modEventBus, ModContainer modContainer) {

        modContainer.registerConfig(ModConfig.Type.SERVER, Config.SPEC,MODID + ".toml");
    }
}
