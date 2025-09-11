package ru.liahim.mist.core.hooks;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import ru.liahim.mist.core.minecraft.HookLoader;
import ru.liahim.mist.core.minecraft.PrimaryClassTransformer;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class MistHookLoader extends HookLoader {

	@Override
	public String[] getASMTransformerClass() {
		return new String[] { PrimaryClassTransformer.class.getName() };
	}

	@Override
	public void registerHooks() {
		registerHookContainer("ru.liahim.mist.core.hooks.AnnotationHooks");
	}
}