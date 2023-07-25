package olsior.shop.telegram.cache;

import olsior.shop.telegram.domain.BotUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BotUserCache implements Cache<BotUser> {

  private static final BotUserCache botUserCache = new BotUserCache();
  private final Map<Long, BotUser> users;

  private BotUserCache() {
    this.users = new HashMap<>();
  }

  public static BotUserCache getBotUserCache() {
    return botUserCache;
  }


  @Override
  public void add(BotUser botUser) {
    if (botUser.getId() != null) {
      users.put(botUser.getId(), botUser);
    }
  }

  @Override
  public void remove(BotUser botUser) {
    users.remove(botUser.getId());
  }

  @Override
  public BotUser findBy(Long id) {
    return users.get(id);
  }

  @Override
  public List<BotUser> getAll() {
    return new ArrayList<>(users.values());
  }
}
