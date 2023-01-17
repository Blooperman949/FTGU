package bluper.ftgu.data.assembly;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;

public interface IAssembly {
	public BlockState getBase();

	public Item getAddition();

	public BlockState getResult();

	@Nullable
	public Item getPop();

	public SoundEvent getSound();

	public default String debugString() {
		String str = '{' + getBase().toString() + '+' + getAddition().getRegistryName() + '=' + getResult().toString();
		if (getPop() != null)
			str += ',' + getPop().getRegistryName().toString();
		return str + '}';
	}
}
