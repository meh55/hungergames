package net.twerion.hungergames.game;

import net.twerion.hungergames.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

@Component
public class FallbackScoreboard implements Listener {

  private Team playerTeam;
  private Scoreboard scoreboard;

  public FallbackScoreboard() {
    this.initializeScoreboard();
  }

  private void initializeScoreboard() {
    scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    playerTeam = scoreboard.registerNewTeam("players");
    playerTeam.setPrefix("§7");

    Objective sidebar = scoreboard.registerNewObjective("sidebar", "dummy");
    sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
    sidebar.setDisplayName("§4§lMinecraftMonday");
    sidebar.getScore("§f§a").setScore(4);
    sidebar.getScore("§8 §7Waiting for").setScore(3);
    sidebar.getScore("§a §7Tributes...").setScore(2);
    sidebar.getScore("§f§b").setScore(1);
    sidebar.getScore("§c@KEEMSTAR").setScore(0);
  }

  @EventHandler
  public void setScoreboard(PlayerJoinEvent join) {
    Player player = join.getPlayer();
    playerTeam.addEntry(player.getDisplayName());
    player.setScoreboard(scoreboard);
  }
}
