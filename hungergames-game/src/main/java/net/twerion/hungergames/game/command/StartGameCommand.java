package net.twerion.hungergames.game.command;

import javax.inject.Inject;

import net.twerion.hungergames.CommandLabel;
import net.twerion.hungergames.game.warmup.WarmupPhase;
import net.twerion.hungergames.game.warmup.WarmupPhaseStarter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.twerion.hungergames.CommonMessageKey;
import net.twerion.hungergames.Component;
import net.twerion.hungergames.Restricted;
import net.twerion.hungergames.game.Game;
import net.twerion.hungergames.game.waiting.WaitingPhaseStarter;
import net.twerion.hungergames.locale.MessageStore;

@Component
@CommandLabel("start")
public final class StartGameCommand extends Restricted implements CommandExecutor {
  private Game game;
  private MessageStore messages;
  private WarmupPhaseStarter phaseStarter;

  @Inject
  private StartGameCommand(
    Game game, MessageStore messages, WarmupPhaseStarter phaseStarter
  ) {
    this.game = game;
    this.messages = messages;
    this.phaseStarter = phaseStarter;
  }

  @Override
  public boolean onCommand(
    CommandSender sender,
    Command command,
    String label,
    String[] arguments
  ) {
    if (!isPermissed(sender)) {
      sender.sendMessage(messages.find(CommonMessageKey.RESTRICTED_ACTION));
      return true;
    }
    if (!phaseStarter.canStart(game)) {
      sender.sendMessage(messages.find("message.game.start.failed"));
      return true;
    }
    phaseStarter.start(game);
    sender.sendMessage(messages.find("message.game.start.success"));
    return true;
  }
}
