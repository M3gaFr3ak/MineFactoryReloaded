package powercrystals.minefactoryreloaded.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import powercrystals.core.position.BlockPosition;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.api.rednet.IRedNetDecorative;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockVineScaffold extends Block implements IRedNetDecorative
{
	private Icon _sideIcon;
	private Icon _topIcon;
	
	private static final ForgeDirection[] _attachDirections = new ForgeDirection[] { ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.EAST, ForgeDirection.WEST };
	private static final int _attachDistance = 16;
	
	public BlockVineScaffold(int id)
	{
		super(id, Material.leaves);
		setUnlocalizedName("mfr.vinescaffold");
		setStepSound(soundGrassFootstep);
		setHardness(0.1F);
		setBlockBounds(0F, 1 / 48f, 0F, 1F, 1f - 1 / 48f, 1F);
		setTickRandomly(true);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		if(entity instanceof EntityPlayerMP)
			((EntityPlayerMP)entity).playerNetServerHandler.ticksForFloatKick = 0;
		entity.fallDistance = 0;
		if (entity.isCollidedHorizontally)
		{
			entity.motionY = 0.2D;
		}
		else if (entity.isSneaking())
		{
			double diff = entity.prevPosY - entity.posY;
			entity.boundingBox.minY += diff;
			entity.boundingBox.maxY += diff;
			entity.posY = entity.prevPosY;
		}
		else
		{
			entity.motionY = -0.10D;
		}
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		float shrinkAmount = 0.125F;
		return AxisAlignedBB.getBoundingBox(x + this.minX + shrinkAmount, y + this.minY,
				z + this.minZ + shrinkAmount, x + this.maxX - shrinkAmount,
				y + this.maxY, z + this.maxZ - shrinkAmount);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister ir)
	{
		_sideIcon = ir.registerIcon("minefactoryreloaded:" + getUnlocalizedName() + ".side");
		_topIcon = ir.registerIcon("minefactoryreloaded:" + getUnlocalizedName() + ".top");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta)
	{
		return side < 2 ? _topIcon : _sideIcon;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
	@Override
	public int getRenderType()
	{
		return MineFactoryReloadedCore.renderIdVineScaffold;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
	{
		return !world.isBlockOpaqueCube(x, y, z);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(int meta)
	{
		return ColorizerFoliage.getFoliageColorBasic();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess world, int x, int y, int z)
	{
		int r = 0;
		int g = 0;
		int b = 0;
		
		for(int zOffset = -1; zOffset <= 1; ++zOffset)
		{
			for(int xOffset = -1; xOffset <= 1; ++xOffset)
			{
				int biomeColor = world.getBiomeGenForCoords(x + xOffset, z + zOffset).getBiomeFoliageColor();
				r += (biomeColor & 16711680) >> 16;
			g += (biomeColor & 65280) >> 8;
		b += biomeColor & 255;
			}
		}
		
		return (r / 9 & 255) << 16 | (g / 9 & 255) << 8 | b / 9 & 255;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xOffset, float yOffset, float zOffset)
	{
		if (player.inventory.mainInventory[player.inventory.currentItem] != null && player.inventory.mainInventory[player.inventory.currentItem].itemID == blockID)
		{
			for(int i = y + 1, e = world.getActualHeight(); i < e; ++i)
			{
				int blockId = world.getBlockId(x, i, z);
				Block block = Block.blocksList[blockId];
				if(block == null || world.isAirBlock(x, i, z) || block.isBlockReplaceable(world, x, i, z))
				{
					if (!world.isRemote && world.setBlock(x, i, z, blockID, 0, 3))
					{
						world.playAuxSFXAtEntity(null, 2001, x, i, z, blockID);
						if (!player.capabilities.isCreativeMode)
						{
							player.inventory.mainInventory[player.inventory.currentItem].stackSize--;
							if(player.inventory.mainInventory[player.inventory.currentItem].stackSize == 0)
							{
								player.inventory.mainInventory[player.inventory.currentItem] = null;
							}
						}
					}
					return true;
				}
				else if (blockId != blockID)
				{
					return false;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		return canBlockStay(world, x, y, z);
	}
	
	@Override
	public boolean canBlockStay(World world, int x, int y, int z)
	{
		if(world.isBlockSolidOnSide(x, y - 1, z, ForgeDirection.UP))
		{
			return true;
		}
		for(ForgeDirection d : _attachDirections)
		{
			BlockPosition bp = new BlockPosition(x, y, z, d);
			for(int i = 0; i < _attachDistance; i++)
			{
				bp.moveForwards(1);
				if(world.getBlockId(bp.x, bp.y, bp.z) == blockID && world.isBlockSolidOnSide(bp.x, bp.y - 1, bp.z, ForgeDirection.UP))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		onNeighborBlockChange(world, x, y, z, ForgeDirection.UNKNOWN.ordinal());
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int side)
	{
		if(!canBlockStay(world, x, y, z))
		{
			dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
			world.setBlockToAir(x, y, z);
		}
	}
	
	@Override
	public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
	{
		return (side == ForgeDirection.UP || side == ForgeDirection.DOWN) ? true : false;
	}
}
