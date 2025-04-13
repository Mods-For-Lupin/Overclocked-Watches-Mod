package com.cursee.overclocked_watches.core.util;

import java.util.List;

public interface IItemCooldowns {
    List<CoolDownRecord> persistcd$getCooldownTicks();

    void persistcd$addCoolDown(CoolDownRecord var1);
}
