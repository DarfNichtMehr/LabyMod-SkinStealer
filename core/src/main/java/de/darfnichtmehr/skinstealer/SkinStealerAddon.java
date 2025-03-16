package de.darfnichtmehr.skinstealer;

import de.darfnichtmehr.skinstealer.interaction.StealSkinBulletPoint;
import de.darfnichtmehr.skinstealer.command.SkinStealerCommand;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;


@AddonMain
public class SkinStealerAddon extends LabyAddon<SkinStealerConfiguration> {

  @Override
  protected void enable() {
    this.registerSettingCategory();

    this.registerCommand(new SkinStealerCommand(this));

    this.labyAPI().interactionMenuRegistry().register(new StealSkinBulletPoint());

    this.logger().info("Enabled the Addon");
  }

  @Override
  protected Class<SkinStealerConfiguration> configurationClass() {
    return SkinStealerConfiguration.class;
  }


}
