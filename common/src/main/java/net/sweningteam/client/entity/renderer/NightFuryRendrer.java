package net.sweningteam.client.entity.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.EntityType;
import net.sweningteam.client.entity.model.NightfuryModel;
import net.sweningteam.common.entity.dragons.NightFury;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class NightFuryRendrer <R extends EntityRenderState & GeoRenderState> extends GeoEntityRenderer<NightFury, R> {
    public NightFuryRendrer(EntityRendererProvider.Context context) {
        super(context,new NightfuryModel<>());
        shadowRadius = 1.5F;
    }
}
