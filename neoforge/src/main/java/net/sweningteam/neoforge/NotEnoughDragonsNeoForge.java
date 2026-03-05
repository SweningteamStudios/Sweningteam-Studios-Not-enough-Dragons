package net.sweningteam.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.sweningteam.NotEnoughDragons;
import net.sweningteam.common.registry.ModEntityTypes;

@Mod(NotEnoughDragons.MOD_ID)
public final class NotEnoughDragonsNeoForge {
    public NotEnoughDragonsNeoForge(final IEventBus eventBus) {
        IEventBus EVENT_BUS = NeoForge.EVENT_BUS;
        // Run our common setup.
        NotEnoughDragons.init();
        EVENT_BUS.addListener((EntityAttributeCreationEvent event) -> ModEntityTypes.registerEntityAtributes(event::put));
    }
}
