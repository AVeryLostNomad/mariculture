package com.thelostnomad.tone.util;

import com.google.common.cache.LoadingCache;
import com.thelostnomad.tone.ThingsOfNaturalEnergies;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;

import java.lang.reflect.*;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobUtil {

    public static List<EntityLiving> mobsForItemLoot(World w, List<ItemStack> lootItems){
        List<ResourceLocation> allKnownEntitiesNow = new ArrayList<ResourceLocation>(EntityList.getEntityNameList());
        List<Class<? extends EntityLiving>> livingEntityClasses = new ArrayList<>();
        for(ResourceLocation rl : allKnownEntitiesNow){
            Class<? extends Entity> e = EntityList.getClassFromName(rl.toString());
            if(e == null || e.getName() == null) continue;
            if(EntityLiving.class.isAssignableFrom(e)){
                // An entity living
                livingEntityClasses.add((Class<? extends EntityLiving>) e);
            }
        }

        Map<EntityLiving,ResourceLocation> lootTableLocations = new HashMap<>();
        for(Class<? extends EntityLiving> e : livingEntityClasses){
            Method m = null;
            try {
                for (Method methodCandidate : e.getDeclaredMethods()) {
                    if (methodCandidate.getName().toLowerCase().contains("loot")) {
                        m = methodCandidate;
                    }
                }
                if (m == null) {
                    continue;
                }
                EntityLiving el = (EntityLiving) e.getConstructor(World.class).newInstance(w);

                m.setAccessible(true);
                ResourceLocation r = (ResourceLocation) m.invoke(el);
                if (r != null) {
                    lootTableLocations.put(el, r);
                }
            } catch(SecurityException se){
                continue;
            } catch (IllegalAccessException e1) {
                continue;
            } catch (InvocationTargetException e1) {
                e1.printStackTrace();
            } catch (InstantiationException e1) {
                e1.printStackTrace();
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
            }
        }

        Map<ResourceLocation, LootTable> resourceLootMap = new HashMap<>();

        LootTableManager ltm = w.getLootTableManager();

        List<EntityLiving> toReturn = new ArrayList<>();
        for(Map.Entry<EntityLiving, ResourceLocation> e : lootTableLocations.entrySet()){
            LootTable lt = ltm.getLootTableFromLocation(e.getValue());
            List<ItemStack> possibleLoot = new ArrayList<ItemStack>();
            for(int i = 0; i < 3; i++){
                possibleLoot.addAll(lt.generateLootForPools(w.rand, new LootContext(100f, (WorldServer) w, ltm, null, null, null)));
            }
            for(ItemStack i : lootItems){
                for(ItemStack s : possibleLoot){
                    if(i.isItemEqual(s)){
                        toReturn.add(e.getKey());
                        break;
                    }
                }
            }
        }

        return toReturn;
    }

}
