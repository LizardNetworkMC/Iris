package com.volmit.iris.util.misc;

import com.volmit.iris.Iris;
import com.volmit.iris.BuildConstants;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.engine.platform.PlatformChunkGenerator;
import com.volmit.iris.util.reflect.ShadeFix;
import com.volmit.iris.util.scheduling.J;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.DrilldownPie;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Bindings {
    public static void setupBstats(Iris plugin) {
        J.s(() -> {
            var metrics = new Metrics(plugin, 24220);
            metrics.addCustomChart(new SingleLineChart("custom_dimensions", () -> Bukkit.getWorlds()
                    .stream()
                    .filter(IrisToolbelt::isIrisWorld)
                    .mapToInt(w -> 1)
                    .sum()));

            metrics.addCustomChart(new DrilldownPie("used_packs", () -> Bukkit.getWorlds().stream()
                    .map(IrisToolbelt::access)
                    .filter(Objects::nonNull)
                    .map(PlatformChunkGenerator::getEngine)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(engine -> engine.getDimension().getLoadKey(), engine -> {
                        var hash32 = engine.getHash32().getNow(null);
                        if (hash32 == null) return Map.of();
                        int version = engine.getDimension().getVersion();
                        String checksum = Long.toHexString(hash32);

                        return Map.of("v" + version + " (" + checksum + ")", 1);
                    }, (a, b) -> {
                        Map<String, Integer> merged = new HashMap<>(a);
                        b.forEach((k, v) -> merged.merge(k, v, Integer::sum));
                        return merged;
                    }))));
            metrics.addCustomChart(new DrilldownPie("environment", () -> Map.of(BuildConstants.ENVIRONMENT, Map.of(BuildConstants.COMMIT, 1))));

            plugin.postShutdown(metrics::shutdown);
        });
    }

    public static class Adventure {
        private final BukkitAudiences audiences;

        public Adventure(Iris plugin) {
            ShadeFix.fix(ComponentSerializer.class);
            this.audiences = BukkitAudiences.create(plugin);
        }

        public Audience player(Player player) {
            return audiences.player(player);
        }

        public Audience sender(CommandSender sender) {
            return audiences.sender(sender);
        }
    }
}
