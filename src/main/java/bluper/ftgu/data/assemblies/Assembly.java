package bluper.ftgu.data.assemblies;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

/**
 * Implementation of {@link IAssembly}.
 * @author Bluperman949
 */
public class Assembly implements IAssembly {
	BlockState base;
	Item addition;
	BlockState result;
	Item pop;
	SoundEvent sound;

	Assembly(BlockState base, Item addition, BlockState result, @Nullable Item pop, SoundEvent sound) {
		this.base = base;
		this.addition = addition;
		this.result = result;
		this.pop = pop;
		this.sound = sound == null ? SoundEvents.STONE_PLACE : sound;
	}

	@Override
	public BlockState getBase() {
		return base;
	}

	@Override
	public Item getAddition() {
		return addition;
	}

	@Override
	public BlockState getResult() {
		return result;
	}

	@Override
	public Item getPop() {
		return pop;
	}

	@Override
	public SoundEvent getSound() {
		return sound;
	}
}
