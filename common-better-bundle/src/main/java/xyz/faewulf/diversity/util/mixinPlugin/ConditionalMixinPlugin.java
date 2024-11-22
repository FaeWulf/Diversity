package xyz.faewulf.diversity.util.mixinPlugin;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;
import xyz.faewulf.diversity.util.config.Config;

import java.util.List;
import java.util.Set;

public class ConditionalMixinPlugin implements IMixinConfigPlugin {

    @SuppressWarnings("unchecked")
    private static <T> T getAnnotationValue(AnnotationNode annotation, String key, T defaultValue) {
        if (annotation.values == null) return defaultValue;

        for (int i = 0; i < annotation.values.size(); i += 2) {
            if (key.equals(annotation.values.get(i))) {
                return (T) annotation.values.get(i + 1);
            }
        }

        return defaultValue;
    }

    @Override
    public void onLoad(String mixinPackage) {
        //load mod config
        Config.init();
    }

    @Override
    public String getRefMapperConfig() {
        return null; // No reference mapper needed
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        try {
            // Load the mixin class and check if it has the @ConditionalMixin annotation
            ClassNode mixinClass = MixinService.getService().getBytecodeProvider().getClassNode(mixinClassName);

            if (mixinClass.visibleAnnotations != null) {
                for (AnnotationNode node : mixinClass.visibleAnnotations) {

                    String fieldName = getAnnotationValue(node, "fieldName", null);

                    Type asmTypeClass = getAnnotationValue(node, "configClass", null);

                    //if not, then don't process, mixin will be process by default's mixin config setting
                    if (asmTypeClass == null || fieldName == null)
                        return true;

                    Class<?> configClass = Class.forName(asmTypeClass.getClassName());

                    boolean fieldValue = configClass.getDeclaredField(fieldName).getBoolean(null);

                    //System.out.println(fieldName + ": " + fieldValue);

                    // Enable or disable the Mixin based on the field value
                    return fieldValue;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Default to true if no annotation is present or an error occurs
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
        // No action needed for accepting targets
    }

    @Override
    public List<String> getMixins() {
        return null; // Return null if no modifications to the mixin list are needed
    }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }
}
