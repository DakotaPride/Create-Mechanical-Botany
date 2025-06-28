package net.dakotapride.mechanical_botany;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CreateMechanicalBotany.MOD_ID)
public class CreateMechanicalBotany {
    public static final String MOD_ID = "mechanical_botany";
    public static final String NAME = "Create: Mechanical Botany";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID)
            .defaultCreativeTab((ResourceKey<CreativeModeTab>) null).setTooltipModifierFactory(item ->
                    new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                    .andThen(TooltipModifier.mapNull(KineticStats.create(item))));

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public CreateMechanicalBotany() {
        ModLoadingContext ctx = ModLoadingContext.get();

        IEventBus eventBus = FMLJavaModLoadingContext.get()
                .getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        // Register the commonSetup method for modloading
        eventBus.addListener(this::commonSetup);

        REGISTRATE.registerEventListeners(eventBus);
        ModBlockEntityTypes.register();
        ModBlocks.register();
        ModItems.register();
        ModCreativeModeTabs.register(eventBus);
        ModRecipeTypes.register(eventBus);
        ModPartialModels.register();
        ModFluids.register();

        eventBus.addListener(this::clientSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        // BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        // ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        // CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        eventBus.addListener(CreateMechanicalBotany::onRegister);

        // Register the item to a creative tab
        // modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        // ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public static void onRegister(final RegisterEvent event) {
        ModArmInteractionPointTypes.register();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        //LOGGER.info("HELLO FROM COMMON SETUP");
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        PonderIndex.addPlugin(new ModPonderPlugin());
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        //LOGGER.info("HELLO from server starting");
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {

//        @SubscribeEvent
//        public static void getRightClickOnBlockEvent(UseItemOnBlockEvent event) {
//            Level level = event.getLevel();
//            ItemStack stack = event.getItemStack();
//            Player player = event.getPlayer();
//            BlockPos pos = event.getPos();
//            BlockState state = level.getBlockState(pos);
//
//            if (stack.is(Items.BUCKET) && state.getValue(ComposterBlock.LEVEL) == 8) {
//                stack.shrink(1);
//                if (player != null)
//                    player.addItem(new ItemStack(ModFluids.COMPOST.get().getBucket()));
//            }
//        }

        @SubscribeEvent
        public static void registerCapabilities(RegisterCapabilitiesEvent event) {
            //MechanicalInsolatorBlockEntity.registerCapabilities(event);
            //MechanicalComposterBlockEntity.registerCapabilities(event);
        }
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            //LOGGER.info("HELLO FROM CLIENT SETUP");
            //LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
            //PonderIndex.addPlugin(new ModPonderPlugin());

            ItemBlockRenderTypes.setRenderLayer(ModBlocks.MECHANICAL_INSOLATOR.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.MECHANICAL_COMPOSTER.get(), RenderType.cutout());
        }
    }
}
