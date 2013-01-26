package powercrystals.minefactoryreloaded.core;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore.Machine;

public class ItemFactoryMachine extends ItemBlock
{
	public ItemFactoryMachine(int i)
	{
		super(i);
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public int getIconFromDamage(int i)
	{
		return Math.min(i, 15);
	}
	
	@Override
	public int getMetadata(int i)
	{
		return i;
	}

	@Override
	public String getItemNameIS(ItemStack itemstack)
	{
		int md = itemstack.getItemDamage();
		
		if(md == MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Planter)) return "factoryPlanterItem";
		if(md == MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Fisher)) return "factoryFisherItem";
		if(md == MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Harvester)) return "factoryHarvesterItem";
		if(md == MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Rancher)) return "factoryRancherItem";
		if(md == MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Fertilizer)) return "factoryFertilizerItem";
		if(md == MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Vet)) return "factoryVetItem";
		if(md == MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Collector)) return "factoryCollectorItem";
		if(md == MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Breaker)) return "factoryBlockBreakerItem";
		if(md == MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Weather)) return "factoryWeatherItem";
		if(md == MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Boiler)) return "factorySludgeBoilerItem";
		if(md == MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Sewer)) return "factorySewerItem";
		if(md == MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Composter)) return "factoryComposterItem";
		if(md == MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Breeder)) return "factoryBreederItem";
		if(md == MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Grinder)) return "factoryGrinderItem";
		if(md == MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Enchanter)) return "factoryEnchanterItem";
		if(md == MineFactoryReloadedCore.machine0MetadataMappings.get(Machine.Chronotyper)) return "factoryChronotyperItem";
		return "Invalid";
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for(int i = 0; i < 16; i++)
		{
			par3List.add(new ItemStack(MineFactoryReloadedCore.machineBlock0.blockID, 1, i));
		}
	}
}
