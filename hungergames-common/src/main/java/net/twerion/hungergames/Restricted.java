package net.twerion.hungergames;

import net.twerion.hungergames.Preconditions;
import org.bukkit.permissions.Permissible;

public class Restricted {
  private String permission;

  protected  Restricted() { }

  protected Restricted(String permission) {
    this.permission = permission;
  }

  public boolean isPermissed(Permissible target) {
    if (permission.isEmpty()) {
      return true;
    }
    return target.hasPermission(permission);
  }

  public String permission() {
    return this.permission;
  }

  public void setPermission(String permission) {
    Preconditions.checkNotNull(permission);
    this.permission =  permission;
  }
}
