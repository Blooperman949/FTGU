package bluper.ftgu.data.recipes;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import bluper.ftgu.data.DataUtils;
import bluper.ftgu.gui.FTGUCraftingInventory;
import bluper.ftgu.registry.registries.FTGURecipeTypes;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.block.Block;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * A shapeless recipe that requires a surface {@code Block} for crafting.
 * @author Bluperman949
 */
public class ShapelessSurfaceRecipe implements IFTGUCraftingRecipe {
	private final ResourceLocation id;
	private final String group;
	private final ItemStack result;
	private final NonNullList<Ingredient> ingredients;
	private final boolean isSimple;
	private final Predicate<Block> surfaceTest;
	private final String surfaceTestLocation;

	ShapelessSurfaceRecipe(ResourceLocation id, String group, ItemStack result, NonNullList<Ingredient> ingredients, Predicate<Block> surfaceTest, String surfaceTestLocation) {
		this.id = id;
		this.group = group;
		this.result = result;
		this.ingredients = ingredients;
		this.isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
		this.surfaceTest = surfaceTest;
		this.surfaceTestLocation = surfaceTestLocation;
	}

	@Override
	public String getSurfaceTestLocation() {
		return surfaceTestLocation;
	}

	@Override
	public ItemStack getOutput() {
		return result;
	}

	public ResourceLocation getId() {
		return this.id;
	}

	public IRecipeSerializer<?> getSerializer() {
		return FTGURecipeTypes.SHAPELESS_SURFACE_SERIALIZER.get();
	}

	public String getGroup() {
		return this.group;
	}

	public ItemStack getResultItem() {
		return this.result;
	}

	public NonNullList<Ingredient> getIngredients() {
		return this.ingredients;
	}

	public boolean matches(FTGUCraftingInventory inv, World world) {
		RecipeItemHelper recipeItemHelper = new RecipeItemHelper();
		List<ItemStack> inputs = new ArrayList<>();
		int i = 0;
		for (int j = 0; j < inv.getContainerSize(); ++j) {
			ItemStack itemStack = inv.getItem(j);
			if (!itemStack.isEmpty()) {
				++i;
				if (isSimple)
					recipeItemHelper.accountStack(itemStack, 1);
				else
					inputs.add(itemStack);
			}
		}
		return i == this.ingredients.size() && (isSimple ? recipeItemHelper.canCraft(this, (IntList) null) : RecipeMatcher.findMatches(inputs, this.ingredients) != null) && surfaceTest.test(inv.getSurface());
	}

	public ItemStack assemble(CraftingInventory inv) {
		return this.result.copy();
	}

	public boolean canCraftInDimensions(int w, int h) {
		return w * h >= this.ingredients.size();
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ShapelessSurfaceRecipe> {
		public ShapelessSurfaceRecipe fromJson(ResourceLocation rl, JsonObject json) {
			String group = JSONUtils.getAsString(json, "group", "");
			NonNullList<Ingredient> ingredients = itemsFromJson(JSONUtils.getAsJsonArray(json, "ingredients"));
			if (ingredients.isEmpty())
				throw new JsonParseException("No ingredients for shapeless recipe");
			else if (ingredients.size() > ShapedSurfaceRecipe.MAX_WIDTH * ShapedSurfaceRecipe.MAX_HEIGHT)
				throw new JsonParseException("Too many ingredients for shapeless recipe the max is " + (ShapedSurfaceRecipe.MAX_WIDTH * ShapedSurfaceRecipe.MAX_HEIGHT));
			else {
				ItemStack result = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
				String surfaceTestLocation = JSONUtils.getAsString(json, "surface", "");
				Predicate<Block> surfaceTest = DataUtils.createSurfaceTest(surfaceTestLocation);
				return new ShapelessSurfaceRecipe(rl, group, result, ingredients, surfaceTest, surfaceTestLocation);
			}
		}

		private static NonNullList<Ingredient> itemsFromJson(JsonArray json) {
			NonNullList<Ingredient> ingredients = NonNullList.create();
			for (int i = 0; i < json.size(); ++i) {
				Ingredient ing = Ingredient.fromJson(json.get(i));
				if (!ing.isEmpty()) {
					ingredients.add(ing);
				}
			}
			return ingredients;
		}

		public ShapelessSurfaceRecipe fromNetwork(ResourceLocation rl, PacketBuffer buf) {
			String group = buf.readUtf(32767);
			int size = buf.readVarInt();
			NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);
			for (int i = 0; i < ingredients.size(); ++i) {
				ingredients.set(i, Ingredient.fromNetwork(buf));
			}
			ItemStack result = buf.readItem();
			String surfaceTestLocation = (String) buf.readCharSequence(0, Charset.defaultCharset());
			Predicate<Block> surfaceTest = DataUtils.createSurfaceTest(surfaceTestLocation);
			return new ShapelessSurfaceRecipe(rl, group, result, ingredients, surfaceTest, surfaceTestLocation);
		}

		public void toNetwork(PacketBuffer buf, ShapelessSurfaceRecipe recipe) {
			buf.writeUtf(recipe.group);
			buf.writeVarInt(recipe.ingredients.size());
			for (Ingredient ingredient : recipe.ingredients) {
				ingredient.toNetwork(buf);
			}
			buf.writeItem(recipe.result);
			buf.writeCharSequence(recipe.surfaceTestLocation, Charset.defaultCharset());
		}
	}

	@Override
	public ItemStack assemble(FTGUCraftingInventory inv) {
		return result.copy();
	}
}
