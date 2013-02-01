package powercrystals.minefactoryreloaded;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import powercrystals.core.render.TextureLiquidStillFX;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore.Machine;
import powercrystals.minefactoryreloaded.render.RendererConveyor;
import powercrystals.minefactoryreloaded.render.RendererFactoryGlassPane;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.TextureFXManager;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

import net.minecraft.client.texturepacks.TexturePackList;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModTextureAnimation;
import net.minecraftforge.client.MinecraftForgeClient;

public class MineFactoryReloadedClient
{	
	private static MineFactoryReloadedClient instance;
	
	private static String[] colorNames = { "White", "Orange", "Magenta", "Light Blue", "Yellow", "Lime", "Pink", "Gray", "Light Gray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black" };

	public MineFactoryReloadedClient()
	{
		instance = this;
		load();
	}

	public static MineFactoryReloadedClient instance()
	{
		return instance;
	}

	private void load()
	{
		MinecraftForgeClient.preloadTexture(MineFactoryReloadedCore.terrainTexture);
		MinecraftForgeClient.preloadTexture(MineFactoryReloadedCore.itemTexture);
		MinecraftForgeClient.preloadTexture(MineFactoryReloadedCore.machine0Texture);
		MinecraftForgeClient.preloadTexture(MineFactoryReloadedCore.machine1Texture);

		LanguageRegistry.addName(new ItemStack(MineFactoryReloadedCore.machineBlock0, 1, MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Planter)), "Planter");
		LanguageRegistry.addName(new ItemStack(MineFactoryReloadedCore.machineBlock0, 1, MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Fisher)), "Fisher");
		LanguageRegistry.addName(new ItemStack(MineFactoryReloadedCore.machineBlock0, 1, MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Harvester)), "Harvester");
		LanguageRegistry.addName(new ItemStack(MineFactoryReloadedCore.machineBlock0, 1, MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Rancher)), "Rancher");
		LanguageRegistry.addName(new ItemStack(MineFactoryReloadedCore.machineBlock0, 1, MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Fertilizer)), "Fertilizer");
		LanguageRegistry.addName(new ItemStack(MineFactoryReloadedCore.machineBlock0, 1, MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Vet)), "Veterinary Station");
		LanguageRegistry.addName(new ItemStack(MineFactoryReloadedCore.machineBlock0, 1, MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Collector)), "Item Collector");
		LanguageRegistry.addName(new ItemStack(MineFactoryReloadedCore.machineBlock0, 1, MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Breaker)), "Block Breaker");
		LanguageRegistry.addName(new ItemStack(MineFactoryReloadedCore.machineBlock0, 1, MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Weather)), "Weather Collector");
		LanguageRegistry.addName(new ItemStack(MineFactoryReloadedCore.machineBlock0, 1, MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Boiler)), "Sludge Boiler");
		LanguageRegistry.addName(new ItemStack(MineFactoryReloadedCore.machineBlock0, 1, MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Sewer)), "Sewage Collector");
		LanguageRegistry.addName(new ItemStack(MineFactoryReloadedCore.machineBlock0, 1, MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Composter)), "Composter");
		LanguageRegistry.addName(new ItemStack(MineFactoryReloadedCore.machineBlock0, 1, MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Breeder)), "Breeder");
		LanguageRegistry.addName(new ItemStack(MineFactoryReloadedCore.machineBlock0, 1, MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Grinder)), "Grinder");
		LanguageRegistry.addName(new ItemStack(MineFactoryReloadedCore.machineBlock0, 1, MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Enchanter)), "Auto-Enchanter");
		LanguageRegistry.addName(new ItemStack(MineFactoryReloadedCore.machineBlock0, 1, MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Chronotyper)), "Chronotyper");;
		
		LanguageRegistry.addName(new ItemStack(MineFactoryReloadedCore.machineBlock1, 1, MineFactoryReloadedCore.machine1MetadataMappings.get(Machine.Ejector)), "Ejector");
		LanguageRegistry.addName(new ItemStack(MineFactoryReloadedCore.machineBlock1, 1, MineFactoryReloadedCore.machine1MetadataMappings.get(Machine.ItemRouter)), "Item Router");
		LanguageRegistry.addName(new ItemStack(MineFactoryReloadedCore.machineBlock1, 1, MineFactoryReloadedCore.machine1MetadataMappings.get(Machine.LiquidRouter)), "Liquid Router");
		LanguageRegistry.addName(new ItemStack(MineFactoryReloadedCore.machineBlock1, 1, MineFactoryReloadedCore.machine1MetadataMappings.get(Machine.DeepStorageUnit)), "Deep Storage Unit");
		LanguageRegistry.addName(new ItemStack(MineFactoryReloadedCore.machineBlock1, 1, MineFactoryReloadedCore.machine1MetadataMappings.get(Machine.LiquiCrafter)), "LiquiCrafter");
		LanguageRegistry.addName(new ItemStack(MineFactoryReloadedCore.machineBlock1, 1, MineFactoryReloadedCore.machine1MetadataMappings.get(Machine.LavaFabricator)), "Lava Fabricator");
		LanguageRegistry.addName(new ItemStack(MineFactoryReloadedCore.machineBlock1, 1, MineFactoryReloadedCore.machine1MetadataMappings.get(Machine.OilFabricator)), "Oil Fabricator");

		LanguageRegistry.addName(new ItemStack(MineFactoryReloadedCore.conveyorBlock, 1, 16), "Conveyor Belt");
		
		LanguageRegistry.addName(MineFactoryReloadedCore.rubberWoodBlock, "Rubber Wood");
		LanguageRegistry.addName(MineFactoryReloadedCore.rubberLeavesBlock, "Rubber Leaves");
		LanguageRegistry.addName(MineFactoryReloadedCore.rubberSaplingBlock, "Rubber Sapling");

		LanguageRegistry.addName(MineFactoryReloadedCore.railPickupCargoBlock, "Cargo Pickup Rail");
		LanguageRegistry.addName(MineFactoryReloadedCore.railDropoffCargoBlock, "Cargo Dropoff Rail");
		LanguageRegistry.addName(MineFactoryReloadedCore.railPickupPassengerBlock, "Passenger Pickup Rail");
		LanguageRegistry.addName(MineFactoryReloadedCore.railDropoffPassengerBlock, "Passenger Dropoff Rail");

		LanguageRegistry.addName(MineFactoryReloadedCore.factoryHammerItem, "Precision Sledgehammer");
		LanguageRegistry.addName(MineFactoryReloadedCore.milkItem, "Spilled Milk");
		LanguageRegistry.addName(MineFactoryReloadedCore.sludgeItem, "Spilled Sludge");
		LanguageRegistry.addName(MineFactoryReloadedCore.sewageItem, "Spilled Sewage");
		LanguageRegistry.addName(MineFactoryReloadedCore.mobEssenceItem, "Spilled Monster Essence");
		LanguageRegistry.addName(MineFactoryReloadedCore.fertilizerItem, "Industrial Fertilizer");
		LanguageRegistry.addName(MineFactoryReloadedCore.plasticSheetItem, "Plastic Sheets");
		LanguageRegistry.addName(MineFactoryReloadedCore.rawPlasticItem, "Raw Plastic");
		LanguageRegistry.addName(MineFactoryReloadedCore.rubberBarItem, "Rubber Bar");
		LanguageRegistry.addName(MineFactoryReloadedCore.sewageBucketItem, "Sewage Bucket");
		LanguageRegistry.addName(MineFactoryReloadedCore.sludgeBucketItem, "Sludge Bucket");
		LanguageRegistry.addName(MineFactoryReloadedCore.mobEssenceBucketItem, "Mob Essence Bucket");
		LanguageRegistry.addName(MineFactoryReloadedCore.syringeEmptyItem, "Empty Syringe");
		LanguageRegistry.addName(MineFactoryReloadedCore.syringeHealthItem, "Healing Syringe");
		LanguageRegistry.addName(MineFactoryReloadedCore.syringeGrowthItem, "Growth Hormone Syringe");
		LanguageRegistry.addName(MineFactoryReloadedCore.rawRubberItem, "Rubber");
		LanguageRegistry.addName(MineFactoryReloadedCore.machineBaseItem, "Factory Machine Block");
		LanguageRegistry.addName(MineFactoryReloadedCore.safariNetItem, "Safari Net");
		
		for(int i = 0; i < 16; i++)
		{
			LanguageRegistry.addName(new ItemStack(MineFactoryReloadedCore.factoryGlassBlock, 1, i), colorNames[i] + " Stained Glass");
			LanguageRegistry.addName(new ItemStack(MineFactoryReloadedCore.factoryGlassPaneBlock, 1, i), colorNames[i] + " Stained Glass Pane");
			LanguageRegistry.addName(new ItemStack(MineFactoryReloadedCore.ceramicDyeItem, 1, i), colorNames[i] + " Ceramic Dye");
			LanguageRegistry.addName(new ItemStack(MineFactoryReloadedCore.conveyorBlock, 1, i), colorNames[i] + " Conveyor Belt");
		}

		if(MineFactoryReloadedCore.animateBlockFaces.getBoolean(true))
		{
			registerAnimation(MineFactoryReloadedCore.conveyorTexture, MineFactoryReloadedCore.animationFolder + "Conveyor.png", MineFactoryReloadedCore.terrainTexture);
			registerAnimation(48, MineFactoryReloadedCore.animationFolder + "Conveyor-White.png", MineFactoryReloadedCore.terrainTexture);
			registerAnimation(49, MineFactoryReloadedCore.animationFolder + "Conveyor-Orange.png", MineFactoryReloadedCore.terrainTexture);
			registerAnimation(50, MineFactoryReloadedCore.animationFolder + "Conveyor-Magenta.png", MineFactoryReloadedCore.terrainTexture);
			registerAnimation(51, MineFactoryReloadedCore.animationFolder + "Conveyor-LightBlue.png", MineFactoryReloadedCore.terrainTexture);
			registerAnimation(52, MineFactoryReloadedCore.animationFolder + "Conveyor-Yellow.png", MineFactoryReloadedCore.terrainTexture);
			registerAnimation(53, MineFactoryReloadedCore.animationFolder + "Conveyor-Lime.png", MineFactoryReloadedCore.terrainTexture);
			registerAnimation(54, MineFactoryReloadedCore.animationFolder + "Conveyor-Pink.png", MineFactoryReloadedCore.terrainTexture);
			registerAnimation(55, MineFactoryReloadedCore.animationFolder + "Conveyor-Gray.png", MineFactoryReloadedCore.terrainTexture);
			registerAnimation(56, MineFactoryReloadedCore.animationFolder + "Conveyor-LightGray.png", MineFactoryReloadedCore.terrainTexture);
			registerAnimation(57, MineFactoryReloadedCore.animationFolder + "Conveyor-Cyan.png", MineFactoryReloadedCore.terrainTexture);
			registerAnimation(58, MineFactoryReloadedCore.animationFolder + "Conveyor-Purple.png", MineFactoryReloadedCore.terrainTexture);
			registerAnimation(59, MineFactoryReloadedCore.animationFolder + "Conveyor-Blue.png", MineFactoryReloadedCore.terrainTexture);
			registerAnimation(60, MineFactoryReloadedCore.animationFolder + "Conveyor-Brown.png", MineFactoryReloadedCore.terrainTexture);
			registerAnimation(61, MineFactoryReloadedCore.animationFolder + "Conveyor-Green.png", MineFactoryReloadedCore.terrainTexture);
			registerAnimation(62, MineFactoryReloadedCore.animationFolder + "Conveyor-Red.png", MineFactoryReloadedCore.terrainTexture);
			registerAnimation(63, MineFactoryReloadedCore.animationFolder + "Conveyor-Black.png", MineFactoryReloadedCore.terrainTexture);
			
			registerMachine0Animation(Machine.Harvester, "Harvester.png");
			registerMachine0Animation(Machine.Rancher, "Rancher.png");
			registerMachine0Animation(Machine.Breaker, "BlockBreaker.png");
			registerMachine0Animation(Machine.Fertilizer, "Fertilizer.png");
			registerMachine0Animation(Machine.Vet, "Vet.png");
			registerMachine0Animation(Machine.Breeder, "Breeder.png");
			registerMachine0Animation(Machine.Grinder, "Mobgrinder.png");
			registerMachine0Animation(Machine.Chronotyper, "Chronotyper.png");
			
			registerMachine1Animation(Machine.OilFabricator, "OilFabricator.png");
			
			TextureFXManager.instance().addAnimation(new TextureLiquidStillFX(2, MineFactoryReloadedCore.itemTexture, 240, 255, 240, 255, 230, 245)); // milk
			TextureFXManager.instance().addAnimation(new TextureLiquidStillFX(3, MineFactoryReloadedCore.itemTexture, 20, 20, 20, 20, 32, 32)); // sludge
			TextureFXManager.instance().addAnimation(new TextureLiquidStillFX(4, MineFactoryReloadedCore.itemTexture, 106, 106, 52, 52, 45, 45)); // sewage
			TextureFXManager.instance().addAnimation(new TextureLiquidStillFX(5, MineFactoryReloadedCore.itemTexture, 0, 0, 100, 128, 0, 0)); // essence
		}
		
		MineFactoryReloadedCore.renderIdConveyor = RenderingRegistry.getNextAvailableRenderId();
		MineFactoryReloadedCore.renderIdFactoryGlassPane = RenderingRegistry.getNextAvailableRenderId();
		
		RenderingRegistry.registerBlockHandler(MineFactoryReloadedCore.renderIdConveyor, new RendererConveyor());
		RenderingRegistry.registerBlockHandler(MineFactoryReloadedCore.renderIdFactoryGlassPane, new RendererFactoryGlassPane());
	}
	
	private void registerMachine0Animation(Machine machine, String animation)
	{
		registerAnimation(MineFactoryReloadedCore.machine0MetadataMappings.get(machine) * 16 + 2, MineFactoryReloadedCore.animationFolder + animation, MineFactoryReloadedCore.machine0Texture);
	}
	
	private void registerMachine1Animation(Machine machine, String animation)
	{
		registerAnimation(MineFactoryReloadedCore.machine1MetadataMappings.get(machine) * 16 + 2, MineFactoryReloadedCore.animationFolder + animation, MineFactoryReloadedCore.machine1Texture);
	}
	
	private void registerAnimation(int index, String animation, String targetTexture)
	{
		TexturePackList tpl = FMLClientHandler.instance().getClient().texturePackList;
		InputStream s;
		
		s = tpl.getSelectedTexturePack().getResourceAsStream(animation);
		if(s == null)
		{
			s = (net.minecraft.client.Minecraft.class).getResourceAsStream(animation);
		}
		
		BufferedImage bufferedimage;
		try
		{
			bufferedimage = ImageIO.read(s);
			TextureFXManager.instance().addAnimation(new ModTextureAnimation(index, 1, targetTexture, bufferedimage, 1));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
