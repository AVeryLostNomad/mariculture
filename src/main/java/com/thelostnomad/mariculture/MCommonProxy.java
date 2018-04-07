package com.thelostnomad.mariculture;

import com.thelostnomad.mariculture.modules.ModuleManager;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class MCommonProxy {

    public void load(String stage) {
        //Continue
        for (Class c : ModuleManager.enabled.values()) {
            try { //Attempt to load, and display errors if it fails
                c.getMethod(stage).invoke(null);
            } catch (NoSuchMethodException nsme) {}
            catch (Exception e) {
                e.printStackTrace();
            }

            //Attempt to load client side only
            if (isClient()) {
                try { //Attempt to load client, and display errors if it fails
                    c.getMethod(stage + "Client").invoke(null);
                } catch (NoSuchMethodException nsme) {/**/}
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void passEvent(String stage, Object ... args){
        for (Class c : ModuleManager.enabled.values()) {
            try { //Attempt to load, and display errors if it fails
                Class[] classParameters = new Class[args.length];
                int index = 0;
                for(Object o : args){
                    classParameters[index] = o.getClass();
                    index++;
                }
                c.getMethod(stage, classParameters).invoke(null, args);
            } catch (NoSuchMethodException nsme) {}
            catch (Exception e) {
                e.printStackTrace();
            }

            //Attempt to load client side only
            if (isClient()) {
                try { //Attempt to load client, and display errors if it fails
                    c.getMethod(stage + "Client").invoke(null, args);
                } catch (NoSuchMethodException nsme) {/**/}
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isClient() {
        return false;
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        Mariculture.proxy.passEvent("registerBlocks", event);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        Mariculture.proxy.passEvent("registerItems", event);
    }

}
