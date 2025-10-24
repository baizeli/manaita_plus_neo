package net.manaita_plus_neo.common.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.LockHelper;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.IntFunction;

import static org.objectweb.asm.Opcodes.*;

public class ManaitaPlusClassLoaderFactory {
    private static final ASMClassLoader LOADER = new ASMClassLoader();
    //private static final HashMap<Class<?>, Class<?>> a = new HashMap<>();
    private static final LockHelper<Class<?>, Class<?>> cache = LockHelper.withHashMap();
//    private static final TransformingClassLoader classLoader;
//
//    static {
//        classLoader = Helper.getFieldValue(Launcher.INSTANCE, "classLoader", TransformingClassLoader.class);
//    }

    public static Class<?> createWrapper(Class<?> callback) {
//        if (Modifier.isFinal(callback.getModifiers()))
//            return null;
        if (!Entity.class.isAssignableFrom(callback) || Player.class.isAssignableFrom(callback)) return null;
        if (callback.getName().endsWith("Manaita")) return null;
//        ManaitaPlus.LOGGER.info("ManaitaPlusClassLoaderFactory.createWrapper: " + callback.getName());
        try {
            return cache.computeIfAbsent(callback, (e) -> {
                var node = new ClassNode();
                transformNode(node, callback);
                return node;
            }, ManaitaPlusClassLoaderFactory::defineClass);
        } catch (Exception e) {
            cache.computeIfAbsent(callback, (notUsed) -> null);
            return null;
        }
    }

    private static Class<?> defineClass(ClassNode node) {
        var cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        node.accept(cw);
        return LOADER.define(node.name.replace('/', '.'), cw.toByteArray());
    }


    private static void transformNode(ClassNode target,Class<?> superClass) {
        MethodVisitor mv;

        String replace = superClass.getName().replace(".", "/");
        target.visit(V16, ACC_PUBLIC | ACC_SUPER | ACC_FINAL, replace + "Manaita", null, replace, null);

        target.visitSource(".dynamic", null);

//        List<Method> initialMethod = new ArrayList<>();
//        Class<?> superClass1 = superClass.getSuperclass();

//        for (Method method : superClass1.getMethods()) {
//            if (method.getName().equals("<init>") && (Modifier.isPublic(method.getModifiers()) || Modifier.isProtected(method.getModifiers()))) {
//                initialMethod.add(method);
//            }
//        }
//        if (!initialMethod.isEmpty()) {
//            for (Method method : initialMethod) {
//                mv = target.visitMethod(method.getModifiers(), "<init>", getDesc(method), null, null);
//                mv.visitCode();
//                mv.visitMethodInsn(INVOKESPECIAL, "<init>", superClass1.getName().replace(",","/"), getDesc(method), false);
//                put(method, mv);
//                mv.visitInsn(RETURN);
//                mv.visitEnd();
//            }

        Set<String> finalMethod = new HashSet<>();
        do {
            for (Method method : superClass.getMethods()) {
                String descriptor = getDesc(method);
                if (finalMethod.contains(method.getName() + descriptor)) {
                    continue;
                }
                Type returnType = Type.getReturnType(method);
                int sort = returnType.getSort();
                if (true) {
                    int modifiers = method.getModifiers();
                    if (Modifier.isFinal(modifiers)) {
                        finalMethod.add(method.getName() + descriptor);
                        continue;
                    }
                    if (!Modifier.isStatic(modifiers) && !Modifier.isInterface(modifiers) && (Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers))) {
                        finalMethod.add(method.getName() + descriptor);
                        String name = method.getName().replace(".", "/");


//                        System.err.println(Modifier.toString(modifiers) + " " + name + descriptor);
                        mv = target.visitMethod(modifiers, name, descriptor, null, null);
                        mv.visitCode();
                        mv.visitFieldInsn(GETSTATIC, "sen/manaita_plus_legacy/common/util/ManaitaEntityList", "death", "Lsen/manaita_plus/util/ManaitaEntityList;");
                        mv.visitVarInsn(ALOAD, 0);
                        mv.visitMethodInsn(INVOKEVIRTUAL, "sen/manaita_plus_legacy/common/util/ManaitaEntityList", "accept", "(Lnet/minecraft/world/entity/Entity;)Z", false);
                        Label labelNode = new Label();
                        mv.visitJumpInsn(IFEQ, labelNode);
                        if (sort == 0) {
                            mv.visitInsn(RETURN);
                        } else if (sort == 1) {
                            mv.visitInsn(ICONST_1);
                            mv.visitInsn(I2B);
                            mv.visitInsn(IRETURN);
                        } else if (sort == 2) {
                            mv.visitInsn(ICONST_0);
                            mv.visitInsn(I2C);
                            mv.visitInsn(IRETURN);
                        } else if (sort == 3) {
                            mv.visitInsn(ICONST_0);
                            mv.visitInsn(I2B);
                            mv.visitInsn(IRETURN);
                        } else if (sort == 4) {
                            mv.visitInsn(ICONST_0);
                            mv.visitInsn(I2S);
                            mv.visitInsn(IRETURN);
                        } else if (sort == 5) {
                            mv.visitInsn(ICONST_0);
                            mv.visitInsn(IRETURN);
                        } else if (sort == 6) {
                            mv.visitInsn(FCONST_0);
                            mv.visitInsn(FRETURN);
                        } else if (sort == 7) {
                            mv.visitInsn(LCONST_0);
                            mv.visitInsn(LRETURN);
                        } else if (sort == 8) {
                            mv.visitInsn(DCONST_0);
                            mv.visitInsn(DRETURN);
                        } else if (sort == 9) {
                            mv.visitInsn(ACONST_NULL);
                            mv.visitInsn(ARETURN);
                        } else if (sort == 10) {
                            Type elementType = returnType.getElementType();
                            int sort1 = elementType.getSort();
                            if (sort1 >= 1 && sort1 <= 8) {
                                mv.visitInsn(ICONST_0);
                                mv.visitIntInsn(NEWARRAY, getOpcode(sort1, T_INT));
                                mv.visitInsn(ARETURN);
                            } else if (sort1 == 9) {
                                mv.visitInsn(ICONST_0);
                                mv.visitTypeInsn(ANEWARRAY, elementType.getDescriptor());
                                mv.visitInsn(ARETURN);
                            }
                        }
                        mv.visitLabel(labelNode);

                        put(method, mv);
                        mv.visitMethodInsn(INVOKESPECIAL, superClass.getName().replace(".", "/"), name, descriptor, false);
                        mv.visitInsn(RETURN);
                        mv.visitEnd();
                    }
                }
            }
            superClass = superClass.getSuperclass();
        } while (superClass.getSuperclass() != Object.class);
//        }
        target.visitEnd();
    }

    public static int getOpcode(int sort,int opcode) {
        return switch (sort) {
            case 1 -> opcode - 6;
            case 2 -> opcode - 5;
            case 3 -> opcode - 2;
            case 4 -> opcode - 1;
            case 5 -> opcode;
            case 6 -> opcode - 4;
            case 7 -> opcode + 1;
            case 8 -> opcode - 3;
            default -> throw new UnsupportedOperationException();
        };
    }

    private static boolean equals(Method method1,Method method2) {
        return method1.getName().equals(method2.getName()) && getDesc(method1).equals(getDesc(method2));
    }

    private static boolean equals(String[] s1,String[] s2) {
        return s1[0].equals(s2[0]) && s1[1].equals(s2[1]);
    }

    private static boolean equals(String name,String desc, MethodNode methodNode) {
        return name.equals(methodNode.name) && desc.equals(methodNode.desc);
    }

    private static void put(final Method method,MethodVisitor mv) {
        Class<?>[] classes = method.getParameterTypes();
        int i1 = 0;
        if (Modifier.isStatic(method.getModifiers())) --i1;
        for (int i = classes.length - 1; i >= 0; --i) {
            Type type = Type.getType(classes[i]);
            i1 += type.getSize();
            mv.visitVarInsn(type.getOpcode(ILOAD), i1);
        }
    }

    private static String getDesc(final Method method) {
        Class<?>[] classes = method.getParameterTypes();
        StringBuilder sb = new StringBuilder();
        Type returnType =  Type.getReturnType(method);
        sb.append("(");
        for (int i = classes.length - 1; i >= 0; --i) {
            Type type = Type.getType(classes[i]);
            if (type.getSort() == 4) sb.append("[");
            sb.append(type.getDescriptor());
        }
        sb.append(")");
        if (returnType.getSort() == 5) sb.append("[");
        sb.append(returnType.getDescriptor());
        return sb.toString();
    }

    private static class ASMClassLoader extends ClassLoader {
        private ASMClassLoader() {
            super(null);
        }

        @Override
        protected Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
            return Class.forName(name, resolve, Thread.currentThread().getContextClassLoader());
        }

        Class<?> define(String name, byte[] data) {
            return defineClass(name, data, 0, data.length);
        }
    }

}
