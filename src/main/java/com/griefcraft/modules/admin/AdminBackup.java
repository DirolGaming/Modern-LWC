/*
 * Copyright 2011 Tyler Blair. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and contributors and should not be interpreted as representing official policies,
 * either expressed or implied, of anybody else.
 */

package com.griefcraft.modules.admin;

import com.griefcraft.io.BackupManager;
import com.griefcraft.lwc.LWC;
import com.griefcraft.scripting.JavaModule;
import com.griefcraft.scripting.event.LWCCommandEvent;
import com.griefcraft.util.StringUtil;

import org.bukkit.command.CommandSender;

public class AdminBackup extends JavaModule {

    @SuppressWarnings("deprecation")
    @Override
    public void onCommand(LWCCommandEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!event.hasFlag("a", "admin")) {
            return;
        }

        final LWC lwc = event.getLWC();
        final CommandSender sender = event.getSender();
        String[] args = event.getArgs();

        if (!args[0].equals("backup")) {
            return;
        }

        // we have the right command
        event.setCancelled(true);

        if (args.length == 1) {
            lwc.sendSimpleUsage(sender, "/lwc admin backup <action>");
            return;
        }

        // The action they want to perform
        String action = args[1].toLowerCase();

        if (action.equals("create")) {
            lwc.getBackupManager().createBackup();
            sender.sendMessage("Backup is being created now.");
        } else if (action.equals("restore")) {
            if (args.length < 3) {
                lwc.sendSimpleUsage(sender, "/lwc admin backup restore <BackupName>");
                return;
            }

            final String backupName = StringUtil.join(args, 2);
            sender.sendMessage("Restoring backup " + backupName);

            lwc.getPlugin().getServer().getScheduler().scheduleAsyncDelayedTask(lwc.getPlugin(), new Runnable() {
                public void run() {
                    BackupManager.Result result = lwc.getBackupManager().restoreBackup(backupName);
                    sender.sendMessage("Result: " + result);
                }
            });
        }
    }

}