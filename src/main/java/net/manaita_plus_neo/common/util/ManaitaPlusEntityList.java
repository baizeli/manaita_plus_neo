package net.manaita_plus_neo.common.util;

import net.minecraft.world.entity.Entity;

public enum ManaitaPlusEntityList {
    manaita,
    death,
    remove;

    public static final String cName = "manaita_plus_legacy:type";

    ManaitaPlusEntityList() {
        flag = pow(2,ordinal());
        name = name();
    }

    public static int pow(int a, int b) {
        if(b == 0) return 1;
        int pow = 1;
        for (int i = 0; i < b; i++) {
            pow *= a;
        }
        return pow;
    }


    private final String name;
    private final int flag;

    public void add(Entity entity) {
        if (entity == null)
            return;
        entity.addTag(name);

        if ((entity.getPersistentData().getInt(cName) & flag) == 0)
            entity.getPersistentData().putInt(cName, flag);
//        if (entity instanceof LivingEntity livingEntity) {
//            AttributeInstance attribute = livingEntity.getAttribute(ManaitaPlusAttributeCore.Type.get());
//
//            if (attribute != null) {
//                int baseValue = (int) attribute.getBaseValue();
//                if ((baseValue & flag) == 0) {
//                    attribute.setBaseValue(baseValue | flag);
//                }
//            }
//        }
    }

    public void remove(Entity entity) {
        if (entity == null) return;
        entity.removeTag(name);

        entity.getPersistentData().putInt(cName,0);
//        if (entity instanceof LivingEntity livingEntity && livingEntity.getAttributes().hasAttribute(ManaitaPlusAttributeCore.Type.get())) {
//            try {
//                AttributeInstance attribute = livingEntity.getAttribute(ManaitaPlusAttributeCore.Type.get());
//                if (attribute != null) {
//                    int baseValue = (int) attribute.getBaseValue();
//                    if ((baseValue & flag) != 0) {
//                        attribute.setBaseValue(baseValue - flag);
//                    }
//                }
//            } catch (RuntimeException e) {
//            }
//        }
    }


    public boolean accept(Entity entity) {
        if (entity == null) return false;
        if(entity.getTags().contains(name) || (entity.getPersistentData().getInt(cName) & flag) != 0) return true;

//        if (entity instanceof LivingEntity livingEntity) {
//            return livingEntity.getAttributes() != null && livingEntity.getAttributes().hasAttribute(ManaitaPlusAttributeCore.Type.get()) && (((int) livingEntity.getAttribute(ManaitaPlusAttributeCore.Type.get()).getBaseValue()) & this.flag) != 0;
//        }
        return false;
    }

    public boolean acceptSafe(Entity entity) {
        if (entity == null) return false;
        try {
            return entity.getTags().contains(name) || (entity.getPersistentData().getInt(cName) & flag) != 0;
        } catch (Exception ex) {
            return false;
        }
    }
}