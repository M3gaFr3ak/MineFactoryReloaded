package powercrystals.minefactoryreloaded.block;

import codechicken.lib.raytracer.ExtendedMOP;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.raytracer.RayTracer;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import cofh.api.block.IBlockInfo;
import cofh.api.block.IDismantleable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

import powercrystals.core.position.BlockPosition;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.api.rednet.IRedNetNetworkContainer;
import powercrystals.minefactoryreloaded.api.rednet.RedNetConnectionType;
import powercrystals.minefactoryreloaded.core.MFRUtil;
import powercrystals.minefactoryreloaded.gui.MFRCreativeTab;
import powercrystals.minefactoryreloaded.item.ItemRedNetMeter;
import powercrystals.minefactoryreloaded.render.block.RedNetCableRenderer;
import powercrystals.minefactoryreloaded.setup.Machine;
import powercrystals.minefactoryreloaded.tile.rednet.RedstoneNetwork;
import powercrystals.minefactoryreloaded.tile.rednet.TileEntityRedNetCable;
import powercrystals.minefactoryreloaded.tile.rednet.TileEntityRedNetEnergy;

public class BlockRedNetCable extends BlockContainer
implements IRedNetNetworkContainer, IBlockInfo, IDismantleable
{
	private static float _wireSize   =  4.0F / 16.0F;
	private static float _cageSize   =  6.0F / 16.0F;
	private static float _gripWidth  =  8.0F / 16.0F;
	private static float _plateWidth = 14.0F / 16.0F;
	private static float _plateDepth =  2.0F / 16.0F;
	private static float _bandWidth  =  5.0F / 16.0F;
	private static float _bandOffset =  2.0F / 16.0F;
	private static float _bandDepth  =  1.0F / 16.0F;

	private static float _wireStart      = 0.5F - _wireSize   / 2.0F;
	private static float _wireEnd        = 0.5F + _wireSize   / 2.0F;
	private static float _cageStart      = 0.5F - _cageSize   / 2.0F;
	private static float _cageEnd        = 0.5F + _cageSize   / 2.0F;
	private static float _gripStart      = 0.5F - _gripWidth  / 2.0F;
	private static float _gripEnd        = 0.5F + _gripWidth  / 2.0F;
	private static float _plateStart     = 0.5F - _plateWidth / 2.0F;
	private static float _plateEnd       = 0.5F + _plateWidth / 2.0F;
	private static float _bandWidthStart = 0.5F - _bandWidth  / 2.0F;
	private static float _bandWidthEnd   = 0.5F + _bandWidth  / 2.0F;

	private static float _bandDepthStart = _bandOffset;
	private static float _bandDepthEnd   = _bandOffset + _bandDepth;

	private static int[] _subSideMappings = new int[] { 6, 6,
		0, 1, 2, 3, 4, 5,
		0, 1, 2, 3, 4, 5,
		0, 1, 2, 3, 4, 5,
		0, 1, 2, 3, 4, 5,
		0, 1, 2, 3, 4, 5 };

	public static Cuboid6[] subSelection = new Cuboid6[2 + 6 * 5];

	static {
		int i = 0;
		// cable hitbox
		subSelection[i++] = new Cuboid6(_wireStart, _wireStart, _wireStart, _wireEnd, _wireEnd, _wireEnd);
		// wire cage hitbox
		subSelection[i++] = new Cuboid6(_cageStart, _cageStart, _cageStart, _cageEnd, _cageEnd, _cageEnd);

		// grip hitbox
		subSelection[i++] = new Cuboid6(_gripStart, 0, _gripStart, _gripEnd, _plateDepth, _gripEnd);
		subSelection[i++] = new Cuboid6(_gripStart, 1 - _plateDepth, _gripStart, _gripEnd, 1, _gripEnd);
		subSelection[i++] = new Cuboid6(_gripStart, _gripStart, 0, _gripEnd, _gripEnd, _plateDepth);
		subSelection[i++] = new Cuboid6(_gripStart, _gripStart, 1 - _plateDepth, _gripEnd, _gripEnd, 1);
		subSelection[i++] = new Cuboid6(0, _gripStart, _gripStart, _plateDepth, _gripEnd, _gripEnd);
		subSelection[i++] = new Cuboid6(1 - _plateDepth, _gripStart, _gripStart, 1, _gripEnd, _gripEnd);

		// plate hitbox
		subSelection[i++] = new Cuboid6(_plateStart, 0, _plateStart, _plateEnd, _plateDepth, _plateEnd);
		subSelection[i++] = new Cuboid6(_plateStart, 1 - _plateDepth, _plateStart, _plateEnd, 1, _plateEnd);
		subSelection[i++] = new Cuboid6(_plateStart, _plateStart, 0, _plateEnd, _plateEnd, _plateDepth);
		subSelection[i++] = new Cuboid6(_plateStart, _plateStart, 1 - _plateDepth, _plateEnd, _plateEnd, 1);
		subSelection[i++] = new Cuboid6(0, _plateStart, _plateStart, _plateDepth, _plateEnd, _plateEnd);
		subSelection[i++] = new Cuboid6(1 - _plateDepth, _plateStart, _plateStart, 1, _plateEnd, _plateEnd);

		// color band hitbox
		subSelection[i++] = new Cuboid6(_bandWidthStart, _bandDepthStart, _bandWidthStart, _bandWidthEnd, _bandDepthEnd, _bandWidthEnd);
		subSelection[i++] = new Cuboid6(_bandWidthStart, 1 - _bandDepthEnd, _bandWidthStart, _bandWidthEnd, 1 - _bandDepthStart, _bandWidthEnd);
		subSelection[i++] = new Cuboid6(_bandWidthStart, _bandWidthStart, _bandDepthStart, _bandWidthEnd, _bandWidthEnd, _bandDepthEnd);
		subSelection[i++] = new Cuboid6(_bandWidthStart, _bandWidthStart, 1 - _bandDepthEnd, _bandWidthEnd, _bandWidthEnd, 1 - _bandDepthStart);
		subSelection[i++] = new Cuboid6(_bandDepthStart, _bandWidthStart, _bandWidthStart, _bandDepthEnd, _bandWidthEnd, _bandWidthEnd);
		subSelection[i++] = new Cuboid6(1 - _bandDepthEnd, _bandWidthStart, _bandWidthStart, 1 - _bandDepthStart, _bandWidthEnd, _bandWidthEnd);

		// cable connection hitbox
		subSelection[i++] = new Cuboid6(_wireStart, _plateDepth, _wireStart, _wireEnd, _wireStart, _wireEnd);
		subSelection[i++] = new Cuboid6(_wireStart, _wireEnd, _wireStart, _wireEnd, 1 - _plateDepth, _wireEnd);
		subSelection[i++] = new Cuboid6(_wireStart, _wireStart, _plateDepth, _wireEnd, _wireEnd, _wireStart);
		subSelection[i++] = new Cuboid6(_wireStart, _wireStart, _wireEnd, _wireEnd, _wireEnd, 1 - _plateDepth);
		subSelection[i++] = new Cuboid6(_plateDepth, _wireStart, _wireStart, _wireStart, _wireEnd, _wireEnd);
		subSelection[i++] = new Cuboid6(_wireEnd, _wireStart, _wireStart, 1 - _plateDepth, _wireEnd, _wireEnd);
		
		// wire cage connection hitbox
		subSelection[i++] = new Cuboid6(_cageStart, _bandDepthEnd, _cageStart, _cageEnd, _cageStart, _cageEnd);
		subSelection[i++] = new Cuboid6(_cageStart, _cageEnd, _cageStart, _cageEnd, 1 - _bandDepthEnd, _cageEnd);
		subSelection[i++] = new Cuboid6(_cageStart, _cageStart, _bandDepthEnd, _cageEnd, _cageEnd, _cageStart);
		subSelection[i++] = new Cuboid6(_cageStart, _cageStart, _cageEnd, _cageEnd, _cageEnd, 1 - _bandDepthEnd);
		subSelection[i++] = new Cuboid6(_bandDepthEnd, _cageStart, _cageStart, _cageStart, _cageEnd, _cageEnd);
		subSelection[i++] = new Cuboid6(_cageEnd, _cageStart, _cageStart, 1 - _bandDepthEnd, _cageEnd, _cageEnd);
	}

	public BlockRedNetCable(int id)
	{
		super(id, Machine.MATERIAL);

		setUnlocalizedName("mfr.cable.redstone");
		setHardness(0.8F);

		setCreativeTab(MFRCreativeTab.tab);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xOffset, float yOffset, float zOffset)
	{
		PlayerInteractEvent e = new PlayerInteractEvent(player, Action.RIGHT_CLICK_BLOCK, x, y, z, side);
		if (MinecraftForge.EVENT_BUS.post(e) || e.getResult() == Result.DENY || e.useBlock == Result.DENY)
		{
			return false;
		}

		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (te instanceof TileEntityRedNetCable)
		{
			TileEntityRedNetCable cable = (TileEntityRedNetCable)te;

			MovingObjectPosition part = collisionRayTrace(world, x, y, z,
					RayTracer.getStartVec(player), RayTracer.getEndVec(player));
			if (part == null)
				return false;
			
			int subHit = ((ExtendedMOP)part).subHit;
			side = _subSideMappings[subHit];

			ItemStack s = player.inventory.getCurrentItem();
			
			if (cable.onPartHit(player, side, subHit))
			{
				;
			}
			else if (subHit >= (2 + 6 * 2) && subHit < (2 + 6 * 3))
			{
				if (MFRUtil.isHoldingUsableTool(player, x, y, z))
				{
					if (!world.isRemote)
					{
						int nextColor;
						if(!player.isSneaking())
						{
							nextColor = cable.getSideColor(ForgeDirection.getOrientation(side)) + 1;
							if(nextColor > 15) nextColor = 0;
						}
						else
						{
							nextColor = cable.getSideColor(ForgeDirection.getOrientation(side)) - 1;
							if(nextColor < 0) nextColor = 15;
						}
						cable.setSideColor(ForgeDirection.getOrientation(side), nextColor);
						world.markBlockForUpdate(x, y, z);
						return true;
					}
				}
				else if (s != null && s.itemID == MineFactoryReloadedCore.rednetMeterItem.itemID)
				{
					// TODO: move to client-side when forge fixes player.getEyeHeight on client
					if (!world.isRemote)
					{
						// TODO: localize
						player.sendChatToPlayer(new ChatMessageComponent().addText("Side is " + 
								ItemRedNetMeter._colorNames[cable.getSideColor(ForgeDirection.getOrientation(side))]));
					}
				}
				else if (s != null && s.itemID == Item.dyePowder.itemID)
				{
					if (!world.isRemote)
					{
						cable.setSideColor(ForgeDirection.getOrientation(side), 15 - s.getItemDamage());
						world.markBlockForUpdate(x, y, z);
						return true;
					}
				}
			}
			else if (subHit >= 0 && subHit < (2 + 6 * 2))
			{
				l: if (MFRUtil.isHoldingUsableTool(player, x, y, z))
				{
					byte mode = cable.getMode(side);
					mode++;
					if (!world.isRemote)
					{
						if (side == 6)
						{
							if (mode > 1)
								mode = 0;
							cable.setMode(side, mode);
							world.markBlockForUpdate(x, y, z);
							switch (mode)
							{
							case 0:
								player.sendChatToPlayer(new ChatMessageComponent().addKey("chat.info.mfr.rednet.tile.standard"));
								break;
							case 1:
								player.sendChatToPlayer(new ChatMessageComponent().addKey("chat.info.mfr.rednet.tile.cableonly"));
								break;
							default:
							}
							break l;
						}
						if (mode > 3)
						{
							mode = 0;
						}
						cable.setMode(side, mode);
						world.markBlockForUpdate(x, y, z);
						switch (mode)
						{
						case 0:
							player.sendChatToPlayer(new ChatMessageComponent().addKey("chat.info.mfr.rednet.connection.standard"));
							break;
						case 1:
							player.sendChatToPlayer(new ChatMessageComponent().addKey("chat.info.mfr.rednet.connection.forced"));
							break;
						case 2:
							player.sendChatToPlayer(new ChatMessageComponent().addKey("chat.info.mfr.rednet.connection.forcedstrong"));
							break;
						case 3:
							player.sendChatToPlayer(new ChatMessageComponent().addKey("chat.info.mfr.rednet.connection.cableonly"));
							break;
						default:
						}
					}
				}
				else if (s != null && s.itemID == MineFactoryReloadedCore.rednetMeterItem.itemID)
				{
					// TODO: move to client-side when forge fixes player.getEyeHeight on client
					if (!world.isRemote)
					{
						// TODO: localize
						player.sendChatToPlayer(new ChatMessageComponent().addText("Side is " + 
								ItemRedNetMeter._colorNames[cable.getSideColor(ForgeDirection.getOrientation(side))]));
					}
				}
				else if (s != null && s.itemID == Item.dyePowder.itemID)
				{
					if (!world.isRemote)
					{
						cable.setSideColor(ForgeDirection.getOrientation(side), 15 - s.getItemDamage());
						world.markBlockForUpdate(x, y, z);
						return true;
					}
				}
			}
		}
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB collisionTest, List collisionBoxList, Entity entity)
	{
		TileEntity cable = world.getBlockTileEntity(x, y, z);
		if (cable instanceof TileEntityRedNetCable)
		{
			List<IndexedCuboid6> cuboids = new LinkedList<IndexedCuboid6>();
			((TileEntityRedNetCable)cable).addTraceableCuboids(cuboids, false);
			for (IndexedCuboid6 c : cuboids)
			{
				AxisAlignedBB aabb = c.toAABB();
				if (collisionTest.intersectsWith(aabb))
					collisionBoxList.add(aabb);
			}
		}
		else
		{
			super.addCollisionBoxesToList(world, x, y, z, collisionTest, collisionBoxList, entity);
		}
	}
	
	private ThreadLocal<Boolean> trace = new ThreadLocal<Boolean>();

	@Override
	public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 start, Vec3 end)
	{
		List<IndexedCuboid6> cuboids = new LinkedList<IndexedCuboid6>();
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (te instanceof TileEntityRedNetCable)
			((TileEntityRedNetCable)te).addTraceableCuboids(cuboids, true, trace.get() == Boolean.TRUE);
		return RayTracer.instance().rayTraceCuboids(new Vector3(start), new Vector3(end), cuboids, new BlockCoord(x, y, z), this);
	}

	@SideOnly(Side.CLIENT)
	@ForgeSubscribe
	public void onBlockHighlight(DrawBlockHighlightEvent event) {
		MovingObjectPosition mop = event.target;
		if (mop.typeOfHit == EnumMovingObjectType.TILE
				&& event.player.worldObj.getBlockId(mop.blockX, mop.blockY, mop.blockZ) == blockID) {
			MovingObjectPosition part = RayTracer.retraceBlock(event.player.worldObj, event.player, mop.blockX, mop.blockY, mop.blockZ);
			if (part == null)
				return;
			int subHit = ((ExtendedMOP)part).subHit;
			if (subHit < 2 | (subHit >= (2 + 6 * 3) & subHit < (2 + 6 * 5)))
			{ // extended checking for hitboxes behind hitboxes
				trace.set(Boolean.TRUE);
				RayTracer.retraceBlock(event.player.worldObj, event.player, mop.blockX, mop.blockY, mop.blockZ);
				trace.set(null);
			}
		}
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
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId)
	{
		super.onNeighborBlockChange(world, x, y, z, blockId);
		if(blockId == blockID || world.isRemote)
		{
			return;
		}
		RedstoneNetwork.log("Cable block at %d, %d, %d got update from ID %d", x, y, z, blockId);

		TileEntity te = world.getBlockTileEntity(x, y, z);
		if(te instanceof TileEntityRedNetCable)
		{
			((TileEntityRedNetCable)te).onNeighboorChanged();
		}
	}
	
	@Override
	public void onNeighborTileChange(World world, int x, int y, int z, int tileX, int tileY, int tileZ)
    {
		TileEntity te = world.getBlockTileEntity(x, y, z);
		
		if(te instanceof TileEntityRedNetEnergy)
		{
			((TileEntityRedNetEnergy)te).onNeighborTileChange(tileX, tileY, tileZ);
		}
    }

	@Override
	public void breakBlock(World world, int x, int y, int z, int id, int meta)
	{
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if(te instanceof TileEntityRedNetCable)
		{
			if (((TileEntityRedNetCable)te).getNetwork() != null)
				((TileEntityRedNetCable)te).getNetwork().setInvalid();
		}
		super.breakBlock(world, x, y, z, id, meta);
		for(ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
		{
			BlockPosition bp = new BlockPosition(x, y, z);
			bp.orientation = d;
			bp.moveForwards(1);
			if (world.getBlockId(bp.x, bp.y, bp.z) != MineFactoryReloadedCore.rednetCableBlock.blockID)
			{
				world.notifyBlockOfNeighborChange(bp.x, bp.y, bp.z,
						MineFactoryReloadedCore.rednetCableBlock.blockID);
				world.notifyBlocksOfNeighborChange(bp.x, bp.y, bp.z,
						MineFactoryReloadedCore.rednetCableBlock.blockID);
			}
		}
	}

	@Override
	public ItemStack dismantleBlock(EntityPlayer player, World world, int x, int y, int z, boolean returnBlock)
	{
		ItemStack machine = new ItemStack(idDropped(blockID, world.rand, 0), 1,
				damageDropped(world.getBlockMetadata(x, y, z)));
		world.setBlockToAir(x, y, z);
		if (!returnBlock)
			dropBlockAsItem_do(world, x, y, z, machine);
		return machine;
	}

	@Override
	public boolean canDismantle(EntityPlayer player, World world, int x, int y, int z)
	{
		MovingObjectPosition part = collisionRayTrace(world, x, y, z,
				RayTracer.getStartVec(player), RayTracer.getEndVec(player));
		if (part == null)
			return false;
		int subHit = ((ExtendedMOP)part).subHit;
		return subHit < (2 + 6 * 2) | (subHit > (2 + 6 * 3));
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side)
	{
		int power = 0;
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if(te instanceof TileEntityRedNetCable)
		{
			TileEntityRedNetCable cable = ((TileEntityRedNetCable)te);
			RedNetConnectionType state = cable.getConnectionState(ForgeDirection.getOrientation(side).getOpposite());
			if(cable.getNetwork() == null || !state.isConnected | !state.isSingleSubnet)
			{
				return 0;
			}

			int subnet = ((TileEntityRedNetCable)te).getSideColor(ForgeDirection.getOrientation(side).getOpposite());
			power = Math.min(Math.max(((TileEntityRedNetCable)te).getNetwork().getPowerLevelOutput(subnet), 0), 15);
			RedstoneNetwork.log("Asked for weak power at " + x + "," + y + "," + z + ";" + ForgeDirection.getOrientation(side).getOpposite() + " - got " + power + " from network " + ((TileEntityRedNetCable)te).getNetwork().getId() + ":" + subnet);
		}
		return power;
	}

	@Override
	public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side)
	{
		int power = 0;
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if(te instanceof TileEntityRedNetCable)
		{
			TileEntityRedNetCable cable = ((TileEntityRedNetCable)te);
			RedNetConnectionType state = cable.getConnectionState(ForgeDirection.getOrientation(side).getOpposite());
			if(cable.getNetwork() == null || !state.isConnected | !state.isSingleSubnet)
			{
				return 0;
			}

			BlockPosition nodebp = new BlockPosition(x, y, z, ForgeDirection.getOrientation(side).getOpposite());
			nodebp.moveForwards(1);

			int subnet = cable.getSideColor(nodebp.orientation);

			if(cable.getNetwork().isWeakNode(nodebp))
			{
				power = 0;
				RedstoneNetwork.log("Asked for strong power at " + x + "," + y + "," + z + ";" + ForgeDirection.getOrientation(side).getOpposite() + " - weak node, power 0");
			}
			else
			{
				power = Math.min(Math.max(cable.getNetwork().getPowerLevelOutput(subnet), 0), 15);
				RedstoneNetwork.log("Asked for strong power at " + x + "," + y + "," + z + ";" + ForgeDirection.getOrientation(side).getOpposite() + " - got " + power + " from network " + ((TileEntityRedNetCable)te).getNetwork().getId() + ":" + subnet);
			}
		}
		return power;
	}

	@Override
	public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
	{
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (te instanceof TileEntityRedNetCable)
			return ((TileEntityRedNetCable)te).isSolidOnSide(side.ordinal());
		
		return false;
	}

	@Override
	public boolean canProvidePower()
	{
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return null;
	}

	@Override
	public TileEntity createTileEntity(World world, int meta)
	{
		switch (meta)
		{
		default:
		case 0:
			return new TileEntityRedNetCable();
		case 2:
			return new TileEntityRedNetEnergy();
		}
	}

	@Override
	public int getRenderType()
	{
		return MineFactoryReloadedCore.renderIdRedNet;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister ir)
	{
		blockIcon = ir.registerIcon("minefactoryreloaded:" + getUnlocalizedName());
		RedNetCableRenderer.updateUVT(blockIcon);
	}

	@Override
	public void updateNetwork(World world, int x, int y, int z)
	{
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if(te instanceof TileEntityRedNetCable && ((TileEntityRedNetCable)te).getNetwork() != null)
		{
			//((TileEntityRedNetCable)te).getNetwork().updatePowerLevels();
			((TileEntityRedNetCable)te).updateNodes();
		}
	}

	@Override
	public void updateNetwork(World world, int x, int y, int z, int subnet)
	{
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if(te instanceof TileEntityRedNetCable && ((TileEntityRedNetCable)te).getNetwork() != null)
		{
			//((TileEntityRedNetCable)te).getNetwork().updatePowerLevels(subnet);
			((TileEntityRedNetCable)te).updateNodes();
		}
	}

	@Override
	public void getBlockInfo(IBlockAccess world, int x, int y, int z,
			ForgeDirection side, EntityPlayer player, List<String> info, boolean debug)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if (tile instanceof TileEntityRedNetCable)
			((TileEntityRedNetCable)tile).getTileInfo(info, side, player, debug);
	}

	@Override
	public int damageDropped(int i)
	{
		return i;
	}
}
