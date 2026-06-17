package net.lanzr.votestopserver;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(VoteStopServer.MODID)
public class VoteStopServer {
    public static final String MODID = "votestopserver";

    public VoteStopServer(FMLJavaModLoadingContext context) {

        MinecraftForge.EVENT_BUS.register(this);

        context.registerConfig(ModConfig.Type.SERVER, Config.SPEC,MODID+".toml");
    }
}
