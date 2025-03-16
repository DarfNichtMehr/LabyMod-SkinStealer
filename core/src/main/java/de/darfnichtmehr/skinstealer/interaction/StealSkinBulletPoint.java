package de.darfnichtmehr.skinstealer.interaction;

import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.interaction.BulletPoint;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.texture.GameImage;
import net.labymod.api.client.resources.texture.Texture;
import net.labymod.api.client.session.MinecraftServices.SkinPayload;
import net.labymod.api.client.session.MinecraftServices.SkinVariant;
import net.labymod.core.main.LabyMod;

public class StealSkinBulletPoint implements BulletPoint {

  @Override
  public Component getTitle() {
    return Component.text("Steal Skin");
  }

  @Override
  public Icon getIcon() {
    return null;
  }
  @Override
  public void execute(Player player) {
      SkinVariant skinVariant = player.isSlim() ? SkinVariant.SLIM : SkinVariant.CLASSIC;

      Texture skinTexture = Laby.references().textureRepository().getTexture(player.skinTexture());
      GameImage skinImage = GameImage.IMAGE_PROVIDER.loadImage(skinTexture);

      LabyMod.references().applyTextureController().uploadSkinAsync(skinVariant, new SkinPayload(skinImage));
  }
}
