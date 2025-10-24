package net.manaita_plus_neo.core.util;

import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.ModuleLayerHandler;
import cpw.mods.modlauncher.api.NamedPath;
import net.neoforged.fml.loading.ModDirTransformerDiscoverer;
import sun.misc.Unsafe;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.module.ResolvedModule;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Helper {
    public static final Unsafe UNSAFE;
    private static final MethodHandles.Lookup lookup;
    private static final Object internalUNSAFE;
    private static MethodHandle objectFieldOffsetInternal;

    static {
        UNSAFE = getUnsafe();
        lookup = getFieldValue(MethodHandles.Lookup.class, "IMPL_LOOKUP", MethodHandles.Lookup.class);
        internalUNSAFE = getInternalUNSAFE();
        try {
            Class<?> internalUNSAFEClass = lookup.findClass("jdk.internal.misc.Unsafe");
            objectFieldOffsetInternal = lookup.findVirtual(internalUNSAFEClass, "objectFieldOffset", MethodType.methodType(long.class, Field.class)).bindTo(internalUNSAFE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Unsafe getUnsafe() {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            return (Unsafe) theUnsafe.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Object getInternalUNSAFE() {
        try {
            Class<?> clazz = lookup.findClass("jdk.internal.misc.Unsafe");
            return lookup.findStatic(clazz, "getUnsafe", MethodType.methodType(clazz)).invoke();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Field f, Object target, Class<T> clazz) {
        try {
            long offset;
            if (Modifier.isStatic(f.getModifiers())) {
                target = UNSAFE.staticFieldBase(f);
                offset = UNSAFE.staticFieldOffset(f);
            } else offset = objectFieldOffset(f);
            return (T) UNSAFE.getObject(target, offset);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long objectFieldOffset(Field f) {
        try {
            return UNSAFE.objectFieldOffset(f);
        } catch (Throwable e) {
            try {
                return (long) objectFieldOffsetInternal.invoke(f);
            } catch (Throwable t1) {
                t1.printStackTrace();
            }
        }
        return 0L;
    }

    public static <T> T getFieldValue(Object target, String fieldName, Class<T> clazz) {
        try {
            return getFieldValue(target.getClass().getDeclaredField(fieldName), target, clazz);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T getFieldValue(Class<?> target, String fieldName, Class<T> clazz) {
        try {
            return getFieldValue(target.getDeclaredField(fieldName), (Object) null, clazz);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setFieldValue(Object target, Class<?> value) {
        try {
            int aVolatile = UNSAFE.getIntVolatile(UNSAFE.allocateInstance(value), 8L);
            UNSAFE.putIntVolatile(target,8L,aVolatile);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void setFieldValue(Object target, String fieldName, Object value) {
        try {
            setFieldValue(target.getClass().getDeclaredField(fieldName), target, value);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    public static void setFieldValue(Field f, Object target, Object value) {
        try {
            long offset;
            if (Modifier.isStatic(f.getModifiers())) {
                target = UNSAFE.staticFieldBase(f);
                offset = UNSAFE.staticFieldOffset(f);
            } else offset = objectFieldOffset(f);
            UNSAFE.putObject(target, offset, value);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static String getJarPath(Class<?> clazz) {
        String file = clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (!file.isEmpty()) {
            if (file.startsWith("union:"))
                file = file.substring(6);
            if (file.startsWith("/"))
                file = file.substring(1);
            file = file.substring(0, file.lastIndexOf(".jar") + 4);
            file = file.replaceAll("/", "\\\\");
        }
        return URLDecoder.decode(file, StandardCharsets.UTF_8);
    }

    @SuppressWarnings({"ConstantConditions", "unchecked", "rawtypes"})
    public static void coexistenceCoreAndMod() {
        List<NamedPath> found = Helper.getFieldValue(ModDirTransformerDiscoverer.class, "found", List.class);
        found.removeIf(namedPath -> Helper.getJarPath(Helper.class).equals(namedPath.paths()[0].toString()));

        Helper.getFieldValue(Helper.getFieldValue(Launcher.INSTANCE, "moduleLayerHandler", ModuleLayerHandler.class), "completedLayers", EnumMap.class).values().forEach(layerInfo -> {
            ModuleLayer layer = Helper.getFieldValue(layerInfo, "layer", ModuleLayer.class);

            layer.modules().forEach(module -> {
                if (module.getName().equals(Helper.class.getModule().getName())) {
                    Set<ResolvedModule> modules = new HashSet<>(Helper.getFieldValue(layer.configuration(), "modules", Set.class));
                    Map<String, ResolvedModule> nameToModule = new HashMap(Helper.getFieldValue(layer.configuration(), "nameToModule", Map.class));

                    modules.remove(nameToModule.remove(Helper.class.getModule().getName()));

                    Helper.setFieldValue(layer.configuration(), "modules", modules);
                    Helper.setFieldValue(layer.configuration(), "nameToModule", nameToModule);
                }
            });
        });
    }

    public static boolean checkClass(Object o) {
        return o.getClass().getName().startsWith("net.minecraft.");
    }

    public static void copyProperties(Class<?> clazz, Object source, Object target) {
        try {
            Field[] fields = clazz.getDeclaredFields();
            AccessibleObject.setAccessible(fields, true);

            for (Field field : fields) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    field.set(target, field.get(source));
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printAllMembers(Class<?> clazz) {
        if (clazz == null) return;
        printAllFields(clazz);
        printAllMethods(clazz);
    }

    public static void printAllFields(Class<?> clazz) {
        if (clazz == null) return;
        System.out.println("【Class】 " + clazz.getName());
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            Field[] fs = c.getDeclaredFields();
            if (fs.length == 0) continue;
            System.out.println("  继承自 " + c.getSimpleName() + "：");
            for (Field f : fs) {
                f.setAccessible(true);
                int mod = f.getModifiers();
                System.out.printf("    %s %s %s %s = %s%n",
                        Modifier.toString(mod),
                        f.getType().getSimpleName(),
                        f.getName(),
                        isStatic(mod) ? "(static)" : "",
                        valueToString(f, mod));
            }
        }
    }

    public static void printAllMethods(Class<?> clazz) {
        if (clazz == null) return;
        System.out.println("\n【Methods】");
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            Method[] ms = c.getDeclaredMethods();
            if (ms.length == 0) continue;
            System.out.println("  继承自 " + c.getSimpleName() + "：");
            Arrays.stream(ms)
                    .sorted((m1, m2) -> m1.getName().compareTo(m2.getName()))
                    .forEach(m -> {
                        m.setAccessible(true);
                        int mod = m.getModifiers();
                        System.out.printf("    %s %s %s(%s)%s%n",
                                Modifier.toString(mod),
                                m.getReturnType().getSimpleName(),
                                m.getName(),
                                parametersToString(m.getParameterTypes()),
                                isStatic(mod) ? " (static)" : "");
                    });
        }
    }

    private static boolean isStatic(int mod) {
        return Modifier.isStatic(mod);
    }

    private static String valueToString(Field f, int mod) {
        try {
            return isStatic(mod) ? String.valueOf(f.get(null)) : "<non-static>";
        } catch (IllegalAccessException e) {
            return "<access error>";
        }
    }

    private static String parametersToString(Class<?>[] pts) {
        if (pts.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pts.length; i++) {
            sb.append(pts[i].getSimpleName()).append(" arg").append(i);
            if (i < pts.length - 1) sb.append(", ");
        }
        return sb.toString();
    }
}
