package io.github.jason13official.overclocked_watches.api.common.data;

import java.util.List;

/// data access interface for ItemCooldowns
public interface IItemCooldowns {

  List<CoolDownRecord> overclocked_watches$getCooldownTicks();

  void overclocked_watches$addCoolDown(CoolDownRecord var1);
}
