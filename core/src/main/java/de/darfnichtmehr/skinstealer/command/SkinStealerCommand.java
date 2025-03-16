package de.darfnichtmehr.skinstealer.command;

import de.darfnichtmehr.skinstealer.SkinStealerAddon;
import java.io.IOException;
import java.util.UUID;
import net.labymod.api.Constants.Urls;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.resources.texture.GameImage;
import net.labymod.api.client.session.MinecraftServices.SkinPayload;
import net.labymod.api.client.session.MinecraftServices.SkinVariant;
import net.labymod.api.notification.Notification;
import net.labymod.api.util.concurrent.task.Task;
import net.labymod.api.util.io.web.WebInputStream;
import net.labymod.api.util.io.web.exception.WebRequestException;
import net.labymod.api.util.io.web.request.Request;
import net.labymod.api.util.io.web.request.Response;
import net.labymod.api.util.io.web.result.Result;
import net.labymod.core.main.LabyMod;

public class SkinStealerCommand extends Command {
  private final SkinStealerAddon ADDON;
  public SkinStealerCommand(SkinStealerAddon addon) {
    super("skinstealer");

    this.ADDON = addon;
  }

  @Override
  public boolean execute(String prefix, String[] args) {

    if (args.length != 1) {
      ADDON.displayMessage(
          Component.translatable("skinstealer.command.usage", NamedTextColor.RED)
      );
      return true;
    }

    Task.builder(() -> {
      try {
        stealSkin(args[0]);
      }
      catch (IOException exception) {
        LabyMod.references().notificationController().push(
            Notification.builder()
                .title(Component.translatable("labymod.activity.skins.apply.title"))
                .text(Component.translatable("labymod.activity.skins.apply.error"))
                .build()
        );
      }

    }).build().execute();

    return true;
  }

  private static void stealSkin(String name) throws IOException {
    Result<UUID> uuid = Laby.references().labyNetController().loadUniqueIdByNameSync(name);

    if (uuid.hasException()) {
      throw new WebRequestException(uuid.exception());
    }

    Response<WebInputStream> skin = Request
        .ofInputStream()
        .url(String.format(Urls.LABYNET_SKIN, uuid.get()))
        .async(false)
        .executeSync();

    if (skin.hasException()) {
      throw new WebRequestException(skin.exception());
    }

    String skinType = skin.getHeaders().get("x-skin-type");
    SkinVariant skinVariant = skinType.equals("slim") ? SkinVariant.SLIM : SkinVariant.CLASSIC;

    GameImage skinImage = GameImage.IMAGE_PROVIDER.getImage(skin.get());

    LabyMod.references().applyTextureController().uploadSkin(skinVariant, new SkinPayload(skinImage));
  }

}
