package com.github.upcraftlp.votifier.util;

import com.github.upcraftlp.votifier.ForgeVotifier;
import com.github.upcraftlp.votifier.config.VotifierConfig;
import net.minecraft.util.StringUtils;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ModUpdateHandler {

    public static boolean hasUpdate(ForgeVersion.CheckResult result) {
        ForgeVersion.Status status = result.status;
        if(status == ForgeVersion.Status.PENDING || status == ForgeVersion.Status.FAILED) {
            ForgeVotifier.getLogger().warn("Error getting update status for {}, found status {}!", ForgeVotifier.MODNAME, status.toString());
            return false;
        }
        else return status == ForgeVersion.Status.OUTDATED || (VotifierConfig.updates.showBetaUpdates && status == ForgeVersion.Status.BETA_OUTDATED);
    }

    public static ForgeVersion.CheckResult getResult() {
        return ForgeVersion.getResult(FMLCommonHandler.instance().findContainerFor(ForgeVotifier.MODID));
    }

    public static void notifyServer() {
        if(ForgeVotifier.isCoreLoaded()) return;
        ForgeVersion.CheckResult result = getResult();
        if (hasUpdate(result)) {
            StringBuilder builder = new StringBuilder();
            if(result.changes != null) result.changes.forEach((version, changes) -> builder.append("\n\t").append(version.toString()).append(":\n\t\t").append(changes));
            ForgeVotifier.getLogger().info("There's an update available for {}" + (StringUtils.isNullOrEmpty(result.url) ? "": ", download version {} here: {}\nChangelog:{}"), ForgeVotifier.MODNAME, result.target, result.url, builder.toString());
        }
    }
}
