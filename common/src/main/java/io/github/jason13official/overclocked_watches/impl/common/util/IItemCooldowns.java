package io.github.jason13official.overclocked_watches.impl.common.util;

import java.util.List;

public interface IItemCooldowns {

  List<CoolDownRecord> persistcd$getCooldownTicks();

  void persistcd$addCoolDown(CoolDownRecord var1);
}
