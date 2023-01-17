package bluper.ftgu;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.FileUtils;

public class FTGUConfig
{
	public static void register()
	{
		FileUtils.getOrCreateDirectory(FMLPaths.CONFIGDIR.get().resolve(FTGU.MODID), FTGU.MODID);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_SPEC, FTGU.MODID + "/common.toml");
	}

	private static final ForgeConfigSpec COMMON_SPEC;
	static
	{
		ForgeConfigSpec.Builder common = new ForgeConfigSpec.Builder();
		COMMON_SPEC = common.build();
	}
}
