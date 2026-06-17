package net.lanzr.votestopserver;


import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;


@EventBusSubscriber(modid = VoteStopServer.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
        public static final ModConfigSpec SPEC;
        static {
                ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
                setup(builder);
                SPEC = builder.build();
        }

        // Scan interval in ticks (20 ticks = 1 second)
        private static final String COMMON_TAB = "votestop_common";

        public static ModConfigSpec.IntValue START_LIMIT_MINUTES;
        public static ModConfigSpec.IntValue VOTE_DURATION_SECONDS;
        public static ModConfigSpec.IntValue BROADCAST_INTERVAL_SECONDS;
        public static ModConfigSpec.ConfigValue<String> MSG_FAILED;
        public static ModConfigSpec.ConfigValue<String> MSG_CANCEL_PREFIX;
        public static ModConfigSpec.ConfigValue<String> MSG_CANCEL_PLAYER;
        public static ModConfigSpec.ConfigValue<String> MSG_CANCEL_TIMEOUT;
        public static ModConfigSpec.ConfigValue<String> MSG_INTERVAL_BROADCAST;
        public static ModConfigSpec.ConfigValue<String> MSG_SUCCESS;

        public static ModConfigSpec.IntValue AFK_THRESHOLD_MINUTES;

        public static int startLimitMinutes;
        public static int voteDurationSeconds;
        public static int broadcastIntervalSeconds;

        public static String msg_failed;
        public static String msg_cancel_prefix;
        public static String msg_cancel_player;
        public static String msg_cancel_timeout;
        public static String msg_interval_broadcast;
        public static String msg_success;

        public static int afkThresholdMinutes;



        private static void setup(ModConfigSpec.Builder builder) {
                START_LIMIT_MINUTES = builder
                        .comment("How many minutes after the game starts is voting not allowed (minutes)")
                        .defineInRange(COMMON_TAB + ".startLimitMinutes", 10, 0, Integer.MAX_VALUE);
                VOTE_DURATION_SECONDS = builder
                        .comment("Duration of voting (seconds)")
                        .defineInRange(COMMON_TAB + ".voteDurationSeconds", 60, 1, Integer.MAX_VALUE);
                BROADCAST_INTERVAL_SECONDS = builder
                        .comment("The interval for broadcasting to all players (seconds)")
                        .defineInRange(COMMON_TAB + ".broadcastIntervalSeconds", 10, 1, Integer.MAX_VALUE);

                MSG_FAILED = builder
                        .comment("Text for failed vote initiation")
                        .comment("[English]: §6Vote initiation failed: You can only vote %s minutes after server start. Remaining: %d s")
                        .define(COMMON_TAB + ".msg_failed","§6发起投票失败: 开服%s分钟后才能投票，剩余：%d s");
                MSG_CANCEL_PREFIX = builder
                        .comment("Text for vote cancellation prefix")
                        .comment("[English]: §c[Voting terminated] ")
                        .define(COMMON_TAB + ".msg_cancel_prefix","§c[投票终止] ");
                MSG_CANCEL_PLAYER = builder
                        .comment("Text for vote cancellation player")
                        .comment("[English]: Player %s cast a dissenting vote, vote canceled")
                        .define(COMMON_TAB + ".msg_cancel_player","玩家 %s 投了反对票，投票取消。");
                MSG_CANCEL_TIMEOUT = builder
                        .comment("Text for vote cancellation timeout")
                        .comment("[English]: The vote timed out and failed to reach unanimous agreement; the restart has been canceled.")
                        .define(COMMON_TAB + ".msg_cancel_timeout","投票超时，未能达成全员一致，取消重启。");
                MSG_INTERVAL_BROADCAST = builder
                        .comment("Text for interval broadcast of voting")
                        .comment("[English]: §6In Voting: §fYes/Total: %d/%d Enter §a/tyj votestop yes §fto agree or §a/tyj votestop no §fto veto. Remaining time: §6%ds")
                        .define(COMMON_TAB + ".msg_interval_broadcast","§6投票中: §f赞成/总人数: %d/%d 输入 §a/tyj votestop yes §f同意 或 §a/tyj votestop no §f否决 剩余时间：§6%ds");
                MSG_SUCCESS = builder
                        .comment("Text for vote success")
                        .comment("[English]: §aAll passed! The server is shutting down...")
                        .define(COMMON_TAB + ".msg_success","§a全员通过！服务器正在关闭...");
                AFK_THRESHOLD_MINUTES = builder
                        .comment("How long without operation is considered AFK (minutes)")
                        .defineInRange(COMMON_TAB + ".afkThresholdMinutes", 5, 1, Integer.MAX_VALUE);

        }

        @SubscribeEvent
        static void onLoad(final ModConfigEvent event)
        {
                startLimitMinutes = START_LIMIT_MINUTES.get();
                voteDurationSeconds = VOTE_DURATION_SECONDS.get();
                broadcastIntervalSeconds = BROADCAST_INTERVAL_SECONDS.get();
                msg_failed = MSG_FAILED.get();
                msg_cancel_prefix = MSG_CANCEL_PREFIX.get();
                msg_cancel_player = MSG_CANCEL_PLAYER.get();
                msg_cancel_timeout = MSG_CANCEL_TIMEOUT.get();
                msg_interval_broadcast = MSG_INTERVAL_BROADCAST.get();
                msg_success = MSG_SUCCESS.get();
                afkThresholdMinutes = AFK_THRESHOLD_MINUTES.get();
        }
}
