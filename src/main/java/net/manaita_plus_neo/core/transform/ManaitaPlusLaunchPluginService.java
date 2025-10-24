package net.manaita_plus_neo.core.transform;

import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ClassInstanceMultiMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.level.entity.EntityTickList;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.EnumSet;



public class ManaitaPlusLaunchPluginService implements ILaunchPluginService {

    private static final String owner = "net/manaita_plus_neo/core/util/EventUtil";
    @Override
    public String name() {
        return "ManaitaLaunchPluginService";
    }

    @Override
    public EnumSet<Phase> handlesClass(Type classType, boolean isEmpty) {
        return EnumSet.of(Phase.AFTER);
    }

    @Override
    public int processClassWithFlags(Phase phase, ClassNode classNode, Type classType, String reason) {
        return ILaunchPluginService.super.processClassWithFlags(phase, classNode, classType, reason);
    }

    @Override
    public boolean processClass(Phase phase, ClassNode classNode, Type classType) {
        if (classNode.name.startsWith("net/manaita_plus_neo")) return false;
        boolean flag = false;
        if ("net/minecraft/client/Minecraft".equals(classNode.name)) {
            for (MethodNode method : classNode.methods) {
                if (method.name.equals("setScreen") && method.desc.equals("(Lnet/minecraft/client/gui/screens/Screen;)V")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,1));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isNotSafe","(Lnet/minecraft/client/gui/screens/Screen;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new InsnNode(Opcodes.RETURN));
                    insnNodes.add(label1);
                    method.instructions.insert(insnNodes);
                    flag = true;
                    break;
                }
            }
        } else if ("net/minecraft/world/level/entity/EntityLookup".equals(classNode.name)) {
            for (MethodNode method : classNode.methods) {
                if (method.name.equals("add") && method.desc.equals("(Lnet/minecraft/world/level/entity/EntityAccess;)V")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    insnNodes.add(new TypeInsnNode(Opcodes.INSTANCEOF, "net/minecraft/world/entity/Entity"));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label1));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    insnNodes.add(new TypeInsnNode(Opcodes.CHECKCAST, "net/minecraft/world/entity/Entity"));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "isRemove", "(Lnet/minecraft/world/entity/Entity;)Z", false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label1));
                    insnNodes.add(new InsnNode(Opcodes.RETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                } else if ((method.name.equals("getEntities") && method.desc.equals("(Lnet/minecraft/world/level/entity/EntityTypeTest;Lnet/minecraft/util/AbortableIterationConsumer;)V")) || (method.name.equals("getAllEntities") && method.desc.equals("()V"))) {
                    InsnList insnNodes = new InsnList();

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new FieldInsnNode(Opcodes.GETFIELD, classNode.name, "byId", "Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;"));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "onIterator", "(Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;)V", false));

                    method.instructions.insert(insnNodes);
                    flag = true;
                }
            }
        } else if ("net/minecraft/world/item/ItemCooldowns".equals(classNode.name)) {
            for (MethodNode method : classNode.methods) {
                if (method.name.equals("isOnCooldown") && method.desc.equals("(Lnet/minecraft/world/item/Item;)Z")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    insnNodes.add(new TypeInsnNode(Opcodes.INSTANCEOF, "net/manaita_plus_neo/item/data/IManaitaPlusKey"));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label1));

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    insnNodes.add(new TypeInsnNode(Opcodes.INSTANCEOF, "net/manaita_plus_neo/item/armor/ManaitaBoots"));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label1));

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    insnNodes.add(new TypeInsnNode(Opcodes.INSTANCEOF, "net/manaita_plus_neo/item/armor/ManaitaChestplate"));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label1));

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    insnNodes.add(new TypeInsnNode(Opcodes.INSTANCEOF, "net/manaita_plus_neo/item/armor/ManaitaHelmet"));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label1));

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    insnNodes.add(new TypeInsnNode(Opcodes.INSTANCEOF, "net/manaita_plus_neo/item/armor/ManaitaLeggings"));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label1));
                    
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new InsnNode(Opcodes.IRETURN));

                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                } else if ((method.name.equals("getEntities") && method.desc.equals("(Lnet/minecraft/world/level/entity/EntityTypeTest;Lnet/minecraft/util/AbortableIterationConsumer;)V")) || (method.name.equals("m_156811_") && method.desc.equals("()V"))) {
                    // 是的，就是在net/minecraft/world/item/ItemCooldowns里面找getEntities，我不知道为什么
                    // TODO 应该可以删掉，但是我不敢
                    InsnList insnNodes = new InsnList();

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new FieldInsnNode(Opcodes.GETFIELD, classNode.name, "byId", "Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;"));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "onIterator", "(Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;)V", false));

                    method.instructions.insert(insnNodes);
                    flag = true;
                }
            }
        } else if ("net/minecraft/util/ClassInstanceMultiMap".equals(classNode.name)) {
            for (MethodNode method : classNode.methods) {
                if ((method.name.equals("iterator") && method.desc.equals("()Ljava/util/Iterator;")) || (method.name.equals("getAllInstances") && method.desc.equals("()Ljava/util/List;"))) {
                    InsnList insnNodes = new InsnList();

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new FieldInsnNode(Opcodes.GETFIELD, classNode.name, "allInstances", "Ljava/util/List;"));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "onIterator", "(Ljava/util/List;)V", false));

                    method.instructions.insert(insnNodes);
                    flag = true;
                } else if (method.name.equals("find") && method.desc.equals("(Ljava/lang/Class;)Ljava/util/Collection;")) {
                    InsnList insnNodes = new InsnList();

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new FieldInsnNode(Opcodes.GETFIELD, classNode.name, "byClass", "Ljava/util/Map;"));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "onFind", "(Ljava/util/Map;)V", false));

                    method.instructions.insert(insnNodes);
                    flag = true;
                }
            }
        } else if ("Lnet/minecraft/world/level/entity/EntityTickList".equals(classNode.name)) {
            for (MethodNode method : classNode.methods) {
                if (method.name.equals("forEach") && method.desc.equals("(Ljava/util/function/Consumer;)V")) {
                    InsnList insnNodes = new InsnList();

                    // 计算局部变量索引 - 确保不会与现有局部变量冲突
                    int baseLocals =  1; // 静态方法没有this参数
                    // 加上方法参数占用的局部变量槽位
                    Type[] argTypes = Type.getArgumentTypes(method.desc);
                    for (Type argType : argTypes) {
                        baseLocals += argType.getSize();
                    }

                    int iterator = baseLocals;
                    int next = baseLocals + 1;
                    method.maxLocals = Math.max(method.maxLocals, next + 1); // 确保maxLocals足够大

                    LabelNode label1 = new LabelNode();
                    LabelNode label2 = new LabelNode();
                    LabelNode label3 = new LabelNode();

                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new FieldInsnNode(Opcodes.GETFIELD, classNode.name, "active", "Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;"));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "it/unimi/dsi/fastutil/ints/Int2ObjectMap", "values", "()Lit/unimi/dsi/fastutil/objects/ObjectCollection;", true));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "it/unimi/dsi/fastutil/objects/ObjectCollection", "iterator", "()Lit/unimi/dsi/fastutil/objects/ObjectIterator;", true));
                    insnNodes.add(new VarInsnNode(Opcodes.ASTORE, iterator));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_APPEND, 1, new Object[]{"it/unimi/dsi/fastutil/objects/ObjectIterator"}, 0, null));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, iterator));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "it/unimi/dsi/fastutil/objects/ObjectIterator", "hasNext", "()Z", true));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label2));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, iterator));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "it/unimi/dsi/fastutil/objects/ObjectIterator", "next", "()Ljava/lang/Object;", true));
                    insnNodes.add(new VarInsnNode(Opcodes.ASTORE, next));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, next));
                    insnNodes.add(new TypeInsnNode(Opcodes.INSTANCEOF, "net/minecraft/world/entity/Entity"));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label3));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, next));
                    insnNodes.add(new TypeInsnNode(Opcodes.CHECKCAST, "net/minecraft/world/entity/Entity"));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, "isRemove", "(Lnet/minecraft/world/entity/Entity;)Z", false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label3));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, iterator));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "it/unimi/dsi/fastutil/objects/ObjectIterator", "remove", "()V", true));
                    insnNodes.add(label3);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    insnNodes.add(new JumpInsnNode(Opcodes.GOTO, label1));
                    insnNodes.add(label2);
                    insnNodes.add(new FrameNode(Opcodes.F_CHOP, 1, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                }
            }
        } else if ("net/minecraft/world/entity/Entity".equals(classNode.name)) {
            for (MethodNode method : classNode.methods) {
                if (method.name.equals("isRemoved") && method.desc.equals("()Z")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    LabelNode label2 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isManaita","(Lnet/minecraft/world/entity/Entity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new InsnNode(Opcodes.IRETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isRemove","(Lnet/minecraft/world/entity/Entity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label2));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_1));
                    insnNodes.add(new InsnNode(Opcodes.IRETURN));
                    insnNodes.add(label2);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                } else if (method.name.equals("getBoundingBoxForCulling")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isRemove","(Lnet/minecraft/world/entity/Entity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new FieldInsnNode(Opcodes.GETSTATIC,classNode.name, "INITIAL_AABB","Lnet/minecraft/world/phys/AABB;"));
                    insnNodes.add(new InsnNode(Opcodes.ARETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                } else if (method.name.equals("blockPosition")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isRemove","(Lnet/minecraft/world/entity/Entity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new FieldInsnNode(Opcodes.GETSTATIC,"net/minecraft/core/BlockPos", "ZERO","Lnet/minecraft/core/BlockPos;"));
                    insnNodes.add(new InsnNode(Opcodes.ARETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                }
            }
        } else if (classNode.name.equals("net/minecraft/world/entity/LivingEntity")) {
            for (MethodNode method : classNode.methods) {
                if (method.name.equals("getAttributeValue")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isManaita","(Lnet/minecraft/world/entity/LivingEntity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,1));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"getAttributeValue","(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/core/Holder;)D",false));
                    insnNodes.add(new InsnNode(Opcodes.DRETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                } else if (method.name.equals("getHealth")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    LabelNode label2 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isManaita","(Lnet/minecraft/world/entity/LivingEntity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,classNode.name,"getMaxHealth","()F",false));
                    insnNodes.add(new InsnNode(Opcodes.FRETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isDead","(Lnet/minecraft/world/entity/LivingEntity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label2));
                    insnNodes.add(new InsnNode(Opcodes.FCONST_0));
                    insnNodes.add(new InsnNode(Opcodes.FRETURN));
                    insnNodes.add(label2);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                } else if (method.name.equals("getMaxHealth")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isManaitaSafe","(Lnet/minecraft/world/entity/LivingEntity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"getMaxHealth","(Lnet/minecraft/world/entity/LivingEntity;)F",false));
                    insnNodes.add(new InsnNode(Opcodes.FRETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                } else if (method.name.equals("die")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isManaita","(Lnet/minecraft/world/entity/LivingEntity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", "hurtDuration", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", "deathTime", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", "hurtTime", "I"));
                    insnNodes.add(new InsnNode(Opcodes.RETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                } else if (method.name.equals("hurt")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isManaita","(Lnet/minecraft/world/entity/LivingEntity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", "hurtDuration", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", "deathTime", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", "hurtTime", "I"));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new InsnNode(Opcodes.IRETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                } else if (method.name.equals("isDeadOrDying")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    LabelNode label2 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isManaita","(Lnet/minecraft/world/entity/LivingEntity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", "hurtDuration", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", "deathTime", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", "hurtTime", "I"));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new InsnNode(Opcodes.IRETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isDead","(Lnet/minecraft/world/entity/LivingEntity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label2));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_1));
                    insnNodes.add(new InsnNode(Opcodes.IRETURN));
                    insnNodes.add(label2);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                } else if (method.name.equals("isAlive")) {
                    InsnList insnNodes = new InsnList();
                    LabelNode label1 = new LabelNode();
                    LabelNode label2 = new LabelNode();
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isManaita","(Lnet/minecraft/world/entity/LivingEntity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label1));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", "hurtDuration", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", "deathTime", "I"));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/entity/LivingEntity", "hurtTime", "I"));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_1));
                    insnNodes.add(new InsnNode(Opcodes.IRETURN));
                    insnNodes.add(label1);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    insnNodes.add(new VarInsnNode(Opcodes.ALOAD,0));
                    insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC,owner,"isDead","(Lnet/minecraft/world/entity/LivingEntity;)Z",false));
                    insnNodes.add(new JumpInsnNode(Opcodes.IFEQ,label2));
                    insnNodes.add(new InsnNode(Opcodes.ICONST_0));
                    insnNodes.add(new InsnNode(Opcodes.IRETURN));
                    insnNodes.add(label2);
                    insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    method.instructions.insert(insnNodes);
                    flag = true;
                }
            }
        }
        return flag;
    }



    public static String getMethodDescriptor(final Method method) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('(');
        Class<?>[] parameters = method.getParameterTypes();
        for (Class<?> parameter : parameters) {
            appendDescriptor(parameter, stringBuilder);
        }
        appendDescriptor(String.class,stringBuilder);
        stringBuilder.append(')');
        appendDescriptor(method.getReturnType(), stringBuilder);
        return stringBuilder.toString();
    }

    private static void appendDescriptor(final Class<?> clazz, final StringBuilder stringBuilder) {
        Class<?> currentClass = clazz;
        while (currentClass.isArray()) {
            stringBuilder.append('[');
            currentClass = currentClass.getComponentType();
        }
        if (currentClass.isPrimitive()) {
            char descriptor;
            if (currentClass == Integer.TYPE) {
                descriptor = 'I';
            } else if (currentClass == Void.TYPE) {
                descriptor = 'V';
            } else if (currentClass == Boolean.TYPE) {
                descriptor = 'Z';
            } else if (currentClass == Byte.TYPE) {
                descriptor = 'B';
            } else if (currentClass == Character.TYPE) {
                descriptor = 'C';
            } else if (currentClass == Short.TYPE) {
                descriptor = 'S';
            } else if (currentClass == Double.TYPE) {
                descriptor = 'D';
            } else if (currentClass == Float.TYPE) {
                descriptor = 'F';
            } else if (currentClass == Long.TYPE) {
                descriptor = 'J';
            } else {
                throw new AssertionError();
            }
            stringBuilder.append(descriptor);
        } else {
            stringBuilder.append('L').append(Type.getInternalName(currentClass)).append(';');
        }
    }
}
