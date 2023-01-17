package bluper.ftgu.data.assemblies;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;

/**
 * Assembly counterpart of {@link IRecipe}. Should not be instantiated from anywhere outside the {@link AssemblyManager}. </p>
 * Assemblies are recipes that take place in the world instead of in a container. Two {@code BlockState}s, an input and an output, called a "base" and a "result".
 * They take an {@code Item} as a secondary input, called an "addition". </p>
 * Assemblies are performed in-world by right-clicking the addition on the base, transforming the base into the result. </p>
 * @author Bluperman949
 */
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
