package com.volmit.iris.core.command.pregen;

import com.volmit.iris.Iris;
import com.volmit.iris.core.gui.PregeneratorJob;
import com.volmit.iris.core.pregenerator.PregenTask;
import com.volmit.iris.core.pregenerator.methods.HybridPregenMethod;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.math.Position2;
import com.volmit.iris.util.plugin.MortarCommand;
import com.volmit.iris.util.plugin.VolmitSender;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class CommandIrisPregenStop extends MortarCommand {

    public CommandIrisPregenStop() {
        super("stop", "s", "x", "close");
        requiresPermission(Iris.perm);
        setCategory("Pregen");
        setDescription("Stop an ongoing pregeneration task");
    }

    @Override
    public boolean handle(VolmitSender sender, String[] args) {
        if (PregeneratorJob.shutdownInstance()){
            sender.sendMessage("Stopped pregeneration task");
        } else {
            sender.sendMessage("No active pregeneration tasks to stop");
        }
        return true;
    }

    @Override
    public void addTabOptions(VolmitSender sender, String[] args, KList<String> list) {

    }

    @Override
    protected String getArgsUsage() {
        return "";
    }
}