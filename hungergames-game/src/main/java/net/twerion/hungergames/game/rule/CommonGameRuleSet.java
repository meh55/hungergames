package net.twerion.hungergames.game.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

public final class CommonGameRuleSet {
  private Collection<CommonGameRule> disabledRules;

  private CommonGameRuleSet(Collection<CommonGameRule> disabledRules) {
    this.disabledRules = disabledRules;
  }

  public boolean isDisabled(CommonGameRule rule) {
    return disabledRules.contains(rule);
  }

  public boolean isEnabled(CommonGameRule rule) {
    return !disabledRules.contains(rule);
  }

  public static CommonGameRuleSet of(Collection<CommonGameRule> disabledRules) {
     return new CommonGameRuleSet(EnumSet.copyOf(disabledRules));
  }

  public static CommonGameRuleSet disallow(
    CommonGameRule... rules
  ) {
    return of(Arrays.asList(rules));
  }

  public static CommonGameRuleSet allow(
    CommonGameRule... rules
  ) {
    Collection<CommonGameRule> allowed = Arrays.asList(rules);
    Collection<CommonGameRule> disallowed = new ArrayList<>();
    for (CommonGameRule rule : CommonGameRule.values()) {
      if (allowed.contains(rule)) {
        continue;
      }
      disallowed.add(rule);
    }
    return of(disallowed);
  }
}
